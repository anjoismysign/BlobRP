package us.mytheria.blobrp;

import com.mongodb.lang.Nullable;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.entities.BlobCrudable;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.entities.playerserializer.PlayerSerializerType;
import us.mytheria.blobrp.inventories.MerchantInventory;
import us.mytheria.blobrp.merchant.MerchantManager;

import java.util.function.Consumer;

public class BlobRPAPI {
    private static BlobRPAPI instance;

    private final RPManagerDirector director;

    protected BlobRPAPI(RPManagerDirector director) {
        this.director = director;
        instance = this;
    }

    /**
     * Adds a transient ShopArticle to the shop.
     *
     * @param display         The ItemStack to create the ShopArticle from
     * @param buyPrice        The buy price
     * @param key             The key
     * @param sellPrice       The sell price
     * @param buyingCurrency  The buying currency. if null, the default currency is used.
     * @param sellingCurrency The selling currency. if null, the default currency is used.
     * @return Whether the ShopArticle was added successfully
     */
    public static boolean addComplexShopArticle(ItemStack display, double buyPrice, NamespacedKey key,
                                                double sellPrice, @Nullable String buyingCurrency,
                                                @Nullable String sellingCurrency) {
        ShopArticle shopArticle = ShopArticle.fromItemStack(display, buyPrice, key.toString(),
                sellPrice, true, buyingCurrency, sellingCurrency);
        if (shopArticle == null)
            return false;
        instance.director.getShopArticleDirector().getObjectManager().addObject(shopArticle.getKey(), shopArticle, null);
        return true;
    }

    /**
     * Adds a transient ShopArticle to the shop.
     * Sell price is 10% of the buy price.
     *
     * @param display  The ItemStack to create the ShopArticle from
     * @param buyPrice The buy price
     * @param key      The key
     * @return Whether the ShopArticle was added successfully
     */
    public static boolean addComplexShopArticle(ItemStack display, double buyPrice, NamespacedKey key) {
        return addComplexShopArticle(display, buyPrice, key, buyPrice / 10,
                null, null);
    }

    /**
     * Reloads all merchants.
     */
    public static void reloadMerchants() {
        RPManagerDirector director = instance.director;
        if (director.getMerchantManager() == null) {
            return;
        }
        director.getMerchantManager().getMerchants().values()
                .forEach(MerchantInventory::loadShopArticles);
    }

    /**
     * Will get a MerchantInventory by its key.
     * It will fail fast if the MerchantManager is not enabled
     * or if key doesn't point to a tracked MerchantInventory.
     *
     * @param key The key
     * @return The MerchantInventory
     */
    @NotNull
    public static MerchantInventory getMerchantInventory(String key) {
        RPManagerDirector director = instance.director;
        MerchantManager manager = director.getMerchantManager();
        if (manager == null)
            throw new IllegalStateException("MerchantManager is not enabled.");
        if (!manager.getMerchants().containsKey(key))
            throw new NullPointerException("Merchant with key " + key + " does not exist.");
        return manager.getMerchants().get(key);
    }

    public static BlobCrudable serialize(Player player, PlayerSerializerType type) {
        if (type != PlayerSerializerType.SIMPLE)
            throw new IllegalArgumentException("Only PlayerSerializerType.SIMPLE is supported at the moment.");
        return BlobRP.getInstance().simplePlayerSerializer.serialize(player);
    }

    public static void deserialize(Player player, BlobCrudable crudable,
                                   PlayerSerializerType type, Consumer<Player> consumer) {
        if (type != PlayerSerializerType.SIMPLE)
            throw new IllegalArgumentException("Only PlayerSerializerType.SIMPLE is supported at the moment.");
        BlobRP.getInstance().simplePlayerSerializer.deserialize(player, crudable, consumer);
    }

    public static void deserialize(Player player, BlobCrudable crudable,
                                   PlayerSerializerType type) {
        deserialize(player, crudable, type, null);
    }
}
