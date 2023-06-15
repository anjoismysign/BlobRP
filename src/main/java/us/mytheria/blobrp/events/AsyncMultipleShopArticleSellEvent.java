package us.mytheria.blobrp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import us.mytheria.blobrp.entities.ShopArticleTransaction;

import java.util.Collection;

public class AsyncMultipleShopArticleSellEvent extends AsyncMultipleShopArticleTransactionEvent {
    public AsyncMultipleShopArticleSellEvent(Collection<ShopArticleTransaction> transaction,
                                             Player player,
                                             TransactionType transactionType) {
        super(transaction, player, transactionType);
    }

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
