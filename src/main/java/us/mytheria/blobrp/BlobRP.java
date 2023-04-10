package us.mytheria.blobrp;

import org.bukkit.Bukkit;
import us.mytheria.bloblib.managers.BlobPlugin;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.playerserializer.SimplePlayerSerializer;

public final class BlobRP extends BlobPlugin {
    private RPManagerDirector director;
    protected SimplePlayerSerializer simplePlayerSerializer;

    public static BlobRP instance;

    public static BlobRP getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        simplePlayerSerializer = new SimplePlayerSerializer();
        director = new RPManagerDirector();
        new BlobRPAPI(director);
        Bukkit.getScheduler().runTask(this, () ->
                director.postWorld());
    }

    @Override
    public RPManagerDirector getManagerDirector() {
        return director;
    }

    public SimplePlayerSerializer getSimplePlayerSerializer() {
        return simplePlayerSerializer;
    }
}
