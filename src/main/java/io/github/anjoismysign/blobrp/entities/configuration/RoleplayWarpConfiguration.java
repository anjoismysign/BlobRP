package io.github.anjoismysign.blobrp.entities.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import io.github.anjoismysign.bloblib.exception.ConfigurationFieldException;

public record RoleplayWarpConfiguration(
        boolean isEnabled,
        boolean useGlobalWarmup,
        long getGlobalWarmup
) {

    @NotNull
    public static RoleplayWarpConfiguration getInstance() {
        return RoleplayConfiguration.getInstance().getRoleplayWarpConfiguration();
    }

    public static RoleplayWarpConfiguration READ(@NotNull ConfigurationSection parent) {
        ConfigurationSection roleplayWarpSection = parent.getConfigurationSection("RoleplayWarp");
        if (roleplayWarpSection == null)
            throw new ConfigurationFieldException("'RoleplayWarp' is not set or valid");
        boolean isEnabled = roleplayWarpSection.getBoolean("Enabled", true);
        boolean useGlobalCooldown = roleplayWarpSection.getBoolean("Use-Global-Warmup", true);
        long globalCooldown = roleplayWarpSection.getLong("Global-Warmup", 100);
        return new RoleplayWarpConfiguration(isEnabled, useGlobalCooldown, globalCooldown);
    }
}
