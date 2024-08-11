package us.mytheria.blobrp;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.entities.PluginUpdater;
import us.mytheria.bloblib.entities.proxy.BlobProxifier;
import us.mytheria.bloblib.managers.BlobPlugin;
import us.mytheria.bloblib.managers.IManagerDirector;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.playerserializer.SimplePlayerSerializer;

public class BlobRP extends BlobPlugin {
    private RPManagerDirector director;
    private IManagerDirector proxy;
    private PluginUpdater updater;
    protected SimplePlayerSerializer simplePlayerSerializer;
    private BlobRPAPI api;
    private RPShortcut shortcut;

    public static BlobRP instance;

    public static BlobRP getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
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

    @Override
    @NotNull
    public PluginUpdater getPluginUpdater() {
        return updater;
    }

    public IManagerDirector getManagerDirector() {
        return proxy;
    }

    public SimplePlayerSerializer getSimplePlayerSerializer() {
        return simplePlayerSerializer;
    }

    public BlobRPAPI getApi() {
        return api;
    }
}
