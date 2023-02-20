package us.mytheria.blobrp.director;

import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.ObjectDirectorData;
import us.mytheria.bloblib.entities.ObjectDirectorManager;
import us.mytheria.bloblib.managers.ManagerDirector;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.manager.CommandManager;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.director.manager.ListenerManager;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.inventories.builder.ShopArticleBuilder;
import us.mytheria.blobrp.inventories.builder.reward.CashRewardBuilder;
import us.mytheria.blobrp.inventories.builder.reward.ItemStackRewardBuilder;
import us.mytheria.blobrp.inventories.builder.reward.PermissionRewardBuilder;
import us.mytheria.blobrp.reward.CashReward;
import us.mytheria.blobrp.reward.ItemStackReward;
import us.mytheria.blobrp.reward.PermissionReward;
import us.mytheria.blobrp.reward.RewardReader;
import us.mytheria.blobrp.trophy.Trophy;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirement;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirementReader;
import us.mytheria.blobrp.trophy.requirements.UIBuilder;

import java.util.HashMap;

public class RPManagerDirector extends ManagerDirector {
    public static RPManagerDirector getInstance() {
        return BlobRP.getInstance().getManagerDirector();
    }

    public RPManagerDirector() {
        super(BlobRP.getInstance());
        addManager("CommandManager", new CommandManager(this));
        addManager("ConfigManager", new ConfigManager(this));
        addManager("ListenerManager", new ListenerManager(this));
        // ShopArticle \\
        ObjectDirectorData shopArticleDirectorData = ObjectDirectorData.simple(getFileManager(), "ShopArticle");
        addManager("ShopArticleDirector",
                new ObjectDirector<>(this,
                        shopArticleDirectorData, ShopArticle::fromFile));
        getShopArticleDirector().getBuilderManager().setBuilderBiFunction(
                ShopArticleBuilder::build);
        // Reward \\
        addManager("RewardDirectorManager", new ObjectDirectorManager(this,
                HashMap::new));
        ObjectDirectorData cashRewardDirectorData = ObjectDirectorData.simple(getFileManager(), "CashReward");
        ObjectDirectorData itemStackRewardDirectorData = ObjectDirectorData.simple(getFileManager(), "ItemStackReward");
        ObjectDirectorData permissionRewardDirectorData = ObjectDirectorData.simple(getFileManager(), "PermissionReward");
        getRewardDirectorManager().addObjectDirector(CashReward.class, new ObjectDirector<>(this,
                cashRewardDirectorData, RewardReader::readCash));
        getRewardDirectorManager().addObjectDirector(ItemStackReward.class, new ObjectDirector<>(this,
                itemStackRewardDirectorData, RewardReader::readItemStack));
        getRewardDirectorManager().addObjectDirector(PermissionReward.class, new ObjectDirector<>(this,
                permissionRewardDirectorData, RewardReader::readPermission));
        getCashRewardDirector().getBuilderManager().setBuilderBiFunction(
                CashRewardBuilder::build);
        getItemStackRewardDirector().getBuilderManager().setBuilderBiFunction(
                ItemStackRewardBuilder::build);
        getPermissionRewardDirector().getBuilderManager().setBuilderBiFunction(
                PermissionRewardBuilder::build);
        // TrophyRequirement \\
        ObjectDirectorData trophyRequirementDirectorData = ObjectDirectorData.simple(getFileManager(), "TrophyRequirement");
        addManager("TrophyRequirementDirector", new ObjectDirector<>(this,
                trophyRequirementDirectorData,
                file -> TrophyRequirementReader.read(file).build()));
        getTrophyRequirementDirector().getBuilderManager().setBuilderBiFunction(
                UIBuilder::build);
        // Trophy \\
//        ObjectDirectorData trophyDirectorData = ObjectDirectorData.simple(getFileManager(), "Trophy");
//        addManager("TrophyDirector", new ObjectDirector<>(this,
//                trophyDirectorData,
//                TrophyReader::read));
    }

    /**
     * From top to bottom, follow the order.
     */
    @Override
    public void reload() {
        getShopArticleDirector().reload();
        getCashRewardDirector().reload();
        getItemStackRewardDirector().reload();
        getPermissionRewardDirector().reload();
        getTrophyRequirementDirector().reload();
    }

    @Override
    public void unload() {
    }

    @Override
    public void postWorld() {
    }

    public final ConfigManager getConfigManager() {
        return (ConfigManager) getManager("ConfigManager");
    }

    public final ListenerManager getListenerManager() {
        return (ListenerManager) getManager("ListenerManager");
    }

    @SuppressWarnings("unchecked")
    public final ObjectDirector<ShopArticle> getShopArticleDirector() {
        return (ObjectDirector<ShopArticle>) getManager("ShopArticleDirector");
    }

    public final ObjectDirectorManager getRewardDirectorManager() {
        return (ObjectDirectorManager) getManager("RewardDirectorManager");
    }

    public final ObjectDirector<CashReward> getCashRewardDirector() {
        return getRewardDirectorManager().getObjectDirector(CashReward.class);
    }

    public final ObjectDirector<ItemStackReward> getItemStackRewardDirector() {
        return getRewardDirectorManager().getObjectDirector(ItemStackReward.class);
    }

    public final ObjectDirector<PermissionReward> getPermissionRewardDirector() {
        return getRewardDirectorManager().getObjectDirector(PermissionReward.class);
    }

    @SuppressWarnings("unchecked")
    public final ObjectDirector<TrophyRequirement> getTrophyRequirementDirector() {
        return (ObjectDirector<TrophyRequirement>) getManager("TrophyRequirementDirector");
    }

    @SuppressWarnings("unchecked")
    public final ObjectDirector<Trophy> getTrophyDirector() {
        return (ObjectDirector<Trophy>) getManager("TrophyDirector");
    }

    public final CommandManager getCommandManager() {
        return (CommandManager) getManager("CommandManager");
    }
}
