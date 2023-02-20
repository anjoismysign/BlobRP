package us.mytheria.blobrp.trophy.requirements;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TrophyRequirementWriter {
    private TrophyRequirement requirement;

    public static TrophyRequirementWriter from(TrophyRequirement requirement) {
        TrophyRequirementWriter writer = new TrophyRequirementWriter();
        writer.requirement = requirement;
        return writer;
    }

    public File saveToFile(File directory) {
        File file = new File(directory + "/" + requirement.key + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        requirement.fireTicks.ifPresent(integer -> config.set("FireTicks", integer));
        requirement.freezeTicks.ifPresent(integer -> config.set("FreezeTicks", integer));
        requirement.lastDamageCause.ifPresent(damageCause -> config.set("LastDamageCause", damageCause.name()));
        requirement.minimumPassengers.ifPresent(integer -> config.set("MinimumPassengers", integer));
        requirement.maximumPassengers.ifPresent(integer -> config.set("MaximumPassengers", integer));
        requirement.ticksLived.ifPresent(integer -> config.set("TicksLived", integer));
        requirement.vehicle.ifPresent(entityType -> config.set("Vehicle", entityType.name()));
        requirement.isCustomNameVisible.ifPresent(aBoolean -> config.set("IsCustomNameVisible", aBoolean));
        requirement.isGlowing.ifPresent(aBoolean -> config.set("IsGlowing", aBoolean));
        requirement.isInWater.ifPresent(aBoolean -> config.set("IsInWater", aBoolean));
        requirement.isOnGround.ifPresent(aBoolean -> config.set("IsOnGround", aBoolean));
        requirement.isPersistent.ifPresent(aBoolean -> config.set("IsPersistent", aBoolean));
        requirement.isSilent.ifPresent(aBoolean -> config.set("IsSilent", aBoolean));
        requirement.customName.ifPresent(string -> config.set("CustomName", string));
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}
