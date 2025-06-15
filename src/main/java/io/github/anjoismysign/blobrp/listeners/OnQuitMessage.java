package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerQuitEvent;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.entities.SimpleEventListener;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

public class OnQuitMessage extends RPListener {
    private SimpleEventListener<String> onQuitMessage;

    public OnQuitMessage(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().onJoinMessage().register()) {
            onQuitMessage = getConfigManager().onQuitMessage();
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        if (onQuitMessage.value() != null) {
            BlobLibMessageAPI.getInstance().broadcast(onQuitMessage.value(), modder -> {
                modder.replace("%player%", player.getName());
            });
        }
    }
}
