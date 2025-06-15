package io.github.anjoismysign.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import io.github.anjoismysign.bloblib.api.BlobLibInventoryAPI;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.entities.SimpleEventListener;
import io.github.anjoismysign.bloblib.entities.inventory.InventoryBuilderCarrier;
import io.github.anjoismysign.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import io.github.anjoismysign.bloblib.entities.inventory.MetaInventoryButton;
import io.github.anjoismysign.bloblib.entities.message.BlobMessage;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

public class WelcomePlayer extends RPListener {
    private SimpleEventListener<String> welcomePlayers;
    private String reference;

    public WelcomePlayer(ConfigManager configManager) {
        super(configManager);
        this.reference = "WelcomeInventory";
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
        InventoryBuilderCarrier<MetaInventoryButton> carrier =
                BlobLibInventoryAPI.getInstance()
                        .getMetaInventoryBuilderCarrier(reference, player.getLocale());
        if (player.hasPlayedBefore())
            return;
        BlobMessage message = BlobLibMessageAPI.getInstance()
                .getMessage(welcomePlayers.value(), player);
        message.modder()
                .replace("%player%", player.getName())
                .get()
                .handle(player);
        MetaBlobPlayerInventoryBuilder.fromInventoryBuilderCarrier
                (carrier, player.getUniqueId());
    }
}
