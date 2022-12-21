package us.mytheria.blobrp.reward;

import org.bukkit.entity.Player;
import us.mytheria.bloblib.BlobLibAPI;
import us.mytheria.bloblib.entities.message.BlobMessage;

import java.util.Optional;

public class CashReward extends Reward<Double> {

    /**
     * Constructs a new Reward with the given values.
     *
     * @param shouldDelay whether the reward should be delayed
     * @param amount      the amount of cash
     * @param delay       the delay before the reward is applied, in milliseconds
     * @param runAsync    whether the reward should be applied asynchronously
     * @param message     the message to send to the player when the reward is given
     */
    public CashReward(boolean shouldDelay, Double amount, Optional<Long> delay,
                      Optional<Boolean> runAsync, Optional<BlobMessage> message) {
        super(shouldDelay, amount, delay, runAsync, message);
    }

    @Override
    public void apply(Player player) {
        BlobLibAPI.addCash(player, getValue());
    }
}
