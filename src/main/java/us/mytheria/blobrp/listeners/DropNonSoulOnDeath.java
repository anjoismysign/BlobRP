package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.mytheria.blobrp.SoulAPI;
import us.mytheria.blobrp.director.manager.ConfigManager;

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
        SoulAPI.dropNonSouls(player);
    }
}
