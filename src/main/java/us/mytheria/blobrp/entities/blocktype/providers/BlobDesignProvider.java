package us.mytheria.blobrp.entities.blocktype.providers;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.blobdesign.entities.PresetData;
import us.mytheria.blobdesign.entities.element.DisplayElementType;
import us.mytheria.blobdesign.entities.presetblock.PresetBlock;
import us.mytheria.bloblib.exception.ConfigurationFieldException;
import us.mytheria.blobrp.entities.blocktype.BlobDesignBlockType;
import us.mytheria.blobrp.entities.blocktype.BlockType;

import java.util.Objects;

public class BlobDesignProvider implements BlockTypeProvider {
    private static BlobDesignProvider instance;

    public static BlobDesignProvider getInstance() {
        if (instance == null)
            instance = new BlobDesignProvider();
        return instance;
    }

    private BlobDesignProvider() {
    }

    @Nullable
    public BlockType read(@NotNull ConfigurationSection section,
                          boolean isDefault) {
        Objects.requireNonNull(section, "'section' cannot be null");
        if (isDefault) {
            ConfigurationSection blockTypeSection = section.getConfigurationSection("BlockType");
            if (blockTypeSection == null)
                throw new ConfigurationFieldException("'BlockType' is not set or valid");
            return read(blockTypeSection, false);
        }
        String readDisplayElementType = section.getString("DisplayElementType");
        if (readDisplayElementType == null)
            return null;
        DisplayElementType type;
        try {
            type = DisplayElementType.valueOf(readDisplayElementType);
        } catch (IllegalArgumentException exception) {
            throw new ConfigurationFieldException("'DisplayElementType' is not valid");
        }
        String key = section.getString("Key");
        if (key == null)
            return null;
        PresetData presetData = new PresetData(type, key);
        return new BlobDesignBlockType(presetData);
    }

    @Nullable
    public BlockType isBlockType(@Nullable Object object) {
        if (object instanceof PresetBlock<?> presetBlock)
            return of(presetBlock);
        if (object instanceof PresetData presetData)
            return of(presetData);
        return null;
    }

    @NotNull
    public BlockType of(@NotNull PresetData presetData) {
        Objects.requireNonNull(presetData, "'presetData' cannot be null");
        return new BlobDesignBlockType(presetData);
    }

    @NotNull
    public BlockType of(@NotNull PresetBlock<?> presetBlock) {
        Objects.requireNonNull(presetBlock, "'presetBlock' cannot be null");
        return new BlobDesignBlockType(presetBlock.getDisplayPreset().getPresetData());
    }
}
