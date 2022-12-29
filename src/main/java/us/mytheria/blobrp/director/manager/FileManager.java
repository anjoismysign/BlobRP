package us.mytheria.blobrp.director.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import us.mytheria.bloblib.BlobLibDevAPI;
import us.mytheria.bloblib.utilities.ResourceUtil;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;

import java.io.File;
import java.util.HashMap;

public class FileManager extends RPManager {
    private File path;

    private HashMap<String, File> files;

    public FileManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        BlobRP main = BlobRP.getInstance();
        BlobLibDevAPI.addDefaultMessagesFile("lang", main);
        path = new File("plugins/BlobRP");
        files = new HashMap<>();
        files.put("ShopArticles", new File(path.getPath() + "/articles"));
        files.put("Inventories", new File(path.getPath() + "/inventories.yml"));

        try {
            if (!path.exists()) path.mkdir();
            if (!getShopArticles().exists()) getShopArticles().mkdir();
            ///////////////////////////////////////////
            if (!inventoriesFile().exists()) inventoriesFile().createNewFile();
            ResourceUtil.updateYml(path, "/tempInventories.yml", "inventories.yml", inventoriesFile(), main);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File inventoriesFile() {
        return files.get("Inventories");
    }

    public File getFile(String key) {
        return files.get(key);
    }

    public File getShopArticles() {
        return getFile("ShopArticles");
    }

    public YamlConfiguration getYml(File f) {
        return YamlConfiguration.loadConfiguration(f);
    }
}
