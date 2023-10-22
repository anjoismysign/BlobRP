package us.mytheria.blobrp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import us.mytheria.bloblib.api.BlobLibInventoryAPI;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.bloblib.entities.inventory.ButtonManager;
import us.mytheria.bloblib.entities.inventory.InventoryBuilderCarrier;
import us.mytheria.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import us.mytheria.bloblib.entities.inventory.MetaInventoryButton;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;
import us.mytheria.blobrp.RPShortcut;
import us.mytheria.blobrp.director.manager.ConfigManager;

public class WelcomePlayer extends RPListener {
    private SimpleEventListener<String> welcomePlayers;
    private InventoryBuilderCarrier<MetaInventoryButton> carrier;
    private boolean isConverted;

    public WelcomePlayer(ConfigManager configManager) {
        super(configManager);
        this.carrier = BlobLibInventoryAPI.getInstance().getMetaInventoryBuilderCarrier("WelcomeInventory");
        isConverted = false;
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
        ReferenceBlobMessage message = BlobLibMessageAPI.getInstance()
                .getMessage(welcomePlayers.value(), player);
        message.modder()
                .replace("%player%", player.getName())
                .get()
                .handle(player);
        if (!isConverted) {
            isConverted = true;
            InventoryBuilderCarrier<MetaInventoryButton> x = BlobLibInventoryAPI
                    .getInstance()
                    .getMetaInventoryBuilderCarrier("WelcomeInventory",
                            player.getLocale());
            ButtonManager<MetaInventoryButton> buttonManager = RPShortcut.getInstance().rewriteShopArticles(x.buttonManager());
            this.carrier = new InventoryBuilderCarrier<>(x.title(), x.size(), buttonManager,
                    x.type(), x.reference(), x.locale());
        }
        MetaBlobPlayerInventoryBuilder.fromInventoryBuilderCarrier
                (carrier, player.getUniqueId());
    }
}
