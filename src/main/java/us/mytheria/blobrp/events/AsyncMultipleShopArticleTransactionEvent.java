package us.mytheria.blobrp.events;

import org.bukkit.entity.Player;
import us.mytheria.blobrp.entities.ShopArticleTransaction;

import java.util.Collection;

public abstract class AsyncMultipleShopArticleTransactionEvent extends MultipleShopArticleTransactionEvent {
    public AsyncMultipleShopArticleTransactionEvent(Collection<ShopArticleTransaction> transaction,
                                                    Player player,
                                                    TransactionType transactionType) {
        super(transaction, player, transactionType, true);
    }
}
