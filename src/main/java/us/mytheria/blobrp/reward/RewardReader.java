package us.mytheria.blobrp.reward;

import global.warming.commons.io.FilenameUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import us.mytheria.bloblib.entities.BlobMessageReader;
import us.mytheria.bloblib.entities.message.BlobMessage;
import us.mytheria.bloblib.itemstack.ItemStackReader;

import java.io.File;
import java.util.Optional;

public class RewardReader {
    public static Reward read(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.contains("Type"))
            throw new IllegalArgumentException("'Type' is required. Missing at: " + file.getPath());
        String type = config.getString("Type");
        boolean shouldDelay = config.getBoolean("ShouldDelay", false);
        Optional<Long> delay = config.contains("Delay") ? Optional.of(config.getLong("Delay")) : Optional.empty();
        Optional<Boolean> runAsync = config.contains("RunAsync") ? Optional.of(config.getBoolean("RunAsync")) : Optional.empty();
        Optional<BlobMessage> message = BlobMessageReader.parse(config);
        String key = FilenameUtils.removeExtension(file.getName());
        switch (type) {
            case "CASH" -> {
                if (!config.contains("Amount"))
                    throw new IllegalArgumentException("'Amount' is required for CASH rewards.");
                return new CashReward(key, shouldDelay, config.getDouble("Amount"), delay, runAsync, message);
            }
            case "ITEM" -> {
                if (!config.contains("Item"))
                    throw new IllegalArgumentException("'Item' is required for ITEM rewards.");
                return new ItemStackReward(key, shouldDelay, ItemStackReader
                        .read(config.getConfigurationSection("Item"))
                        .build(), delay, runAsync, message);
            }
            case "PERMISSION" -> {
                if (!config.contains("Permission"))
                    throw new IllegalArgumentException("'Permission' is required for PERMISSION rewards.");
                Optional<String> world = config.contains("World") ? Optional.of(config.getString("World")) : Optional.empty();
                Optional<Boolean> currentWorld = config.contains("CurrentWorld") ? Optional.of(config.getBoolean("CurrentWorld")) : Optional.empty();
                return new PermissionReward(key, shouldDelay, config.getString("Permission"), delay, runAsync, message, world, currentWorld);
            }
            default -> throw new IllegalArgumentException("Invalid reward type: " + type);
        }
    }
}
