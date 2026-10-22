package io.github.anjoismysign.blobrp.director.manager;

import io.github.anjoismysign.bloblib.manager.SmartListenerManager;
import io.github.anjoismysign.blobrp.configuration.RPListenersConfiguration;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;

public class RPListenerManager extends SmartListenerManager<RPListenersConfiguration> {

    public RPListenerManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public Class<RPListenersConfiguration> getConfigurationClass() {
        return RPListenersConfiguration.class;
    }
}
