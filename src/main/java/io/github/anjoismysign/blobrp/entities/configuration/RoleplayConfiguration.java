package io.github.anjoismysign.blobrp.entities.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class RoleplayConfiguration {
    private static RoleplayConfiguration instance;

    public static RoleplayConfiguration getInstance() {
        if (instance == null)
            instance = new RoleplayConfiguration();
        return instance;
    }

    private RoleplayWarpConfiguration roleplayWarpConfiguration;
    private PressureConfiguration pressureConfiguration;

    private RoleplayConfiguration() {
    }

    public void reload(@NotNull ConfigurationSection settingsSection) {
        roleplayWarpConfiguration = RoleplayWarpConfiguration.READ(settingsSection);
        pressureConfiguration = PressureConfiguration.READ(settingsSection);
    }

    @NotNull
    public RoleplayWarpConfiguration getRoleplayWarpConfiguration() {
        return roleplayWarpConfiguration;
    }

    @NotNull
    public PressureConfiguration getPressureConfiguration() {
        return pressureConfiguration;
    }
}
