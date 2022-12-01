package us.mytheria.blobrp.director.manager;

import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;

public class BuilderManager extends RPManager {
    private ShopArticleBuilderManager articleBuilderManager;

    public BuilderManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        this.articleBuilderManager = new ShopArticleBuilderManager(getManagerDirector());
    }

    public ShopArticleBuilderManager getShopArticleBuilderManager() {
        return articleBuilderManager;
    }

    @Override
    public void reload() {
        articleBuilderManager.reload();
    }
}
