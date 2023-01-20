package us.mytheria.blobrp.director;

import me.anjoismysign.anjo.entities.Tuple2;
import us.mytheria.bloblib.entities.BlobFileManager;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.ObjectDirectorData;
import us.mytheria.bloblib.entities.manager.ManagerDirector;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.manager.CommandManager;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.director.manager.ListenerManager;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.inventories.builder.ShopArticleBuilder;
import us.mytheria.blobrp.inventories.builder.reward.CashRewardBuilder;
import us.mytheria.blobrp.inventories.builder.reward.ItemStackRewardBuilder;
import us.mytheria.blobrp.inventories.builder.reward.PermissionRewardBuilder;
import us.mytheria.blobrp.reward.Reward;
import us.mytheria.blobrp.reward.RewardReader;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirement;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirementBuilder;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirementReader;

public class RPManagerDirector extends ManagerDirector {
    public static RPManagerDirector getInstance() {
        return BlobRP.getInstance().getDirector();
    }

    public RPManagerDirector() {
        super(BlobRP.getInstance(), "plugins/BlobRP");
        addManager("CommandManager", new CommandManager(this));
        addManager("ConfigManager", new ConfigManager(this));
        addManager("ListenerManager", new ListenerManager(this));
        // ShopArticle \\
        ObjectDirectorData shopArticleDirectorData = ObjectDirectorData.simple(getFileManager(), "ShopArticle");
        addManager("ShopArticleDirector",
                new ObjectDirector<>(this, shopArticleDirectorData.objectBuilderKey(),
                        shopArticleDirectorData.objectDirectory(), file -> {
                    ShopArticle article = ShopArticle.fromFile(file);
                    return new Tuple2<>(article, article.key());
                }).getBuilderManager().addBuilderFunction(ShopArticleBuilder::build));
        // Reward \\
        ObjectDirectorData rewardDirectorData = ObjectDirectorData.simple(getFileManager(), "Reward");
        addManager("RewardDirector", new ObjectDirector<>(this,
                rewardDirectorData.objectBuilderKey(),
                rewardDirectorData.objectDirectory(),
                file -> {
                    Reward reward = RewardReader.read(file);
                    return new Tuple2<>(reward, reward.getKey());
                }).getBuilderManager().addBuilderFunction("CashReward",
                        CashRewardBuilder::build)
                .addBuilderFunction("ItemStackReward",
                        ItemStackRewardBuilder::build)
                .addBuilderFunction("PermissionReward",
                        PermissionRewardBuilder::build));
        // TrophyRequirement \\
        ObjectDirectorData trophyRequirementDirectorData = ObjectDirectorData.simple(getFileManager(), "TrophyRequirement");
        addManager("TrophyRequirementDirector", new ObjectDirector<>(this,
                trophyRequirementDirectorData.objectBuilderKey(),
                trophyRequirementDirectorData.objectDirectory(),
                file -> {
                    TrophyRequirementBuilder builder = TrophyRequirementReader.read(file);
                    TrophyRequirement requirement = builder.build();
                    return new Tuple2<>(requirement, requirement.getKey());
                }).getBuilderManager().addBuilderFunction(
                CashRewardBuilder::build));
    }

    /**
     * From top to bottom, follow the order.
     */
    @Override
    public void reload() {
        getShopArticleDirector().reload();
    }

    @Override
    public void unload() {
    }

    @Override
    public void postWorld() {
    }

    public ConfigManager getConfigManager() {
        return (ConfigManager) getManager("ConfigManager");
    }

    public BlobFileManager getFileManager() {
        return getFileManager();
    }


    public ListenerManager getListenerManager() {
        return (ListenerManager) getManager("ListenerManager");
    }

    public ObjectDirector<ShopArticle> getShopArticleDirector() {
        return (ObjectDirector<ShopArticle>) getManager("ShopArticleManager");
    }

    public ObjectDirector<Reward> getRewardDirector() {
        return (ObjectDirector<Reward>) getManager("RewardDirector");
    }

    public CommandManager getCommandManager() {
        return (CommandManager) getManager("CommandManager");
    }
}
