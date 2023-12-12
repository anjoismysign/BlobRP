package us.mytheria.blobrp.entities;

import net.md_5.bungee.api.chat.TranslatableComponent;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.api.BlobLibInventoryAPI;
import us.mytheria.bloblib.api.BlobLibTranslatableAPI;
import us.mytheria.bloblib.entities.BlobObject;
import us.mytheria.bloblib.entities.inventory.BlobInventoryTracker;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.bloblib.exception.ConfigurationFieldException;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

public class ShopArticle implements BlobObject {

    private final Material material;
    private final boolean hasCustomModelData;
    private final int customModelData;
    private final double buyPrice;
    private final double sellPrice;
    private final TranslatableItem display;
    private final String key;
    private final boolean isDefault;
    private final boolean isTransient;
    private Optional<String> sellingCurrency;
    private Optional<String> buyingCurrency;

    /**
     * Creates a ShopArticle from a TranslatableItem.
     * Sell price is 10% of the buy price.
     *
     * @param display     The TranslatableItem to create the ShopArticle from
     * @param buyPrice    The buy price
     * @param key         The key
     * @param isTransient Whether the article is transient
     * @return The ShopArticle
     */
    @Nullable
    public static ShopArticle fromTranslatableItem(TranslatableItem display, double buyPrice, String key,
                                                   boolean isTransient) {
        return fromTranslatableItem(display, buyPrice, key, buyPrice / 10, isTransient, null, null);
    }

    /**
     * Creates a ShopArticle from a TranslatableItem
     *
     * @param display         The TranslatableItem to create the ShopArticle from
     * @param buyPrice        The buy price
     * @param key             The key
     * @param sellPrice       The sell price
     * @param isTransient     Whether the article is transient
     * @param buyingCurrency  The buying currency. if null, the default currency is used.
     * @param sellingCurrency The selling currency. if null, the default currency is used.
     * @return The ShopArticle
     */
    @Nullable
    public static ShopArticle fromTranslatableItem(TranslatableItem display, double buyPrice, String key,
                                                   double sellPrice, boolean isTransient,
                                                   @Nullable String buyingCurrency,
                                                   @Nullable String sellingCurrency) {
        if (display == null) {
            return null;
        }
        Optional<String> buyingCurrencyOptional = Optional.ofNullable(buyingCurrency);
        Optional<String> sellingCurrencyOptional = Optional.ofNullable(sellingCurrency);
        ItemStack clone = display.getClone();
        ItemMeta itemMeta = clone.getItemMeta();
        if (itemMeta == null) {
            return new ShopArticle(clone.getType(), false,
                    0, buyPrice, sellPrice, display,
                    key, false, isTransient,
                    buyingCurrencyOptional, sellingCurrencyOptional);
        }
        return new ShopArticle(clone.getType(), itemMeta.hasCustomModelData(),
                itemMeta.hasCustomModelData() ? itemMeta.getCustomModelData() : 0, buyPrice,
                sellPrice, display, key, false, isTransient,
                buyingCurrencyOptional, sellingCurrencyOptional);
    }

    public static ShopArticle fromFile(File file) {
        String fileName = file.getName();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.isString("Material"))
            throw new ConfigurationFieldException("'Material' is missing or not valid");
        String inputMaterial = config.getString("Material");
        Material material;
        try {
            material = Material.valueOf(inputMaterial);
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().severe("Material " + inputMaterial + " is not a valid material! Inside file " + fileName);
            return null;
        }
        double buyPrice = config.getDouble("BuyPrice", 0);
        double sellPrice = config.getDouble("SellPrice", 0);
        boolean hasCustomModelData = config.getBoolean("HasCustomModelData",
                false);
        int customModelData = config.getInt("CustomModelData", 0);
        if (!config.isString("Display"))
            throw new ConfigurationFieldException("'Display' is missing or not valid");
        String displayKey = config.getString("Display");
        TranslatableItem display = BlobLibTranslatableAPI.getInstance()
                .getTranslatableItem(displayKey);
        if (display == null)
            throw new NullPointerException("Display '" + displayKey + "' doesn't point to a TranslatableItem!");
        Optional<String> buyingCurrency = Optional.empty();
        if (config.isString("Buying-Currency"))
            buyingCurrency = Optional.ofNullable(config.getString("Buying-Currency"));
        Optional<String> sellingCurrency = Optional.empty();
        if (config.isString("Selling-Currency"))
            sellingCurrency = Optional.ofNullable(config.getString("Selling-Currency"));
        String key = FilenameUtils.removeExtension(fileName);
        return new ShopArticle(material, hasCustomModelData, customModelData, buyPrice,
                sellPrice, display, key, false, false,
                buyingCurrency, sellingCurrency);
    }

    public ShopArticle(Material material, boolean hasCustomModelData, int customModelData,
                       double buyPrice, double sellPrice, TranslatableItem display, String key,
                       boolean isDefault, boolean isTransient,
                       Optional<String> buyingCurrency, Optional<String> sellingCurrency) {
        this.material = material;
        this.hasCustomModelData = hasCustomModelData;
        this.customModelData = customModelData;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.display = display;
        this.key = key;
        this.isDefault = isDefault;
        this.isTransient = isTransient;
        this.buyingCurrency = buyingCurrency;
        this.sellingCurrency = sellingCurrency;
    }

    public File saveToFile(File directory) {
        if (isTransient)
            return null;
        File file = instanceFile(directory);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("Material", getMaterial().name());
        config.set("BuyPrice", getBuyPrice());
        config.set("SellPrice", getSellPrice());
        config.set("HasCustomModelData", hasCustomModelData());
        if (hasCustomModelData())
            config.set("CustomModelData", getCustomModelData());
        if (buyingCurrency.isPresent())
            config.set("Buying-Currency", buyingCurrency.get());
        if (sellingCurrency.isPresent())
            config.set("Selling-Currency", sellingCurrency.get());
        config.set("Display", getDisplay().getReference());
        try {
            config.save(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return file;
    }

    public boolean matches(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
            //Null itemStacks are not allowed to match.
        }
        if (itemStack.getType() != getMaterial()) {
            return false;
        }
        //Both display & itemStack match material type.
        if (!itemStack.hasItemMeta() && !hasCustomModelData)
            return true;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return false;
            /*
            ShopArticle has customModelData, but itemStack doesn't have ItemMeta.
            Cannot match.
             */
        }
        if (!itemMeta.hasCustomModelData() && hasCustomModelData()) {
            return false;
            /*
            ShopArticle has customModelData, but itemStack doesn't have customModelData.
            Cannot match.
             */
        }
        if (!hasCustomModelData() && !itemMeta.hasCustomModelData()) {
            return true;
            /*
            ShopArticle doesn't have customModelData, and itemStack doesn't have customModelData.
            Match.
             */
        }
        return itemMeta.getCustomModelData() == getCustomModelData();
    }

    public ItemStack cloneDisplay(Player player, int amount) {
        return cloneDisplay(player.getLocale(), amount);
    }

    public ItemStack cloneDisplay(@NotNull String locale, int amount) {
        Objects.requireNonNull(locale);
        ItemStack clone = display.localize(locale).getClone();
        clone.setAmount(amount);
        return clone;
    }

    public ItemStack cloneDisplay() {
        return cloneDisplay("en_us", 1);
    }

    public Material getMaterial() {
        return material;
    }

    public boolean hasCustomModelData() {
        return hasCustomModelData;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public TranslatableItem getDisplay() {
        return display;
    }

    @Override
    public String getKey() {
        return key;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public static void openSellInventory(Player player) {
        BlobInventoryTracker tracker = BlobLibInventoryAPI.getInstance()
                .trackInventory(player, "Sell-Articles");
        tracker.getInventory().open(player);
    }

    public String display() {
        ItemStack display = cloneDisplay();
        String defaultDisplay = new TranslatableComponent(display.getTranslationKey()).toPlainText();
        if (!display.hasItemMeta())
            return defaultDisplay;
        ItemMeta itemMeta = display.getItemMeta();
        if (itemMeta == null)
            return defaultDisplay;
        if (!itemMeta.hasDisplayName())
            return defaultDisplay;
        return itemMeta.getDisplayName();
    }

    /**
     * If empty, it's meant to use default currency (regarding VaultAPI2's MultiEconomy)
     *
     * @return the optional
     */
    @NotNull
    public Optional<String> getBuyingCurrency() {
        return buyingCurrency;
    }

    /**
     * If empty, it's meant to use default currency (regarding VaultAPI2's MultiEconomy)
     *
     * @return the optional
     */
    @NotNull
    public Optional<String> getSellingCurrency() {
        return sellingCurrency;
    }
}
