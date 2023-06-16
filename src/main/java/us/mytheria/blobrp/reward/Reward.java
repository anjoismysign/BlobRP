package us.mytheria.blobrp.reward;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import us.mytheria.bloblib.entities.BlobObject;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.BlobRPAPI;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The Reward class represents a reward that can be given to a player.
 *
 * @param <T> the type of the reward
 */
public abstract class Reward<T> implements BlobObject {
    private static final BlobRP main = BlobRP.getInstance();

    /**
     * Checks if a generic reward is being tracked.
     * If so, it will be passed to the consumer.
     *
     * @param key      The key
     * @param consumer The consumer
     * @deprecated Use {@link BlobRPAPI#ifReward(String, Consumer)} instead
     */
    @Deprecated
    public static void ifReward(String key, Consumer<Reward<?>> consumer) {
        BlobRPAPI.INSTANCE.ifReward(key, consumer);
    }

    protected final String key;

    protected final T value;

    // If should delay the reward after it is given
    protected final boolean shouldDelay;

    // The delay before the reward is applied, in milliseconds
    protected final Optional<Long> delay;

    // Whether the reward should be ran asynchronously whenever delayed
    protected final boolean runAsync;

    // The message to send to the player when the reward is given
    protected final Optional<ReferenceBlobMessage> message;

    /**
     * Constructs a new Reward with the given values.
     *
     * @param shouldDelay whether the reward should be delayed
     * @param value       the value
     * @param delay       the delay before the reward is applied, in ticks
     * @param runAsync    whether the reward should be applied asynchronously
     * @param message     the message to send to the player when the reward is given
     */
    public Reward(String key,
                  boolean shouldDelay, T value, Optional<Long> delay,
                  boolean runAsync, Optional<ReferenceBlobMessage> message) {
        this.key = key;
        this.shouldDelay = shouldDelay;
        this.value = value;
        this.delay = delay;
        this.runAsync = runAsync;
        this.message = message;
    }

    /**
     * Applies the reward to the given player.
     *
     * @param player the player to apply the reward to
     */
    public abstract void apply(Player player);

    /**
     * Applies the reward to the given player
     * and tries to send the reward message to the player.
     *
     * @param player
     */
    public void applyAndMessage(Player player) {
        apply(player);
        message.ifPresent(blobMessage -> blobMessage.handle(player));
    }

    public String getKey() {
        return key;
    }

    /**
     * Returns the value of the reward.
     *
     * @return the value of the reward
     */
    public T getValue() {
        return value;
    }

    /**
     * Returns whether the reward should be delayed.
     *
     * @return whether the reward should be delayed
     */
    public boolean shouldDelay() {
        return shouldDelay;
    }

    /**
     * Returns the delay before the reward is applied, in milliseconds.
     *
     * @return the delay before the reward is applied, in milliseconds
     */
    public Optional<Long> getDelay() {
        return delay;
    }

    /**
     * Returns whether the reward should be applied asynchronously.
     *
     * @return true if the reward should be applied asynchronously, false otherwise
     */
    public boolean runsAsynchronously() {
        return runAsync;
    }

    /**
     * Delays the reward by the given amount of time.
     *
     * @param player the player to apply the reward to
     * @return the task that will apply the reward
     */
    public BukkitTask delay(Player player) {
        if (!delay.isPresent()) {
            throw new IllegalStateException("Cannot delay a reward that has no delay");
        }
        long delay = this.delay.get();

        if (runAsync)
            return new BukkitRunnable() {
                @Override
                public void run() {
                    if (player == null || !player.isOnline())
                        return;
                    applyAndMessage(player);
                }
            }.runTaskLaterAsynchronously(BlobRP.getInstance(), delay);
        else
            return new BukkitRunnable() {
                @Override
                public void run() {
                    if (player == null || !player.isOnline())
                        return;
                    applyAndMessage(player);
                }
            }.runTaskLater(null, delay);
    }

    public abstract File saveToFile(File directory);
}