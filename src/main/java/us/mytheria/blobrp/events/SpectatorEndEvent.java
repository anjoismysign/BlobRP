package us.mytheria.blobrp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpectatorEndEvent extends SpectatorEvent {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public SpectatorEndEvent(Player player, boolean isAsync) {
        super(player, isAsync);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
