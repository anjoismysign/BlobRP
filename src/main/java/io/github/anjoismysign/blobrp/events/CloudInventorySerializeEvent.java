package io.github.anjoismysign.blobrp.events;

import io.github.anjoismysign.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import io.github.anjoismysign.blobrp.entities.inventorydriver.InventoryDriver;
import io.github.anjoismysign.blobrp.entities.inventorydriver.InventoryDriverType;

public class CloudInventorySerializeEvent extends CloudInventoryIOEvent {
    public CloudInventorySerializeEvent(InventoryDriver driver,
                                        InventoryDriverType driverType,
                                        MetaBlobPlayerInventoryBuilder inventoryBuilder) {
        super(driver, driverType, false, inventoryBuilder);
    }
}