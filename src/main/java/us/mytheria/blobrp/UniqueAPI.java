package us.mytheria.blobrp;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class UniqueAPI {
    private static final BlobRP main = BlobRP.getInstance();

    private static final NamespacedKey uniqueItemKey = new NamespacedKey(main, "unique");

    /**
     * Sets a PersistentDataHolder to be unique alike.
     */
    public static void setUnique(PersistentDataHolder holder) {
        UUID random = UUID.randomUUID();
        holder.getPersistentDataContainer().set(uniqueItemKey, PersistentDataType.STRING, random.toString());
    }

    /**
     * @param itemStack The item you want to set as a unique alike item.
     * @return true if succesful, false if not.
     */
    public static boolean setUnique(ItemStack itemStack) {
        if (itemStack == null) return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return false;
        setUnique(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return true;
    }

    /**
     * @param holder The PersistentDataHolder you want to check.
     * @return true if the PersistentDataHolder is unique alike, false if not.
     */
    public static boolean isUnique(PersistentDataHolder holder) {
        PersistentDataContainer container = holder.getPersistentDataContainer();
        return container.has(uniqueItemKey, PersistentDataType.STRING);
    }

    /**
     * @param itemStack The item you want to check.
     * @return true if the item is unique alike, false if not.
     */
    public static boolean isUnique(ItemStack itemStack) {
        if (itemStack == null) return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return false;
        return isUnique(itemMeta);
    }
}
