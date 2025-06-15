package io.github.anjoismysign.blobrp.director;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import io.github.anjoismysign.bloblib.entities.GenericManagerDirector;
import io.github.anjoismysign.bloblib.entities.ObjectDirector;
import io.github.anjoismysign.bloblib.managers.Manager;
import io.github.anjoismysign.blobrp.BlobRP;
import io.github.anjoismysign.blobrp.director.command.OpenSellInventory;
import io.github.anjoismysign.blobrp.director.command.RoleplayRecipeCmd;
import io.github.anjoismysign.blobrp.director.command.WarpCmd;
import io.github.anjoismysign.blobrp.director.manager.CloudInventoryManager;
import io.github.anjoismysign.blobrp.director.manager.CommandManager;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.director.manager.ListenerManager;
import io.github.anjoismysign.blobrp.entities.RoleplayRecipe;
import io.github.anjoismysign.blobrp.entities.RoleplayWarp;
import io.github.anjoismysign.blobrp.entities.ShopArticle;
import io.github.anjoismysign.blobrp.entities.blockphatloot.BlockPhatLootDirector;
import io.github.anjoismysign.blobrp.entities.regenable.RegenableBlockDirector;
import io.github.anjoismysign.blobrp.events.AsyncShopArticleReloadEvent;
import io.github.anjoismysign.blobrp.merchant.MerchantManager;
import io.github.anjoismysign.blobrp.placeholderapi.PressurePH;
import io.github.anjoismysign.blobrp.pressure.PressureManager;

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
        // Pressure \\
        addManager("Pressure", new PressureManager(this));
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
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            PressurePH.getInstance(getPlugin());
    }

    @Nullable
    public final Manager getBlockPhatLootDirector() {
        return getManager("PhatLoot");
    }

    public final PressureManager getPressureManager() {
        return getManager("Pressure", PressureManager.class);
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
