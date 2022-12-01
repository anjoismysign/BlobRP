package us.mytheria.blobrp.inventories.builder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.blobrp.BlobRPAPI;
import us.mytheria.blobrp.entities.ShopArticle;

import java.util.UUID;

public class ShopArticleBuilder extends RPObjectBuilder<ShopArticle> {
    private String key;
    private Material material;
    private boolean hasCustomModelData;
    private int customModelData;
    private double buyPrice, sellPrice;
    private ItemStack display;

    public static ShopArticleBuilder build(UUID builderId) {
        return new ShopArticleBuilder(BlobRPAPI.buildInventory("ShopArticleBuilder"), builderId);
    }

    private ShopArticleBuilder(BlobInventory blobInventory, UUID builderId) {
        super(blobInventory, builderId);
        setKey(null);
        setMaterial(null);
        setDisplay(null);
        setHasCustomModelData(false);
        setCustomModelData(0);
        setSellPrice(-1);
        setBuyPrice(-1);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String input) {
        this.key = input;
        updateDefaultButton("Key", "%key%", getKey() == null ? "N/A" : getKey());
        openInventory();
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
        updateDefaultButton("Material", "%material%", getMaterial() == null ? "N/A" : getMaterial().name());
        openInventory();
    }

    public boolean hasCustomModelData() {
        return hasCustomModelData;
    }

    public void setHasCustomModelData(String input) {
        try {
            boolean hasCustomModelData = Boolean.parseBoolean(input);
            setHasCustomModelData(hasCustomModelData);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setHasCustomModelData(boolean hasCustomModelData) {
        this.hasCustomModelData = hasCustomModelData;
        updateDefaultButton("HasCustomModelData", "%hasCustomModelData%", hasCustomModelData() ? "Yes" : "No");
        openInventory();
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(String input) {
        try {
            int customModelData = Integer.parseInt(input);
            setCustomModelData(customModelData);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
        updateDefaultButton("CustomModelData", "%customModelData%", String.valueOf(getCustomModelData()));
        openInventory();
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String input) {
        try {
            double buyPrice = Double.parseDouble(input);
            if (buyPrice <= 0) {
                buyPrice = -1;
            }
            setBuyPrice(buyPrice);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
        updateDefaultButton("BuyPrice", "%buyPrice%", getBuyPrice() == -1 ? "N/A" : getBuyPrice() + "");
        openInventory();
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String input) {
        try {
            double sellPrice = Double.parseDouble(input);
            if (sellPrice <= 0) {
                sellPrice = -1;
            }
            setSellPrice(sellPrice);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
        updateDefaultButton("SellPrice", "%sellPrice%", getSellPrice() == -1 ? "N/A" : getSellPrice() + "");
        openInventory();
    }

    public ItemStack getDisplay() {
        return display;
    }

    public void setDisplay(ItemStack display) {
        ItemStack clone = display;
        if (display != null) {
            clone = display.clone();
            clone.setAmount(1);
        }
        this.display = clone;
        updateDefaultButton("Display", "%display%", displayToString());
        openInventory();
    }

    public String displayToString() {
        if (display == null)
            return "N/A";
        Material material = display.getType();
        ItemMeta itemMeta = display.getItemMeta();
        if (itemMeta == null)
            return material.name();
        if (!itemMeta.hasDisplayName())
            return material.name();
        return itemMeta.getDisplayName();
    }

    /**
     * @return true if display is succesful, false if display is null.
     */
    public boolean matchDisplay() {
        if (display == null)
            return false;
        setMaterial(display.getType());
        ItemMeta itemMeta = display.getItemMeta();
        if (itemMeta == null) {
            setHasCustomModelData(false);
            setCustomModelData(0);
            return true;
        }
        setHasCustomModelData(itemMeta.hasCustomModelData());
        setCustomModelData(itemMeta.getCustomModelData());
        return true;
    }

    @Override
    public ShopArticle build() {
        if (getKey() == null || getMaterial() == null ||
                getDisplay() == null || getBuyPrice() == -1 ||
                getSellPrice() == -1)
            return null;

        return new ShopArticle(getMaterial(), hasCustomModelData(),
                getCustomModelData(), getBuyPrice(), getSellPrice(), getDisplay(),
                getKey());
    }

    public boolean isKeyButton(int slot) {
        return getSlots("Key").contains(slot);
    }

    public boolean isMaterialButton(int slot) {
        return getSlots("Material").contains(slot);
    }

    public boolean isHasCustomModelDataButton(int slot) {
        return getSlots("HasCustomModelData").contains(slot);
    }

    public boolean isCustomModelDataButton(int slot) {
        return getSlots("CustomModelData").contains(slot);
    }

    public boolean isBuyPriceButton(int slot) {
        return getSlots("BuyPrice").contains(slot);
    }

    public boolean isSellPriceButton(int slot) {
        return getSlots("SellPrice").contains(slot);
    }

    public boolean isDisplayButton(int slot) {
        return getSlots("Display").contains(slot);
    }

    public boolean isMatchDisplayButton(int slot) {
        return getSlots("MatchDisplay").contains(slot);
    }
}