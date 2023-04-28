package us.mytheria.blobrp.director.manager;

import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.director.command.SoulCmd;

public class CommandManager extends RPManager {

    public CommandManager(RPManagerDirector managerDirector) {
        super(managerDirector);
        new SoulCmd();
//        new BalloonCmd();
    }
}
