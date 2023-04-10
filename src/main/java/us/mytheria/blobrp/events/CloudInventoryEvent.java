package us.mytheria.blobrp.events;

import org.bukkit.event.Event;
import us.mytheria.blobrp.entities.inventorydriver.InventoryDriver;
import us.mytheria.blobrp.entities.inventorydriver.InventoryDriverType;

public abstract class CloudInventoryEvent extends Event {

    private final InventoryDriver driver;
    private final InventoryDriverType driverType;

    public CloudInventoryEvent(InventoryDriver driver, InventoryDriverType driverType,
                               boolean isAsync) {
        super(isAsync);
        this.driver = driver;
        this.driverType = driverType;
    }
}
