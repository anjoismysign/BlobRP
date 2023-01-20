package us.mytheria.blobrp.inventories.builder;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.BlobLibAPI;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.entities.inventory.ItemMaterialSelector;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButton;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButtonBuilder;
import us.mytheria.bloblib.entities.message.BlobSound;
import us.mytheria.bloblib.utilities.ItemStackUtil;
import us.mytheria.blobrp.BlobRPAPI;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;

import java.util.UUID;

public class ShopArticleBuilder extends RPObjectBuilder<ShopArticle> {
    /*
    Cuando el atributo es de tipo boolean, usar un VariableSelector es tonto
    debido a que solo tiene dos opciones, por lo que se usa un switch
    que se activa al hacer click derecho.
    */
    private boolean hasCustomModelData;

    public static ShopArticleBuilder build(UUID builderId) {
        return new ShopArticleBuilder(BlobRPAPI.buildInventory("ShopArticleBuilder"), builderId);
    }

    private ShopArticleBuilder(BlobInventory blobInventory, UUID builderId) {
        super(blobInventory, builderId);
        ObjectBuilderButton<String> keyButton = ObjectBuilderButtonBuilder.STRING("Key",
                300, "Builder.Key-Timeout",
                "Builder.Key", string -> {
                    updateDefaultButton("Key", "%key%",
                            string == null ? "N/A" : string);
                    openInventory();
                    return true;
                });
        ObjectBuilderButton<Material> materialButton = ObjectBuilderButtonBuilder.SELECTOR("Material",
                "Builder.Material",
                material -> {
                    updateDefaultButton("Material", "%material%",
                            material == null ? "N/A" : material.name());
                    openInventory();
                    return true;
                }, ItemMaterialSelector.build(builderId));
        ObjectBuilderButton<Integer> customModelDataButton = ObjectBuilderButtonBuilder.INTEGER("CustomModelData",
                300, "Builder.CustomModelData-Timeout",
                "Builder.CustomModelData", integer -> {
                    updateDefaultButton("CustomModelData", "%customModelData%",
                            "" + integer);
                    openInventory();
                    return true;
                });
        ObjectBuilderButton<Double> buyPriceButton = ObjectBuilderButtonBuilder.DOUBLE("BuyPrice",
                300, "Builder.BuyPrice-Timeout",
                "Builder.BuyPrice", doble -> {
                    updateDefaultButton("BuyPrice", "%buyPrice%",
                            doble == -1 ? "N/A" : doble + "");
                    openInventory();
                    return true;
                });
        ObjectBuilderButton<Double> sellPriceButton = ObjectBuilderButtonBuilder.DOUBLE("SellPrice",
                300, "Builder.SellPrice-Timeout",
                "Builder.SellPrice", doble -> {
                    updateDefaultButton("SellPrice", "%sellPrice%",
                            doble == -1 ? "N/A" : doble + "");
                    openInventory();
                    return true;
                });
        ObjectBuilderButton<ItemStack> displayButton = ObjectBuilderButtonBuilder.ITEM("ItemStack",
                "Builder.ItemStack", itemStack -> {
                    updateDefaultButton("ItemStack", "%itemStack%",
                            itemStack == null ? "N/A" : ItemStackUtil.display(itemStack));
                    openInventory();
                    return true;
                });
//        if (builder.isBuildButton(slot)) {
//            ShopArticle build = builder.build();
//            if (build == null)
//                return;
//            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1.35f);
//            player.closeInventory();
//            build.saveToFile();
//            shopArticleManager.addShopArticle(build);
//            removeBuilder(player);
//        }
        addObjectBuilderButton(keyButton).addObjectBuilderButton(materialButton)
                .addObjectBuilderButton(customModelDataButton).addObjectBuilderButton(buyPriceButton)
                .addObjectBuilderButton(sellPriceButton).addObjectBuilderButton(displayButton)
                .setFunction(builder -> {
                    ShopArticle build = builder.build();
                    if (build == null)
                        return null;
                    Player player = getPlayer();
                    BlobSound sound = BlobLibAPI.getSound("Builder.Build-Complete");
                    sound.play(player);
                    player.closeInventory();
                    build.saveToFile();
                    ObjectDirector<ShopArticle> director = RPManagerDirector.getInstance().getShopArticleDirector();
                    director.getObjectManager().addObject(build.key(), build);
                    director.getBuilderManager().removeBuilder(player);
                    return builder.build();
                });
    }

//    public String getKey() {
//        return key;
//    }
//
//    public void setKey(String input) {
//        this.key = input;
//        updateDefaultButton("Key", "%key%", getKey() == null ? "N/A" : getKey());
//        openInventory();
//    }
//
//    public Material getMaterial() {
//        return material;
//    }
//
//    public void setMaterial(Material material) {
//        this.material = material;
//        updateDefaultButton("Material", "%material%", getMaterial() == null ? "N/A" : getMaterial().name());
//        openInventory();
//    }

    public boolean hasCustomModelData() {
        return hasCustomModelData;
    }

    public void setHasCustomModelData(boolean hasCustomModelData) {
        this.hasCustomModelData = hasCustomModelData;
        updateDefaultButton("HasCustomModelData", "%hasCustomModelData%", hasCustomModelData() ? "Yes" : "No");
        openInventory();
    }

//    public void setHasCustomModelData(String input) {
//        try {
//            boolean hasCustomModelData = Boolean.parseBoolean(input);
//            setHasCustomModelData(hasCustomModelData);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public int getCustomModelData() {
//        return customModelData;
//    }
//
//    public void setCustomModelData(String input) {
//        try {
//            int customModelData = Integer.parseInt(input);
//            setCustomModelData(customModelData);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setCustomModelData(int customModelData) {
//        this.customModelData = customModelData;
//        updateDefaultButton("CustomModelData", "%customModelData%", String.valueOf(getCustomModelData()));
//        openInventory();
//    }
//
//    public double getBuyPrice() {
//        return buyPrice;
//    }
//
//    public void setBuyPrice(String input) {
//        try {
//            double buyPrice = Double.parseDouble(input);
//            if (buyPrice <= 0) {
//                buyPrice = -1;
//            }
//            setBuyPrice(buyPrice);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setBuyPrice(double buyPrice) {
//        this.buyPrice = buyPrice;
//        updateDefaultButton("BuyPrice", "%buyPrice%", getBuyPrice() == -1 ? "N/A" : getBuyPrice() + "");
//        openInventory();
//    }
//
//    public double getSellPrice() {
//        return sellPrice;
//    }
//
//    public void setSellPrice(String input) {
//        try {
//            double sellPrice = Double.parseDouble(input);
//            if (sellPrice <= 0) {
//                sellPrice = -1;
//            }
//            setSellPrice(sellPrice);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setSellPrice(double sellPrice) {
//        this.sellPrice = sellPrice;
//        updateDefaultButton("SellPrice", "%sellPrice%", getSellPrice() == -1 ? "N/A" : getSellPrice() + "");
//        openInventory();
//    }
//
//    public ItemStack getDisplay() {
//        return display;
//    }
//
//    public void setDisplay(ItemStack display) {
//        ItemStack clone = display;
//        if (display != null) {
//            clone = display.clone();
//            clone.setAmount(1);
//        }
//        this.display = clone;
//        updateDefaultButton("Display", "%display%", displayToString());
//        openInventory();
//    }
//
//    public String displayToString() {
//        if (display == null)
//            return "N/A";
//        Material material = display.getType();
//        ItemMeta itemMeta = display.getItemMeta();
//        if (itemMeta == null)
//            return material.name();
//        if (!itemMeta.hasDisplayName())
//            return material.name();
//        return itemMeta.getDisplayName();
//    }
//
//    /**
//     * @return true if display is succesful, false if display is null.
//     */
//    public boolean matchDisplay() {
//        if (display == null)
//            return false;
//        setMaterial(display.getType());
//        ItemMeta itemMeta = display.getItemMeta();
//        if (itemMeta == null) {
//            setHasCustomModelData(false);
//            setCustomModelData(0);
//            return true;
//        }
//        setHasCustomModelData(itemMeta.hasCustomModelData());
//        setCustomModelData(itemMeta.getCustomModelData());
//        return true;
//    }
//
//    @Override
//    public ShopArticle build() {
//        if (getKey() == null || getMaterial() == null ||
//                getDisplay() == null || getBuyPrice() == -1 ||
//                getSellPrice() == -1)
//            return null;
//
//        return new ShopArticle(getMaterial(), hasCustomModelData(),
//                getCustomModelData(), getBuyPrice(), getSellPrice(), getDisplay(),
//                getKey());
//    }
//
//    public boolean isKeyButton(int slot) {
//        return getSlots("Key").contains(slot);
//    }
//
//    public boolean isMaterialButton(int slot) {
//        return getSlots("Material").contains(slot);
//    }
//
//    public boolean isHasCustomModelDataButton(int slot) {
//        return getSlots("HasCustomModelData").contains(slot);
//    }
//
//    public boolean isCustomModelDataButton(int slot) {
//        return getSlots("CustomModelData").contains(slot);
//    }
//
//    public boolean isBuyPriceButton(int slot) {
//        return getSlots("BuyPrice").contains(slot);
//    }
//
//    public boolean isSellPriceButton(int slot) {
//        return getSlots("SellPrice").contains(slot);
//    }
//
//    public boolean isDisplayButton(int slot) {
//        return getSlots("Display").contains(slot);
//    }
//
//    public boolean isMatchDisplayButton(int slot) {
//        return getSlots("MatchDisplay").contains(slot);
//    }
}