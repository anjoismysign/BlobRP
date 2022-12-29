package us.mytheria.blobrp;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.blobrp.director.manager.FileManager;

public final class BlobRPAPI extends JavaPlugin {

    static BlobRP main = BlobRP.getInstance();

    //todo make inventories folder
    public static BlobInventory buildInventory(String path) {
        FileManager fileManager = main.getDirector().getFileManager();
        YamlConfiguration inventories = fileManager.getYml(fileManager.inventoriesFile());
        return BlobInventory.fromConfigurationSection(inventories.getConfigurationSection(path));
    }
}
