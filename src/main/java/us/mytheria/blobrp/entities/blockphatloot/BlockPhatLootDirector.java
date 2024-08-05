package us.mytheria.blobrp.entities.blockphatloot;

import com.codisimus.plugins.phatloots.loot.LootBundle;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.ObjectDirectorData;
import us.mytheria.bloblib.exception.ConfigurationFieldException;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.blocktype.BlockType;
import us.mytheria.blobrp.entities.blocktype.BlockTypeFactory;

import java.util.*;

public class BlockPhatLootDirector extends ObjectDirector<BlockPhatLoot> {
    private static final Map<BlockType, BlockPhatLoot> uniques = new HashMap<>();
    private static final BlockTypeFactory factory = BlockTypeFactory.getInstance();

    public BlockPhatLootDirector(RPManagerDirector managerDirector) {
        super(managerDirector, ObjectDirectorData
                        .simple(managerDirector.getRealFileManager(), "BlockPhatLoot"),
                file -> {
                    String fileName = file.getName();
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    if (!config.isString("PhatLootName"))
                        throw new ConfigurationFieldException("'PhatLootName' is missing or not valid");
                    BlockType blockType = factory.readDefault(config);
                    if (blockType == null)
                        throw new ConfigurationFieldException("'BlockType' didn't point to a valid BlockType");
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
                    BlockPhatLoot blockPhatLoot = new BlockPhatLoot(FilenameUtils.removeExtension(fileName), blockType, phatLootName, shouldCancel, applicableOn, isGlobal);
                    uniques.put(blockType, blockPhatLoot);
                    return blockPhatLoot;
                }, false);
    }

    @EventHandler
    public void onVanillaBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockPhatLoot blockPhatLoot = isLinked(factory.isBlockType(block.getType()));
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
    public static BlockPhatLoot isLinked(@Nullable BlockType other) {
        Objects.requireNonNull(other, "'other' cannot be null");
        return uniques.entrySet().stream()
                .filter(entry -> entry.getKey().matches(other))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
}
