package io.github.anjoismysign.blobrp.entities.inventorydriver;

import io.github.anjoismysign.bloblib.entities.BlobCrudable;
import io.github.anjoismysign.bloblib.entities.BlobSerializable;
import io.github.anjoismysign.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import io.github.anjoismysign.blobrp.BlobRPAPI;
import io.github.anjoismysign.blobrp.entities.playerserializer.PlayerSerializerType;

public abstract class InventoryDriver implements BlobSerializable {
    private final BlobCrudable crudable;
    private final PlayerSerializerType serializerType;

    /**
     * Creates a new InventoryDriver.
     * Will automatically deserialize the Player.
     *
     * @param crudable the crudable
     */
    public InventoryDriver(BlobCrudable crudable, PlayerSerializerType serializerType) {
        this.crudable = crudable;
        this.serializerType = serializerType;
    }

    @Override
    public BlobCrudable blobCrudable() {
        return crudable;
    }

    @Override
    public BlobCrudable serializeAllAttributes() {
        return BlobRPAPI.getInstance().serialize(getPlayer(), serializerType);
    }

    public PlayerSerializerType getSerializerType() {
        return serializerType;
    }

    public abstract MetaBlobPlayerInventoryBuilder getInventoryBuilder();

    public abstract boolean isInsideInventoryMenu(int slot);
}