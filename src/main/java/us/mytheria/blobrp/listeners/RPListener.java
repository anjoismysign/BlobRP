package us.mytheria.blobrp.listeners;

import org.bukkit.event.Listener;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.director.manager.ConfigManager;

public abstract class RPListener implements Listener {
    private final ConfigManager configManager;

    public RPListener(ConfigManager manager) {
        this.configManager = manager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public RPManagerDirector getManagerDirector() {
        return configManager.getManagerDirector();
    }

    abstract void reload();
}
