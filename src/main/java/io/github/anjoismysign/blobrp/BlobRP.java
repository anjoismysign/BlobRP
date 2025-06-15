package io.github.anjoismysign.blobrp;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import io.github.anjoismysign.bloblib.entities.PluginUpdater;
import io.github.anjoismysign.bloblib.entities.proxy.BlobProxifier;
import io.github.anjoismysign.bloblib.managers.BlobPlugin;
import io.github.anjoismysign.bloblib.managers.IManagerDirector;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.entities.configuration.RoleplayConfiguration;
import io.github.anjoismysign.blobrp.entities.playerserializer.SimplePlayerSerializer;
import io.github.anjoismysign.blobrp.util.RoleplayMovementWarmup;

public class BlobRP extends BlobPlugin {
    public static BlobRP instance;
    protected SimplePlayerSerializer simplePlayerSerializer;
    private RPManagerDirector director;
    private IManagerDirector proxy;
    private PluginUpdater updater;
    private BlobRPAPI api;
    private RoleplayConfiguration configuration;
    private RPShortcut shortcut;
    private RoleplayMovementWarmup movementWarmup;

    public static BlobRP getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        configuration = RoleplayConfiguration.getInstance();
        movementWarmup = RoleplayMovementWarmup.initialize(this);
        updater = generateGitHubUpdater("anjoismysign", "BlobRP");
        simplePlayerSerializer = new SimplePlayerSerializer();
        director = new RPManagerDirector(this);
        proxy = BlobProxifier.PROXY(director);
        api = BlobRPAPI.getInstance(director);
        shortcut = RPShortcut.getInstance(director);
        Bukkit.getScheduler().runTask(this, () ->
                director.postWorld());
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

    public SimplePlayerSerializer getSimplePlayerSerializer() {
        return simplePlayerSerializer;
    }

    public BlobRPAPI getApi() {
        return api;
    }
}
