package us.mytheria.blobrp.entities.regenable;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.ObjectDirectorData;
import us.mytheria.bloblib.exception.ConfigurationFieldException;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.blocktype.BlockType;
import us.mytheria.blobrp.entities.blocktype.BlockTypeFactory;

import java.util.*;

public class RegenableBlockDirector extends ObjectDirector<RegenableBlockData> {
    private static final Map<BlockType, RegenableBlockData> datas = new HashMap<>();
    private static final Map<BlockType, Integer> priority = new HashMap<>();
    private static final BlockTypeFactory factory = BlockTypeFactory.getInstance();

    private final Set<RegenableBlock> regenableBlocks = new HashSet<>();

    public RegenableBlockDirector(RPManagerDirector managerDirector) {
        super(managerDirector, ObjectDirectorData
                        .simple(managerDirector.getRealFileManager(), "RegenableBlockData"),
                file -> {
                    String fileName = file.getName();
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    if (!configuration.isInt("Priority"))
                        throw new ConfigurationFieldException("'Priority' is not valid or set");
                    ConfigurationSection newBlockTypeSection = configuration.getConfigurationSection("NewBlockType");
                    if (newBlockTypeSection == null)
                        throw new ConfigurationFieldException("'NewBlockType' section is not valid or set");
                    ConfigurationSection delaySection = configuration.getConfigurationSection("Delay");
                    if (delaySection == null)
                        throw new ConfigurationFieldException("'Delay' section is not valid or set");
                    int priority = configuration.getInt("Priority");
                    BlockType blockType = factory.readDefault(configuration);
                    if (blockType == null)
                        throw new ConfigurationFieldException("'BlockType' didn't point to a valid BlockType");
                    RegenableBlockData duplicate = isRegenableBlock(blockType);
                    if (duplicate != null)
                        throw new ConfigurationFieldException("'BlockType' is already registered in '" + duplicate.getKey() + "'");
                    BlockType newBlockType = factory.read(newBlockTypeSection, false);
                    if (newBlockType == null)
                        throw new ConfigurationFieldException("'NewBlockType' didn't point to a valid BlockType");
                    RandomInterval delay = RandomInterval.READ(delaySection, false);
                    RegenableBlockData regenableBlock = new RegenableBlockData(
                            FilenameUtils.removeExtension(fileName),
                            blockType,
                            newBlockType,
                            delay,
                            priority);
                    datas.put(blockType, regenableBlock);
                    return regenableBlock;
                }, false);
        priority.put(factory.isBlockType(Material.AIR), 0);
        priority.put(factory.isBlockType(Material.BEDROCK), 0);
    }

    @EventHandler
    public void onVanillaBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material material = block.getType();
        RegenableBlockData data = isRegenableBlock(factory.isBlockType(material));
        if (data == null)
            return;
        RegenableBlock regenableBlock = new RegenableBlock(block, data, this);
        addRegenableBlock(regenableBlock);
    }

    @Override
    public void reload() {
        reloadRegenables();
        datas.clear();
        super.reload();
    }

    @Override
    public void unload() {
        reloadRegenables();
    }

    private void reloadRegenables() {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(getPlugin(), this::reloadRegenables);
            return;
        }
        regenableBlocks.forEach(regenable -> regenable.cancel(true));
        regenableBlocks.clear();
    }

    public void addRegenableBlock(@NotNull RegenableBlock regenableBlock) {
        Objects.requireNonNull(regenableBlock, "'regenableBlock' cannot be null");
        regenableBlocks.add(regenableBlock);
    }

    protected void removeRegenableBlock(@NotNull RegenableBlock regenableBlock) {
        Objects.requireNonNull(regenableBlock, "'regenableBlock' cannot be null");
        regenableBlocks.remove(regenableBlock);
    }

    @Nullable
    public static RegenableBlockData isRegenableBlock(@Nullable BlockType other) {
        if (other == null)
            return null;
        return datas.entrySet().stream()
                .filter(entry -> entry.getKey().matches(other))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    public static int getPriority(@Nullable BlockType blockType) {
        Integer getPriority = hasPriority(blockType);
        if (getPriority == null) {
            getPriority = getRegenablePriority(blockType);
            return getPriority;
        }
        return getPriority;
    }

    @Nullable
    private static Integer hasPriority(@Nullable BlockType other) {
        if (other == null)
            return null;
        return priority.entrySet().stream()
                .filter(entry -> entry.getKey().matches(other))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private static int getRegenablePriority(@Nullable BlockType blockType) {
        RegenableBlockData regenableBlock = isRegenableBlock(blockType);
        if (regenableBlock == null)
            return 1;
        return regenableBlock.getPriority();
    }
}
