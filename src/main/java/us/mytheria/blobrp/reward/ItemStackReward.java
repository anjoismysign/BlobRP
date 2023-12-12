package us.mytheria.blobrp.reward;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.utilities.ItemStackUtil;
import us.mytheria.bloblib.utilities.PlayerUtil;

import java.io.File;
import java.util.Optional;

public class ItemStackReward extends Reward<ItemStack> {
    /**
     * Constructs a new Reward with the given values.
     *
     * @param key         how the file will be named
     * @param shouldDelay whether the reward should be delayed
     * @param itemStack   the itemStack
     * @param delay       the delay before the reward is applied, in milliseconds
     * @param runAsync    whether the reward should be applied asynchronously
     * @param message     the message to send to the player when the reward is given
     */
    public static ItemStackReward build(String key, boolean shouldDelay, ItemStack itemStack,
                                        Optional<Long> delay, boolean runAsync,
                                        Optional<String> message) {
        return new ItemStackReward(key, shouldDelay, itemStack, delay, runAsync, message);
    }

    /**
     * Constructs a new Reward with the given values.
     *
     * @param key         how the file will be named
     * @param shouldDelay whether the reward should be delayed
     * @param itemStack   the itemStack
     * @param delay       the delay before the reward is applied, in milliseconds
     * @param runAsync    whether the reward should be applied asynchronously
     * @param message     the message to send to the player when the reward is given
     */
    protected ItemStackReward(String key, boolean shouldDelay, ItemStack itemStack,
                              Optional<Long> delay, boolean runAsync,
                              Optional<String> message) {
        super(key, shouldDelay, itemStack, delay, runAsync, message);
    }

    @Override
    public void apply(Player player) {
        PlayerUtil.giveItemToInventoryOrDrop(player, getValue());
    }

    /**
     * Applies the reward to the given player
     * and tries to send the reward message to the player.
     *
     * @param player
     */
    public void applyAndMessage(Player player) {
        message.ifPresent(key -> BlobLibMessageAPI.getInstance()
                .getMessage(key, player)
                .modder()
                .replace("%itemStack%", ItemStackUtil.display(getValue()))
                .get()
                .handle(player));
        apply(player);
    }

    @Override
    public File saveToFile(File directory) {
        File file = instanceFile(directory);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("Value", value);
        config.set("ShouldDelay", shouldDelay);
        if (delay.isPresent()) {
            config.set("Delay", delay.get());
            config.set("RunAsynchronously", runAsync);
        }
        if (message.isPresent()) {
            config.set("Message", message.get());
        }
        try {
            config.save(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return file;
    }
}
