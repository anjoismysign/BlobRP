package us.mytheria.blobrp.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import us.mytheria.blobrp.entities.inventorydriver.InventoryDriver;
import us.mytheria.blobrp.entities.inventorydriver.InventoryDriverType;

public class CloudInventoryIOEvent extends CloudInventoryEvent {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    private final MetaBlobPlayerInventoryBuilder inventoryBuilder;

    public CloudInventoryIOEvent(InventoryDriver driver,
                                 InventoryDriverType driverType,
                                 boolean isAsync,
                                 MetaBlobPlayerInventoryBuilder inventoryHolder) {
        super(driver, driverType, isAsync);
        this.inventoryBuilder = inventoryHolder;
    }

    public MetaBlobPlayerInventoryBuilder getInventoryBuilder() {
        return inventoryBuilder;
    }
}