package us.mytheria.blobrp.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import us.mytheria.blobrp.entities.ShopArticle;

public class ShopArticleEvent extends Event {
    private final ShopArticle shopArticle;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public ShopArticleEvent(ShopArticle shopArticle, boolean isAsync) {
        super(isAsync);
        this.shopArticle = shopArticle;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @NotNull
    public ShopArticle getShopArticle() {
        return shopArticle;
    }
}
