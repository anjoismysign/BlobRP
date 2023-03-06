package us.mytheria.blobrp.merchant;

import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.bloblib.managers.BlobPlugin;
import us.mytheria.bloblib.managers.MetaInventoryShard;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.inventories.MerchantInventory;

import java.util.HashMap;
import java.util.Optional;

public class MerchantManager extends RPManager {
    public BlobPlugin plugin;
    private HashMap<String, MerchantInventory> merchants;
    private HashMap<String, MerchantInventory> merchantsByTitle;

    public MerchantManager(RPManagerDirector director) {
        super(director);
        this.plugin = director.getPlugin();
        reload();
        new MerchantCmd(director);
    }

    public void reload() {
        merchants = new HashMap<>();
        merchantsByTitle = new HashMap<>();
        loadInventories();
    }

    private void loadInventories() {
        Optional<MetaInventoryShard> optional = BlobLibAssetAPI.hasMetaInventoryShard("MERCHANT");
        if (optional.isEmpty()) {
            getPlugin().getAnjoLogger().singleError("There are no MERCHANT inventories to load.");
            return;
        }
        MetaInventoryShard shard = optional.get();
        shard.allInventories().forEach(referenceMetaBlobInventory -> {
            MerchantInventory merchantInventory = new MerchantInventory(getManagerDirector(),
                    referenceMetaBlobInventory);
            merchants.put(merchantInventory.getKey(), merchantInventory);
            merchantsByTitle.put(merchantInventory.getTitle(), merchantInventory);
        });
    }

    public HashMap<String, MerchantInventory> getMerchants() {
        return merchants;
    }

    public HashMap<String, MerchantInventory> getMerchantsByTitle() {
        return merchantsByTitle;
    }
}
