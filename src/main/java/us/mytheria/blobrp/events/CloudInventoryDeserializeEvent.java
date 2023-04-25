package us.mytheria.blobrp.events;

import org.bukkit.entity.Player;
import us.mytheria.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import us.mytheria.blobrp.entities.inventorydriver.InventoryDriver;
import us.mytheria.blobrp.entities.inventorydriver.InventoryDriverType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CloudInventoryDeserializeEvent extends CloudInventoryIOEvent {
    private final List<Consumer<Player>> ifStillOnline = new ArrayList<>();
    private final List<Consumer<Player>> ifStillOnlineAsynchronous = new ArrayList<>();
    private final boolean hasPlayedBefore;

    public CloudInventoryDeserializeEvent(InventoryDriver driver,
                                          InventoryDriverType driverType,
                                          MetaBlobPlayerInventoryBuilder inventoryBuilder,
                                          boolean hasPlayedBefore) {
        super(driver, driverType, true, inventoryBuilder);
        this.hasPlayedBefore = hasPlayedBefore;
    }

    /**
     * Adds a consumer to be executed if the player is still online.
     *
     * @param consumer The consumer to be executed.
     */
    public void ifStillOnline(Consumer<Player> consumer) {
        this.ifStillOnline.add(consumer);
    }

    /**
     * @return A list of consumers to be executed if the player is still online.
     */
    public List<Consumer<Player>> fetch() {
        return new ArrayList<>(ifStillOnline);
    }

    /**
     * Adds a consumer to be executed if the player is still online.
     *
     * @param consumer The consumer to be executed.
     */
    public void ifStillOnlineAsynchronous(Consumer<Player> consumer) {
        this.ifStillOnlineAsynchronous.add(consumer);
    }

    /**
     * @return A list of consumers to be executed if the player is still online.
     */
    public List<Consumer<Player>> fetchAsynchronous() {
        return new ArrayList<>(ifStillOnlineAsynchronous);
    }

    /**
     * @return Whether the player has played before.
     */
    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }
}