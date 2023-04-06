package us.mytheria.blobrp.entities;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.mytheria.bloblib.entities.PlayerTask;
import us.mytheria.blobrp.BlobRP;

public class Spectator {
    private final GameMode gameMode;
    private final PlayerTask task;

    public Spectator(Player player, int seconds, Location respawnLocation) {
        this.gameMode = player.getGameMode();
        player.setGameMode(GameMode.SPECTATOR);
        this.task = PlayerTask.SYNCHRONOUS(player, BlobRP.getInstance(), seconds, spectator -> {
            spectator.setGameMode(getGameMode());
            spectator.teleport(respawnLocation);
        });
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public PlayerTask getTask() {
        return task;
    }
}
