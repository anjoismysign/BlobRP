package us.mytheria.blobrp.director.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;

public class ConfigManager extends RPManager {
    private SimpleEventListener<Boolean> dropNonSoulOnDeath;
    private SimpleEventListener<Boolean> playerKeepExperienceOnDeath;
    private SimpleEventListener<Integer> playerDropExperienceOnDeath;
    private SimpleEventListener<Integer> entitiesDropExperienceOnDeath;
    private SimpleEventListener<Boolean> entitiesClearDropsOnDeath;
    private SimpleEventListener<Boolean> entityDropItem;
    private SimpleEventListener<Double> sellArticles;


    public ConfigManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        BlobRP main = BlobRP.getInstance();
        FileConfiguration config = main.getConfig();
        config.options().copyDefaults(true);
        ConfigurationSection listeners = config.getConfigurationSection("Listeners");
        dropNonSoulOnDeath = SimpleEventListener.BOOLEAN(listeners.getConfigurationSection("DropNonSoulOnDeath"), "Drop");
        playerKeepExperienceOnDeath = SimpleEventListener.BOOLEAN(listeners.getConfigurationSection("PlayerKeepExperienceOnDeath"), "Keep");
        playerDropExperienceOnDeath = SimpleEventListener.INTEGER(listeners.getConfigurationSection("PlayerDropExperienceOnDeath"), "Amount");
        entitiesDropExperienceOnDeath = SimpleEventListener.INTEGER(listeners.getConfigurationSection("EntitiesDropExperienceOnDeath"), "Amount");
        entitiesClearDropsOnDeath = SimpleEventListener.BOOLEAN(listeners.getConfigurationSection("EntitiesClearDropsOnDeath"), "Clear");
        entityDropItem = SimpleEventListener.BOOLEAN(listeners.getConfigurationSection("EntityDropItem"), "Cancel");
        sellArticles = SimpleEventListener.DOUBLE(listeners.getConfigurationSection("SellArticles"), "DefaultPrice");
        main.saveConfig();
    }

    public SimpleEventListener<Boolean> dropNonSoulOnDeath() {
        return dropNonSoulOnDeath;
    }

    public SimpleEventListener<Boolean> playerKeepExperienceOnDeath() {
        return playerKeepExperienceOnDeath;
    }

    public SimpleEventListener<Integer> playerDropExperienceOnDeath() {
        return playerDropExperienceOnDeath;
    }

    public SimpleEventListener<Integer> entitiesDropExperienceOnDeath() {
        return entitiesDropExperienceOnDeath;
    }

    public SimpleEventListener<Boolean> entitiesClearDropsOnDeath() {
        return entitiesClearDropsOnDeath;
    }

    public SimpleEventListener<Boolean> entityDropItem() {
        return entityDropItem;
    }

    public SimpleEventListener<Double> sellArticles() {
        return sellArticles;
    }
}
