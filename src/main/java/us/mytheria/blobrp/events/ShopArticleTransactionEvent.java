package us.mytheria.blobrp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import us.mytheria.blobrp.entities.ShopArticleTransaction;

public abstract class ShopArticleTransactionEvent extends Event implements Cancellable {
    private boolean cancelled;
    private final ShopArticleTransaction transaction;
    private final TransactionType transactionType;
    private final TransactionStatus transactionStatus;
    private final Player player;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public ShopArticleTransactionEvent(ShopArticleTransaction transaction, Player player,
                                       TransactionType transactionType,
                                       TransactionStatus transactionStatus,
                                       boolean isAsync) {
        super(isAsync);
        this.transaction = transaction;
        this.player = player;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @NotNull
    public ShopArticleTransaction getTransaction() {
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

    @NotNull
    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
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
