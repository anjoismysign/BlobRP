package io.github.anjoismysign.blobrp.entity.regenable;

import io.github.anjoismysign.bloblib.entities.BlobObject;
import io.github.anjoismysign.blobrp.entity.blocktype.BlobDesignBlockType;
import io.github.anjoismysign.blobrp.entity.blocktype.BlockType;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public record RegenableBlockData(
        @NotNull String getKey,
        @NotNull BlockType getBlockType,
        @NotNull BlockType getNewBlockType,
        @NotNull RandomInterval getDelay,
        int getPriority) implements BlobObject {

    @Override
    public File saveToFile(File directory) {
        File file = instanceFile(directory);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        getBlockType.serializeDefault(config);
        getNewBlockType.serialize(config.createSection("NewBlockType"), false);
        getDelay.serialize(config.createSection("Delay"), false);
        config.set("Priority", getPriority);
        try {
            config.save(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return file;
    }

    public void regen(@NotNull Block block) {
        getBlockType.set(block);
    }

    public void setNew(@NotNull Block block) {
        if (BlobDesignBlockType.isBlobDesign(getNewBlockType))
            throw new RuntimeException("'NewBlockType' cannot be of BlobDesign type");
        getNewBlockType.set(block);
    }
}
