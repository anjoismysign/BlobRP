package us.mytheria.blobrp.director.manager;

import us.mytheria.bloblib.entities.SimpleEventListener;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.listeners.*;

public class ListenerManager extends RPManager {
    private ArticleSell articleSell;

    public ListenerManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void reload() {
        if (articleSell != null)
            articleSell.reload();
    }

    @Override
    public void loadInConstructor() {
        ConfigManager configManager = getManagerDirector().getConfigManager();
        SimpleEventListener<Boolean> dropNonSoulOnDeath = configManager.dropNonSoulOnDeath();
        if (dropNonSoulOnDeath.register() && dropNonSoulOnDeath.value())
            new DropNonSoulOnDeath();
        if (configManager.playerDropExperienceOnDeath().register())
            new PlayerDropExperienceOnDeath(configManager);
        if (configManager.playerKeepExperienceOnDeath().register())
            new KeepExperienceOnDeath(configManager);
        SimpleEventListener<Integer> entitiesDropExperienceOnDeath = configManager.entitiesDropExperienceOnDeath();
        if (entitiesDropExperienceOnDeath.register())
            new EntitiesDropExperienceOnDeath(configManager);
        SimpleEventListener<Boolean> entitiesClearDropsOnDeath = configManager.entitiesClearDropsOnDeath();
        if (entitiesClearDropsOnDeath.register() && entitiesClearDropsOnDeath.value())
            new EntitiesClearDropsOnDeath(configManager);
        SimpleEventListener<Boolean> entityDropItem = configManager.entityDropItem();
        if (entityDropItem.register())
            new EntityDropItem(configManager);
        SimpleEventListener<Double> sellArticles = configManager.sellArticles();
        if (sellArticles.register())
            articleSell = new ArticleSell(sellArticles.value());
    }
}
