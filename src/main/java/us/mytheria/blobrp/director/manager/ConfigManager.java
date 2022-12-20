package us.mytheria.blobrp.director.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.SimpleListener;

public class ConfigManager extends RPManager {
    private SimpleListener<Boolean> dropNonSoulOnDeath;
    private SimpleListener<Boolean> playerKeepExperienceOnDeath;
    private SimpleListener<Integer> playerDropExperienceOnDeath;
    private SimpleListener<Integer> entitiesDropExperienceOnDeath;
    private SimpleListener<Boolean> entitiesClearDropsOnDeath;
    private SimpleListener<Boolean> entityDropItem;


    public ConfigManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        BlobRP main = BlobRP.getInstance();
        FileConfiguration config = main.getConfig();
        config.options().copyDefaults(true);
        ConfigurationSection listeners = config.getConfigurationSection("Listeners");
        dropNonSoulOnDeath = SimpleListener.BOOLEAN(listeners.getConfigurationSection("DropNonSoulOnDeath"), "Drop");
        playerKeepExperienceOnDeath = SimpleListener.BOOLEAN(listeners.getConfigurationSection("PlayerKeepExperienceOnDeath"), "Keep");
        playerDropExperienceOnDeath = SimpleListener.INTEGER(listeners.getConfigurationSection("PlayerDropExperienceOnDeath"), "Amount");
        entitiesDropExperienceOnDeath = SimpleListener.INTEGER(listeners.getConfigurationSection("EntitiesDropExperienceOnDeath"), "Amount");
        entitiesClearDropsOnDeath = SimpleListener.BOOLEAN(listeners.getConfigurationSection("EntitiesClearDropsOnDeath"), "Clear");
        entityDropItem = SimpleListener.BOOLEAN(listeners.getConfigurationSection("EntityDropItem"), "Cancel");
        main.saveConfig();
    }

    public SimpleListener<Boolean> dropNonSoulOnDeath() {
        return dropNonSoulOnDeath;
    }

    public SimpleListener<Boolean> playerKeepExperienceOnDeath() {
        return playerKeepExperienceOnDeath;
    }

    public SimpleListener<Integer> playerDropExperienceOnDeath() {
        return playerDropExperienceOnDeath;
    }

    public SimpleListener<Integer> entitiesDropExperienceOnDeath() {
        return entitiesDropExperienceOnDeath;
    }

    public SimpleListener<Boolean> entitiesClearDropsOnDeath() {
        return entitiesClearDropsOnDeath;
    }

    public SimpleListener<Boolean> entityDropItem() {
        return entityDropItem;
    }
}
