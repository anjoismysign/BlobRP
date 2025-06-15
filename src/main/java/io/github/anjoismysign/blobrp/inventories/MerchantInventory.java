package io.github.anjoismysign.blobrp.inventories;

import io.github.anjoismysign.anjo.entities.Result;
import io.github.anjoismysign.bloblib.api.BlobLibEconomyAPI;
import io.github.anjoismysign.bloblib.api.BlobLibTranslatableAPI;
import io.github.anjoismysign.bloblib.entities.SimpleEventListener;
import io.github.anjoismysign.bloblib.entities.inventory.MetaBlobInventory;
import io.github.anjoismysign.bloblib.entities.inventory.MetaBlobInventoryTracker;
import io.github.anjoismysign.bloblib.entities.inventory.MetaInventoryButton;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableBlock;
import io.github.anjoismysign.bloblib.utilities.TextColor;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.entities.ShopArticle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MerchantInventory {
    private final static String META = "BLOBRP_SHOPARTICLE";
    @NotNull
    private final RPManagerDirector director;
    @NotNull
    private final MetaBlobInventoryTracker tracker;

    public static String META() {
        return META;
    }

    public MerchantInventory(@NotNull RPManagerDirector director,
                             @NotNull MetaBlobInventoryTracker tracker) {
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
