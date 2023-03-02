package us.mytheria.blobrp;

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

    public static boolean addShopArticle(ItemStack display, double buyPrice, double sellPrice) {
        ShopArticle shopArticle = ShopArticle.fromItemStack(display, buyPrice, sellPrice);
        if (shopArticle == null)
            return false;
        instance.director.getShopArticleDirector().getObjectManager().addObject(shopArticle.getKey(), shopArticle);
        return true;
    }

    public static boolean addShopArticle(ItemStack display, double buyPrice) {
        return addShopArticle(display, buyPrice, buyPrice / 10);
    }

    public static void reloadMerchants() {
        instance.director.getMerchantManager().getMerchants().values()
                .forEach(MerchantInventory::loadShopArticles);
    }
}
