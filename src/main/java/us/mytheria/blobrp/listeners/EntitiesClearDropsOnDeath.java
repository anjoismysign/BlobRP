package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import us.mytheria.bloblib.utilities.Debug;
import us.mytheria.blobrp.director.manager.ConfigManager;

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
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getType() == EntityType.PLAYER) {
            Debug.debug("Player died, not dropping items");
            return;
        }
        e.getDrops().clear();
    }
}
