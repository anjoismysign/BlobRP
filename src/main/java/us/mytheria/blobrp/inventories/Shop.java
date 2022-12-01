package us.mytheria.blobrp.inventories;

import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.blobrp.entities.ShopArticle;

import java.io.File;
import java.util.HashMap;

public class Shop extends BlobInventory {
    private final HashMap<Integer, ShopArticle> shopArticles;

    public static Shop build(File file) {
        return new Shop(file);
    }

    private Shop(File file) {
        super(file);
        this.shopArticles = new HashMap<>();
    }

    public void addArticle(ShopArticle article, int slot) {
        this.shopArticles.put(slot, article);
    }

    public void removeArticle(int slot) {
        this.shopArticles.remove(slot);
    }
}
