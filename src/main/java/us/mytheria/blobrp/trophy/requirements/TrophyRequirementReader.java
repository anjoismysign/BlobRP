package us.mytheria.blobrp.trophy.requirements;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;

public class TrophyRequirementReader {
    /**
     * Reads the requirements from the given file.
     *
     * @param file the file to read from
     * @return the requirements read from the file
     */
    public static TrophyRequirementBuilder read(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        TrophyRequirementBuilder builder = new TrophyRequirementBuilder();
        if (config.contains("FireTicks"))
            builder.withFireTicks(config.getInt("FireTicks"));
        if (config.contains("FreezeTicks"))
            builder.withFreezeTicks(config.getInt("FreezeTicks"));
        if (config.contains("LastDamageCause"))
            builder.withLastDamageCause(EntityDamageEvent.DamageCause.valueOf(config.getString("LastDamageCause")));
        if (config.contains("MinimumPassengers"))
            builder.withMinimumPassengers(config.getInt("MinimumPassengers"));
        if (config.contains("MaximumPassengers"))
            builder.withMaximumPassengers(config.getInt("MaximumPassengers"));
        if (config.contains("TicksLived"))
            builder.withTicksLived(config.getInt("TicksLived"));
        if (config.contains("VehicleType"))
            builder.withVehicle(EntityType.valueOf(config.getString("VehicleType")));
        if (config.contains("CustomNameVisible"))
            builder.withCustomNameVisible(config.getBoolean("CustomNameVisible"));
        if (config.contains("Glowing"))
            builder.withGlowing(config.getBoolean("Glowing"));
        if (config.contains("InWater"))
            builder.withInWater(config.getBoolean("InWater"));
        if (config.contains("OnGround"))
            builder.withOnGround(config.getBoolean("OnGround"));
        if (config.contains("Persistent"))
            builder.withPersistent(config.getBoolean("Persistent"));
        if (config.contains("Silent"))
            builder.withSilent(config.getBoolean("Silent"));
        if (config.contains("CustomName"))
            builder.withCustomName(config.getString("CustomName"));
        return builder;
    }
}
