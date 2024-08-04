package us.mytheria.blobrp.entities.customblock;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.blobdesign.entities.PresetData;
import us.mytheria.blobdesign.entities.element.DisplayElementType;
import us.mytheria.blobdesign.entities.presetblock.PresetBlock;
import us.mytheria.bloblib.exception.ConfigurationFieldException;

import java.util.Objects;

public record CustomBlockIdentifier(
        @NotNull DisplayElementType getType,
        @NotNull String getKey) {

    @NotNull
    public static CustomBlockIdentifier of(@NotNull PresetBlock<?> presetBlock) {
        PresetData presetData = presetBlock.getDisplayPreset().getPresetData();
        return new CustomBlockIdentifier(presetData.type(), presetData.key());
    }

    @NotNull
    public static CustomBlockIdentifier READ(@NotNull ConfigurationSection section) {
        Objects.requireNonNull(section, "'section' cannot be null");
        if (!section.isString("DisplayElementType"))
            throw new ConfigurationFieldException("'DisplayElementType' field is missing or not a String");
        if (!section.isString("Key"))
            throw new ConfigurationFieldException("'Key' field is missing or not a String");
        String readDisplayElementType = section.getString("DisplayElementType");
        DisplayElementType type;
        try {
            type = DisplayElementType.valueOf(readDisplayElementType);
        } catch (IllegalArgumentException exception) {
            throw new ConfigurationFieldException("'DisplayElementType' field is not valid");
        }
        String key = section.getString("Key");
        return new CustomBlockIdentifier(type, key);
    }

    /**
     * Whether another CustomBlockIdentifier matches with this
     *
     * @param other The one to compare with
     * @return true if so, false otherwise
     */
    public boolean matches(@Nullable CustomBlockIdentifier other) {
        if (other == null)
            return false;
        return other.getType.equals(getType) && other.getKey.equals(getKey);
    }
}
