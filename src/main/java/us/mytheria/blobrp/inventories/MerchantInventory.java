package us.mytheria.blobrp.inventories;

import me.anjoismysign.anjo.entities.Result;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.mytheria.bloblib.BlobLibAPI;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.bloblib.entities.inventory.MetaInventoryButton;
import us.mytheria.bloblib.entities.inventory.ReferenceMetaBlobInventory;
import us.mytheria.bloblib.utilities.TextColor;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;

import java.util.List;
import java.util.Optional;

public class MerchantInventory extends ReferenceMetaBlobInventory {
    public final static String META = "BLOBRP_SHOPARTICLE";

    private final RPManagerDirector director;

    public MerchantInventory(RPManagerDirector director, ReferenceMetaBlobInventory referenceMetaBlobInventory) {
        super(referenceMetaBlobInventory.getTitle(), referenceMetaBlobInventory.getSize(),
                referenceMetaBlobInventory.getButtonManager(),
                referenceMetaBlobInventory.getType(),
                referenceMetaBlobInventory.getKey());
        this.director = director;
        loadShopArticles();
    }

    public Result<ShopArticle> isLinked(MetaInventoryButton button) {
        if (button.getSubMeta() == null)
            return Result.invalidBecauseNull();
        String subMeta = button.getSubMeta();
        ShopArticle article = director.getShopArticleDirector().getObjectManager().getObject(subMeta);
        if (article == null)
            return Result.invalidBecauseNull();
        return Result.valid(article);
    }

    /**
     * Will attempt to load ShopArticle's to buttons if possible.
     */
    public void loadShopArticles() {
        getButtonManager().getAllButtons().forEach(button -> {
            if (!button.hasMeta())
                return;
            if (!button.getMeta().equals(META))
                return;
            Result<ShopArticle> result = isLinked(button);
            if (!result.isValid())
                return;
            ShopArticle article = result.value();
            ItemStack itemStack = article.cloneDisplay();
            Optional<String> buyingCurrency = article.getBuyingCurrency();
            double price = article.getBuyPrice();
            SimpleEventListener<List<String>> merchantsView = director.getConfigManager().merchantsView();
            if (merchantsView.register()) {
                List<String> parseLore = merchantsView.value()
                        .stream().map(TextColor::PARSE)
                        .map(s -> s.replace("%format%",
                                BlobLibAPI.getElasticEconomy()
                                        .map(buyingCurrency)
                                        .format(price)))
                        .toList();
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta == null)
                    button.setDisplay(itemStack, this);
                if (itemMeta.hasLore()) {
                    List<String> lore = itemMeta.getLore();
                    lore.addAll(parseLore);
                    itemMeta.setLore(lore);
                } else {
                    itemMeta.setLore(parseLore);
                }
                itemStack.setItemMeta(itemMeta);
            }
            button.setDisplay(itemStack, this);
        });
    }
}
