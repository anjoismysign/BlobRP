package io.github.anjoismysign.blobrp.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import io.github.anjoismysign.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import io.github.anjoismysign.blobrp.entities.inventorydriver.InventoryDriver;
import io.github.anjoismysign.blobrp.entities.inventorydriver.InventoryDriverType;

public class CloudInventoryIOEvent extends CloudInventoryEvent {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final MetaBlobPlayerInventoryBuilder inventoryBuilder;

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public CloudInventoryIOEvent(InventoryDriver driver,
                                 InventoryDriverType driverType,
                                 boolean isAsync,
                                 MetaBlobPlayerInventoryBuilder inventoryHolder) {
        super(driver, driverType, isAsync);
        this.inventoryBuilder = inventoryHolder;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public MetaBlobPlayerInventoryBuilder getInventoryBuilder() {
        return inventoryBuilder;
    }
}