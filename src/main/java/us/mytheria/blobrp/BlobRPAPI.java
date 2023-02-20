package us.mytheria.blobrp;

import org.bukkit.Bukkit;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.inventory.BlobInventory;

public final class BlobRPAPI {
    static BlobRP main = BlobRP.getInstance();

    public static BlobInventory buildInventory(String fileName) {
        BlobInventory inventory = BlobLibAssetAPI.getInventoryManager().cloneInventory(fileName);
        if (inventory == null) {
            Bukkit.getLogger().info("Inventory " + fileName + " not found");
            throw new NullPointerException("Inventory '" + fileName + "' not found");
        }
        return inventory;
    }
}
