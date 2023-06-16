package us.mytheria.blobrp.inventories;

import us.mytheria.bloblib.entities.BlobObject;
import us.mytheria.bloblib.entities.ObjectDirector;
import us.mytheria.bloblib.entities.inventory.BlobInventory;
import us.mytheria.bloblib.entities.inventory.ObjectBuilder;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManagerDirector;

import java.util.UUID;

public abstract class RPObjectBuilder<T extends BlobObject> extends ObjectBuilder<T> {
    private final RPManagerDirector director;

    public RPObjectBuilder(BlobInventory blobInventory, UUID builderId,
                           ObjectDirector<T> objectDirector,
                           RPManagerDirector director) {
        super(blobInventory, builderId, objectDirector);
        this.director = director;
    }

    public RPManagerDirector getManagerDirector() {
        return director;
    }

    public BlobRP getPlugin() {
        return getManagerDirector().getPlugin();
    }
}