package us.mytheria.blobrp.director.manager;

import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.listeners.DropNonSoulOnDeath;

public class ListenerManager extends RPManager {

    public ListenerManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        if (getManagerDirector().getConfigManager().registerDropNonSoulOnDeath())
            new DropNonSoulOnDeath();
    }
}
