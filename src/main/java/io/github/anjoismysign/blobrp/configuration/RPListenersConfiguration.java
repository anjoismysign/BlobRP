package io.github.anjoismysign.blobrp.configuration;

public class RPListenersConfiguration {
    private boolean dropNonSoulOnDeath;
    private boolean keepExperienceOnDeath;
    private boolean entitiesClearDropsOnDeath;
    private boolean entityDropItem;
    private boolean blockFade;
    private boolean translateOnPickup;
    private boolean translateOnLocaleSwitch;
    private boolean translateOnJoin;
    private boolean spawnOnNewProfile;
    private boolean spawnOnRespawn;
    private boolean highestKitPriorityOnRespawn;
    private boolean defaultKitOnNewProfile;
    private boolean cancelTameableDeathMessageEvent;

    public boolean isDropNonSoulOnDeath() {
        return dropNonSoulOnDeath;
    }

    public void setDropNonSoulOnDeath(boolean dropNonSoulOnDeath) {
        this.dropNonSoulOnDeath = dropNonSoulOnDeath;
    }

    public boolean isKeepExperienceOnDeath() {
        return keepExperienceOnDeath;
    }

    public void setKeepExperienceOnDeath(boolean keepExperienceOnDeath) {
        this.keepExperienceOnDeath = keepExperienceOnDeath;
    }

    public boolean isEntitiesClearDropsOnDeath() {
        return entitiesClearDropsOnDeath;
    }

    public void setEntitiesClearDropsOnDeath(boolean entitiesClearDropsOnDeath) {
        this.entitiesClearDropsOnDeath = entitiesClearDropsOnDeath;
    }

    public boolean isEntityDropItem() {
        return entityDropItem;
    }

    public void setEntityDropItem(boolean entityDropItem) {
        this.entityDropItem = entityDropItem;
    }

    public boolean isBlockFade() {
        return blockFade;
    }

    public void setBlockFade(boolean blockFade) {
        this.blockFade = blockFade;
    }

    public boolean isTranslateOnPickup() {
        return translateOnPickup;
    }

    public void setTranslateOnPickup(boolean translateOnPickup) {
        this.translateOnPickup = translateOnPickup;
    }

    public boolean isTranslateOnLocaleSwitch() {
        return translateOnLocaleSwitch;
    }

    public void setTranslateOnLocaleSwitch(boolean translateOnLocaleSwitch) {
        this.translateOnLocaleSwitch = translateOnLocaleSwitch;
    }

    public boolean isTranslateOnJoin() {
        return translateOnJoin;
    }

    public void setTranslateOnJoin(boolean translateOnJoin) {
        this.translateOnJoin = translateOnJoin;
    }

    public boolean isSpawnOnNewProfile() {
        return spawnOnNewProfile;
    }

    public void setSpawnOnNewProfile(boolean spawnOnNewProfile) {
        this.spawnOnNewProfile = spawnOnNewProfile;
    }

    public boolean isSpawnOnRespawn() {
        return spawnOnRespawn;
    }

    public void setSpawnOnRespawn(boolean spawnOnRespawn) {
        this.spawnOnRespawn = spawnOnRespawn;
    }

    public boolean isHighestKitPriorityOnRespawn() {
        return highestKitPriorityOnRespawn;
    }

    public void setHighestKitPriorityOnRespawn(boolean highestKitPriorityOnRespawn) {
        this.highestKitPriorityOnRespawn = highestKitPriorityOnRespawn;
    }

    public boolean isDefaultKitOnNewProfile() {
        return defaultKitOnNewProfile;
    }

    public void setDefaultKitOnNewProfile(boolean defaultKitOnNewProfile) {
        this.defaultKitOnNewProfile = defaultKitOnNewProfile;
    }

    public boolean isCancelTameableDeathMessageEvent() {
        return cancelTameableDeathMessageEvent;
    }

    public void setCancelTameableDeathMessageEvent(boolean cancelTameableDeathMessageEvent) {
        this.cancelTameableDeathMessageEvent = cancelTameableDeathMessageEvent;
    }
}
