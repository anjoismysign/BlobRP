package us.mytheria.blobrp.entities.regenable;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.managers.BlobPlugin;
import us.mytheria.blobrp.entities.blocktype.BlockTypeFactory;

public class RegenableBlock {
    private final Block block;
    private final RegenableBlockData data;
    private final RegenableBlockDirector regenableBlockDirector;
    private BukkitTask task;

    public RegenableBlock(
            Block block,
            RegenableBlockData data,
            RegenableBlockDirector regenableBlockDirector) {
        this.block = block;
        this.data = data;
        this.regenableBlockDirector = regenableBlockDirector;
        runTask();
    }

    @NotNull
    private BlobPlugin getPlugin() {
        return regenableBlockDirector.getPlugin();
    }

    private void runTask() {
        BlobPlugin plugin = getPlugin();
        Bukkit.getScheduler().runTask(plugin, () -> data.setNew(block));
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (isCancelled())
                    return;
                if (!isAllowed()) {
                    regenableBlockDirector.removeRegenableBlock(RegenableBlock.this);
                    return;
                }
                Bukkit.getScheduler().runTask(plugin, () -> regen(false));
            }
        }.runTaskLaterAsynchronously(plugin, data.getDelay().next());
    }

    private boolean isAllowed() {
        return RegenableBlockDirector.getPriority(BlockTypeFactory.getInstance()
                .isBlockType(block.getType())) <= data.getPriority();
    }

    private void regen(boolean force) {
        data.regen(block);
        if (!force)
            regenableBlockDirector.removeRegenableBlock(this);
    }

    /**
     * Will cancel the regenable and restore the block to its original state.
     *
     * @param force If true, it won't remove the regenable from the manager.
     */
    public void cancel(boolean force) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(getPlugin(), () -> cancel(force));
            return;
        }
        if (task != null)
            task.cancel();
        if (isAllowed())
            regen(force);
    }
}
