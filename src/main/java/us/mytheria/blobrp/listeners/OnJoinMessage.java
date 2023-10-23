package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.blobrp.director.manager.ConfigManager;

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
