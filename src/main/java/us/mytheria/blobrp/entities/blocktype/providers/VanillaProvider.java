package us.mytheria.blobrp.entities.blocktype.providers;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.exception.ConfigurationFieldException;
import us.mytheria.blobrp.entities.blocktype.BlockType;
import us.mytheria.blobrp.entities.blocktype.VanillaBlockType;

import java.util.Objects;

public class VanillaProvider implements BlockTypeProvider {
    private static VanillaProvider instance;

    public static VanillaProvider getInstance() {
        if (instance == null)
            instance = new VanillaProvider();
        return instance;
    }

    private VanillaProvider() {
    }

    @Nullable
    public BlockType isBlockType(@Nullable Object object) {
        if (!(object instanceof Material material))
            return null;
        return new VanillaBlockType(material);
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
        String readMaterial = section.getString("Material");
        if (readMaterial == null)
            return null;
        Material material = Material.getMaterial(readMaterial);
        if (material == null)
            throw new ConfigurationFieldException("'Material' is not valid");
        return new VanillaBlockType(material);
    }
}
