package us.mytheria.blobrp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import us.mytheria.blobdesign.entities.presetblock.PresetBlock;

public class CustomBlockBreakEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @NotNull
    private final PresetBlock<?> presetBlock;
    @NotNull
    private final Player player;
    private boolean cancel;

    public CustomBlockBreakEvent(@NotNull PresetBlock<?> presetBlock,
                                 @NotNull Player player) {
        super(false);
        this.presetBlock = presetBlock;
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
    public PresetBlock<?> getPresetBlock() {
        return presetBlock;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
