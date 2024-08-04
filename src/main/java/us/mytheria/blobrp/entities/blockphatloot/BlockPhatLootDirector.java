package us.mytheria.blobrp.entities.blockphatloot;

import com.codisimus.plugins.phatloots.loot.LootBundle;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.ObjectDirectorData;
import us.mytheria.bloblib.exception.ConfigurationFieldException;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.RPBlockType;
import us.mytheria.blobrp.events.CustomBlockBreakEvent;

import java.util.*;

public class BlockPhatLootDirector extends ObjectDirector<BlockPhatLoot> {
    private static final Map<RPBlockType, BlockPhatLoot> uniques = new HashMap<>();

    public BlockPhatLootDirector(RPManagerDirector managerDirector) {
        super(managerDirector, ObjectDirectorData
                        .simple(managerDirector.getRealFileManager(), "BlockPhatLoot"),
                file -> {
                    String fileName = file.getName();
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    if (!config.isString("PhatLootName"))
                        throw new ConfigurationFieldException("'PhatLootName' is missing or not valid");
                    RPBlockType blockType = RPBlockType.READ(config);
                    BlockPhatLoot duplicate = isLinked(blockType);
                    if (duplicate != null)
                        throw new ConfigurationFieldException("'BlockType' is already registered in '" + duplicate.getKey() + "'");
                    String phatLootName = config.getString("PhatLootName");
                    if (phatLootName == null)
                        throw new ConfigurationFieldException("'PhatLootName' is not valid or set");
                    boolean shouldCancel = config.getBoolean("Should-Cancel", false);
                    Set<String> applicableOn = new HashSet<>();
                    if (config.isList("Applicable-On"))
                        applicableOn.addAll(config.getStringList("Applicable-On"));
                    boolean isGlobal = config.getBoolean("Is-Global", true);
                    BlockPhatLoot blockPhatLoot = new BlockPhatLoot(fileName, blockType, phatLootName, shouldCancel, applicableOn, isGlobal);
                    uniques.put(blockType, blockPhatLoot);
                    return blockPhatLoot;
                }, false);
    }

    @EventHandler
    public void onCustomBreak(CustomBlockBreakEvent event) {
        Player player = event.getPlayer();
        BlockPhatLoot blockPhatLoot = isLinked(RPBlockType.of(event.getPresetBlock()));
        if (blockPhatLoot == null)
            return;
        Block block = event.getBlock();
        World world = block.getWorld();
        if (!blockPhatLoot.applies(world))
            return;
        Location location = block.getLocation();
        LootBundle lootBundle = blockPhatLoot.rollForLoot();
        lootBundle.getItemList().forEach(itemStack -> {
            block.getWorld().dropItem(location, itemStack);
        });
        if (blockPhatLoot.shouldCancel())
            event.setCancelled(true);
    }

    @EventHandler
    public void onVanillaBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockPhatLoot blockPhatLoot = isLinked(RPBlockType.of(block.getType()));
        if (blockPhatLoot == null)
            return;
        World world = block.getWorld();
        if (!blockPhatLoot.applies(world))
            return;
        Location location = block.getLocation();
        LootBundle lootBundle = blockPhatLoot.rollForLoot();
        lootBundle.getItemList().forEach(itemStack -> {
            block.getWorld().dropItem(location, itemStack);
        });
        if (blockPhatLoot.shouldCancel())
            event.setCancelled(true);
    }

    @Override
    public void reload() {
        uniques.clear();
        super.reload();
    }

    @Nullable
    public static BlockPhatLoot isLinked(@NotNull RPBlockType other) {
        Objects.requireNonNull(other, "'other' cannot be null");
        return uniques.entrySet().stream()
                .filter(entry -> entry.getKey().matches(other))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
}
