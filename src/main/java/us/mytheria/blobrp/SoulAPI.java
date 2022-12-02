package us.mytheria.blobrp;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SoulAPI {
    private static final BlobRP main = BlobRP.getInstance();

    private static final NamespacedKey soulItemKey = new NamespacedKey(main, "soul");

    /**
     * Sets a PersistentDataHolder to be soul alike.
     */
    public static void setSoul(PersistentDataHolder holder) {
        holder.getPersistentDataContainer().set(soulItemKey, PersistentDataType.BYTE, (byte) 1);
    }

    /**
     * @param itemStack The item you want to set as a soul alike item.
     * @return true if succesful, false if not.
     */
    public static boolean setSoul(ItemStack itemStack) {
        if (itemStack == null) return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return false;
        setSoul(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return true;
    }

    /**
     * @param holder The PersistentDataHolder you want to check.
     * @return true if the PersistentDataHolder is soul alike, false if not.
     */
    public static boolean isSoul(PersistentDataHolder holder) {
        PersistentDataContainer container = holder.getPersistentDataContainer();
        if (!container.has(soulItemKey, PersistentDataType.BYTE))
            return false;
        return container.get(soulItemKey, PersistentDataType.BYTE) == 1;
    }

    /**
     * @param itemStack The item you want to check.
     * @return true if the item is soul alike, false if not.
     */
    public static boolean isSoul(ItemStack itemStack) {
        if (itemStack == null) return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return false;
        return isSoul(itemMeta);
    }

    /**
     * Will drop all non soul alike items from the inventory.
     *
     * @param inventoryHolder The inventory holder you want to drop the items from.
     * @param dropLocation    The location where the items will be dropped.
     */
    public static void dropNonSouls(InventoryHolder inventoryHolder, Location dropLocation) {
        Inventory inventory = inventoryHolder.getInventory();
        for (ItemStack itemStack : inventoryHolder.getInventory().getContents()) {
            if (itemStack == null)
                continue;
            if (isSoul(itemStack)) continue;
            dropLocation.getWorld().dropItemNaturally(dropLocation, itemStack.clone());
            itemStack.setAmount(0);
        }
    }

    /**
     * Will drop all non soul alike items from the inventory.
     *
     * @param player The player you want to drop the items from.
     */
    public static void dropNonSouls(Player player) {
        dropNonSouls(player, player.getLocation());
    }

    /**
     * @param inventory The inventory you want to check.
     * @return A list of all soul alike items in the inventory.
     */
    public static List<ItemStack> getSouls(Inventory inventory) {
        List<ItemStack> souls = new ArrayList<>();
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null)
                continue;
            if (isSoul(itemStack)) souls.add(itemStack);
        }
        return souls;
    }

    /**
     * @param inventoryHolder The inventory holder you want to check.
     * @return A list of all soul alike items from inventory holder.
     */
    public static List<ItemStack> getSouls(InventoryHolder inventoryHolder) {
        return getSouls(inventoryHolder.getInventory());
    }
}
