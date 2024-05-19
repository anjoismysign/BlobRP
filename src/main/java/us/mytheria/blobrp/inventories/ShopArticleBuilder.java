package us.mytheria.blobrp.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.api.BlobLibInventoryAPI;
import us.mytheria.bloblib.api.BlobLibSoundAPI;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButton;
import us.mytheria.bloblib.entities.inventory.ObjectBuilderButtonBuilder;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;

import java.util.UUID;

public class ShopArticleBuilder extends RPObjectBuilder<ShopArticle> {
    private TranslatableItem display;

    public static ShopArticleBuilder build(UUID builderId,
                                           ObjectDirector<ShopArticle> objectDirector,
                                           RPManagerDirector managerDirector) {
        Player player = Bukkit.getPlayer(builderId);
        return new ShopArticleBuilder(
                BlobLibInventoryAPI.getInstance().buildInventory("ShopArticleBuilder", player.getLocale()), builderId,
                objectDirector,
                managerDirector);
    }

    private ShopArticleBuilder(BlobInventory blobInventory, UUID builderId,
                               ObjectDirector<ShopArticle> objectDirector,
                               RPManagerDirector managerDirector) {
        super(blobInventory, builderId, objectDirector, managerDirector);
        ObjectBuilderButton<String> keyButton = ObjectBuilderButtonBuilder.QUICK_STRING(
                "Key", 300, this);
        ObjectBuilderButton<Double> buyPriceButton = ObjectBuilderButtonBuilder.QUICK_DOUBLE(
                "BuyPrice", 300, this);
        ObjectBuilderButton<Double> sellPriceButton = ObjectBuilderButtonBuilder.QUICK_DOUBLE(
                "SellPrice", 300, this);
        ObjectBuilderButton<ItemStack> itemStackButton = ObjectBuilderButtonBuilder.QUICK_ACTION_ITEM(
                "Display", this, itemStack -> {
                    TranslatableItem instance = TranslatableItem.isInstance(itemStack);
                    if (instance != null)
                        this.display = instance;
                });
        ObjectBuilderButton<String> buyingCurrencyButton = ObjectBuilderButtonBuilder.QUICK_STRING(
                "BuyingCurrency", 300, this);
        ObjectBuilderButton<String> sellingCurrencyButton = ObjectBuilderButtonBuilder.QUICK_STRING(
                "SellingCurrency", 300, this);
        addObjectBuilderButton(keyButton).addObjectBuilderButton(buyPriceButton)
                .addObjectBuilderButton(sellPriceButton).addObjectBuilderButton(itemStackButton)
                .addObjectBuilderButton(buyingCurrencyButton).addObjectBuilderButton(sellingCurrencyButton)
                .setFunction(builder -> {
                    ShopArticle build = builder.construct();
                    if (build == null)
                        return null;
                    Player player = getPlayer();
                    BlobLibSoundAPI.getInstance().getSound("Builder.Build-Complete")
                            .handle(player);
                    player.closeInventory();
                    build.saveToFile(objectDirector.getObjectManager().getLoadFilesDirectory());
                    objectDirector.getObjectManager().addObject(build.getKey(), build);
                    objectDirector.getBuilderManager().removeBuilder(player);
                    return build;
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public ShopArticle construct() {
        ObjectBuilderButton<String> keyButton = (ObjectBuilderButton<String>) getObjectBuilderButton("Key");
        ObjectBuilderButton<Double> buyPriceButton = (ObjectBuilderButton<Double>) getObjectBuilderButton("BuyPrice");
        ObjectBuilderButton<Double> sellPriceButton = (ObjectBuilderButton<Double>) getObjectBuilderButton("SellPrice");
        ObjectBuilderButton<String> buyingCurrencyButton = (ObjectBuilderButton<String>)
                getObjectBuilderButton("BuyingCurrency");
        ObjectBuilderButton<String> sellingCurrencyButton = (ObjectBuilderButton<String>)
                getObjectBuilderButton("SellingCurrency");

        if (keyButton.get().isEmpty() || this.display == null
                || buyPriceButton.get().isEmpty() || sellPriceButton.get().isEmpty())
            return null;

        String key = keyButton.get().get();
        double buyPrice = buyPriceButton.get().get();
        double sellPrice = sellPriceButton.get().get();

        return new ShopArticle(buyPrice, sellPrice, display, key, true, false,
                buyingCurrencyButton.get(), sellingCurrencyButton.get());
    }
}