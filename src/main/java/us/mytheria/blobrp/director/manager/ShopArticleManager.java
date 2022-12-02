package us.mytheria.blobrp.director.manager;

import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

public class ShopArticleManager extends RPManager {
    private HashMap<String, ShopArticle> shopArticles;

    public ShopArticleManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        shopArticles = new HashMap<>();
        loadFiles(getManagerDirector().getFileManager().getShopArticles());
    }

    private void loadFiles(File path) {
        File[] listOfFiles = path.listFiles();
        for (File file : listOfFiles) {
            if (file.getName().equals(".DS_Store"))
                continue;
            if (file.isFile()) {
                addShopArticle(ShopArticle.fromFile(file));
            }
            if (file.isDirectory())
                loadFiles(file);
        }
    }

    public void addShopArticle(ShopArticle drop) {
        shopArticles.put(drop.key(), drop);
    }

    @Override
    public void reload() {
        loadInConstructor();
    }

    /**
     * @param key The key/fileName of the shop article
     * @return The shop article if found, null otherwise
     */
    public ShopArticle getShopArticle(String key) {
        return shopArticles.get(key);
    }

    public Collection<ShopArticle> values() {
        return shopArticles.values();
    }
}
