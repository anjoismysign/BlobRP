package us.mytheria.blobrp.director;

import me.anjoismysign.anjo.entities.Result;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.entities.*;
import us.mytheria.bloblib.managers.ManagerDirector;
import us.mytheria.blobrp.BlobRP;
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
import us.mytheria.blobrp.trophy.requirements.UIBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RPManagerDirector extends ManagerDirector {
    public static RPManagerDirector getInstance() {
        return BlobRP.getInstance().getManagerDirector();
    }

    public RPManagerDirector() {
        super(BlobRP.getInstance());
        registerAndUpdateBlobInventory("WelcomeInventory");
        registerAndUpdateMetaBlobInventory("PlayerInventory");
        registerAndUpdateMetaBlobInventory("EventPlayerInventory");
        addManager("CommandManager", new CommandManager(this));
        addManager("ConfigManager", new ConfigManager(this));
        addManager("ListenerManager", new ListenerManager(this));
        addManager("CloudInventoryManager", new CloudInventoryManager(this));
        // ShopArticle \\
        ObjectDirectorData shopArticleDirectorData = ObjectDirectorData.simple(getFileManager(), "ShopArticle");
        addDirector("ShopArticle", ShopArticle::fromFile);
        getShopArticleDirector().getBuilderManager().setBuilderBiFunction(
                ShopArticleBuilder::build);
        getShopArticleDirector().whenObjectManagerFilesLoad(manager -> addManager("MerchantManager", new MerchantManager(this)));
        getShopArticleDirector().addAdminChildCommand(data -> {
            String[] args = data.args();
            BlobExecutor executor = data.executor();
            CommandSender sender = data.sender();
            Result<BlobChildCommand> result = executor
                    .isChildCommand("opensellinventory", args);
            if (result.isValid()) {
                switch (args.length) {
                    case 1 -> {
                        return executor.ifInstanceOfPlayer(sender, ShopArticle::openSellInventory);
                    }
                    case 2 -> {
                        String playerName = args[1];
                        Player input = Bukkit.getPlayer(playerName);
                        if (input == null) {
                            BlobLibAssetAPI.getMessage("Player.Not-Found").toCommandSender(sender);
                            return true;
                        }
                        ShopArticle.openSellInventory(input);
                        return true;
                    }
                    default -> {
                        return false;
                    }
                }

            }
            return false;
        });
        getShopArticleDirector().addAdminChildTabCompleter(data -> {
            String[] args = data.args();
            List<String> suggestions = new ArrayList<>();
            switch (args.length) {
                case 1 -> {
                    suggestions.add("openSellInventory");
                    return suggestions;
                }
                case 2 -> {
                    BlobExecutor executor = data.executor();
                    Result<BlobChildCommand> result = executor
                            .isChildCommand("opensellinventory", args);
                    if (!result.isValid())
                        return null;
                    Bukkit.getOnlinePlayers().forEach(player -> suggestions.add(player.getName()));
                    executor.ifInstanceOfPlayer(data.sender(), player -> suggestions.remove(player.getName()));
                    return suggestions;
                }
                default -> {
                    return suggestions;
                }
            }
        });
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
        addManager("TrophyRequirementDirector", new ObjectDirector<>(
                this, trophyRequirementDirectorData,
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
