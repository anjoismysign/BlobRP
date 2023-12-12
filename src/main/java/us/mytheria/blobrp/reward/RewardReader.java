package us.mytheria.blobrp.reward;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Optional;

public class RewardReader {
    public static CashReward readCash(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        boolean shouldDelay = config.getBoolean("ShouldDelay", false);
        Optional<Long> delay = config.isLong("Delay") ? Optional.of(config.getLong("Delay")) : Optional.empty();
        boolean runAsync = config.getBoolean("RunAsync", false);
        Optional<String> message = Optional.ofNullable(config.getString("Message", null));
        Optional<String> currency = Optional.empty();
        if (config.isString("Currency"))
            currency = Optional.ofNullable(config.getString("Currency"));
        String key = FilenameUtils.removeExtension(file.getName());
        if (!config.contains("Value") || !config.isDouble("Value"))
            throw new IllegalArgumentException("'Value' is required for CASH rewards.");
        return new CashReward(key, shouldDelay, config.getDouble("Value"), delay,
                runAsync, message, currency);
    }

    public static ItemStackReward readItemStack(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        boolean shouldDelay = config.getBoolean("ShouldDelay", false);
        Optional<Long> delay = config.contains("Delay") ? Optional.of(config.getLong("Delay")) : Optional.empty();
        boolean runAsync = config.getBoolean("RunAsync", false);
        Optional<String> message = Optional.ofNullable(config.getString("Message", null));
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
        Optional<String> message = Optional.ofNullable(config.getString("Message", null));
        String key = FilenameUtils.removeExtension(file.getName());
        if (!config.contains("Value") || !config.isString("Value"))
            throw new IllegalArgumentException("'Value' is required for PERMISSION rewards.");
        Optional<String> world = config.contains("World") && config.isString("World") ? Optional.of(config.getString("World")) : Optional.empty();
        return new PermissionReward(key, shouldDelay, config.getString("Value"), delay, runAsync, message, world);
    }
}
