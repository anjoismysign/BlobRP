package us.mytheria.blobrp.director.manager;

import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.listeners.DropExperienceOnDeath;
import us.mytheria.blobrp.listeners.DropNonSoulOnDeath;
import us.mytheria.blobrp.listeners.KeepExperienceOnDeath;

public class ListenerManager extends RPManager {

    public ListenerManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        ConfigManager configManager = getManagerDirector().getConfigManager();
        if (configManager.registerDropNonSoulOnDeath())
            new DropNonSoulOnDeath();
        if (configManager.dropExperienceOnDeath().register())
            new DropExperienceOnDeath(configManager);
        if (configManager.keepExperienceOnDeath().register())
            new KeepExperienceOnDeath(configManager);
    }
}
