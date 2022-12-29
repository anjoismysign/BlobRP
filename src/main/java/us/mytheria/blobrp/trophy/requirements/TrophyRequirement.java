package us.mytheria.blobrp.trophy.requirements;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;

/**
 * The TrophyRequirements class represents the requirements that must be met in order to receive the reward for hunting a trophy.
 */
public class TrophyRequirement {
    private final String key;

    // The number of fire ticks the entity has remaining
    protected Optional<Integer> fireTicks;

    // The number of freeze ticks the entity has remaining
    protected Optional<Integer> freezeTicks;

    // The last damage cause that affected the entity
    protected Optional<EntityDamageEvent.DamageCause> lastDamageCause;

    // The minimum number of passengers the entity can have
    protected Optional<Integer> minimumPassengers;

    // The maximum number of passengers the entity can have
    protected Optional<Integer> maximumPassengers;

    // The number of ticks the entity has lived for
    protected Optional<Integer> ticksLived;

    // The type of vehicle the entity is
    protected Optional<EntityType> vehicle;

    // Whether the entity's custom name is visible
    protected Optional<Boolean> isCustomNameVisible;

    // Whether the entity is glowing
    protected Optional<Boolean> isGlowing;

    // Whether the entity is in water
    protected Optional<Boolean> isInWater;

    // Whether the entity is on the ground
    protected Optional<Boolean> isOnGround;

    // Whether the entity is persistent
    protected Optional<Boolean> isPersistent;

    // Whether the entity is silent
    protected Optional<Boolean> isSilent;

    // The entity's custom name
    protected Optional<String> customName;

    public static TrophyRequirement EMPTY(String key) {
        return new TrophyRequirement(key,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }

    /**
     * Constructs a new TrophyRequirements with the given values.
     *
     * @param fireTicks           the number of fire ticks the entity has remaining
     * @param freezeTicks         the number of freeze ticks the entity has remaining
     * @param lastDamageCause     the last damage cause that affected the entity
     * @param minimumPassengers   the minimum number of passengers the entity can have
     * @param maximumPassengers   the maximum number of passengers the entity can have
     * @param ticksLived          the number of ticks the entity has lived for
     * @param vehicle             the type of vehicle the entity is
     * @param isCustomNameVisible whether the entity's custom name is visible
     * @param isGlowing           whether the entity is glowing
     * @param isInWater           whether the entity is in water
     * @param isOnGround          whether the entity is on the ground
     * @param isPersistent        whether the entity is persistent
     * @param isSilent            whether the entity is silent
     * @param customName          the entity's custom name
     */
    public TrophyRequirement(String key, Optional<Integer> fireTicks,
                             Optional<Integer> freezeTicks,
                             Optional<EntityDamageEvent.DamageCause> lastDamageCause,
                             Optional<Integer> minimumPassengers,
                             Optional<Integer> maximumPassengers,
                             Optional<Integer> ticksLived,
                             Optional<EntityType> vehicle,
                             Optional<Boolean> isCustomNameVisible,
                             Optional<Boolean> isGlowing,
                             Optional<Boolean> isInWater,
                             Optional<Boolean> isOnGround,
                             Optional<Boolean> isPersistent,
                             Optional<Boolean> isSilent,
                             Optional<String> customName) {
        this.key = key;
        this.fireTicks = fireTicks;
        this.freezeTicks = freezeTicks;
        this.lastDamageCause = lastDamageCause;
        this.minimumPassengers = minimumPassengers;
        this.maximumPassengers = maximumPassengers;
        this.ticksLived = ticksLived;
        this.vehicle = vehicle;
        this.isCustomNameVisible = isCustomNameVisible;
        this.isGlowing = isGlowing;
        this.isInWater = isInWater;
        this.isOnGround = isOnGround;
        this.isPersistent = isPersistent;
        this.isSilent = isSilent;
        this.customName = customName;
    }

    public Optional<Integer> getFireTicks() {
        return fireTicks;
    }

    public Optional<Integer> getFreezeTicks() {
        return freezeTicks;
    }

    public Optional<EntityDamageEvent.DamageCause> getLastDamageCause() {
        return lastDamageCause;
    }

    public Optional<Integer> getMinimumPassengers() {
        return minimumPassengers;
    }

    public Optional<Integer> getMaximumPassengers() {
        return maximumPassengers;
    }

    public Optional<Integer> getTicksLived() {
        return ticksLived;
    }

    public Optional<EntityType> getVehicle() {
        return vehicle;
    }

    public Optional<Boolean> getIsCustomNameVisible() {
        return isCustomNameVisible;
    }

    public Optional<Boolean> getIsGlowing() {
        return isGlowing;
    }

    public Optional<Boolean> getIsInWater() {
        return isInWater;
    }

    public Optional<Boolean> getIsOnGround() {
        return isOnGround;
    }

    public Optional<Boolean> getIsPersistent() {
        return isPersistent;
    }

    public Optional<Boolean> getIsSilent() {
        return isSilent;
    }

    public Optional<String> getCustomName() {
        return customName;
    }

    public String getKey() {
        return key;
    }

    /**
     * Checks if the entity meets the requirements.
     *
     * @param entity the entity to check
     * @return true if the entity meets the requirements, false otherwise
     */
    public boolean meetsRequirements(Entity entity) {
        if (fireTicks.isPresent() && entity.getFireTicks() != fireTicks.get()) {
            return false;
        }
        if (freezeTicks.isPresent() && entity.getFreezeTicks() != freezeTicks.get()) {
            return false;
        }
        if (lastDamageCause.isPresent()) {
            EntityDamageEvent lastDamageEvent = entity.getLastDamageCause();
            if (lastDamageEvent == null || lastDamageEvent.getCause() != lastDamageCause.get()) {
                return false;
            }
        }
        if (minimumPassengers.isPresent() && entity.getPassengers().size() < minimumPassengers.get()) {
            return false;
        }
        if (maximumPassengers.isPresent() && entity.getPassengers().size() > maximumPassengers.get()) {
            return false;
        }
        if (ticksLived.isPresent() && entity.getTicksLived() != ticksLived.get()) {
            return false;
        }
        if (vehicle.isPresent()) {
            Entity vehicle = entity.getVehicle();
            if (vehicle == null || vehicle.getType() != this.vehicle.get()) {
                return false;
            }
            return false;
        }
        if (isCustomNameVisible.isPresent() && entity.isCustomNameVisible() != isCustomNameVisible.get()) {
            return false;
        }
        if (isGlowing.isPresent() && entity.isGlowing() != isGlowing.get()) {
            return false;
        }
        if (isInWater.isPresent() && entity.isInWater() != isInWater.get()) {
            return false;
        }
        if (isOnGround.isPresent() && entity.isOnGround() != isOnGround.get()) {
            return false;
        }
        if (isPersistent.isPresent() && entity.isPersistent() != isPersistent.get()) {
            return false;
        }
        if (isSilent.isPresent() && entity.isSilent() != isSilent.get()) {
            return false;
        }
        return customName.isEmpty() || entity.getCustomName().equals(customName.get());
    }
}