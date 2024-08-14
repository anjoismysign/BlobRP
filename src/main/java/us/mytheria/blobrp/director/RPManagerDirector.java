package us.mytheria.blobrp.director;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.entities.GenericManagerDirector;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.managers.Manager;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.command.OpenSellInventory;
import us.mytheria.blobrp.director.command.RoleplayRecipeCmd;
import us.mytheria.blobrp.director.command.WarpCmd;
import us.mytheria.blobrp.director.manager.CloudInventoryManager;
import us.mytheria.blobrp.director.manager.CommandManager;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.director.manager.ListenerManager;
import us.mytheria.blobrp.entities.RoleplayRecipe;
import us.mytheria.blobrp.entities.RoleplayWarp;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.entities.blockphatloot.BlockPhatLootDirector;
import us.mytheria.blobrp.entities.regenable.RegenableBlockDirector;
import us.mytheria.blobrp.events.AsyncShopArticleReloadEvent;
import us.mytheria.blobrp.merchant.MerchantManager;

public class RPManagerDirector extends GenericManagerDirector<BlobRP> {

    public RPManagerDirector(BlobRP plugin) {
        super(plugin);
        registerMetaBlobInventory("WelcomeInventory", "PlayerInventory", "EventPlayerInventory");
        registerMetaBlobInventory("es_es/PlayerInventory", "es_es/EventPlayerInventory");
        registerBlobMessage("es_es/blobrp_lang");
        registerTranslatableBlock("es_es/blobrp_translatable_blocks");
        registerBlobInventory("RoleplayWarps");
        registerBlobInventory("es_es/ShopArticleBuilder");
        addManager("CommandManager", new CommandManager(this));
        addManager("ConfigManager", new ConfigManager(this));
        addManager("ListenerManager", new ListenerManager(this));
        addManager("CloudInventoryManager", new CloudInventoryManager(this));
        // ShopArticle \\
        addDirector("ShopArticle", ShopArticle::fromFile);
        addDirector("RoleplayWarp", RoleplayWarp::fromFile, false);
        if (Bukkit.getPluginManager().isPluginEnabled("PhatLoots")) {
            // BlockPhatLoot \\
            addManager("PhatLoot", new BlockPhatLootDirector(this));
        }
        addManager("RegenableBlock", new RegenableBlockDirector(this));
        getShopArticleDirector().whenObjectManagerFilesLoad(manager -> {
            try {
                addManager("MerchantManager", new MerchantManager(this));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        getShopArticleDirector().addAdminChildCommand(OpenSellInventory::command);
        // RoleplayRecipe \\
        addDirector("RoleplayRecipe", file ->
                RoleplayRecipe.of(this, file), false);
        RoleplayRecipeCmd.of(this);
        WarpCmd.getInstance();
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
        getRoleplayWarpDirector().reload();
        Manager phatLootDirector = getBlockPhatLootDirector();
        if (phatLootDirector != null)
            phatLootDirector.reload();
        getRegenableBlockDirector().reload();
        getShopArticleDirector().whenObjectManagerFilesLoad(manager -> {
            AsyncShopArticleReloadEvent event = new AsyncShopArticleReloadEvent();
            Bukkit.getPluginManager().callEvent(event);
            getMerchantManager().reload();
        });
        getRoleplayRecipeDirector().reload();
    }

    @Override
    public void unload() {
        getCloudInventoryManager().unload();
        getRegenableBlockDirector().unload();
    }

    @Override
    public void postWorld() {
    }

    @Nullable
    public final Manager getBlockPhatLootDirector() {
        return getManager("PhatLoot");
    }

    public final RegenableBlockDirector getRegenableBlockDirector() {
        return getManager("RegenableBlock", RegenableBlockDirector.class);
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

    public final ObjectDirector<RoleplayWarp> getRoleplayWarpDirector() {
        return getDirector("RoleplayWarp", RoleplayWarp.class);
    }

    public final ObjectDirector<RoleplayRecipe> getRoleplayRecipeDirector() {
        return getDirector("RoleplayRecipe", RoleplayRecipe.class);
    }

    public final CommandManager getCommandManager() {
        return getManager("CommandManager", CommandManager.class);
    }
}
