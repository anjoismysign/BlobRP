package us.mytheria.blobrp.entities;

import org.bson.Document;
import org.bukkit.entity.Player;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.BlobCrudable;
import us.mytheria.bloblib.entities.BlobSerializable;
import us.mytheria.bloblib.entities.inventory.InventoryBuilderCarrier;
import us.mytheria.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import us.mytheria.bloblib.entities.inventory.MetaInventoryButton;

import java.util.UUID;

public class RolePlayInventoryHolder implements BlobSerializable {
    private final BlobCrudable crudable;
    private MetaBlobPlayerInventoryBuilder inventoryHolder;
    private boolean isUpgraded;

    public RolePlayInventoryHolder(BlobCrudable crudable) {
        this.crudable = crudable;
        boolean isUpgraded = crudable.hasBoolean("isUpgraded").orElse(false);
        Player player = getPlayer();
        if (isUpgraded) {
            upgrade(player.getUniqueId());
        } else {
            downgrade(player.getUniqueId());
        }
    }

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
    }

    public void downgrade() {
        downgrade(getInventoryBuilder().getHolderId());
    }

    @Override
    public BlobCrudable blobCrudable() {
        return crudable;
    }

    @Override
    public BlobCrudable serializeAllAttributes() {
        Document document = crudable.getDocument();
        document.put("isUpgraded", isUpgraded);
        return crudable;
    }
}
