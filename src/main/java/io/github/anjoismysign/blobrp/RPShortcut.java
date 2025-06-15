package io.github.anjoismysign.blobrp;

import org.bukkit.inventory.ItemStack;
import io.github.anjoismysign.bloblib.entities.inventory.ButtonManager;
import io.github.anjoismysign.bloblib.entities.inventory.MetaBlobButtonManager;
import io.github.anjoismysign.bloblib.entities.inventory.MetaInventoryButton;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.entities.ShopArticle;
import io.github.anjoismysign.blobrp.inventories.MerchantInventory;

import java.util.Map;

public final class RPShortcut {
    private static RPShortcut instance;
    private final RPManagerDirector director;

    public static RPShortcut getInstance(RPManagerDirector director) {
        if (instance == null) {
            if (director == null)
                throw new NullPointerException("injected dependency is null");
            RPShortcut.instance = new RPShortcut(director);
        }
        return instance;
    }

    public static RPShortcut getInstance() {
        return getInstance(null);
    }

    private RPShortcut(RPManagerDirector director) {
        this.director = director;
    }

    public ButtonManager<MetaInventoryButton> rewriteShopArticles(
            ButtonManager<MetaInventoryButton> y) {
        ButtonManager<MetaInventoryButton> buttonManager = new MetaBlobButtonManager();
        Map<Integer, ItemStack> dupe = y.copyIntegerKeys();
        /*
         * Spaghetti code, but in a few words it overwrites
         * ItemStacks with their ShopArticles' display
         */
        y.getAllButtons().forEach(button -> {
            if (!button.hasMeta())
                return;
            String meta = button.getMeta();
            if (!meta.equals(MerchantInventory.META()))
                return;
            //It's a ShopArticle
            if (button.getSubMeta() == null)
                return;
            String subMeta = button.getSubMeta();
            ShopArticle article = director.getShopArticleDirector().getObjectManager().getObject(subMeta);
            if (article == null)
                return;
            ItemStack itemStack = article.cloneDisplay();
            button.getSlots().forEach(slot -> {
                dupe.put(slot, itemStack);
            });
        });
        buttonManager.setIntegerKeys(dupe);
        buttonManager.setStringKeys(y.copyStringKeys());
        return buttonManager;
    }
}
