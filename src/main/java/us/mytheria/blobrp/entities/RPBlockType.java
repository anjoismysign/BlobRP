package us.mytheria.blobrp.entities;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.blobdesign.entities.element.DisplayElementType;
import us.mytheria.blobdesign.entities.presetblock.PresetBlock;
import us.mytheria.bloblib.exception.ConfigurationFieldException;
import us.mytheria.blobrp.entities.customblock.CustomBlockIdentifier;

import java.util.Objects;

public record RPBlockType(
        @NotNull Material getMaterial,
        @Nullable CustomBlockIdentifier getCustomBlockIdentifier
) {

    @NotNull
    public static RPBlockType of(@NotNull Material material) {
        Objects.requireNonNull(material, "'material' cannot be null");
        return new RPBlockType(material, null);
    }

    @NotNull
    public static RPBlockType of(@NotNull PresetBlock<?> presetBlock) {
        Objects.requireNonNull(presetBlock, "'presetBlock' cannot be null");
        CustomBlockIdentifier identifier = CustomBlockIdentifier.of(presetBlock);
        return new RPBlockType(presetBlock.getLocation().getBlock().getType(), identifier);
    }

    public boolean isVanilla() {
        return getCustomBlockIdentifier == null;
    }

    @NotNull
    public static RPBlockType READ(@NotNull ConfigurationSection section) {
        ConfigurationSection blockTypeSection = section.getConfigurationSection("BlockType");
        if (blockTypeSection == null)
            throw new ConfigurationFieldException("'BlockType' is not set or valid");
        String readMaterial = blockTypeSection.getString("Material", "BARRIER");
        Material material = Material.getMaterial(readMaterial);
        if (material == null)
            throw new ConfigurationFieldException("'BlockType.Material' is not set or valid");
        if (!blockTypeSection.isString("DisplayElementType") ||
                !blockTypeSection.isString("Key"))
            return new RPBlockType(material, null);
        String readDisplayElementType = blockTypeSection.getString("DisplayElementType");
        DisplayElementType type;
        try {
            type = DisplayElementType.valueOf(readDisplayElementType);
        } catch (IllegalArgumentException exception) {
            throw new ConfigurationFieldException("'BlockType.DisplayElementType' is not valid");
        }
        String key = blockTypeSection.getString("Key");
        CustomBlockIdentifier identifier = new CustomBlockIdentifier(type, key);
        return new RPBlockType(material, identifier);
    }

    public void serialize(@NotNull ConfigurationSection section) {
        Objects.requireNonNull(section, "'section' cannot be null");
        ConfigurationSection blockTypeSection = section.createSection("BlockType");
        if (isVanilla())
            blockTypeSection.set("Material", getMaterial);
        else {
            blockTypeSection.set("DisplayElementType", getCustomBlockIdentifier.getType());
            blockTypeSection.set("Key", getCustomBlockIdentifier.getKey());
        }
    }

    /**
     * Whether another RPBlockType matches this
     *
     * @param other The one to compare with
     * @return true if so, false otherwise
     */
    public boolean matches(@Nullable RPBlockType other) {
        if (other == null)
            return false;
        boolean matchesMaterial = other.getMaterial.equals(getMaterial);
        CustomBlockIdentifier otherIdentifier = other.getCustomBlockIdentifier;
        if (otherIdentifier == null)
            return matchesMaterial && getCustomBlockIdentifier == null;
        return matchesMaterial && otherIdentifier.matches(getCustomBlockIdentifier);
    }
}
