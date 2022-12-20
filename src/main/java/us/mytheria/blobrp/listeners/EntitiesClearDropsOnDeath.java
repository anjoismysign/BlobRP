package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import us.mytheria.bloblib.utilities.Debug;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.manager.ConfigManager;

public class EntitiesClearDropsOnDeath implements Listener {

    public EntitiesClearDropsOnDeath(ConfigManager configManager) {
        BlobRP main = BlobRP.getInstance();
        Bukkit.getPluginManager().registerEvents(this, main);
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
