package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.manager.ConfigManager;

public class EntitiesDropExperienceOnDeath implements Listener {
    private final int exp;

    public EntitiesDropExperienceOnDeath(ConfigManager configManager) {
        BlobRP main = BlobRP.getInstance();
        Bukkit.getPluginManager().registerEvents(this, main);
        this.exp = configManager.entitiesDropExperienceOnDeath().value();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        e.setDroppedExp(exp);
    }
}
