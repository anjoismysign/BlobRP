package us.mytheria.blobrp.events;

import org.bukkit.entity.Player;
import us.mytheria.blobrp.entities.ShopArticleTransaction;

import java.util.Collection;

public class AsyncMultipleShopArticleSellEvent extends AsyncMultipleShopArticleTransactionEvent {
    public AsyncMultipleShopArticleSellEvent(Collection<ShopArticleTransaction> transaction,
                                             Player player,
                                             TransactionType transactionType) {
        super(transaction, player, transactionType);
    }
}
