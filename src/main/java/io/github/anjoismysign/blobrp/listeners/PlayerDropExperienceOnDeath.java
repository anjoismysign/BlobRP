package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

public class PlayerDropExperienceOnDeath extends RPListener {
    private int exp;

    public PlayerDropExperienceOnDeath(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().playerDropExperienceOnDeath().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
            exp = getConfigManager().playerDropExperienceOnDeath().value();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDroppedExp(exp);
    }
}
