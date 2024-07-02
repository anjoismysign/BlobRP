package us.mytheria.blobrp.merchant;

import me.anjoismysign.anjo.entities.Result;
import net.milkbowl.vault.economy.IdentityEconomy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.api.BlobLibEconomyAPI;
import us.mytheria.bloblib.api.BlobLibInventoryAPI;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.entities.inventory.InventoryDataRegistry;
import us.mytheria.bloblib.entities.inventory.MetaBlobInventoryTracker;
import us.mytheria.bloblib.entities.inventory.MetaInventoryButton;
import us.mytheria.bloblib.entities.message.BlobMessage;
import us.mytheria.bloblib.managers.MetaInventoryShard;
import us.mytheria.bloblib.utilities.PlayerUtil;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.entities.ShopArticleTransaction;
import us.mytheria.blobrp.events.ShopArticleSaleFailEvent;
import us.mytheria.blobrp.events.ShopArticleSellEvent;
import us.mytheria.blobrp.events.TransactionStatus;
import us.mytheria.blobrp.events.TransactionType;
import us.mytheria.blobrp.inventories.MerchantInventory;

import java.util.*;

public class MerchantManager extends RPManager {
    private String boughtMessage;
    private final Set<String> cache;

    public MerchantManager(RPManagerDirector director) {
        super(director);
        cache = new HashSet<>();
        reload();
        new MerchantCmd(director);
    }

    public Set<String> getMerchantKeys() {
        return Collections.unmodifiableSet(cache);
    }

    @Nullable
    public MerchantInventory getMerchant(@NotNull String key, @NotNull Player player) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(player);
        MetaBlobInventoryTracker tracker = BlobLibInventoryAPI.getInstance()
                .trackMetaInventory(player, key);
        if (tracker == null)
            return null;
        return new MerchantInventory(getManagerDirector(), tracker);
    }

    public void reload() {
        cache.clear();
        boughtMessage = getManagerDirector().getConfigManager().merchants().value();
        Optional<MetaInventoryShard> optional = BlobLibInventoryAPI.getInstance()
                .hasMetaInventoryShard("MERCHANT");
        if (optional.isEmpty()) {
            return;
        }
        MetaInventoryShard shard = optional.get();
        shard.allInventories().forEach(merchantInventory -> {
            String key = merchantInventory.getKey();
            if (cache.contains(key))
                return;
            InventoryDataRegistry<MetaInventoryButton> registry =
                    BlobLibInventoryAPI.getInstance()
                            .getMetaInventoryDataRegistry(merchantInventory.getKey());
            if (registry == null)
                throw new NullPointerException("No Registry found for " + key);
            cache.add(key);
            registry.onClick("BlobRP", (clickEvent, button) -> {
                int slot = clickEvent.getRawSlot();
                Result<MetaInventoryButton> result = merchantInventory.belongsToAMetaButton(slot);
                if (!result.isValid())
                    return;
                if (!button.hasMeta())
                    return;
                Player player = (Player) clickEvent.getWhoClicked();
                String meta = button.getMeta();
                switch (meta) {
                    case "CLOSEINVENTORY" -> {
                        player.closeInventory();
                    }
                    case "BLOBRP_SHOPARTICLE" -> {
                        ShopArticle article = isLinked(button);
                        if (article == null)
                            return;
                        double price = article.getBuyPrice();
                        BlobMessage notEnough = BlobLibMessageAPI.getInstance()
                                .getMessage("Economy.Not-Enough", player);
                        Optional<String> buyingCurrency = article.getBuyingCurrency();
                        IdentityEconomy economy = BlobLibEconomyAPI.getInstance().getElasticEconomy().map(buyingCurrency);
                        if (!economy.has(player.getUniqueId(), price)) {
                            ShopArticleSaleFailEvent saleFailEvent = new ShopArticleSaleFailEvent(
                                    article, player, buyingCurrency.orElse(null), price);
                            Bukkit.getPluginManager().callEvent(saleFailEvent);
                            if (saleFailEvent.isFixed()) {
                                handleSale(economy, player, price, article);
                                return;
                            }
                            double missing = price - economy.getBalance(player);
                            BlobMessage message = notEnough
                                    .modder()
                                    .replace("%display%", economy.format(missing))
                                    .get();
                            ShopArticleSellEvent event = new ShopArticleSellEvent(
                                    new ShopArticleTransaction(article, 1),
                                    player, TransactionType.SELL, TransactionStatus.NOT_ENOUGH_MONEY,
                                    false, boughtMessage, "Economy.Not-Enough", message, null,
                                    price);
                            Bukkit.getPluginManager().callEvent(event);
                            if (event.isCancelled())
                                return;
                            if (event.getNotEnoughMessage() != null)
                                event.getNotEnoughMessage().handle(player);
                            player.closeInventory();
                            return;
                        }
                        handleSale(economy, player, price, article);
                    }
                    default -> {
                        BlobLibMessageAPI.getInstance().getMessage("System.Error").handle(player);
                        throw new IllegalStateException("Unknown meta " + meta);
                    }
                }
            });
        });
    }

    private void handleSale(@NotNull IdentityEconomy economy,
                            @NotNull Player player,
                            double price,
                            @NotNull ShopArticle article) {
        economy.withdraw(player.getUniqueId(), price);
        BlobMessage message =
                BlobLibMessageAPI.getInstance().getMessage(boughtMessage, player)
                        .modder()
                        .replace("%display%", economy.format(price))
                        .get();
        ShopArticleSellEvent event = new ShopArticleSellEvent(
                new ShopArticleTransaction(article, 1),
                player, TransactionType.SELL, TransactionStatus.SUCCESS,
                false, boughtMessage, "Economy.Not-Enough", null, message,
                price);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;
        if (event.getSuccessMessage() != null)
            event.getSuccessMessage().handle(player);
        PlayerUtil.giveItemToInventoryOrDrop(player, article.cloneDisplay(player, 1));
    }

    @Nullable
    private ShopArticle isLinked(MetaInventoryButton button) {
        if (button.getSubMeta() == null)
            return null;
        String subMeta = button.getSubMeta();
        ShopArticle article = getManagerDirector().getShopArticleDirector().getObjectManager().getObject(subMeta);
        if (article == null)
            return null;
        return article;
    }
}
