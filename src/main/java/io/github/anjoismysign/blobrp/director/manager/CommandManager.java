package io.github.anjoismysign.blobrp.director.manager;

import io.github.anjoismysign.blobrp.director.RPManager;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.director.command.SoulCmd;

public class CommandManager extends RPManager {

    public CommandManager(RPManagerDirector managerDirector) {
        super(managerDirector);
        new SoulCmd();
//        new BalloonCmd();
    }
}
