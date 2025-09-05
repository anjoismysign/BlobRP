package io.github.anjoismysign.blobrp.entity.blocktype.providers;

import io.github.anjoismysign.blobrp.entity.blocktype.BlockType;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockTypeProvider {
    /**
     * Whether an object can be instanced for BlockType
     *
     * @param object The object indeed
     * @return The BlockType if found, null otherwise
     */
    @Nullable
    BlockType isBlockType(@Nullable Object object);

    /**
     * Reads a BlockType from a ConfigurationSection
     *
     * @param section   The ConfigurationSection indeed
     * @param isDefault Whether to use a default, nested ConfigurationSection
     * @return The BlockType if it can be read, null if not
     */
    @Nullable
    BlockType read(@NotNull ConfigurationSection section,
                   boolean isDefault);
}
