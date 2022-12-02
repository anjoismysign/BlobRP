package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.manager.ConfigManager;

public class DropExperienceOnDeath implements Listener {
    private final int exp;

    public DropExperienceOnDeath(ConfigManager configManager) {
        BlobRP main = BlobRP.getInstance();
        Bukkit.getPluginManager().registerEvents(this, main);
        this.exp = configManager.dropExperienceOnDeath().value();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDroppedExp(exp);
    }
}
