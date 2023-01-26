package us.mytheria.blobrp.trophy.requirements;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.blobrp.BlobRPAPI;
import us.mytheria.blobrp.inventories.builder.RPObjectBuilder;
import us.mytheria.blobrp.reward.CashReward;

import java.util.Optional;
import java.util.UUID;

public class UIBuilder extends RPObjectBuilder<CashReward> {
    private final TrophyRequirement requirements;
    protected String key;

    public static UIBuilder build(UUID builderId) {
        return new UIBuilder(BlobRPAPI.buildInventory("TrophyRequirementBuilder"), builderId);
    }

    private UIBuilder(BlobInventory blobInventory, UUID builderId) {
        super(blobInventory, builderId);
        requirements = TrophyRequirement.EMPTY();
        updateDefaultButtons();
    }

    public TrophyRequirement getRequirements() {
        return requirements;
    }


    private UIBuilder updateDefaultButtons() {
        updateDefaultButton("FireTicks", "%fireTicks%",
                this.requirements.fireTicks.map(integer -> integer + "").orElse("Not present"));
        updateDefaultButton("FreezeTicks", "%freezeTicks%",
                this.requirements.freezeTicks.map(integer -> integer + "").orElse("Not present"));
        updateDefaultButton("LastDamageCause", "%lastDamageCause%",
                this.requirements.lastDamageCause.map(damageCause -> damageCause + "").orElse("Not present"));
        updateDefaultButton("MinimumPassengers", "%minimumPassengers%",
                this.requirements.minimumPassengers.map(integer -> integer + "").orElse("Not present"));
        updateDefaultButton("MaximumPassengers", "%maximumPassengers%",
                this.requirements.maximumPassengers.map(integer -> integer + "").orElse("Not present"));
        updateDefaultButton("TicksLived", "%ticksLived%",
                this.requirements.ticksLived.map(integer -> integer + "").orElse("Not present"));
        updateDefaultButton("Vehicle", "%vehicle%",
                this.requirements.vehicle.map(entityType -> entityType + "").orElse("Not present"));
        updateDefaultButton("CustomNameVisible", "%customNameVisible%",
                this.requirements.isCustomNameVisible.map(aBoolean -> aBoolean + "").orElse("Not present"));
        updateDefaultButton("Glowing", "%glowing%",
                this.requirements.isGlowing.map(aBoolean -> aBoolean + "").orElse("Not present"));
        updateDefaultButton("InWater", "%inWater%",
                this.requirements.isInWater.map(aBoolean -> aBoolean + "").orElse("Not present"));
        updateDefaultButton("OnGround", "%onGround%",
                this.requirements.isOnGround.map(aBoolean -> aBoolean + "").orElse("Not present"));
        updateDefaultButton("Persistent", "%persistent%",
                this.requirements.isPersistent.map(aBoolean -> aBoolean + "").orElse("Not present"));
        updateDefaultButton("Silent", "%silent%",
                this.requirements.isSilent.map(aBoolean -> aBoolean + "").orElse("Not present"));
        updateDefaultButton("CustomName", "%customName%",
                this.requirements.customName.map(s -> s + "").orElse("Not present"));
        return this;
    }

    /**
     * Sets the number of fire ticks the entity has remaining.
     *
     * @param fireTicks the number of fire ticks the entity has remaining
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withFireTicks(Integer fireTicks) {
        this.requirements.fireTicks = Optional.ofNullable(fireTicks);
        updateDefaultButton("FireTicks", "%fireTicks%",
                this.requirements.fireTicks.map(integer -> integer + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets the number of freeze ticks the entity has remaining.
     *
     * @param freezeTicks the number of freeze ticks the entity has remaining
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withFreezeTicks(Integer freezeTicks) {
        this.requirements.freezeTicks = Optional.ofNullable(freezeTicks);
        updateDefaultButton("FreezeTicks", "%freezeTicks%",
                this.requirements.freezeTicks.map(integer -> integer + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets the last damage cause that affected the entity.
     *
     * @param lastDamageCause the last damage cause that affected the entity
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withLastDamageCause(EntityDamageEvent.DamageCause lastDamageCause) {
        this.requirements.lastDamageCause = Optional.ofNullable(lastDamageCause);
        updateDefaultButton("LastDamageCause", "%lastDamageCause%",
                this.requirements.lastDamageCause.map(damageCause -> damageCause + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets the minimum number of passengers the entity can have.
     *
     * @param minimumPassengers the minimum number of passengers the entity can have
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withMinimumPassengers(Integer minimumPassengers) {
        this.requirements.minimumPassengers = Optional.ofNullable(minimumPassengers);
        updateDefaultButton("MinimumPassengers", "%minimumPassengers%",
                this.requirements.minimumPassengers.map(integer -> integer + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets the maximum number of passengers the entity can have.
     *
     * @param maximumPassengers the maximum number of passengers the entity can have
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withMaximumPassengers(Integer maximumPassengers) {
        this.requirements.maximumPassengers = Optional.ofNullable(maximumPassengers);
        updateDefaultButton("MaximumPassengers", "%maximumPassengers%",
                this.requirements.maximumPassengers.map(integer -> integer + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets the number of ticks the entity has lived for.
     *
     * @param ticksLived the number of ticks the entity has lived for
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withTicksLived(Integer ticksLived) {
        this.requirements.ticksLived = Optional.ofNullable(ticksLived);
        updateDefaultButton("TicksLived", "%ticksLived%",
                this.requirements.ticksLived.map(integer -> integer + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets the type of vehicle the entity is.
     *
     * @param vehicle the type of vehicle the entity is
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withVehicle(EntityType vehicle) {
        this.requirements.vehicle = Optional.ofNullable(vehicle);
        updateDefaultButton("Vehicle", "%vehicle%",
                this.requirements.vehicle.map(entityType -> entityType + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets whether the entity's custom name is visible.
     *
     * @param isCustomNameVisible whether the entity's custom name is visible
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withCustomNameVisible(Boolean isCustomNameVisible) {
        this.requirements.isCustomNameVisible = Optional.ofNullable(isCustomNameVisible);
        updateDefaultButton("CustomNameVisible", "%customNameVisible%",
                this.requirements.isCustomNameVisible.map(aBoolean -> aBoolean + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets whether the entity is glowing.
     *
     * @param isGlowing whether the entity is glowing
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withGlowing(Boolean isGlowing) {
        this.requirements.isGlowing = Optional.ofNullable(isGlowing);
        updateDefaultButton("Glowing", "%glowing%",
                this.requirements.isGlowing.map(aBoolean -> aBoolean + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets whether the entity is in water.
     *
     * @param isInWater whether the entity is in water
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withInWater(Boolean isInWater) {
        this.requirements.isInWater = Optional.ofNullable(isInWater);
        updateDefaultButton("InWater", "%inWater%",
                this.requirements.isInWater.map(aBoolean -> aBoolean + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets whether the entity is on the ground.
     *
     * @param isOnGround whether the entity is on the ground
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withOnGround(Boolean isOnGround) {
        this.requirements.isOnGround = Optional.ofNullable(isOnGround);
        updateDefaultButton("OnGround", "%onGround%",
                this.requirements.isOnGround.map(aBoolean -> aBoolean + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets whether the entity is persistent.
     *
     * @param isPersistent whether the entity is persistent
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withPersistent(Boolean isPersistent) {
        this.requirements.isPersistent = Optional.ofNullable(isPersistent);
        updateDefaultButton("Persistent", "%persistent%",
                this.requirements.isPersistent.map(aBoolean -> aBoolean + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets whether the entity is silent.
     *
     * @param isSilent whether the entity is silent
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withSilent(Boolean isSilent) {
        this.requirements.isSilent = Optional.ofNullable(isSilent);
        updateDefaultButton("Silent", "%silent%",
                this.requirements.isSilent.map(aBoolean -> aBoolean + "").orElse("Not present"));
        openInventory();
        return this;
    }

    /**
     * Sets the custom name of the entity.
     *
     * @param customName the custom name of the entity
     * @return this TrophyRequirements object, for method chaining
     */
    protected UIBuilder withCustomName(String customName) {
        this.requirements.customName = Optional.ofNullable(customName);
        updateDefaultButton("CustomName", "%customName%",
                this.requirements.customName.map(s -> s + "").orElse("Not present"));
        openInventory();
        return this;
    }
}
