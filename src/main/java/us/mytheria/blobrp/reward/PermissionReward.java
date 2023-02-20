package us.mytheria.blobrp.reward;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import us.mytheria.bloblib.BlobLibAPI;
import us.mytheria.bloblib.entities.message.ReferenceBlobMessage;

import java.io.File;
import java.util.Optional;

public class PermissionReward extends Reward<String> {
    private final Optional<String> world;

    /**
     * Constructs a new Reward with the given values.
     *
     * @param key         how the file will be named
     * @param shouldDelay whether the reward should be delayed
     * @param permission  the permission
     * @param delay       the delay before the reward is applied, in milliseconds
     * @param runAsync    whether the reward should be applied asynchronously
     * @param message     the message to send to the player when the reward is given
     */
    public static PermissionReward build(String key, boolean shouldDelay, String permission, Optional<Long> delay,
                                         boolean runAsync, Optional<ReferenceBlobMessage> message, Optional<String> world) {
        return new PermissionReward(key, shouldDelay, permission, delay, runAsync, message, world);
    }

    /**
     * Constructs a new Reward with the given values.
     *
     * @param key         how the file will be named
     * @param shouldDelay whether the reward should be delayed
     * @param permission  the permission
     * @param delay       the delay before the reward is applied, in milliseconds
     * @param runAsync    whether the reward should be applied asynchronously
     * @param message     the message to send to the player when the reward is given
     */
    protected PermissionReward(String key, boolean shouldDelay, String permission,
                               Optional<Long> delay, boolean runAsync,
                               Optional<ReferenceBlobMessage> message, Optional<String> world) {
        super(key, shouldDelay, permission, delay, runAsync, message);
        this.world = world;
    }

    @Override
    public void apply(Player player) {
        if (world.isPresent())
            BlobLibAPI.addPermission(player, getValue(), world.get());
        else
            BlobLibAPI.addGlobalPermission(player, getValue());
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
        if (message.isPresent()) {
            config.set("Message", message.get().getReference());
        }
        if (world.isPresent()) {
            config.set("World", world.get());
        }
        try {
            config.save(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return file;
    }
}
