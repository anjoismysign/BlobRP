package us.mytheria.blobrp.inventories.builder;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.entities.inventory.ItemMaterialSelector;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButton;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButtonBuilder;
import us.mytheria.bloblib.entities.message.BlobSound;
import us.mytheria.blobrp.BlobRPAPI;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;

import java.util.Optional;
import java.util.UUID;

public class ShopArticleBuilder extends RPObjectBuilder<ShopArticle> {
    /*
    When the attribute is of boolean type, using a VariableSelector is stupid
    because it's a two state variable, so a switch is used
    that is called by interacting (lets say by clicking through InventoryClickEvent).
    */

    public static ShopArticleBuilder build(UUID builderId) {
        return new ShopArticleBuilder(BlobRPAPI.buildInventory("ShopArticleBuilder"), builderId);
    }

    private ShopArticleBuilder(BlobInventory blobInventory, UUID builderId) {
        super(blobInventory, builderId);
        ObjectBuilderButton<String> keyButton = ObjectBuilderButtonBuilder.QUICK_STRING(
                "Key", 300, this);
        ObjectBuilderButton<Material> materialButton = ObjectBuilderButtonBuilder.QUICK_SELECTOR(
                "Material", ItemMaterialSelector.build(builderId),
                Material::name, this);
        ObjectBuilderButton<Integer> customModelDataButton = ObjectBuilderButtonBuilder.QUICK_INTEGER(
                "CustomModelData", 300, this);
        ObjectBuilderButton<Double> buyPriceButton = ObjectBuilderButtonBuilder.OPTIONAL_QUICK_DOUBLE(
                "BuyPrice", 300, this);
        ObjectBuilderButton<Double> sellPriceButton = ObjectBuilderButtonBuilder.OPTIONAL_QUICK_DOUBLE(
                "SellPrice", 300, this);
        ObjectBuilderButton<ItemStack> displayButton = ObjectBuilderButtonBuilder.QUICK_ITEM(
                "ItemStack", this);
        addObjectBuilderButton(keyButton).addObjectBuilderButton(materialButton)
                .addObjectBuilderButton(customModelDataButton).addObjectBuilderButton(buyPriceButton)
                .addObjectBuilderButton(sellPriceButton).addObjectBuilderButton(displayButton)
                .setFunction(builder -> {
                    ShopArticle build = builder.build();
                    if (build == null)
                        return null;
                    Player player = getPlayer();
                    BlobSound sound = BlobLibAssetAPI.getSound("Builder.Build-Complete");
                    sound.play(player);
                    player.closeInventory();
                    build.saveToFile();
                    ObjectDirector<ShopArticle> director = RPManagerDirector.getInstance().getShopArticleDirector();
                    director.getObjectManager().addObject(build.key(), build);
                    director.getBuilderManager().removeBuilder(player);
                    return build;
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public ShopArticle build() {
        ObjectBuilderButton<String> keyButton = (ObjectBuilderButton<String>) getObjectBuilderButton("Key");
        ObjectBuilderButton<Material> materialButton = (ObjectBuilderButton<Material>) getObjectBuilderButton("Material");
        ObjectBuilderButton<Integer> customModelDataButton = (ObjectBuilderButton<Integer>) getObjectBuilderButton("CustomModelData");
        ObjectBuilderButton<Double> buyPriceButton = (ObjectBuilderButton<Double>) getObjectBuilderButton("BuyPrice");
        ObjectBuilderButton<Double> sellPriceButton = (ObjectBuilderButton<Double>) getObjectBuilderButton("SellPrice");
        ObjectBuilderButton<ItemStack> displayButton = (ObjectBuilderButton<ItemStack>) getObjectBuilderButton("ItemStack");

        if (keyButton.get().isEmpty() || materialButton.get().isEmpty() || displayButton.get().isEmpty()
                || buyPriceButton.get().isEmpty() || sellPriceButton.get().isEmpty())
            return null;

        String key = keyButton.get().get();
        Material material = materialButton.get().get();
        Optional<Integer> customModelData = customModelDataButton.get();
        boolean hasCustomModelData = customModelData.isPresent();
        double buyPrice = buyPriceButton.get().get();
        double sellPrice = sellPriceButton.get().get();
        ItemStack display = displayButton.get().get();

        return new ShopArticle(material, hasCustomModelData,
                customModelData.orElse(0), buyPrice, sellPrice, display, key);
    }
}