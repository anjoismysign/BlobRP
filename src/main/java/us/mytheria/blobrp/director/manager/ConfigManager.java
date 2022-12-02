package us.mytheria.blobrp.director.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.SimpleListener;

public class ConfigManager extends RPManager {
    private boolean registerDropNonSoulOnDeath;
    private SimpleListener<Boolean> keepExperienceOnDeath;
    private SimpleListener<Integer> dropExperienceOnDeath;

    public ConfigManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        BlobRP main = BlobRP.getInstance();
        FileConfiguration config = main.getConfig();
        config.options().copyDefaults(true);
        ConfigurationSection listeners = config.getConfigurationSection("Listeners");
        registerDropNonSoulOnDeath = listeners.getBoolean("DropNonSoulOnDeath.Register");
        keepExperienceOnDeath = SimpleListener.BOOLEAN(listeners.getConfigurationSection("KeepExperienceOnDeath"), "Keep");
        dropExperienceOnDeath = SimpleListener.INTEGER(listeners.getConfigurationSection("DropExperienceOnDeath"), "Amount");
        main.saveConfig();
    }

    public boolean registerDropNonSoulOnDeath() {
        return registerDropNonSoulOnDeath;
    }

    public SimpleListener<Boolean> keepExperienceOnDeath() {
        return keepExperienceOnDeath;
    }

    public SimpleListener<Integer> dropExperienceOnDeath() {
        return dropExperienceOnDeath;
    }
}
