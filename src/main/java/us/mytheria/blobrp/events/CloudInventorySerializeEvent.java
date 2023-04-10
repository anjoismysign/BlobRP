package us.mytheria.blobrp.events;

import us.mytheria.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import us.mytheria.blobrp.entities.inventorydriver.InventoryDriver;
import us.mytheria.blobrp.entities.inventorydriver.InventoryDriverType;

public class CloudInventorySerializeEvent extends CloudInventoryIOEvent {
    public CloudInventorySerializeEvent(InventoryDriver driver,
                                        InventoryDriverType driverType,
                                        MetaBlobPlayerInventoryBuilder inventoryBuilder) {
        super(driver, driverType, false, inventoryBuilder);
    }
}