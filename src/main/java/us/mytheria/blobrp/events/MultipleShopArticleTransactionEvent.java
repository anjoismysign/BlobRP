package us.mytheria.blobrp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import us.mytheria.blobrp.entities.ShopArticleTransaction;

import java.util.Collection;

public abstract class MultipleShopArticleTransactionEvent extends Event implements Cancellable {
    private boolean cancelled;
    private final Collection<ShopArticleTransaction> transaction;
    private final Player player;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public MultipleShopArticleTransactionEvent(Collection<ShopArticleTransaction> transaction, Player player, boolean isAsync) {
        super(isAsync);
        this.transaction = transaction;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @NotNull
    public Collection<ShopArticleTransaction> getTransaction() {
        return transaction;
    }

    @NotNull
    public Player getPlayer() {
        return player;
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
