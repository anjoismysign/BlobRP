package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.util.Transformation;
import us.mytheria.blbi.Breaking;
import us.mytheria.blbi.Breaking_1_20_5;
import us.mytheria.blobdesign.BlobDesignAPI;
import us.mytheria.blobdesign.entities.PresetData;
import us.mytheria.blobdesign.entities.element.DisplayElementType;
import us.mytheria.blobdesign.entities.presetblock.PresetBlock;
import us.mytheria.bloblib.utilities.MinecraftVersion;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.entities.Breaker;
import us.mytheria.blobrp.entities.CustomBlock;
import us.mytheria.blobrp.events.CustomBlockBreakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlobDesignCustomMining extends RPListener {
    private final Breaking breaking;
    private final Map<DisplayElementType, Map<String, CustomBlock>> customBlocks;
    private final Map<UUID, Breaker> breakers;

    public BlobDesignCustomMining(ConfigManager configManager) {
        super(configManager);
        customBlocks = new HashMap<>();
        breakers = new HashMap<>();
        if (MinecraftVersion.getRunning()
                .compareTo(MinecraftVersion.of("1.20.5")) < 0)
            breaking = null;
        else
            breaking = Breaking_1_20_5.getInstance();
    }

    public void reload() {
        if (breaking == null)
            return;
        HandlerList.unregisterAll(this);
        if (getConfigManager().blobDesignCustomMining().register()) {
            for (DisplayElementType type : DisplayElementType.values()) {
                ConfigurationSection typeSection = getConfigManager().blobDesignCustomMining()
                        .getConfigurationSection(type.name());
                if (typeSection != null) {
                    typeSection.getKeys(false).forEach(key -> {
                        ConfigurationSection display = typeSection.getConfigurationSection(key);
                        CustomBlock customBlock = CustomBlock.READ(display);
                        customBlocks.computeIfAbsent(type, k -> new HashMap<>()).put(key, customBlock);
                    });
                }
            }
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onDamage(BlockDamageEvent event) {
        Block block = event.getBlock();
        PresetBlock<?> presetBlock = BlobDesignAPI.isPresetBlock(block.getLocation());
        if (presetBlock == null)
            return;
        PresetData data = presetBlock.getDisplayPreset().getPresetData();
        DisplayElementType displayElementType = data.type();
        Map<String, CustomBlock> customBlockMap = customBlocks.get(displayElementType);
        if (customBlockMap == null)
            return;
        String key = data.key();
        CustomBlock customBlock = customBlockMap.get(key);
        if (customBlock == null)
            return;
        float hardness = customBlock.getHardness();
        Player player = event.getPlayer();
        double breakSpeed = breaking.getBreakSpeed(player);
        double damage = breakSpeed / hardness;
        if (customBlock.canHarvest(event.getItemInHand()))
            damage /= 30;
        else
            damage /= 100;
        // Instant breaking
        if (damage > 1) {
            CustomBlockBreakEvent breakEvent = new CustomBlockBreakEvent(presetBlock, player);
            Bukkit.getPluginManager().callEvent(breakEvent);
            if (breakEvent.isCancelled())
                return;
            presetBlock.despawn(true);
            return;
        }
        int ticks = (int) Math.ceil(1 / damage);
        Breaker breaker = Breaker.of(block, presetBlock, ticks, player);
        breakers.put(player.getUniqueId(), breaker);
    }

    @EventHandler
    public void onDamageAborted(BlockDamageAbortEvent event) {
        Player player = event.getPlayer();
        Breaker breaker = breakers.remove(player.getUniqueId());
        if (breaker == null)
            return;
        PresetBlock<?> presetBlock = breaker.getPresetBlock();
        Display display = presetBlock.getDecorator().call();
        Transformation transformation = display.getTransformation();
        display.setTransformation(new Transformation(
                transformation.getTranslation(),
                transformation.getLeftRotation(),
                breaker.getOriginalScale(),
                transformation.getRightRotation()));
        breaker.cancel();
    }
}
