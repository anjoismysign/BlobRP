package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import io.github.anjoismysign.bloblib.managers.BlobPlugin;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

public class ForceGamemodeOnJoin extends RPListener {
    private GameMode gameMode;

    public ForceGamemodeOnJoin(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        BlobPlugin plugin = getConfigManager().getPlugin();
        getConfigManager().forceGamemode().ifRegister(eventListener -> {
            String value = eventListener.value();
            try {
                gameMode = GameMode.valueOf(value);
                Bukkit.getPluginManager().registerEvents(this, plugin);
            } catch (IllegalArgumentException e) {
                plugin.getAnjoLogger().error("Invalid Gamemode '" + value + "' at " +
                        "BlobRP/config.yml#Listeners#ForceGamemode#Gamemode");
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(gameMode);
    }
}
