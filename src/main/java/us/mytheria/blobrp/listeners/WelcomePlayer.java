package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.bloblib.entities.inventory.BlobPlayerInventoryHolder;
import us.mytheria.bloblib.entities.inventory.InventoryBuilderCarrier;
import us.mytheria.bloblib.entities.inventory.InventoryButton;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;
import us.mytheria.blobrp.director.manager.ConfigManager;

public class WelcomePlayer extends RPListener {
    private SimpleEventListener<String> welcomePlayers;
    private final InventoryBuilderCarrier<InventoryButton> carrier;

    public WelcomePlayer(ConfigManager configManager) {
        super(configManager);
        carrier = BlobLibAssetAPI.getInventoryBuilderCarrier("WelcomeInventory");
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().welcomePlayers().register()) {
            welcomePlayers = getConfigManager().welcomePlayers();
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPlayedBefore())
            return;
        ReferenceBlobMessage message = BlobLibAssetAPI.getMessage(welcomePlayers.value());
        message.modify(string -> string.replace("%player%", player.getName()));
        message.sendAndPlayInWorld(player);
        BlobPlayerInventoryHolder.fromInventoryBuilderCarrier
                (carrier, player.getUniqueId());
    }
}
