package us.mytheria.blobrp;

import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.inventory.BlobInventory;

public final class BlobRPAPI {
    static BlobRP main = BlobRP.getInstance();

    public static BlobInventory buildInventory(String fileName) {
        BlobInventory inventory = BlobLibAssetAPI.getInventoryManager().getInventory(fileName);
        if (inventory == null)
            throw new NullPointerException("Inventory '" + fileName + "' not found");
        return inventory;
    }
}
