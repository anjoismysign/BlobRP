package io.github.anjoismysign.blobrp.ui;

import io.github.anjoismysign.bloblib.api.BlobLibInventoryAPI;

public class RoleplayUI {
    private static RoleplayUI instance;
    private final RoleplayWarpsUI roleplayWarpsUI;

    public static RoleplayUI getInstance() {
        if (instance == null) {
            instance = new RoleplayUI();
        }
        return instance;
    }

    private RoleplayUI() {
        roleplayWarpsUI = new RoleplayWarpsUI();
    }

    public void reload() {
        BlobLibInventoryAPI inventoryAPI = BlobLibInventoryAPI.getInstance();
        roleplayWarpsUI.reload(inventoryAPI);
    }
}
