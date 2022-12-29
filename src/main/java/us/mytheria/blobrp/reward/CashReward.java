package us.mytheria.blobrp.reward;

import org.bukkit.entity.Player;
import us.mytheria.bloblib.BlobLibAPI;
import us.mytheria.bloblib.entities.BlobMessageModder;
import us.mytheria.bloblib.entities.message.BlobMessage;
import us.mytheria.bloblib.utilities.CashFormat;

import java.util.Optional;

public class CashReward extends Reward<Double> {

    /**
     * Constructs a new Reward with the given values.
     *
     * @param key         how the file will be named
     * @param shouldDelay whether the reward should be delayed
     * @param amount      the amount of cash
     * @param delay       the delay before the reward is applied, in milliseconds
     * @param runAsync    whether the reward should be applied asynchronously
     * @param message     the message to send to the player when the reward is given
     */
    public static CashReward build(String key, boolean shouldDelay, Double amount, Optional<Long> delay,
                                   Optional<Boolean> runAsync, Optional<BlobMessage> message) {
        return new CashReward(key, shouldDelay, amount, delay, runAsync, message);
    }

    /**
     * Constructs a new Reward with the given values.
     *
     * @param shouldDelay whether the reward should be delayed
     * @param amount      the amount of cash
     * @param delay       the delay before the reward is applied, in milliseconds
     * @param runAsync    whether the reward should be applied asynchronously
     * @param message     the message to send to the player when the reward is given
     */
    protected CashReward(String key, boolean shouldDelay, Double amount, Optional<Long> delay,
                         Optional<Boolean> runAsync, Optional<BlobMessage> message) {
        super(key, shouldDelay, amount, delay, runAsync, message);
    }

    @Override
    public void apply(Player player) {
        BlobLibAPI.addCash(player, getValue());
    }

    /**
     * Applies the reward to the given player
     * and tries to send the reward message to the player.
     *
     * @param player
     */
    public void applyAndMessage(Player player) {
        message.ifPresent(blobMessage -> {
            BlobMessageModder<BlobMessage> modder = BlobMessageModder.mod(blobMessage);
            modder.replace("%cash%", CashFormat.format(getValue()));
            blobMessage = modder.get();
            blobMessage.sendAndPlay(player);
        });
        apply(player);
    }
}
