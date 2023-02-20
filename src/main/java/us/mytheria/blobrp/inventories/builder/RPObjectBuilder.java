package us.mytheria.blobrp.inventories.builder;

import us.mytheria.bloblib.entities.BlobObject;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.entities.inventory.ObjectBuilder;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManagerDirector;

import java.util.UUID;

public abstract class RPObjectBuilder<T extends BlobObject> extends ObjectBuilder<T> {
    private final BlobRP main;

    public RPObjectBuilder(BlobInventory blobInventory, UUID builderId,
                           ObjectDirector<T> objectDirector) {
        super(blobInventory, builderId, objectDirector);
        this.main = BlobRP.getInstance();
    }

    public RPManagerDirector getManagerDirector() {
        return main.getManagerDirector();
    }
}