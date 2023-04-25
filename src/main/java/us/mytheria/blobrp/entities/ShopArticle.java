package us.mytheria.blobrp.entities;

import global.warming.commons.io.FilenameUtils;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.BlobObject;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.utilities.ItemStackSerializer;

import java.io.File;
import java.util.Optional;

public class ShopArticle implements BlobObject {

    private final Material material;
    private final boolean hasCustomModelData;
    private final int customModelData;
    private final double buyPrice;
    private final double sellPrice;
    private final ItemStack display;
    private final String key;
    private final boolean isDefault;
    private final boolean isTransient;
    private Optional<String> sellingCurrency;
    private Optional<String> buyingCurrency;

    /**
     * Creates a ShopArticle from an ItemStack.
     * Sell price is 10% of the buy price.
     *
     * @param display  The ItemStack to create the ShopArticle from
     * @param buyPrice The buy price
     * @return The ShopArticle
     */
    @Nullable
    public static ShopArticle fromItemStack(ItemStack display, double buyPrice, String key,
                                            boolean isTransient) {
        return fromItemStack(display, buyPrice, key, buyPrice / 10, isTransient, null, null);
    }

    /**
     * Creates a ShopArticle from an ItemStack
     *
     * @param display         The ItemStack to create the ShopArticle from
     * @param buyPrice        The buy price
     * @param sellPrice       The sell price
     * @param buyingCurrency  The buying currency. if null, the default currency is used.
     * @param sellingCurrency The selling currency. if null, the default currency is used.
     * @return The ShopArticle
     */
    @Nullable
    public static ShopArticle fromItemStack(ItemStack display, double buyPrice, String key,
                                            double sellPrice, boolean isTransient,
                                            @Nullable String buyingCurrency,
                                            @Nullable String sellingCurrency) {
        if (display == null) {
            return null;
        }
        Optional<String> buyingCurrencyOptional = Optional.ofNullable(buyingCurrency);
        Optional<String> sellingCurrencyOptional = Optional.ofNullable(sellingCurrency);
        ItemMeta itemMeta = display.getItemMeta();
        if (itemMeta == null) {
            return new ShopArticle(display.getType(), false,
                    0, buyPrice, sellPrice, display,
                    key, false, isTransient,
                    buyingCurrencyOptional, sellingCurrencyOptional);
        }
        return new ShopArticle(display.getType(), itemMeta.hasCustomModelData(),
                itemMeta.hasCustomModelData() ? itemMeta.getCustomModelData() : 0, buyPrice,
                sellPrice, display, key, false, isTransient,
                buyingCurrencyOptional, sellingCurrencyOptional);
    }

    public static ShopArticle fromFile(File file) {
        String fileName = file.getName();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
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
        ItemStack display = ItemStackSerializer.deserialize(config, "Display");
        if (display == null) {
            Bukkit.getLogger().severe("Display is null! Inside file " + fileName);
            return null;
        }
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
                       double buyPrice, double sellPrice, ItemStack display, String key,
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
        File file = new File(directory + "/" + getKey() + ".yml");
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
        ItemStackSerializer.serialize(getDisplay(), config, "Display");
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

    public ItemStack cloneDisplay(int amount) {
        ItemStack clone = getDisplay().clone();
        clone.setAmount(amount);
        return clone;
    }

    public ItemStack cloneDisplay() {
        return getDisplay().clone();
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

    public ItemStack getDisplay() {
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
        BlobInventory sellInventory = BlobLibAssetAPI.getBlobInventory("Sell-Articles");
        sellInventory = sellInventory.copy();
        player.openInventory(sellInventory.getInventory());
    }

    public String display() {
        ItemStack display = getDisplay();
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
