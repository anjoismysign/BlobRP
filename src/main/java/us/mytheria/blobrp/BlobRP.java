package us.mytheria.blobrp;

import org.bukkit.Bukkit;
import us.mytheria.bloblib.entities.proxy.BlobProxifier;
import us.mytheria.bloblib.managers.BlobPlugin;
import us.mytheria.bloblib.managers.IManagerDirector;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.playerserializer.SimplePlayerSerializer;

public class BlobRP extends BlobPlugin {
    private RPManagerDirector director;
    private IManagerDirector proxy;
    protected SimplePlayerSerializer simplePlayerSerializer;
    private BlobRPAPI api;
    private SoulAPI soulAPI;
    private UniqueAPI uniqueAPI;

    public static BlobRP instance;

    public static BlobRP getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        simplePlayerSerializer = new SimplePlayerSerializer();
        director = new RPManagerDirector();
        proxy = BlobProxifier.PROXY(director);
        api = BlobRPAPI.getInstance(director);
        soulAPI = SoulAPI.getInstance(director);
        uniqueAPI = UniqueAPI.getInstance(director);
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

    public SimplePlayerSerializer getSimplePlayerSerializer() {
        return simplePlayerSerializer;
    }

    public BlobRPAPI getApi() {
        return api;
    }

    public SoulAPI getSoulAPI() {
        return soulAPI;
    }

    public UniqueAPI getUniqueAPI() {
        return uniqueAPI;
    }
}
