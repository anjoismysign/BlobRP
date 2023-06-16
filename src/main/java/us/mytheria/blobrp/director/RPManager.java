package us.mytheria.blobrp.director;

import us.mytheria.bloblib.entities.GenericManager;
import us.mytheria.blobrp.BlobRP;

public class RPManager extends GenericManager<BlobRP, RPManagerDirector> {

    public RPManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }
}
