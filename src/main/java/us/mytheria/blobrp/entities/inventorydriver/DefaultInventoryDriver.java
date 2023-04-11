package us.mytheria.blobrp.entities.inventorydriver;

import org.bson.Document;
import org.bukkit.entity.Player;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.BlobCrudable;
import us.mytheria.bloblib.entities.inventory.InventoryBuilderCarrier;
import us.mytheria.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import us.mytheria.bloblib.entities.inventory.MetaInventoryButton;
import us.mytheria.blobrp.BlobRPAPI;
import us.mytheria.blobrp.SoulAPI;
import us.mytheria.blobrp.entities.playerserializer.PlayerSerializerType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DefaultInventoryDriver extends InventoryDriver {
    private MetaBlobPlayerInventoryBuilder inventoryHolder;
    private boolean isUpgraded;
    private HashSet<Integer> cache;

    public DefaultInventoryDriver(BlobCrudable crudable, PlayerSerializerType type) {
        super(crudable, type);
        BlobRPAPI.deserialize(getPlayer(), crudable, getSerializerType(), player -> {
            boolean isUpgraded = crudable.hasBoolean("isUpgraded").orElse(false);
            if (isUpgraded) {
                upgrade(player);
            } else {
                downgrade(player);
            }
        });
    }

    @Override
    public MetaBlobPlayerInventoryBuilder getInventoryBuilder() {
        return inventoryHolder;
    }

    public void rebuildInventory() {
        inventoryHolder.buildInventory();
    }

    private void upgrade(Player player) {
        isUpgraded = true;
        InventoryBuilderCarrier<MetaInventoryButton> carrier = BlobLibAssetAPI.getMetaInventoryBuilderCarrier("EventPlayerInventory");
        this.inventoryHolder = MetaBlobPlayerInventoryBuilder.fromInventoryBuilderCarrier(carrier, player.getUniqueId());
        cache = new HashSet<>();
        inventoryHolder.getKeys().forEach(key -> cache.addAll(inventoryHolder.getButton(key).getSlots()));
        //Sets the default inventory as soul
        Set<Integer> slots = new HashSet<>();
        getInventoryBuilder().getKeys().forEach(key ->
                slots.addAll(getInventoryBuilder().getButton(key)
                        .getSlots()));
        slots.forEach(slot -> SoulAPI.setSoul(player.getInventory().getItem(slot)));
    }

    public void upgrade() {
        upgrade(Objects.requireNonNull(getInventoryBuilder().getPlayer(), "Player is null"));
    }

    private void downgrade(Player player) {
        isUpgraded = false;
        InventoryBuilderCarrier<MetaInventoryButton> carrier = BlobLibAssetAPI.getMetaInventoryBuilderCarrier("PlayerInventory");
        this.inventoryHolder = MetaBlobPlayerInventoryBuilder.fromInventoryBuilderCarrier(carrier, player.getUniqueId());
        cache = new HashSet<>();
        inventoryHolder.getKeys().forEach(key -> cache.addAll(inventoryHolder.getButton(key).getSlots()));
        //Sets the default inventory as soul
        Set<Integer> slots = new HashSet<>();
        getInventoryBuilder().getKeys().forEach(key ->
                slots.addAll(getInventoryBuilder().getButton(key)
                        .getSlots()));
        slots.forEach(slot -> SoulAPI.setSoul(player.getInventory().getItem(slot)));
    }

    public void downgrade() {
        downgrade(Objects.requireNonNull(getInventoryBuilder().getPlayer(), "Player is null"));
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
