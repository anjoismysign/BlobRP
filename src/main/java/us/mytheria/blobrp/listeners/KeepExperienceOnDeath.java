package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.mytheria.blobrp.director.manager.ConfigManager;

public class KeepExperienceOnDeath extends RPListener {
    private boolean keepExperienceOnDeath;

    public KeepExperienceOnDeath(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().playerKeepExperienceOnDeath().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
            keepExperienceOnDeath = getConfigManager().playerKeepExperienceOnDeath().value();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setKeepLevel(keepExperienceOnDeath);
    }
}
