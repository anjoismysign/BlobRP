package us.mytheria.blobrp.entities.blocktype;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record VanillaBlockType(@NotNull Material getMaterial)
        implements BlockType {

    /**
     * Check if a BlockType is Vanilla
     *
     * @param blockType The BlockType indeed
     * @return true if so, false otherwise
     */
    public static boolean isVanilla(@NotNull BlockType blockType) {
        return DefaultBlockType.byName(blockType.getType()) == DefaultBlockType.VANILLA;
    }

    public void serialize(@NotNull ConfigurationSection section,
                          boolean create) {
        Objects.requireNonNull(section, "'section' cannot be null");
        if (create) {
            ConfigurationSection blockTypeSection = section.createSection("BlockType");
            serialize(blockTypeSection, false);
            return;
        }
        section.set("Material", getMaterial);
    }

    public boolean matches(@Nullable BlockType other) {
        if (other == null)
            return false;
        if (!isVanilla(other))
            return false;
        VanillaBlockType otherVanilla = (VanillaBlockType) other;
        return otherVanilla.getMaterial.equals(getMaterial);
    }

    public String getType() {
        return DefaultBlockType.VANILLA.name();
    }

    public void set(@NotNull Block block) {
        Objects.requireNonNull(block, "'block' cannot be null");
        block.setType(getMaterial);
    }
}
