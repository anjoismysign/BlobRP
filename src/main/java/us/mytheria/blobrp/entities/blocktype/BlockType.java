package us.mytheria.blobrp.entities.blocktype;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface BlockType {

    /**
     * Whether this BlockType matches another BlockType
     *
     * @param other The blockType to compare with
     * @return true if so, false otherwise
     */
    boolean matches(@NotNull BlockType other);

    /**
     * Gets the type of the BlockType
     *
     * @return The type
     */
    String getType();

    /**
     * Sets this BlockType in a specific Block
     *
     * @param block The Block indeed.
     */
    void set(@NotNull Block block);

    /**
     * Serializes this BlockType in a ConfigurationSection
     *
     * @param section The ConfigurationSection in which will be serialized
     * @param create  Whether to create a nested ConfigurationSection "a la" default
     */
    void serialize(
            @NotNull ConfigurationSection section,
            boolean create);

    /**
     * Serializes this BlockType in a ConfigurationSection.
     * Creates a default, nested ConfigurationSection
     *
     * @param section The ConfigurationSection in which will be serialized
     */
    default void serializeDefault(@NotNull ConfigurationSection section) {
        serialize(section, true);
    }
}
