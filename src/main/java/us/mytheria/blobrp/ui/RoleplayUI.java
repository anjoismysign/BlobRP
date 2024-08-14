package us.mytheria.blobrp.ui;

import us.mytheria.bloblib.api.BlobLibInventoryAPI;

public class RoleplayUI {
    private static RoleplayUI instance;

    private RoleplayUI() {
        roleplayWarpsUI = new RoleplayWarpsUI();
    }

    public static RoleplayUI getInstance() {
        if (instance == null) {
            instance = new RoleplayUI();
        }
        return instance;
    }

    private final RoleplayWarpsUI roleplayWarpsUI;

    public void reload() {
        BlobLibInventoryAPI inventoryAPI = BlobLibInventoryAPI.getInstance();
        roleplayWarpsUI.reload(inventoryAPI);
    }
}
