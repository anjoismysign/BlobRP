package io.github.anjoismysign.blobrp;

import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.entity.RoleplayWarp;
import io.github.anjoismysign.blobrp.entity.ShopArticle;
import io.github.anjoismysign.blobrp.inventory.MerchantInventory;
import io.github.anjoismysign.blobrp.merchant.MerchantManager;
import io.github.anjoismysign.blobrp.pressure.PlayerPressure;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BlobRPAPI {
    private static BlobRPAPI instance;
    private final RPManagerDirector director;

    public static BlobRPAPI getInstance(RPManagerDirector director) {
        if (instance == null) {
            if (director == null)
                throw new NullPointerException("injected dependency is null");
            BlobRPAPI.instance = new BlobRPAPI(director);
        }
        return instance;
    }

    public static BlobRPAPI getInstance() {
        return getInstance(null);
    }

    private BlobRPAPI(RPManagerDirector director) {
        this.director = director;
    }

    /**
     * Adds a transient ShopArticle to the shop.
     *
     * @param display         The TranslatableItem to create the ShopArticle from
     * @param buyPrice        The buy price
     * @param key             The key
     * @param sellPrice       The sell price
     * @param buyingCurrency  The buying currency. if null, the default currency is used.
     * @param sellingCurrency The selling currency. if null, the default currency is used.
     * @return Whether the ShopArticle was added successfully
     */
    public boolean addComplexShopArticle(@NotNull TranslatableItem display,
                                         double buyPrice,
                                         @NotNull NamespacedKey key,
                                         double sellPrice,
                                         @Nullable String buyingCurrency,
                                         @Nullable String sellingCurrency) {
        Objects.requireNonNull(display);
        Objects.requireNonNull(key);
        ShopArticle shopArticle = ShopArticle.fromTranslatableItem(display, buyPrice, key.toString(),
                sellPrice, true, buyingCurrency, sellingCurrency);
        director.getShopArticleDirector().getObjectManager().addObject(shopArticle.getKey(), shopArticle, null);
        return true;
    }

    /**
     * Adds a transient ShopArticle to the shop.
     * Sell price is 10% of the buy price.
     *
     * @param display  The TranslatableItem to create the ShopArticle from
     * @param buyPrice The buy price
     * @param key      The key
     * @return Whether the ShopArticle was added successfully
     */
    public boolean addComplexShopArticle(@NotNull TranslatableItem display,
                                         double buyPrice,
                                         @NotNull NamespacedKey key) {
        return addComplexShopArticle(display, buyPrice, key, buyPrice / 10,
                null, null);
    }

    /**
     * Reloads all merchants.
     */
    public void reloadMerchants() {
        if (director.getMerchantManager() == null) {
            return;
        }
        director.getMerchantManager().reload();
    }

    /**
     * Reloads all recipes.
     */
    public void reloadRecipes() {
        if (director.getRoleplayRecipeDirector() == null) {
            return;
        }
        director.getRoleplayRecipeDirector().reload();
    }

    /**
     * Will get a MerchantInventory by its key.
     * It will fail fast if the MerchantManager is not enabled.
     *
     * @param identifier How this MerchantInventory is identified
     * @return The MerchantInventory
     */
    @Nullable
    public MerchantInventory getMerchantInventory(@NotNull String identifier, @NotNull Player player) {
        MerchantManager manager = director.getMerchantManager();
        if (manager == null)
            throw new NullPointerException("MerchantManager is not enabled.");
        return manager.getMerchant(identifier, player);
    }

    
    /**
     * Retrieves a list of RoleplayWarp objects that the given Permissible has permission to use.
     *
     * @param permissible The Permissible to check permissions for (e.g., a Player or CommandSender)
     * @return A list of accessible RoleplayWarps
     */
    @NotNull
    public List<RoleplayWarp> getWarpsForPermissible(@NotNull Permissible permissible) {
        Objects.requireNonNull(permissible, "'permissible' cannot be null");
        return director.getRoleplayWarpDirector().getObjectManager().values().stream()
                .filter(warp -> warp.hasPermission(permissible))
                .toList();
    }

    /**
     * Retrieves a list of all available RoleplayWarp objects.
     *
     * @return A list of all RoleplayWarps
     */
    @NotNull
    public List<RoleplayWarp> getWarps(){
        return director.getRoleplayWarpDirector().getObjectManager().values().stream().toList();
    }

    /**
     * Retrieves a specific RoleplayWarp by its identifier.
     *
     * @param identifier How this RoleplayWarp is identified
     * @return The RoleplayWarp if found, null otherwise
     */
    @Nullable
    public RoleplayWarp getWarp(@NotNull String identifier){
        return director.getRoleplayWarpDirector().getObjectManager().getObject(identifier);
    }

    /**
     * Retrieves the PlayerPressure for the given UUID.
     *
     * @param uuid The UUID of the player
     * @return The PlayerPressure if exists, null otherwise
     */
    @Nullable
    public PlayerPressure getPressure(@NotNull UUID uuid) {
        return director.getPressureManager().getPlayerPressure(uuid);
    }
}
