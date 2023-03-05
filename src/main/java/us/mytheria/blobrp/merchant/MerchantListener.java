package us.mytheria.blobrp.merchant;

import me.anjoismysign.anjo.entities.Result;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import us.mytheria.bloblib.BlobLibAPI;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.inventory.MetaInventoryButton;
import us.mytheria.bloblib.entities.message.BlobMessage;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;
import us.mytheria.bloblib.utilities.PlayerUtil;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.entities.ShopArticleTransaction;
import us.mytheria.blobrp.events.ShopArticleSellEvent;
import us.mytheria.blobrp.events.TransactionStatus;
import us.mytheria.blobrp.events.TransactionType;
import us.mytheria.blobrp.inventories.MerchantInventory;
import us.mytheria.blobrp.listeners.RPListener;

import java.util.HashMap;

public class MerchantListener extends RPListener {
    private final BlobRP plugin;
    private ReferenceBlobMessage boughtMessage;

    public MerchantListener(ConfigManager configManager) {
        super(configManager);
        this.plugin = configManager.getPlugin();
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        if (getConfigManager().merchants().register()) {
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
            boughtMessage = BlobLibAssetAPI.getMessage(getConfigManager().merchants().value());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        String invname = e.getView().getTitle();
        HashMap<String, MerchantInventory> merchants = plugin.getManagerDirector().getMerchantManager().getMerchantsByTitle();
        if (!merchants.containsKey(invname))
            return;
        e.setCancelled(true);
        MerchantInventory merchantInventory = merchants.get(invname);
        Player player = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();
        Result<MetaInventoryButton> result = merchantInventory.belongsToAMetaButton(slot);
        if (!result.isValid())
            return;
        MetaInventoryButton button = result.value();
        if (!button.hasMeta())
            return;
        String meta = button.getMeta();
        switch (meta) {
            case "CLOSEINVENTORY" -> {
                player.closeInventory();
            }
            case "BLOBRP_SHOPARTICLE" -> {
                Result<ShopArticle> articleResult = merchantInventory.isLinked(button);
                if (!articleResult.isValid())
                    return;
                ShopArticle article = articleResult.value();
                double price = article.getBuyPrice();
                ReferenceBlobMessage referenceNotEnough = BlobLibAssetAPI.getMessage("Economy.Not-Enough");
                if (!BlobLibAPI.hasCashAmount(player, price)) {
                    BlobMessage message = referenceNotEnough.modify(string -> string.replace("%display%",
                            BlobLibAPI.getCash(player) - price + ""));
                    ShopArticleSellEvent event = new ShopArticleSellEvent(
                            new ShopArticleTransaction(article, 1),
                            player, TransactionType.SELL, TransactionStatus.NOT_ENOUGH_MONEY,
                            false, boughtMessage, referenceNotEnough, message, null,
                            price);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled())
                        return;
                    if (event.getNotEnoughMessage() != null)
                        event.getNotEnoughMessage().sendAndPlayInWorld(player);
                    player.closeInventory();
                    return;
                }
                BlobLibAPI.withdrawCash(player, price);
                BlobMessage message = boughtMessage.modify(s -> s.replace("%display%", price + ""));
                ShopArticleSellEvent event = new ShopArticleSellEvent(
                        new ShopArticleTransaction(article, 1),
                        player, TransactionType.SELL, TransactionStatus.SUCCESS,
                        false, boughtMessage, referenceNotEnough, null, message,
                        price);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled())
                    return;
                if (event.getSuccessMessage() != null)
                    event.getSuccessMessage().sendAndPlayInWorld(player);
                PlayerUtil.giveItemToInventoryOrDrop(player, article.cloneDisplay());
            }
            default -> {
                BlobLibAssetAPI.getMessage("System.Error").sendAndPlayInWorld(player);
                Bukkit.getLogger().info("Unknown meta " + meta);
            }
        }
    }
}
