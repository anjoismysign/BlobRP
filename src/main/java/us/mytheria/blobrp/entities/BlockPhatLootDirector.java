package us.mytheria.blobrp.entities;

import com.codisimus.plugins.phatloots.loot.LootBundle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.ObjectDirectorData;
import us.mytheria.bloblib.exception.ConfigurationFieldException;
import us.mytheria.blobrp.director.RPManagerDirector;

import java.util.*;

public class BlockPhatLootDirector extends ObjectDirector<BlockPhatLoot> {
    private static final Map<Material, BlockPhatLoot> uniques = new HashMap<>();

    public BlockPhatLootDirector(RPManagerDirector managerDirector) {
        super(managerDirector, ObjectDirectorData
                        .simple(managerDirector.getRealFileManager(), "BlockPhatLoot"),
                file -> {
                    String fileName = file.getName();
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    if (!config.isString("Material"))
                        throw new ConfigurationFieldException("'Material' is missing or not valid");
                    if (!config.isString("PhatLootName"))
                        throw new ConfigurationFieldException("'PhatLootName' is missing or not valid");
                    Material material = Material.getMaterial(config.getString("Material"));
                    Objects.requireNonNull(material, "'Material' is not valid");
                    BlockPhatLoot duplicate = uniques.get(material);
                    if (duplicate != null)
                        throw new ConfigurationFieldException("Material '" + material.name() + "' is already registered in '" + duplicate.getKey() + "'");
                    String phatLootName = config.getString("PhatLootName");
                    boolean shouldCancel = config.getBoolean("Should-Cancel", false);
                    Set<String> applicableOn = new HashSet<>();
                    if (config.isList("Applicable-On"))
                        applicableOn.addAll(config.getStringList("Applicable-On"));
                    boolean isGlobal = config.getBoolean("Is-Global", true);
                    BlockPhatLoot blockPhatLoot = new BlockPhatLoot(fileName, material, phatLootName, shouldCancel, applicableOn, isGlobal);
                    uniques.put(material, blockPhatLoot);
                    return blockPhatLoot;
                }, false);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockPhatLoot blockPhatLoot = isLinked(block.getType());
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
    public BlockPhatLoot isLinked(@NotNull Material material) {
        Objects.requireNonNull(material, "'Material' is not valid");
        return uniques.get(material);
    }
}
