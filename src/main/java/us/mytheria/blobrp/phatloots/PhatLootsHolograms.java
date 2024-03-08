package us.mytheria.blobrp.phatloots;

import com.codisimus.plugins.phatloots.PhatLoot;
import com.codisimus.plugins.phatloots.PhatLootChest;
import com.codisimus.plugins.phatloots.PhatLootsAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import us.mytheria.bloblib.api.BlobLibHologramAPI;
import us.mytheria.bloblib.api.BlobLibTranslatableAPI;
import us.mytheria.bloblib.entities.ComplexEventListener;
import us.mytheria.bloblib.entities.translatable.TranslatableBlock;
import us.mytheria.blobrp.director.manager.ConfigManager;
import us.mytheria.blobrp.listeners.RPListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PhatLootsHolograms extends RPListener {
    private final Map<String, BukkitTask> holograms;

    private static final BlobLibTranslatableAPI translatableAPI = BlobLibTranslatableAPI.getInstance();
    private static final BlobLibHologramAPI hologramAPI = BlobLibHologramAPI.getInstance();

    public PhatLootsHolograms(ConfigManager configManager) {
        super(configManager);
        holograms = new HashMap<>();
    }

    private String parseTime(long milliseconds) {
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
                    long timeMillis = phatLoot.getTimeRemaining(null, phatLootChest);
                    TranslatableBlock translatableBlock = timeMillis > 0
                            ? translatableAPI.getTranslatableBlock("BlobRP.PhatLoots-Hologram-Looted")
                            : translatableAPI.getTranslatableBlock("BlobRP.PhatLoots-Hologram-Restocked");
                    UUID random = UUID.randomUUID();
                    String key = random.toString();
                    hologramAPI.createHologram(key,
                            phatLootChest.getBlock().getLocation().clone().add(pivot),
                            translatableBlock.get(), false);
                    add(key, phatLoot, phatLootChest);
                });
            });
        }
    }

    private void add(String key, PhatLoot phatLoot, PhatLootChest chest) {
        holograms.put(key, Bukkit.getScheduler().runTaskTimer(getConfigManager().getPlugin(), () -> {
            long remainingMillis = phatLoot.getTimeRemaining(null, chest);
            TranslatableBlock newTranslatableBlock = remainingMillis > 0
                    ? translatableAPI.getTranslatableBlock("BlobRP.PhatLoots-Hologram-Looted")
                    : translatableAPI.getTranslatableBlock("BlobRP.PhatLoots-Hologram-Restocked");
            List<String> lines = newTranslatableBlock.modder().replace("%time%",
                    parseTime(remainingMillis)).get().get();
            hologramAPI.setHologramLines(key, lines);
        }, 20L, 20L));
    }
}
