package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.event.Listener;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

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

    public abstract void reload();
}
