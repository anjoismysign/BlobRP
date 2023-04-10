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

    public CloudInventoryDeserializeEvent(InventoryDriver driver,
                                          InventoryDriverType driverType,
                                          MetaBlobPlayerInventoryBuilder inventoryBuilder) {
        super(driver, driverType, true, inventoryBuilder);
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
}