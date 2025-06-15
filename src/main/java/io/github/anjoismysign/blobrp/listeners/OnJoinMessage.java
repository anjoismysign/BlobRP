package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.entities.SimpleEventListener;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

public class OnJoinMessage extends RPListener {
    private SimpleEventListener<String> onJoinMessage;

    public OnJoinMessage(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().onJoinMessage().register()) {
            onJoinMessage = getConfigManager().onJoinMessage();
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        if (onJoinMessage.value() != null) {
            BlobLibMessageAPI.getInstance().broadcast(onJoinMessage.value(), modder -> {
                modder.replace("%player%", player.getName());
            });
        }
    }
}
