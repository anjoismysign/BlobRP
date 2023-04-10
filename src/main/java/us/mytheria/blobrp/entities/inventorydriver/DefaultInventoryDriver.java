package us.mytheria.blobrp.entities.inventorydriver;

import org.bson.Document;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.BlobCrudable;
import us.mytheria.bloblib.entities.inventory.InventoryBuilderCarrier;
import us.mytheria.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import us.mytheria.bloblib.entities.inventory.MetaInventoryButton;
import us.mytheria.blobrp.entities.playerserializer.PlayerSerializerType;

import java.util.HashSet;
import java.util.UUID;

public class DefaultInventoryDriver extends InventoryDriver {
    private MetaBlobPlayerInventoryBuilder inventoryHolder;
    private boolean isUpgraded;
    private HashSet<Integer> cache;

    public DefaultInventoryDriver(BlobCrudable crudable, PlayerSerializerType type) {
        super(crudable, type);
        boolean isUpgraded = crudable.hasBoolean("isUpgraded").orElse(false);
        if (isUpgraded) {
            upgrade(getPlayer().getUniqueId());
        } else {
            downgrade(getPlayer().getUniqueId());
        }
    }

    @Override
    public MetaBlobPlayerInventoryBuilder getInventoryBuilder() {
        return inventoryHolder;
    }

    public void rebuildInventory() {
        inventoryHolder.buildInventory();
    }

    private void upgrade(UUID uuid) {
        if (isUpgraded)
            return;
        isUpgraded = true;
        InventoryBuilderCarrier<MetaInventoryButton> carrier = BlobLibAssetAPI.getMetaInventoryBuilderCarrier("EventPlayerInventory");
        this.inventoryHolder = MetaBlobPlayerInventoryBuilder.fromInventoryBuilderCarrier(carrier, uuid);
        cache = new HashSet<>();
        inventoryHolder.getKeys().forEach(key -> {
            cache.addAll(inventoryHolder.getButton(key).getSlots());
        });
    }

    public void upgrade() {
        upgrade(getInventoryBuilder().getHolderId());
    }

    private void downgrade(UUID uuid) {
        if (!isUpgraded)
            return;
        isUpgraded = false;
        InventoryBuilderCarrier<MetaInventoryButton> carrier = BlobLibAssetAPI.getMetaInventoryBuilderCarrier("PlayerInventory");
        this.inventoryHolder = MetaBlobPlayerInventoryBuilder.fromInventoryBuilderCarrier(carrier, uuid);
        cache = new HashSet<>();
        inventoryHolder.getKeys().forEach(key -> {
            cache.addAll(inventoryHolder.getButton(key).getSlots());
        });

    }

    public void downgrade() {
        downgrade(getInventoryBuilder().getHolderId());
    }

    @Override
    public BlobCrudable serializeAllAttributes() {
        BlobCrudable crudable = super.serializeAllAttributes();
        Document document = crudable.getDocument();
        document.put("isUpgraded", isUpgraded);
        return crudable;
    }

    @Override
    public boolean isInsideInventoryMenu(int slot) {
        return cache.contains(slot);
    }
}
