package io.github.anjoismysign.blobrp.entities.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.github.anjoismysign.bloblib.exception.ConfigurationFieldException;
import io.github.anjoismysign.blobrp.pressure.WorldPressure;

import java.util.HashMap;
import java.util.Map;

public record PressureConfiguration(
        boolean isEnabled,
        Map<String, WorldPressure> getWorldPressures,
        double getHealthToPressure,
        double getTolerance
) {

    @NotNull
    public static PressureConfiguration getInstance() {
        return RoleplayConfiguration.getInstance().getPressureConfiguration();
    }

    public static PressureConfiguration READ(@NotNull ConfigurationSection parent) {
        ConfigurationSection pressureConfiguration = parent.getConfigurationSection("Pressure");
        if (pressureConfiguration == null)
            throw new ConfigurationFieldException("'Pressure' is not set or valid");
        boolean isEnabled = pressureConfiguration.getBoolean("Enabled", true);
        double healthToPressure = pressureConfiguration.getDouble("Health-To-Pressure", 1.46959);
        double tolerance = pressureConfiguration.getDouble("Tolerance", 29.3918);
        Map<String, WorldPressure> worldPressures = new HashMap<>();
        ConfigurationSection worldsConfiguration = pressureConfiguration.getConfigurationSection("Worlds");
        if (worldsConfiguration == null)
            throw new ConfigurationFieldException("'Pressure.Worlds' is not set or valid");
        worldsConfiguration.getKeys(false).forEach(worldName -> {
            ConfigurationSection worldConfiguration = worldsConfiguration.getConfigurationSection(worldName);
            if (worldConfiguration == null)
                throw new ConfigurationFieldException("'Pressure.Worlds." + worldName + "' is not set or valid");
            WorldPressure worldPressure = WorldPressure.read(worldConfiguration, worldName);
            worldPressures.put(worldName, worldPressure);
        });
        return new PressureConfiguration(isEnabled, Map.copyOf(worldPressures), healthToPressure, tolerance);
    }

    public double toHealth(double pressure) {
        return Math.abs(pressure) / getHealthToPressure;
    }

    @Nullable
    public WorldPressure getPressure(@NotNull String worldName) {
        return getWorldPressures.get(worldName);
    }
}
