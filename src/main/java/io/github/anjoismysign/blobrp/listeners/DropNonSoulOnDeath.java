package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import io.github.anjoismysign.bloblib.SoulAPI;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

public class DropNonSoulOnDeath extends RPListener {

    public DropNonSoulOnDeath(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().dropNonSoulOnDeath().register())
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        SoulAPI.getInstance().dropAll(player);
    }
}
