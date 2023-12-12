package us.mytheria.blobrp.inventories;

import me.anjoismysign.anjo.entities.Result;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.api.BlobLibEconomyAPI;
import us.mytheria.bloblib.api.BlobLibTranslatableAPI;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.bloblib.entities.inventory.MetaBlobInventory;
import us.mytheria.bloblib.entities.inventory.MetaBlobInventoryTracker;
import us.mytheria.bloblib.entities.inventory.MetaInventoryButton;
import us.mytheria.bloblib.entities.translatable.TranslatableBlock;
import us.mytheria.bloblib.utilities.TextColor;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MerchantInventory {
    private final static String META = "BLOBRP_SHOPARTICLE";

    public static String META() {
        return META;
    }

    private final RPManagerDirector director;
    private final MetaBlobInventoryTracker tracker;

    public MerchantInventory(RPManagerDirector director, MetaBlobInventoryTracker tracker) {
        this.director = director;
        this.tracker = tracker;
        String locale = tracker.getLocale();
        MetaBlobInventory inventory = tracker.getInventory();
        inventory.getButtonManager().getAllButtons().forEach(button -> {
            if (!button.hasMeta())
                return;
            if (!button.getMeta().equals(META))
                return;
            Result<ShopArticle> result = isLinked(button);
            if (!result.isValid())
                return;
            ShopArticle article = result.value();
            ItemStack itemStack = article.cloneDisplay(locale, 1);
            Optional<String> buyingCurrency = article.getBuyingCurrency();
            double price = article.getBuyPrice();
            SimpleEventListener<String> merchantsView = director.getConfigManager().merchantsView();
            if (merchantsView.register()) {
                TranslatableBlock block = BlobLibTranslatableAPI.getInstance().getTranslatableBlock(merchantsView.value(), locale);
                List<String> parseLore = block.get()
                        .stream().map(TextColor::PARSE)
                        .map(s -> s.replace("%format%",
                                BlobLibEconomyAPI.getInstance().getElasticEconomy()
                                        .map(buyingCurrency)
                                        .format(price)))
                        .toList();
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta == null)
                    button.setDisplay(itemStack, inventory);
                if (itemMeta.hasLore()) {
                    List<String> lore = itemMeta.getLore();
                    lore.addAll(parseLore);
                    itemMeta.setLore(lore);
                } else {
                    itemMeta.setLore(parseLore);
                }
                itemStack.setItemMeta(itemMeta);
            }
            button.setDisplay(itemStack, inventory);
        });
    }

    public MetaBlobInventoryTracker getTracker() {
        return tracker;
    }

    public void open(@NotNull Player player) {
        Objects.requireNonNull(player);
        player.openInventory(tracker.getInventory().getInventory());
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
}
