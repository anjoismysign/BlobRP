package us.mytheria.blobrp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpectatorStartEvent extends SpectatorEvent implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean cancelled;

    public SpectatorStartEvent(Player player, boolean isAsync) {
        super(player, isAsync);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Set the event to be cancelled or not.
     * If so, player will automatically respawn.
     *
     * @param cancel True to cancel the event.
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
