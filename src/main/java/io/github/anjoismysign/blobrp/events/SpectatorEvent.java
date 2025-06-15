package io.github.anjoismysign.blobrp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class SpectatorEvent extends Event {

    private final Player player;

    public SpectatorEvent(Player player,
                          boolean isAsync) {
        super(isAsync);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
