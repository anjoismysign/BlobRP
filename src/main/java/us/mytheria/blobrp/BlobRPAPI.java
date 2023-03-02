package us.mytheria.blobrp;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.inventories.MerchantInventory;

public class BlobRPAPI {
    private static BlobRPAPI instance;

    private final RPManagerDirector director;

    protected BlobRPAPI(RPManagerDirector director) {
        this.director = director;
        instance = this;
    }

    public static boolean addShopArticle(ItemStack display, double buyPrice, NamespacedKey key, double sellPrice) {
        ShopArticle shopArticle = ShopArticle.fromItemStack(display, buyPrice, key.toString(), sellPrice);
        if (shopArticle == null)
            return false;
        instance.director.getShopArticleDirector().getObjectManager().addObject(shopArticle.getKey(), shopArticle);
        return true;
    }

    public static boolean addShopArticle(ItemStack display, double buyPrice, NamespacedKey key) {
        return addShopArticle(display, buyPrice, key, buyPrice / 10);
    }

    public static void reloadMerchants() {
        RPManagerDirector director = instance.director;
        if (director.getMerchantManager() == null) {
            return;
        }
        director.getMerchantManager().getMerchants().values()
                .forEach(MerchantInventory::loadShopArticles);
    }
}
