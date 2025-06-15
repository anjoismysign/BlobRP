package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

public class PlayerHunger extends RPListener {
    private boolean requiresPermission = false;

    public PlayerHunger(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().playerHunger().register()) {
            requiresPermission = getConfigManager().playerHunger().value();
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if (requiresPermission)
            if (player.hasPermission("blobrp.nohunger"))
                event.setCancelled(false);
            else
                event.setCancelled(true);
        else
            event.setCancelled(true);

    }
}
