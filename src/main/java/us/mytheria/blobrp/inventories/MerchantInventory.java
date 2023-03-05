package us.mytheria.blobrp.inventories;

import global.warming.commons.io.FilenameUtils;
import me.anjoismysign.anjo.entities.Result;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.mytheria.bloblib.BlobLibAPI;
import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.bloblib.entities.inventory.MetaBlobInventory;
import us.mytheria.bloblib.entities.inventory.MetaInventoryButton;
import us.mytheria.bloblib.managers.BlobPlugin;
import us.mytheria.bloblib.utilities.TextColor;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;

import java.io.File;
import java.util.List;

public class MerchantInventory extends MetaBlobInventory {
    public final static String SHOPARTICLE_META = "BLOBRP_SHOPARTICLE";

    private final RPManagerDirector director;
    private final BlobPlugin plugin;
    private final String id;

    public static MerchantInventory fromFile(File file, RPManagerDirector director) {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        String id = FilenameUtils.removeExtension(file.getName());
        if (!yamlConfiguration.isString("Type"))
            return null;
        if (!yamlConfiguration.getString("Type").equals("MERCHANT"))
            return null;
        MetaBlobInventory blobInventory = MetaBlobInventory.fromConfigurationSection(yamlConfiguration);
        if (blobInventory == null) {
            return null;
        }
        MerchantInventory inventory = new MerchantInventory(director, id, blobInventory);
        inventory.loadShopArticles();
        return inventory;
    }

    public MerchantInventory(RPManagerDirector director, String id, MetaBlobInventory metaInventory) {
        super(metaInventory.getTitle(), metaInventory.getSize(), metaInventory.getButtonManager());
        this.director = director;
        this.plugin = director.getPlugin();
        this.id = id;
    }

    public String getId() {
        return id;
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
            if (!button.getMeta().equals(SHOPARTICLE_META))
                return;
            Result<ShopArticle> result = isLinked(button);
            if (!result.isValid())
                return;
            ShopArticle article = result.value();
            ItemStack itemStack = article.cloneDisplay();
            SimpleEventListener<List<String>> merchantsView = director.getConfigManager().merchantsView();
            if (merchantsView.register()) {
                List<String> parseLore = merchantsView.value()
                        .stream().map(TextColor::PARSE)
                        .map(s -> s.replace("%format%",
                                BlobLibAPI.format(article.getBuyPrice())))
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
