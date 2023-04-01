package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import us.mytheria.blobrp.director.manager.ConfigManager;

public class EntitiesDropExperienceOnDeath extends RPListener {
    private int exp;

    public EntitiesDropExperienceOnDeath(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().entitiesDropExperienceOnDeath().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
            exp = getConfigManager().entitiesDropExperienceOnDeath().value();
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        event.setDroppedExp(exp);
    }
}
