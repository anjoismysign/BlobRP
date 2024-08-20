package us.mytheria.blobrp.pressure;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record WorldPressure(
        @NotNull String getWorldName,
        double getAtmosphere,
        int getSeaLevel
) {

    @NotNull
    public static WorldPressure read(
            @NotNull ConfigurationSection section,
            @NotNull String worldName
    ) {
        double atmosphere = section.getDouble("Atmosphere", 14.6959);
        int seaLevel = section.getInt("Sea-Level", 62);
        return of(worldName, atmosphere, seaLevel);
    }

    @NotNull
    public static WorldPressure of(
            @NotNull String worldName,
            double atmosphere,
            int seaLevel
    ) {
        Objects.requireNonNull(worldName, "'worldName' cannot be null");
        return new WorldPressure(worldName, atmosphere, seaLevel);
    }

    /**
     * Rough estimation of pressure.
     * Doesn't consider pressure over sea level (if being over sea level, will always return 1 atmosphere)
     *
     * @param entity The entity to check
     * @return The pressure
     */
    public double getPressure(@NotNull Entity entity) {
        Objects.requireNonNull(entity, "'entity' cannot be null");
        Block block = entity.getLocation().getBlock();
        int height = block.getY();
        if (height > getSeaLevel)
            return getAtmosphere;
        int depth = (getSeaLevel - height) + 1;
        double atmospheres = (depth / 10.0) + 1.0;
        return atmospheres * getAtmosphere;
    }
}
