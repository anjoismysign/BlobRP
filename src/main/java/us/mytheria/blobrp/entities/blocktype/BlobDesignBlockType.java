package us.mytheria.blobrp.entities.blocktype;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.blobdesign.BlobDesignAPI;
import us.mytheria.blobdesign.entities.PresetData;
import us.mytheria.blobdesign.entities.presetblock.PresetBlockAsset;

import java.util.Objects;

public record BlobDesignBlockType(
        @NotNull PresetData getPresetData)
        implements BlockType {

    /**
     * Check if a BlockType is Vanilla
     *
     * @param blockType The BlockType indeed
     * @return true if so, false otherwise
     */
    public static boolean isBlobDesign(@NotNull BlockType blockType) {
        return DefaultBlockType.byName(blockType.getType()) == DefaultBlockType.BLOB_DESIGN;
    }

    public void serialize(@NotNull ConfigurationSection section,
                          boolean create) {
        Objects.requireNonNull(section, "'section' cannot be null");
        if (create) {
            ConfigurationSection blockTypeSection = section.createSection("BlockType");
            serialize(blockTypeSection, false);
            return;
        }
        section.set("DisplayElementType", getPresetData.type());
        section.set("Key", getPresetData.key());
    }

    /**
     * Whether another RPBlockType matches this
     *
     * @param other The one to compare with
     * @return true if so, false otherwise
     */
    public boolean matches(@Nullable BlockType other) {
        if (other == null)
            return false;
        if (!isBlobDesign(other))
            return false;
        BlobDesignBlockType otherBlobDesign = (BlobDesignBlockType) other;
        PresetData otherPresetData = otherBlobDesign.getPresetData;
        return otherPresetData.type().equals(getPresetData.type()) &&
                otherPresetData.key().equals(getPresetData.key());
    }

    public String getType() {
        return DefaultBlockType.BLOB_DESIGN.name();
    }

    public void set(@NotNull Block block) {
        Objects.requireNonNull(block, "'block' cannot be null");
        PresetBlockAsset<?> presetBlockAsset = BlobDesignAPI.isPresetBlock(block.getLocation());
        if (presetBlockAsset == null)
            return;
        presetBlockAsset.respawn(true);
    }
}
