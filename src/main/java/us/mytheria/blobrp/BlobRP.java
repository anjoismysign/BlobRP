package us.mytheria.blobrp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.mytheria.blobrp.director.RPManagerDirector;

public final class BlobRP extends JavaPlugin {
    private RPManagerDirector director;

    public static BlobRP instance;

    public static BlobRP getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        director = new RPManagerDirector();
        Bukkit.getScheduler().runTask(this, () ->
                director.postWorld());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public RPManagerDirector getDirector() {
        return director;
    }
}
