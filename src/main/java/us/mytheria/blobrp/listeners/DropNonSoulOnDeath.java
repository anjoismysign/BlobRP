package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.SoulAPI;

public class DropNonSoulOnDeath implements Listener {
    private final BlobRP main;

    public DropNonSoulOnDeath() {
        this.main = BlobRP.getInstance();
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        SoulAPI.dropNonSouls(player);
    }
}
