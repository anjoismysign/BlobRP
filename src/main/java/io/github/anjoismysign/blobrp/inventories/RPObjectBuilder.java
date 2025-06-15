package io.github.anjoismysign.blobrp.inventories;

import io.github.anjoismysign.bloblib.entities.BlobObject;
import io.github.anjoismysign.bloblib.entities.ObjectDirector;
import io.github.anjoismysign.bloblib.entities.inventory.BlobInventory;
import io.github.anjoismysign.bloblib.entities.inventory.ObjectBuilder;
import io.github.anjoismysign.blobrp.BlobRP;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;

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