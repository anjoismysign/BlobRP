package us.mytheria.blobrp;

import com.mongodb.lang.Nullable;
import me.anjoismysign.anjo.entities.Result;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.entities.BlobCrudable;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.entities.playerserializer.PlayerSerializerType;
import us.mytheria.blobrp.inventories.MerchantInventory;
import us.mytheria.blobrp.merchant.MerchantManager;
import us.mytheria.blobrp.reward.CashReward;
import us.mytheria.blobrp.reward.ItemStackReward;
import us.mytheria.blobrp.reward.PermissionReward;
import us.mytheria.blobrp.reward.Reward;
import us.mytheria.blobrp.trophy.Trophy;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class BlobRPAPI {
    private static BlobRPAPI instance;
    private final RPManagerDirector director;

    private BlobRPAPI(RPManagerDirector director) {
        this.director = director;
    }

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
        if (shopArticle == null)
            return false;
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
     * @param key The key
     * @return The MerchantInventory
     */
    @Nullable
    public MerchantInventory getMerchantInventory(@NotNull String key, @NotNull Player player) {
        MerchantManager manager = director.getMerchantManager();
        if (manager == null)
            throw new NullPointerException("MerchantManager is not enabled.");
        return manager.getMerchant(key, player);
    }

    public BlobCrudable serialize(Player player, PlayerSerializerType type) {
        if (type != PlayerSerializerType.SIMPLE)
            throw new IllegalArgumentException("Only PlayerSerializerType.SIMPLE is supported at the moment.");
        return BlobRP.getInstance().simplePlayerSerializer.serialize(player);
    }

    public void deserialize(Player player, BlobCrudable crudable,
                            PlayerSerializerType type, Consumer<Player> consumer) {
        if (type != PlayerSerializerType.SIMPLE)
            throw new IllegalArgumentException("Only PlayerSerializerType.SIMPLE is supported at the moment.");
        BlobRP.getInstance().simplePlayerSerializer.deserialize(player, crudable, consumer);
    }

    public void deserialize(Player player, BlobCrudable crudable,
                            PlayerSerializerType type) {
        deserialize(player, crudable, type, null);
    }

    /**
     * Checks if a generic reward is being tracked.
     * If so, it will be passed to the consumer.
     *
     * @param key      The key
     * @param consumer The consumer
     */
    public void ifReward(String key, Consumer<Reward<?>> consumer) {
        ObjectDirector<CashReward> cashRewardObjectDirector = director.getCashRewardDirector();
        Result<CashReward> cashRewardResult = cashRewardObjectDirector.getObjectManager().searchObject(key);
        if (cashRewardResult.isValid()) {
            consumer.accept(cashRewardResult.value());
            return;
        }
        ObjectDirector<ItemStackReward> itemStackRewardObjectDirector = director.getItemStackRewardDirector();
        Result<ItemStackReward> itemStackRewardResult = itemStackRewardObjectDirector.getObjectManager().searchObject(key);
        if (itemStackRewardResult.isValid()) {
            consumer.accept(itemStackRewardResult.value());
            return;
        }
        ObjectDirector<PermissionReward> permissionRewardObjectDirector = director.getPermissionRewardDirector();
        Result<PermissionReward> permissionRewardResult = permissionRewardObjectDirector.getObjectManager().searchObject(key);
        if (permissionRewardResult.isValid()) {
            consumer.accept(permissionRewardResult.value());
        }
    }

    /**
     * Reads a Trophy from a file.
     *
     * @param file The file to read from
     * @return The Trophy
     */
    public Trophy readTrophy(File file) {
        YamlConfiguration section = YamlConfiguration.loadConfiguration(file);
        if (!section.contains("EntityType"))
            throw new IllegalArgumentException("'EntityType' is required. Missing at: " + file.getPath());
        EntityType type = EntityType.valueOf(section.getString("EntityType"));
        if (!section.contains("Rewards"))
            throw new IllegalArgumentException("'Rewards' is required. Missing at: " + file.getPath());
        List<String> rewardKeys = section.getStringList("Rewards");
        List<Reward> rewards = new ArrayList<>();
        rewardKeys.forEach(key -> {
            ifReward(key, rewards::add);
        });
        if (!section.contains("Requirements"))
            throw new IllegalArgumentException("'Requirements' is required. Missing at: " + file.getPath());
        String trophyRequirementKey = section.getString("Requirements");
        TrophyRequirement trophyRequirement = director.getTrophyRequirementDirector().getObjectManager().getObject(trophyRequirementKey);
        if (trophyRequirement == null)
            throw new IllegalArgumentException("TrophyRequirement not found: " + trophyRequirementKey);
        return new Trophy(type, rewards, trophyRequirement, FilenameUtils.removeExtension(file.getName()));
    }
}
