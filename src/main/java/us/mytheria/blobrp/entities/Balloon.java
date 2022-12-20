package us.mytheria.blobrp.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Shulker;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import us.mytheria.blobrp.BlobRP;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class Balloon implements Ship {
    private final HashMap<UUID, Shulker> shulkers;
    private final HashMap<UUID, FallingBlock> blocks;
    private final BukkitTask task;

    public static Balloon build() {
        return new Balloon();
    }

    private Balloon() {
        shulkers = new HashMap<>();
        blocks = new HashMap<>();
        task = new BukkitRunnable() {
            public void run() {
                getBlocks().forEach(block -> block.setTicksLived(1));
            }
        }.runTaskTimerAsynchronously(BlobRP.getInstance(), 0, 100);
    }

    @Override
    public boolean addPart(Shulker shulker, Entity entity) {
        if (!(entity instanceof FallingBlock fallingBlock)) {
            return false;
        }
        if (shulkers.containsKey(shulker.getUniqueId())) {
            shulker.remove();
            return false;
        }
        shulkers.put(shulker.getUniqueId(), shulker);
        if (blocks.containsKey(fallingBlock.getUniqueId())) {
            fallingBlock.remove();
            return false;
        }
        shulker.getBoundingBox().expand(2, 2, 2);
        shulker.setGravity(false);
        shulker.setAI(false);
        shulker.setInvisible(false);
//        shulker.addPassenger(fallingBlock);
        blocks.put(entity.getUniqueId(), fallingBlock);
        return true;
    }

    @Override
    public boolean addPart(ShipPart<?> shipPart) {
        return addPart(shipPart.shulker(), shipPart.entity());
    }

    @Override
    public void remove() {
        task.cancel();
        shulkers.values().forEach(Shulker::remove);
        blocks.values().forEach(FallingBlock::remove);
    }

    @Override
    public void move(Location location, double blocksPerDistanceCall,
                     long callDelay, long callPeriod) {
        Collection<Shulker> shulkers = getParts();
        new BukkitRunnable() {
            public void run() {
                if (shulkers.isEmpty()) {
                    cancel();
                    return;
                }
                shulkers.forEach(shulker -> {
                    Location clone = shulker.getLocation().clone();
                    double distance = clone.distance(location);
                    Vector subtract = location.toVector().subtract(clone.toVector());
                    if (distance > blocksPerDistanceCall) {
                        shulker.teleport(clone.add(subtract.multiply(blocksPerDistanceCall / distance)));
                    } else {
                        cancel();
                    }
                });
            }
        }.runTaskTimer(BlobRP.getInstance(), callDelay, callPeriod);
    }

    public Collection<FallingBlock> getBlocks() {
        return blocks.values();
    }

    @Override
    public Collection<Shulker> getParts() {
        return shulkers.values();
    }
}