package us.mytheria.blobrp.util;

import me.anjoismysign.anjo.entities.Uber;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.entities.MinecraftTimeUnit;
import us.mytheria.bloblib.entities.message.BlobMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class RoleplayMovementWarmup implements Listener {
    private static RoleplayMovementWarmup instance;

    public static RoleplayMovementWarmup initialize(@NotNull Plugin plugin) {
        if (instance == null)
            instance = new RoleplayMovementWarmup(plugin);
        return instance;
    }

    public static RoleplayMovementWarmup getInstance() {
        return instance;
    }

    private final Plugin plugin;
    private final Map<UUID, String> warmup = new HashMap<>();


    private RoleplayMovementWarmup(Plugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        warmup.remove(uuid);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!warmup.containsKey(uuid))
            return;
        BlockVector from = event.getFrom().toVector().toBlockVector();
        BlockVector to = event.getTo().toVector().toBlockVector();
        if (from.equals(to))
            return;
        String failMessageKey = warmup.remove(uuid);
        BlobMessage blobMessage = BlobLibMessageAPI.getInstance().getMessage(failMessageKey, player);
        Objects.requireNonNull(blobMessage, "failMessageKey didn't point to a BlobMessage: " + failMessageKey);
        blobMessage.handle(player);
    }

    public void ofTicks(long ticks,
                        @NotNull Player player,
                        @NotNull Consumer<UUID> consumer,
                        @NotNull String messageKey,
                        @NotNull String failMessageKey) {
        Objects.requireNonNull(player, "'player' cannot be null");
        Objects.requireNonNull(consumer, "'consumer' cannot be null");
        Objects.requireNonNull(messageKey, "'messageKey' cannot be null");
        Objects.requireNonNull(failMessageKey, "'failMessageKey' cannot be null");
        UUID uuid = player.getUniqueId();
        if (warmup.containsKey(uuid)) {
            BlobLibMessageAPI.getInstance()
                    .getMessage("BlobRP.Already-On-Warmup", player)
                    .handle(player);
            return;
        }
        warmup.put(uuid, failMessageKey);
        Uber<Long> left =
                Uber.drive(ticks - 1);
        BlobMessage message = BlobLibMessageAPI.getInstance().getMessage(messageKey, player);
        Objects.requireNonNull(message, "messageKey didn't point to a BlobMessage: " + messageKey);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!warmup.containsKey(uuid)) {
                    cancel();
                    return;
                }
                if (player != Bukkit.getPlayer(uuid)) {
                    cancel();
                    return;
                }
                long leftTicks = left.thanks();
                double seconds = MinecraftTimeUnit.SECONDS.convert(leftTicks, MinecraftTimeUnit.TICKS);
                long leftSeconds = (long) seconds;
                message.modder()
                        .replace("%time%", leftSeconds + "")
                        .get()
                        .handle(player);
                left.talk(leftTicks - 20);
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!warmup.containsKey(uuid))
                return;
            warmup.remove(uuid);
            consumer.accept(uuid);
        }, ticks);
    }

    public void ofSeconds(int seconds,
                          @NotNull Player player,
                          @NotNull Consumer<UUID> consumer,
                          @NotNull String messageKey,
                          @NotNull String failMessageKey) {
        long l = (long) MinecraftTimeUnit.TICKS.convert(seconds, MinecraftTimeUnit.SECONDS);
        ofTicks(l, player, consumer, messageKey, failMessageKey);
    }
}
