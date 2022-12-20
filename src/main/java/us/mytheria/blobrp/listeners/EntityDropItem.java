package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.manager.ConfigManager;

public class EntityDropItem implements Listener {
    private final boolean cancel;

    public EntityDropItem(ConfigManager configManager) {
        BlobRP main = BlobRP.getInstance();
        Bukkit.getPluginManager().registerEvents(this, main);
        this.cancel = configManager.entityDropItem().value();
    }

    @EventHandler
    public void onSpawn(EntityDropItemEvent e) {
        e.setCancelled(cancel);
    }
}
