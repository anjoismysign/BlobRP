package us.mytheria.blobrp.reward;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.entities.message.BlobMessage;
import us.mytheria.bloblib.utilities.PlayerUtil;

import java.util.Optional;

public class ItemStackReward extends Reward<ItemStack> {
    /**
     * Constructs a new Reward with the given values.
     *
     * @param shouldDelay whether the reward should be delayed
     * @param itemStack   the itemStack
     * @param delay       the delay before the reward is applied, in milliseconds
     * @param runAsync    whether the reward should be applied asynchronously
     * @param message     the message to send to the player when the reward is given
     */
    public ItemStackReward(boolean shouldDelay, ItemStack itemStack,
                           Optional<Long> delay, Optional<Boolean> runAsync,
                           Optional<BlobMessage> message) {
        super(shouldDelay, itemStack, delay, runAsync, message);
    }

    @Override
    public void apply(Player player) {
        PlayerUtil.giveItemToInventoryOrDrop(player, getValue());
    }
}
