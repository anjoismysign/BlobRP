package us.mytheria.blobrp.merchant;

import global.warming.commons.io.FilenameUtils;
import us.mytheria.bloblib.managers.BlobPlugin;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.inventories.MerchantInventory;

import java.io.File;
import java.util.HashMap;

public class MerchantManager extends RPManager {
    public BlobPlugin plugin;
    private HashMap<String, MerchantInventory> merchants;
    private HashMap<String, MerchantInventory> merchantsByTitle;
    private final File directory;

    public MerchantManager(RPManagerDirector director) {
        super(director);
        this.plugin = director.getPlugin();
        directory = director.getFileManager().metaInventoriesDirectory();
        reload();
        new MerchantCmd(director);
    }

    public void reload() {
        merchants = new HashMap<>();
        merchantsByTitle = new HashMap<>();
        loadFiles(directory, getManagerDirector());
    }

    private void loadFiles(File path, RPManagerDirector director) {
        File[] listOfFiles = path.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().equals(".DS_Store"))
                    continue;
                MerchantInventory merchantInventory = MerchantInventory.fromFile(file, director);
                if (merchantInventory == null)
                    continue;
                merchants.put(FilenameUtils.removeExtension(file.getName()), merchantInventory);
                merchantsByTitle.put(merchantInventory.getTitle(), merchantInventory);
            }
            if (file.isDirectory())
                loadFiles(file, director);
        }
    }

    public HashMap<String, MerchantInventory> getMerchants() {
        return merchants;
    }

    public HashMap<String, MerchantInventory> getMerchantsByTitle() {
        return merchantsByTitle;
    }
}
