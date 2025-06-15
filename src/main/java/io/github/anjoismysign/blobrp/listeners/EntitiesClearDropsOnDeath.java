package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

public class EntitiesClearDropsOnDeath extends RPListener {

    public EntitiesClearDropsOnDeath(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().entitiesClearDropsOnDeath().register())
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER)
            return;
        event.getDrops().clear();
    }
}
