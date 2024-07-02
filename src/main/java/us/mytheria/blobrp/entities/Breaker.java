package us.mytheria.blobrp.entities;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import us.mytheria.blobdesign.entities.presetblock.PresetBlock;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.events.CustomBlockBreakEvent;

public class Breaker {
    private final Block block;
    private final PresetBlock<?> presetBlock;
    private final Display display;
    private final Vector3f originalScale;
    private final BukkitTask task;
    private int remainingTicks;
    private final int totalTicks;

    public static Breaker of(@NotNull Block block,
                             @NotNull PresetBlock<?> presetBlock,
                             int ticks,
                             @NotNull Player owner) {
        return new Breaker(block, presetBlock, ticks, owner);
    }

    private Breaker(Block block,
                    PresetBlock<?> presetBlock,
                    int ticks,
                    Player owner) {
        this.block = block;
        this.presetBlock = presetBlock;
        this.totalTicks = ticks;
        this.remainingTicks = ticks;
        this.display = presetBlock.getDecorator().call();
        this.originalScale = display.getTransformation().getScale();
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Transformation transformation = display.getTransformation();
                    display.setTransformation(new Transformation(
                            transformation.getTranslation(),
                            transformation.getLeftRotation(),
                            scaleProgress(),
                            transformation.getRightRotation()));
                    Breaker.this.remainingTicks = remainingTicks - 1;
                    if (Breaker.this.remainingTicks <= 0) {
                        cancel();
                        CustomBlockBreakEvent event = new CustomBlockBreakEvent(presetBlock, owner);
                        Bukkit.getPluginManager().callEvent(event);
                        if (event.isCancelled())
                            return;
                        presetBlock.despawn(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(BlobRP.getInstance(), 0, 1);
    }

    private Vector3f scaleProgress() {
        float progress = getProgress();
        float scaleProgress = 1 - progress;
        Vector3f scale = new Vector3f(originalScale);
        if (scaleProgress <= 0.0001)
            return display.getTransformation().getScale();
        return scale.mul(scaleProgress);
    }

    private int getRemainingTicks() {
        return remainingTicks;
    }

    public void cancel() {
        task.cancel();
    }

    public float getProgress() {
        return (float) (totalTicks - remainingTicks) / totalTicks;
    }

    public PresetBlock<?> getPresetBlock() {
        return presetBlock;
    }

    public Vector3f getOriginalScale() {
        return originalScale;
    }
}
