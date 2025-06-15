package io.github.anjoismysign.blobrp.pressure;

import org.bukkit.Bukkit;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;
import io.github.anjoismysign.bloblib.FluidPressureAPI;
import io.github.anjoismysign.blobrp.BlobRP;
import io.github.anjoismysign.blobrp.entities.configuration.PressureConfiguration;

import java.util.UUID;

public class PlayerPressure {
    private final Player player;
    private final UUID uuid;
    private final BukkitTask task;

    private boolean isAffected;
    private double pressure;
    private double depthRating;

    protected PlayerPressure(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.task = runnable();
    }

    public void cancel() {
        task.cancel();
    }

    private BukkitTask runnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getPlayer(uuid) != player) {
                    cancel();
                    return;
                }
                PressureConfiguration pressureConfiguration = PressureConfiguration.getInstance();
                @Nullable WorldPressure worldPressure = pressureConfiguration.getPressure(player.getWorld().getName());
                if (worldPressure == null)
                    return;
                pressure = worldPressure.getPressure(player);
                depthRating = FluidPressureAPI.getInstance().getEquipment(player).stream()
                        .mapToDouble(Double::doubleValue)
                        .sum() + pressureConfiguration.getTolerance();
                double supported = (depthRating - pressure);
                if (supported >= 0.0) {
                    isAffected = false;
                    return;
                }
                isAffected = true;
                player.damage(pressureConfiguration.toHealth(supported), DamageSource.builder(DamageType.CRAMMING).build());
            }
        }.runTaskTimer(BlobRP.getInstance(), 0, 1);
    }

    public boolean isAffected() {
        return isAffected;
    }

    public double getPressure() {
        return pressure;
    }

    public double getDepthRating() {
        return depthRating;
    }
}
