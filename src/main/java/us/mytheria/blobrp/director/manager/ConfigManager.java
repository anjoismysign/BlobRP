package us.mytheria.blobrp.director.manager;

import org.bukkit.configuration.file.FileConfiguration;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;

public class ConfigManager extends RPManager {
    private boolean registerDropNonSoulOnDeath;

    public ConfigManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        BlobRP main = BlobRP.getInstance();
        FileConfiguration config = main.getConfig();
        config.options().copyDefaults(true);
        registerDropNonSoulOnDeath = config.getBoolean("Listeners.DropNonSoulOnDeath");
        main.saveConfig();
    }

    public boolean registerDropNonSoulOnDeath() {
        return registerDropNonSoulOnDeath;
    }
}
