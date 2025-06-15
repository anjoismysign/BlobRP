package io.github.anjoismysign.blobrp.entities.blocktype;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.github.anjoismysign.blobrp.entities.blocktype.providers.BlobDesignProvider;
import io.github.anjoismysign.blobrp.entities.blocktype.providers.BlockTypeProvider;
import io.github.anjoismysign.blobrp.entities.blocktype.providers.VanillaProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockTypeFactory implements Listener {
    private static BlockTypeFactory instance;
    private final List<BlockTypeProvider> providers;

    private BlockTypeFactory() {
        providers = new ArrayList<>();
        addProvider(VanillaProvider.getInstance());
        boolean isBlobDesignEnabled = Bukkit.getPluginManager().isPluginEnabled("BlobDesign");
        if (isBlobDesignEnabled)
            addProvider(BlobDesignProvider.getInstance());
    }

    public static BlockTypeFactory getInstance() {
        if (instance == null)
            instance = new BlockTypeFactory();
        return instance;
    }

    public void addProvider(@NotNull BlockTypeProvider provider) {
        Objects.requireNonNull(provider, "'provider' cannot be null");
        providers.add(provider);
    }

    public void removeProvider(@NotNull BlockTypeProvider provider) {
        Objects.requireNonNull(provider, "'provider' cannot be null");
        providers.remove(provider);
    }

    @Nullable
    public BlockType isBlockType(@Nullable Object object) {
        for (BlockTypeProvider provider : providers) {
            BlockType blockType = provider.isBlockType(object);
            if (blockType == null)
                continue;
            return blockType;
        }
        return null;
    }

    @Nullable
    public BlockType readDefault(@NotNull ConfigurationSection section) {
        return read(section, true);
    }

    @Nullable
    public BlockType read(@NotNull ConfigurationSection section,
                          boolean isDefault) {
        for (BlockTypeProvider provider : providers) {
            BlockType blockType = provider.read(section, isDefault);
            if (blockType == null)
                continue;
            return blockType;
        }
        return null;
    }
}
