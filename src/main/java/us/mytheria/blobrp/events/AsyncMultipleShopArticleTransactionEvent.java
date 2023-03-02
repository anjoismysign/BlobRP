package us.mytheria.blobrp.events;

import org.bukkit.entity.Player;
import us.mytheria.blobrp.entities.ShopArticleTransaction;

import java.util.Collection;

public class AsyncMultipleShopArticleTransactionEvent extends MultipleShopArticleTransactionEvent {
    public AsyncMultipleShopArticleTransactionEvent(Collection<ShopArticleTransaction> transaction, Player player) {
        super(transaction, player, true);
    }
}
