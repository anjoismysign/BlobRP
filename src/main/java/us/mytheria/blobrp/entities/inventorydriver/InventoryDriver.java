package us.mytheria.blobrp.entities.inventorydriver;

import us.mytheria.bloblib.entities.BlobCrudable;
import us.mytheria.bloblib.entities.BlobSerializable;
import us.mytheria.blobrp.BlobRPAPI;
import us.mytheria.blobrp.entities.playerserializer.PlayerSerializerType;

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
        BlobRPAPI.deserialize(getPlayer(), crudable, serializerType);
    }

    @Override
    public BlobCrudable serializeAllAttributes() {
        return BlobRPAPI.serialize(getPlayer(), serializerType);
    }

    @Override
    public BlobCrudable blobCrudable() {
        return crudable;
    }

    public PlayerSerializerType getSerializerType() {
        return serializerType;
    }
}