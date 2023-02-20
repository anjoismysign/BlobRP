package us.mytheria.blobrp.events;

import org.bukkit.event.Cancellable;
import us.mytheria.blobrp.entities.ShopArticle;

public class AsyncShopArticleSoldEvent extends ShopArticleEvent implements Cancellable {
    private boolean isCancelled;

    public AsyncShopArticleSoldEvent(ShopArticle shopArticle) {
        super(shopArticle, true);
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }
}
