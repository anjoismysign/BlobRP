package us.mytheria.blobrp.inventories.builder;

import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.entities.inventory.ObjectBuilder;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManagerDirector;

import java.util.UUID;

public abstract class RPObjectBuilder<T> extends ObjectBuilder<T> {
    private final BlobRP main;

    public RPObjectBuilder(BlobInventory blobInventory, UUID builderId) {
        super(blobInventory, builderId);
        this.main = BlobRP.getInstance();
    }

    public RPManagerDirector getManagerDirector() {
        return main.getDirector();
    }
}