package us.mytheria.blobrp.reward;

import global.warming.commons.io.FilenameUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import us.mytheria.bloblib.entities.BlobMessageReader;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;

import java.io.File;
import java.util.Optional;

public class RewardReader {
    public static CashReward readCash(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        boolean shouldDelay = config.getBoolean("ShouldDelay", false);
        Optional<Long> delay = config.contains("Delay") ? Optional.of(config.getLong("Delay")) : Optional.empty();
        boolean runAsync = config.getBoolean("RunAsync", false);
        Optional<ReferenceBlobMessage> message = BlobMessageReader.readReference(config);
        String key = FilenameUtils.removeExtension(file.getName());
        if (!config.contains("Value") || !config.isDouble("Value"))
            throw new IllegalArgumentException("'Value' is required for CASH rewards.");
        return new CashReward(key, shouldDelay, config.getDouble("Value"), delay, runAsync, message);
    }

    public static ItemStackReward readItemStack(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        boolean shouldDelay = config.getBoolean("ShouldDelay", false);
        Optional<Long> delay = config.contains("Delay") ? Optional.of(config.getLong("Delay")) : Optional.empty();
        boolean runAsync = config.getBoolean("RunAsync", false);
        Optional<ReferenceBlobMessage> message = BlobMessageReader.readReference(config);
        String key = FilenameUtils.removeExtension(file.getName());
        if (!config.contains("Value") || !config.isItemStack("Value"))
            throw new IllegalArgumentException("'Value' is required for ITEM rewards.");
        return new ItemStackReward(key, shouldDelay, config.getItemStack("Value"),
                delay, runAsync, message);
    }

    public static PermissionReward readPermission(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        boolean shouldDelay = config.getBoolean("ShouldDelay", false);
        Optional<Long> delay = config.contains("Delay") ? Optional.of(config.getLong("Delay")) : Optional.empty();
        boolean runAsync = config.getBoolean("RunAsync", false);
        Optional<ReferenceBlobMessage> message = BlobMessageReader.readReference(config);
        String key = FilenameUtils.removeExtension(file.getName());
        if (!config.contains("Value") || !config.isString("Value"))
            throw new IllegalArgumentException("'Value' is required for PERMISSION rewards.");
        Optional<String> world = config.contains("World") && config.isString("World") ? Optional.of(config.getString("World")) : Optional.empty();
        return new PermissionReward(key, shouldDelay, config.getString("Value"), delay, runAsync, message, world);
    }
}
