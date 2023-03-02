package us.mytheria.blobrp.events;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
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
     * @param display   The display item of the article
     * @param buyPrice  The buy price of the article
     * @param key       The NamespacedKey of the article
     * @param sellPrice The sell price of the article
     * @return Whether the article was added successfully or not
     */
    public boolean addShopArticle(ItemStack display, double buyPrice, NamespacedKey key, double sellPrice) {
        return BlobRPAPI.addShopArticle(display, buyPrice, key, sellPrice);
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
    public boolean addShopArticle(ItemStack display, double buyPrice, NamespacedKey key) {
        return BlobRPAPI.addShopArticle(display, buyPrice, key);
    }
}
