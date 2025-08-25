package io.github.anjoismysign.blobrp.event;

import io.github.anjoismysign.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import io.github.anjoismysign.blobrp.entity.inventorydriver.InventoryDriver;
import io.github.anjoismysign.blobrp.entity.inventorydriver.InventoryDriverType;

public class CloudInventorySerializeEvent extends CloudInventoryIOEvent {
    public CloudInventorySerializeEvent(InventoryDriver driver,
                                        InventoryDriverType driverType,
                                        MetaBlobPlayerInventoryBuilder inventoryBuilder) {
        super(driver, driverType, false, inventoryBuilder);
    }
}