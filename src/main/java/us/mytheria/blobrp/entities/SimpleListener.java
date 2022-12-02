package us.mytheria.blobrp.entities;

import org.bukkit.configuration.ConfigurationSection;

public record SimpleListener<T>(boolean register, T value) {

    public static SimpleListener<Double> DOUBLE(ConfigurationSection section, String path) {
        boolean register = section.getBoolean("Register");
        double value = section.getDouble(path);
        return new SimpleListener<>(register, value);
    }

    public static SimpleListener<Integer> INTEGER(ConfigurationSection section, String path) {
        boolean register = section.getBoolean("Register");
        int value = section.getInt(path);
        return new SimpleListener<>(register, value);
    }

    public static SimpleListener<Boolean> BOOLEAN(ConfigurationSection section, String path) {
        boolean register = section.getBoolean("Register");
        boolean value = section.getBoolean(path);
        return new SimpleListener<>(register, value);
    }

    public static SimpleListener<String> STRING(ConfigurationSection section, String path) {
        boolean register = section.getBoolean("Register");
        String value = section.getString(path);
        return new SimpleListener<>(register, value);
    }

    public static SimpleListener<Long> LONG(ConfigurationSection section, String path) {
        boolean register = section.getBoolean("Register");
        long value = section.getLong(path);
        return new SimpleListener<>(register, value);
    }
}