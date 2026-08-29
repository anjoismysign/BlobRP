package io.github.anjoismysign.blobrp.phatloots;

import com.codisimus.plugins.phatloots.PhatLootsAPI;
import io.github.anjoismysign.bloblib.api.BlobLibHologramAPI;
import io.github.anjoismysign.bloblib.api.BlobLibTranslatableAPI;
import io.github.anjoismysign.bloblib.listener.ComplexEventListener;
import io.github.anjoismysign.bloblib.translatable.TranslatableBlock;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;
import io.github.anjoismysign.blobrp.listener.RPListener;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PhatLootsHolograms extends RPListener {
    private static final BlobLibTranslatableAPI translatableAPI = BlobLibTranslatableAPI.getInstance();
    private static final BlobLibHologramAPI hologramAPI = BlobLibHologramAPI.getInstance();
    private final Map<String, BukkitTask> holograms;

    public PhatLootsHolograms(ConfigManager configManager) {
        super(configManager);
        holograms = new HashMap<>();
    }

    public static String parseTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 24;

        StringBuilder result = new StringBuilder();
        if (days > 0) {
            result.append(days).append("d ");
        }
        if (hours > 0) {
            result.append(hours).append("h ");
        }
        if (minutes > 0) {
            result.append(minutes).append("m ");
        }
        result.append(seconds).append("s");
        return result.toString();
    }

    public void reload() {
        HandlerList.unregisterAll(this);
        holograms.forEach((key, task) -> {
            task.cancel();
            hologramAPI.removeHologram(key);
        });
        holograms.clear();
        ComplexEventListener phatLootsHolograms = getConfigManager().phatLootsHolograms();
        if (phatLootsHolograms.register()) {
            Vector pivot = new Vector(phatLootsHolograms.getDouble("X"),
                    phatLootsHolograms.getDouble("Y"),
                    phatLootsHolograms.getDouble("Z"));
            Bukkit.getPluginManager().registerEvents(this, getConfigManager().getPlugin());
            PhatLootsAPI.getAllPhatLoots().forEach(phatLoot -> {
                phatLoot.getChests().forEach(phatLootChest -> {
                    Block block = phatLootChest.getBlock();
                    String worldName = block.getWorld().getName();
                    int x = block.getX();
                    int y = block.getY();
                    int z = block.getZ();
                    TranslatableBlock translatableBlock = Objects.requireNonNull(TranslatableBlock.by("PhatLoots.Loot"), "'PhatLoots.Loot' is not a TranslatableBlock!");
                    UUID random = UUID.randomUUID();
                    String key = random.toString();
                    hologramAPI.createHologram(key,
                            phatLootChest.getBlock().getLocation().clone().add(pivot),
                            translatableBlock.modder().replace("$phatLoot$", phatLoot.getName()+"_"+worldName+"_"+x+"_"+y+"_"+z).get().get(),
                            false);
                });
            });
        }
    }
}
