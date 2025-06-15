package io.github.anjoismysign.blobrp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import io.github.anjoismysign.blobrp.entities.ShopArticleTransaction;

import java.util.Collection;

public abstract class MultipleShopArticleTransactionEvent extends Event implements Cancellable {
    private boolean cancelled;
    private final Collection<ShopArticleTransaction> transaction;
    private final TransactionType transactionType;
    private final Player player;

    public MultipleShopArticleTransactionEvent(Collection<ShopArticleTransaction> transaction,
                                               Player player,
                                               TransactionType transactionType,
                                               boolean isAsync) {
        super(isAsync);
        this.transaction = transaction;
        this.player = player;
        this.transactionType = transactionType;
    }

    @NotNull
    public Collection<ShopArticleTransaction> getTransaction() {
        return transaction;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public TransactionType getTransactionType() {
        return transactionType;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
