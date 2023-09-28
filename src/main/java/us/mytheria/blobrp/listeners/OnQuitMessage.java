package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerQuitEvent;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;
import us.mytheria.blobrp.director.manager.ConfigManager;

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
            ReferenceBlobMessage message = BlobLibMessageAPI.getInstance().getMessage(onQuitMessage.value());
            Bukkit.getOnlinePlayers().forEach(player1 -> {
                message.modder()
                        .replace("%player%", player.getName())
                        .get()
                        .handle(player1);
            });
        }
    }
}
