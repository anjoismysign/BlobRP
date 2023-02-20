package us.mytheria.blobrp.entities;

import global.warming.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.mytheria.bloblib.entities.BlobObject;
import us.mytheria.bloblib.utilities.Debug;
import us.mytheria.bloblib.utilities.ItemStackSerializer;

import java.io.File;

public class ShopArticle implements BlobObject {

    private final Material material;
    private final boolean hasCustomModelData;
    private final int customModelData;
    private final double buyPrice;
    private final double sellPrice;
    private final ItemStack display;
    private final String key;
    private final boolean isDefault;

    public static ShopArticle fromItemStack(double buyPrice, ItemStack itemStack) {
        double sellPrice = buyPrice / 2;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return new ShopArticle(itemStack.getType(), false,
                    0, buyPrice, sellPrice, itemStack,
                    "null", true);
        }
        return new ShopArticle(itemStack.getType(), itemMeta.hasCustomModelData(),
                itemMeta.getCustomModelData(), buyPrice, sellPrice, itemStack,
                "null", true);
    }

    public static ShopArticle fromFile(File file) {
        Debug.debug("Loading ShopArticle " + file.getName());
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
        String key = FilenameUtils.removeExtension(fileName);
        return new ShopArticle(material, hasCustomModelData, customModelData, buyPrice,
                sellPrice, display, key, false);
    }

    public ShopArticle(Material material, boolean hasCustomModelData, int customModelData,
                       double buyPrice, double sellPrice, ItemStack display, String key,
                       boolean isDefault) {
        this.material = material;
        this.hasCustomModelData = hasCustomModelData;
        this.customModelData = customModelData;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.display = display;
        this.key = key;
        this.isDefault = isDefault;
    }

    public File saveToFile(File directory) {
        File file = new File(directory + "/" + getKey() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("Material", getMaterial().name());
        config.set("BuyPrice", getBuyPrice());
        config.set("SellPrice", getSellPrice());
        config.set("HasCustomModelData", hasCustomModelData());
        if (hasCustomModelData())
            config.set("CustomModelData", getCustomModelData());
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
}
