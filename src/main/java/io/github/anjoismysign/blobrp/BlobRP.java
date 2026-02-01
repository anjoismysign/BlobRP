package io.github.anjoismysign.blobrp;

import io.github.anjoismysign.bloblib.entities.PluginUpdater;
import io.github.anjoismysign.bloblib.entities.proxy.BlobProxifier;
import io.github.anjoismysign.bloblib.managers.BlobPlugin;
import io.github.anjoismysign.bloblib.managers.IManagerDirector;
import io.github.anjoismysign.bloblib.managers.PluginManager;
import io.github.anjoismysign.bloblib.managers.asset.BukkitIdentityManager;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.entity.RoleplayKit;
import io.github.anjoismysign.blobrp.entity.configuration.RoleplayConfiguration;
import io.github.anjoismysign.blobrp.util.RoleplayMovementWarmup;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class BlobRP extends BlobPlugin {
    public static BlobRP INSTANCE;
    private RPManagerDirector director;
    private IManagerDirector proxy;
    private PluginUpdater updater;
    private BlobRPAPI api;
    private RoleplayConfiguration configuration;
    private RoleplayMovementWarmup movementWarmup;

    private BukkitIdentityManager<RoleplayKit> kitManager;

    public static BlobRP getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        configuration = RoleplayConfiguration.getInstance();
        movementWarmup = RoleplayMovementWarmup.initialize(this);
        updater = generateGitHubUpdater("anjoismysign", "BlobRP");

        PluginManager pluginManager = PluginManager.getInstance();
        kitManager = pluginManager.addIdentityManager(RoleplayKit.Info.class, this, "kits", true);

        director = new RPManagerDirector(this);
        proxy = BlobProxifier.PROXY(director);
        api = BlobRPAPI.getInstance(director);

        Bukkit.getScheduler().runTask(this, () -> {
                director.postWorld();
        });
    }

    @Override
    public void onDisable() {
        director.unload();
    }

    public IManagerDirector getManagerDirector() {
        return proxy;
    }

    @Override
    @NotNull
    public PluginUpdater getPluginUpdater() {
        return updater;
    }

    public BlobRPAPI getApi() {
        return api;
    }

    public BukkitIdentityManager<RoleplayKit> getKitManager() {
        return kitManager;
    }
}
