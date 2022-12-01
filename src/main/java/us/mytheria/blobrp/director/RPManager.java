package us.mytheria.blobrp.director;

import us.mytheria.bloblib.entities.manager.Manager;

public class RPManager extends Manager {

    public RPManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public RPManagerDirector getManagerDirector() {
        return (RPManagerDirector) super.getManagerDirector();
    }
}
