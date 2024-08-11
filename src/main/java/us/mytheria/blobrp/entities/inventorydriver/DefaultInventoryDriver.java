package us.mytheria.blobrp.entities.inventorydriver;

import org.bson.Document;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.SoulAPI;
import us.mytheria.bloblib.api.BlobLibInventoryAPI;
import us.mytheria.bloblib.entities.BlobCrudable;
import us.mytheria.bloblib.entities.inventory.InventoryBuilderCarrier;
import us.mytheria.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import us.mytheria.bloblib.entities.inventory.MetaInventoryButton;
import us.mytheria.blobrp.BlobRPAPI;
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
        BlobRPAPI.getInstance().deserialize(getPlayer(), crudable, getSerializerType(), player -> {
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

    private void updateInventoryHolder(Player player, @Nullable String locale) {
        locale = locale == null ? player.getLocale() : locale;
        if (isUpgraded) {
            InventoryBuilderCarrier<MetaInventoryButton> carrier = BlobLibInventoryAPI
                    .getInstance()
                    .getMetaInventoryBuilderCarrier("EventPlayerInventory", locale);
            this.inventoryHolder = MetaBlobPlayerInventoryBuilder.fromInventoryBuilderCarrier(carrier, player.getUniqueId());
        } else {
            InventoryBuilderCarrier<MetaInventoryButton> carrier = BlobLibInventoryAPI
                    .getInstance()
                    .getMetaInventoryBuilderCarrier("PlayerInventory", locale);
            this.inventoryHolder = MetaBlobPlayerInventoryBuilder.fromInventoryBuilderCarrier(carrier, player.getUniqueId());
        }
    }

    public void rebuildInventory() {
        inventoryHolder.buildInventory();
    }

    /**
     * Will update the locale of the inventory.
     *
     * @param player The player
     */
    public void updateLocale(Player player, @NotNull String locale) {
        updateInventoryHolder(player, locale);
        inventoryHolder.buildInventory();
        Set<Integer> slots = new HashSet<>();
        for (String key : getInventoryBuilder().getKeys()) {
            slots.addAll(getInventoryBuilder().getButton(key).getSlots());
        }
        for (int slot : slots) {
            SoulAPI.getInstance().setSoul(player.getInventory().getItem(slot));
        }
    }

    private void upgrade(Player player) {
        isUpgraded = true;
        updateInventoryHolder(player, null);
        cache = new HashSet<>();
        inventoryHolder.getKeys().forEach(key -> cache.addAll(inventoryHolder.getButton(key).getSlots()));
        Set<Integer> slots = new HashSet<>();
        getInventoryBuilder().getKeys().forEach(key ->
                slots.addAll(getInventoryBuilder().getButton(key)
                        .getSlots()));
        slots.forEach(slot -> SoulAPI.getInstance().setSoul(player.getInventory().getItem(slot)));
    }

    public void upgrade() {
        upgrade(Objects.requireNonNull(getInventoryBuilder().getPlayer(), "Player is null"));
    }

    private void downgrade(Player player) {
        isUpgraded = false;
        updateInventoryHolder(player, null);
        cache = new HashSet<>();
        for (String key : inventoryHolder.getKeys()) {
            cache.addAll(inventoryHolder.getButton(key).getSlots());
        }
        Set<Integer> slots = new HashSet<>();
        for (String key : getInventoryBuilder().getKeys()) {
            slots.addAll(getInventoryBuilder().getButton(key).getSlots());
        }
        for (int slot : slots) {
            SoulAPI.getInstance().setSoul(player.getInventory().getItem(slot));
        }
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
