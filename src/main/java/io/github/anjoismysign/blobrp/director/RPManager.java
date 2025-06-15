package io.github.anjoismysign.blobrp.director;

import io.github.anjoismysign.bloblib.entities.GenericManager;
import io.github.anjoismysign.blobrp.BlobRP;

public class RPManager extends GenericManager<BlobRP, RPManagerDirector> {

    public RPManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }
}
