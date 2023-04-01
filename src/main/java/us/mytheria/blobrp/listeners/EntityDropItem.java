package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDropItemEvent;
import us.mytheria.blobrp.director.manager.ConfigManager;

public class EntityDropItem extends RPListener {
    private boolean cancel;

    public EntityDropItem(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().entityDropItem().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
            cancel = getConfigManager().entityDropItem().value();
        }
    }

    @EventHandler
    public void onSpawn(EntityDropItemEvent event) {
        event.setCancelled(cancel);
    }
}
