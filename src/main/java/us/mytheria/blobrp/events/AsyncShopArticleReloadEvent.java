package us.mytheria.blobrp.events;

import com.mongodb.lang.Nullable;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.blobrp.BlobRPAPI;

public class AsyncShopArticleReloadEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public AsyncShopArticleReloadEvent() {
        super(true);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    /**
     * Will add/register a ShopArticle to the plugin.
     * It's not needed to reload merchants because
     * it is automatically done after this event.
     *
     * @param display         The display item of the article
     * @param buyPrice        The buy price of the article
     * @param key             The NamespacedKey of the article
     * @param buyingCurrency  The buying currency. if null, the default currency is used.
     * @param sellingCurrency The selling currency. if null, the default currency is used.
     * @return Whether the article was added successfully or not
     */
    public boolean addComplexShopArticle(TranslatableItem display, double buyPrice, double sellPrice,
                                         NamespacedKey key,
                                         @Nullable String buyingCurrency,
                                         @Nullable String sellingCurrency) {
        return BlobRPAPI.getInstance().addComplexShopArticle(display, buyPrice, key, sellPrice, buyingCurrency, sellingCurrency);
    }

    /**
     * Will add/register a ShopArticle to the plugin.
     * It's not needed to reload merchants because
     * it is automatically done after this event.
     * Sell price will be set to the 10% of the buy price.
     *
     * @param display  The display item of the article
     * @param buyPrice The buy price of the article
     * @param key      The NamespacedKey of the article
     * @return Whether the article was added successfully or not
     */
    public boolean addShopArticle(TranslatableItem display, double buyPrice, NamespacedKey key) {
        return BlobRPAPI.getInstance().addComplexShopArticle(display, buyPrice, key);
    }
}
