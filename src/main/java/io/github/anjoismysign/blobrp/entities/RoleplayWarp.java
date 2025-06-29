package io.github.anjoismysign.blobrp.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.github.anjoismysign.bloblib.entities.BlobObject;
import io.github.anjoismysign.bloblib.entities.message.BlobSound;
import io.github.anjoismysign.bloblib.entities.positionable.Positionable;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatablePositionable;
import io.github.anjoismysign.bloblib.exception.ConfigurationFieldException;
import io.github.anjoismysign.blobrp.entities.configuration.RoleplayWarpConfiguration;
import io.github.anjoismysign.blobrp.util.RoleplayMovementWarmup;

import java.io.File;
import java.util.Objects;

public record RoleplayWarp(
        @NotNull String getKey,
        @NotNull TranslatablePositionable getPositionable,
        boolean requiresPermission,
        @NotNull String getPermission,
        @Nullable BlobSound getTeleportSound,
        boolean hasWarmup,
        long getWarmup)
        implements BlobObject {

    private static final Plugin plugin = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("BlobRP"), "BlobRP not found in PluginManager");

    public static RoleplayWarp fromFile(File file) {
        String fileName = file.getName();
        String key = fileName.replace(".yml", "");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.isString("Positionable"))
            throw new ConfigurationFieldException("'Positionable' is not valid or set");
        String positionableReference = config.getString("Positionable");
        TranslatablePositionable positionable = TranslatablePositionable.by(positionableReference);
        if (positionable == null)
            throw new ConfigurationFieldException("'Positionable' doesn't point to a TranslatablePositionable");
        String permission = config.getString("Permission", "");
        boolean requiresPermission = !permission.trim().isEmpty();
        @Nullable String blobSoundKey = config.getString("Teleport-Sound");
        @Nullable BlobSound blobSound = blobSoundKey == null ? null : BlobSound.by(blobSoundKey);
        long cooldown = config.getLong("Warmup", 0);
        boolean hasCooldown = cooldown > 0;
        return new RoleplayWarp(key, positionable, requiresPermission, permission, blobSound, hasCooldown, cooldown);
    }

    @Override
    public File saveToFile(File directory) {
        File file = instanceFile(directory);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("Positionable", getPositionable.identifier());
        if (requiresPermission)
            config.set("Permission", getPermission);
        if (getTeleportSound != null)
            config.set("Teleport-Sound", getTeleportSound.identifier());
        if (hasWarmup)
            config.set("Warmup", getWarmup);
        try {
            config.save(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return file;
    }

    public boolean hasPermission(@NotNull Permissible permissible) {
        String permission = getPermission.trim();
        if (!requiresPermission || permission.isEmpty())
            return true;
        return permissible.hasPermission(permission);
    }

    private void teleport(@NotNull Entity entity) {
        Positionable positionable = getPositionable.get();
        Location location = positionable.getPositionableType().isLocatable() ? positionable.toLocation() : positionable.toLocation(entity.getWorld());
        entity.teleport(location);
        if (getTeleportSound != null)
            getTeleportSound.handle(entity);
    }

    public void warp(@NotNull Entity entity) {
        Objects.requireNonNull(entity, "'entity' cannot be null");
        RoleplayWarpConfiguration configuration = RoleplayWarpConfiguration.getInstance();
        long warmup = configuration.useGlobalWarmup() ? configuration.getGlobalWarmup() : getWarmup;
        if (warmup <= 0) {
            teleport(entity);
            return;
        }
        if (entity.getType() != EntityType.PLAYER) {
            teleport(entity);
            return;
        }
        Player player = (Player) entity;
        RoleplayMovementWarmup.getInstance()
                .ofTicks(
                        warmup,
                        player,
                        uuid -> {
                            Player onlinePlayer = Bukkit.getPlayer(uuid);
                            if (onlinePlayer == null)
                                return;
                            teleport(player);
                        },
                        "BlobRP.Warping",
                        "BlobRP.Warping-Failed");

    }
}
