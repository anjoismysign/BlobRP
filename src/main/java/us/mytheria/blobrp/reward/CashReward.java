package us.mytheria.blobrp.reward;

import net.milkbowl.vault.economy.IdentityEconomy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import us.mytheria.bloblib.BlobLibAPI;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;
import us.mytheria.bloblib.vault.multieconomy.ElasticEconomy;

import java.io.File;
import java.util.Optional;

public class CashReward extends Reward<Double> {
    private final Optional<String> currency;

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
                                   boolean runAsync, Optional<ReferenceBlobMessage> message,
                                   Optional<String> currency) {
        return new CashReward(key, shouldDelay, amount, delay, runAsync, message, currency);
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
                         boolean runAsync, Optional<ReferenceBlobMessage> message,
                         Optional<String> currency) {
        super(key, shouldDelay, amount, delay, runAsync, message);
        this.currency = currency;
    }

    @Override
    public void apply(Player player) {
        IdentityEconomy economy = BlobLibAPI.getElasticEconomy().map(currency);
        economy.deposit(player.getUniqueId(), getValue());
    }

    /**
     * Applies the reward to the given player
     * and tries to send the reward message to the player.
     *
     * @param player the player to apply the reward to
     */
    public void applyAndMessage(Player player) {
        ElasticEconomy economy = BlobLibAPI.getElasticEconomy();
        message.ifPresent(blobMessage -> {
            blobMessage.modder()
                    .replace("%cash%", getCurrency().isPresent() ?
                            economy.getImplementation(getCurrency().get()).format(getValue()) :
                            economy.getDefault().format(getValue()))
                    .get()
                    .handle(player);
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
        currency.ifPresent(string -> config.set("Currency", string));
        message.ifPresent(message -> config.set("Message", message.getReference()));
        try {
            config.save(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return file;
    }

    public Optional<String> getCurrency() {
        return currency;
    }
}
