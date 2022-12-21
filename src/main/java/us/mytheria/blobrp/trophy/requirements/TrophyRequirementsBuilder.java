package us.mytheria.blobrp.trophy.requirements;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;

public class TrophyRequirementsBuilder {
    private final TrophyRequirements requirements;

    public TrophyRequirementsBuilder() {
        requirements = TrophyRequirements.EMPTY();
    }

    /**
     * Sets the number of fire ticks the entity has remaining.
     *
     * @param fireTicks the number of fire ticks the entity has remaining
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withFireTicks(int fireTicks) {
        this.requirements.fireTicks = Optional.of(fireTicks);
        return this;
    }

    /**
     * Sets the number of freeze ticks the entity has remaining.
     *
     * @param freezeTicks the number of freeze ticks the entity has remaining
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withFreezeTicks(int freezeTicks) {
        this.requirements.freezeTicks = Optional.of(freezeTicks);
        return this;
    }

    /**
     * Sets the last damage cause that affected the entity.
     *
     * @param lastDamageCause the last damage cause that affected the entity
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withLastDamageCause(EntityDamageEvent.DamageCause lastDamageCause) {
        this.requirements.lastDamageCause = Optional.of(lastDamageCause);
        return this;
    }

    /**
     * Sets the minimum number of passengers the entity can have.
     *
     * @param minimumPassengers the minimum number of passengers the entity can have
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withMinimumPassengers(int minimumPassengers) {
        this.requirements.minimumPassengers = Optional.of(minimumPassengers);
        return this;
    }

    /**
     * Sets the maximum number of passengers the entity can have.
     *
     * @param maximumPassengers the maximum number of passengers the entity can have
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withMaximumPassengers(int maximumPassengers) {
        this.requirements.maximumPassengers = Optional.of(maximumPassengers);
        return this;
    }

    /**
     * Sets the number of ticks the entity has lived for.
     *
     * @param ticksLived the number of ticks the entity has lived for
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withTicksLived(int ticksLived) {
        this.requirements.ticksLived = Optional.of(ticksLived);
        return this;
    }

    /**
     * Sets the type of vehicle the entity is.
     *
     * @param vehicle the type of vehicle the entity is
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withVehicle(EntityType vehicle) {
        this.requirements.vehicle = Optional.of(vehicle);
        return this;
    }

    /**
     * Sets whether the entity's custom name is visible.
     *
     * @param isCustomNameVisible whether the entity's custom name is visible
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withCustomNameVisible(boolean isCustomNameVisible) {
        this.requirements.isCustomNameVisible = Optional.of(isCustomNameVisible);
        return this;
    }

    /**
     * Sets whether the entity is glowing.
     *
     * @param isGlowing whether the entity is glowing
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withGlowing(boolean isGlowing) {
        this.requirements.isGlowing = Optional.of(isGlowing);
        return this;
    }

    /**
     * Sets whether the entity is in water.
     *
     * @param isInWater whether the entity is in water
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withInWater(boolean isInWater) {
        this.requirements.isInWater = Optional.of(isInWater);
        return this;
    }

    /**
     * Sets whether the entity is on the ground.
     *
     * @param isOnGround whether the entity is on the ground
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withOnGround(boolean isOnGround) {
        this.requirements.isOnGround = Optional.of(isOnGround);
        return this;
    }

    /**
     * Sets whether the entity is persistent.
     *
     * @param isPersistent whether the entity is persistent
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withPersistent(boolean isPersistent) {
        this.requirements.isPersistent = Optional.of(isPersistent);
        return this;
    }

    /**
     * Sets whether the entity is silent.
     *
     * @param isSilent whether the entity is silent
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withSilent(boolean isSilent) {
        this.requirements.isSilent = Optional.of(isSilent);
        return this;
    }

    /**
     * Sets the custom name of the entity.
     *
     * @param customName the custom name of the entity
     * @return this TrophyRequirements object, for method chaining
     */
    protected TrophyRequirementsBuilder withCustomName(String customName) {
        this.requirements.customName = Optional.of(customName);
        return this;
    }

    public TrophyRequirements build() {
        return requirements;
    }
}
