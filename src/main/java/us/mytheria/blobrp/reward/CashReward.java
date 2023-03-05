package us.mytheria.blobrp.reward;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import us.mytheria.bloblib.BlobLibAPI;
import us.mytheria.bloblib.entities.BlobMessageModder;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;
import us.mytheria.bloblib.utilities.CashFormat;

import java.io.File;
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
                                   boolean runAsync, Optional<ReferenceBlobMessage> message) {
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
                         boolean runAsync, Optional<ReferenceBlobMessage> message) {
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
            BlobMessageModder<ReferenceBlobMessage> modder = BlobMessageModder.mod(blobMessage);
            modder.replace("%cash%", CashFormat.format(getValue()));
            blobMessage = modder.get();
            blobMessage.sendAndPlayInWorld(player);
        });
        apply(player);
    }

    @Override
    public File saveToFile(File directory) {
        File file = new File(directory + "/" + key + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("Value", value);
        config.set("ShouldDelay", shouldDelay);
        if (delay.isPresent()) {
            config.set("Delay", delay.get());
            config.set("RunAsynchronously", runAsync);
        }
        message.ifPresent(referenceBlobMessage -> config.set("Message", referenceBlobMessage.getReference()));
        try {
            config.save(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return file;
    }
}
