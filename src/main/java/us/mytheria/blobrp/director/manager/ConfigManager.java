package us.mytheria.blobrp.director.manager;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;

public class ConfigManager extends RPManager {
    private boolean registerWindmillListener;
    private boolean registerRegenablesListener;
    private Material adminWandMaterial;

    public ConfigManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        BlobRP main = BlobRP.getInstance();
        FileConfiguration config = main.getConfig();
        config.options().copyDefaults(true);
        registerWindmillListener = config.getBoolean("Setup.RegisterWindmillListener");
        registerRegenablesListener = config.getBoolean("Setup.RegisterRegenablesListener");
        adminWandMaterial = Material.valueOf(config.getString("Administrators.Wand", "WOODEN_SHOVEL"));

        main.saveConfig();
    }

    public Material getAdminWandMaterial() {
        return adminWandMaterial;
    }

    public boolean registerWindmillListener() {
        return registerWindmillListener;
    }

    public boolean isRegisterRegenablesListener() {
        return registerRegenablesListener;
    }
}
