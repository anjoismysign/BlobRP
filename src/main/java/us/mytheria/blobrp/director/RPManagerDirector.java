package us.mytheria.blobrp.director;

import org.bukkit.Bukkit;
import us.mytheria.bloblib.entities.GenericManagerDirector;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.ObjectDirectorData;
import us.mytheria.bloblib.entities.ObjectDirectorManager;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.command.OpenSellInventory;
import us.mytheria.blobrp.director.manager.CloudInventoryManager;
import us.mytheria.blobrp.director.manager.CommandManager;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.director.manager.ListenerManager;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.events.AsyncShopArticleReloadEvent;
import us.mytheria.blobrp.inventories.CashRewardBuilder;
import us.mytheria.blobrp.inventories.ItemStackRewardBuilder;
import us.mytheria.blobrp.inventories.PermissionRewardBuilder;
import us.mytheria.blobrp.inventories.ShopArticleBuilder;
import us.mytheria.blobrp.merchant.MerchantManager;
import us.mytheria.blobrp.reward.CashReward;
import us.mytheria.blobrp.reward.ItemStackReward;
import us.mytheria.blobrp.reward.PermissionReward;
import us.mytheria.blobrp.reward.RewardReader;
import us.mytheria.blobrp.trophy.Trophy;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirement;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirementReader;
import us.mytheria.blobrp.trophy.requirements.TrophyRequirementUIBuilder;

import java.util.HashMap;

public class RPManagerDirector extends GenericManagerDirector<BlobRP> {

    public RPManagerDirector(BlobRP plugin) {
        super(plugin);
        registerMetaBlobInventory("WelcomeInventory", "PlayerInventory", "EventPlayerInventory");
        registerMetaBlobInventory("es_es/PlayerInventory", "es_es/EventPlayerInventory");
        registerBlobMessage("es_es/blobrp_lang");
        registerBlobInventory("es_es/CashRewardBuilder", "es_es/ItemStackRewardBuilder", "es_es/PermissionRewardBuilder", "es_es/ShopArticleBuilder");
        addManager("CommandManager", new CommandManager(this));
        addManager("ConfigManager", new ConfigManager(this));
        addManager("ListenerManager", new ListenerManager(this));
        addManager("CloudInventoryManager", new CloudInventoryManager(this));
        // ShopArticle \\
        addDirector("ShopArticle", ShopArticle::fromFile);
        getShopArticleDirector().getBuilderManager().setBuilderBiFunction((uuid, shopArticleObjectDirector) ->
                ShopArticleBuilder.build(uuid, shopArticleObjectDirector, this));
        getShopArticleDirector().whenObjectManagerFilesLoad(manager -> addManager("MerchantManager", new MerchantManager(this)));
        getShopArticleDirector().addAdminChildCommand(OpenSellInventory::command);
        // Reward \\
        addManager("RewardDirectorManager", new ObjectDirectorManager(this,
                HashMap::new));
        ObjectDirectorData cashRewardDirectorData = ObjectDirectorData.simple(getRealFileManager(), "CashReward");
        ObjectDirectorData itemStackRewardDirectorData = ObjectDirectorData.simple(getRealFileManager(), "ItemStackReward");
        ObjectDirectorData permissionRewardDirectorData = ObjectDirectorData.simple(getRealFileManager(), "PermissionReward");
        getRewardDirectorManager().addObjectDirector(CashReward.class, new ObjectDirector<>(this,
                cashRewardDirectorData, RewardReader::readCash));
        getRewardDirectorManager().addObjectDirector(ItemStackReward.class, new ObjectDirector<>(this,
                itemStackRewardDirectorData, RewardReader::readItemStack));
        getRewardDirectorManager().addObjectDirector(PermissionReward.class, new ObjectDirector<>(this,
                permissionRewardDirectorData, RewardReader::readPermission));
        getCashRewardDirector().getBuilderManager().setBuilderBiFunction(
                (uuid, cashRewardObjectDirector) -> CashRewardBuilder.build(uuid, cashRewardObjectDirector, this));
        getItemStackRewardDirector().getBuilderManager().setBuilderBiFunction(
                (uuid, itemStackRewardObjectDirector) -> ItemStackRewardBuilder.build(uuid, itemStackRewardObjectDirector, this));
        getPermissionRewardDirector().getBuilderManager().setBuilderBiFunction(
                (uuid, permissionRewardObjectDirector) -> PermissionRewardBuilder.build(uuid, permissionRewardObjectDirector, this));
        // TrophyRequirement \\
        addDirector("TrophyRequirement", file ->
                TrophyRequirementReader.read(file).build());
        getTrophyRequirementDirector().getBuilderManager().setBuilderBiFunction(
                (uuid, trophyRequirementObjectDirector) -> TrophyRequirementUIBuilder.build(uuid, trophyRequirementObjectDirector, this));
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
        getCommandManager().reload();
        getConfigManager().reload();
        getListenerManager().reload();
        getShopArticleDirector().reload();
        getShopArticleDirector().whenObjectManagerFilesLoad(manager -> {
            AsyncShopArticleReloadEvent event = new AsyncShopArticleReloadEvent();
            Bukkit.getPluginManager().callEvent(event);
            getMerchantManager().reload();
        });
        getCashRewardDirector().reload();
        getItemStackRewardDirector().reload();
        getPermissionRewardDirector().reload();
        getTrophyRequirementDirector().reload();
    }

    @Override
    public void unload() {
        getCloudInventoryManager().unload();
    }

    @Override
    public void postWorld() {
    }

    public final CloudInventoryManager getCloudInventoryManager() {
        return getManager("CloudInventoryManager", CloudInventoryManager.class);
    }

    public final MerchantManager getMerchantManager() {
        return getManager("MerchantManager", MerchantManager.class);
    }

    public final ConfigManager getConfigManager() {
        return getManager("ConfigManager", ConfigManager.class);
    }

    public final ListenerManager getListenerManager() {
        return getManager("ListenerManager", ListenerManager.class);
    }

    public final ObjectDirector<ShopArticle> getShopArticleDirector() {
        return getDirector("ShopArticle", ShopArticle.class);
    }

    public final ObjectDirectorManager getRewardDirectorManager() {
        return getManager("RewardDirectorManager", ObjectDirectorManager.class);
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

    public final ObjectDirector<TrophyRequirement> getTrophyRequirementDirector() {
        return getDirector("TrophyRequirement", TrophyRequirement.class);
    }

    public final ObjectDirector<Trophy> getTrophyDirector() {
        return getDirector("Trophy", Trophy.class);
    }

    public final CommandManager getCommandManager() {
        return getManager("CommandManager", CommandManager.class);
    }
}
