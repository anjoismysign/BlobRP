package io.github.anjoismysign.blobrp.event;

import org.bukkit.event.Event;
import io.github.anjoismysign.blobrp.entity.inventorydriver.InventoryDriver;
import io.github.anjoismysign.blobrp.entity.inventorydriver.InventoryDriverType;

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
