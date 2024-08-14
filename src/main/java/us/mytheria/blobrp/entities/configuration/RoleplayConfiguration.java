package us.mytheria.blobrp.entities.configuration;

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

    private RoleplayConfiguration() {
    }

    public void reload(@NotNull ConfigurationSection settingsSection) {
        roleplayWarpConfiguration = RoleplayWarpConfiguration.READ(settingsSection);
    }

    @NotNull
    public RoleplayWarpConfiguration getRoleplayWarpConfiguration() {
        return roleplayWarpConfiguration;
    }
}
