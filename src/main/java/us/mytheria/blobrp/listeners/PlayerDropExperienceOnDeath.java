package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.mytheria.blobrp.director.manager.ConfigManager;

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
