package us.mytheria.blobrp.director;

import us.mytheria.bloblib.managers.Manager;
import us.mytheria.blobrp.BlobRP;

public class RPManager extends Manager {

    public RPManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public RPManagerDirector getManagerDirector() {
        return (RPManagerDirector) super.getManagerDirector();
    }

    @Override
    public BlobRP getPlugin() {
        return (BlobRP) super.getPlugin();
    }
}
