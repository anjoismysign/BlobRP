package us.mytheria.blobrp.entities;

import global.warming.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.util.ItemStackSerializer;

import java.io.File;

public record ShopArticle(Material material, boolean hasCustomModelData,
                          int customModelData, double buyPrice,
                          double sellPrice, ItemStack display, String key) {

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
        String key = FilenameUtils.removeExtension(fileName);
        return new ShopArticle(material, hasCustomModelData, customModelData, buyPrice, sellPrice, display, key);
    }

    public File saveToFile() {
        File file = new File(BlobRP.getInstance().getManagerDirector().getShopArticleDirector().getObjectManager().getLoadFilesPath() + "/" + key + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("Material", material.name());
        config.set("BuyPrice", buyPrice);
        config.set("SellPrice", sellPrice);
        config.set("HasCustomModelData", hasCustomModelData);
        if (hasCustomModelData)
            config.set("CustomModelData", customModelData);
        ItemStackSerializer.serialize(display, config, "Display");
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
        if (itemStack.getType() != material) {
            return false;
        }
        //Both display & itemStack match material type.
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null && !hasCustomModelData) {
            return true;
            /*
            There's no way to check if the itemStack has a custom model data.
            Since both of them don't have customModelData and match the material type,
            they match.
             */
        }
        if (itemMeta == null) {
            return false;
            /*
            ShopArticle has customModelData, but itemStack doesn't have ItemMeta.
            Cannot match.
             */
        }
        if (itemMeta.hasCustomModelData() && !hasCustomModelData) {
            return false;
            /*
            ShopArticle doesn't have customModelData, but itemStack has customModelData.
            Cannot match.
             */
        }
        if (!itemMeta.hasCustomModelData() && hasCustomModelData) {
            return false;
            /*
            ShopArticle has customModelData, but itemStack doesn't have customModelData.
            Cannot match.
             */
        }
        return itemMeta.getCustomModelData() == customModelData;
    }

    public ItemStack cloneDisplay(int amount) {
        ItemStack clone = display.clone();
        clone.setAmount(amount);
        return clone;
    }

    public ItemStack cloneDisplay() {
        return display.clone();
    }
}
