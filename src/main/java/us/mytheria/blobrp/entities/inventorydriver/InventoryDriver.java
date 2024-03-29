package us.mytheria.blobrp.entities.inventorydriver;

import us.mytheria.bloblib.entities.BlobCrudable;
import us.mytheria.bloblib.entities.BlobSerializable;
import us.mytheria.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
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
    }

    @Override
    public BlobCrudable serializeAllAttributes() {
        return BlobRPAPI.getInstance().serialize(getPlayer(), serializerType);
    }

    @Override
    public BlobCrudable blobCrudable() {
        return crudable;
    }

    public PlayerSerializerType getSerializerType() {
        return serializerType;
    }

    public abstract MetaBlobPlayerInventoryBuilder getInventoryBuilder();

    public abstract boolean isInsideInventoryMenu(int slot);
}