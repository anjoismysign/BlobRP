package io.github.anjoismysign.blobrp.placeholderapi;

import com.codisimus.plugins.phatloots.PhatLoot;
import com.codisimus.plugins.phatloots.PhatLootChest;
import com.codisimus.plugins.phatloots.PhatLootsAPI;
import io.github.anjoismysign.bloblib.BlobLib;
import io.github.anjoismysign.bloblib.api.BlobLibTranslatableAPI;
import io.github.anjoismysign.bloblib.placeholderapi.BlobPHExpansion;
import io.github.anjoismysign.blobrp.BlobRP;
import io.github.anjoismysign.blobrp.phatloots.PhatLootsHolograms;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PhatLootsPH {
    private static PhatLootsPH instance;
    private BlobPHExpansion expansion;
    private static BlobLibTranslatableAPI API = BlobLibTranslatableAPI.getInstance();

    @NotNull
    public static PhatLootsPH getInstance(@NotNull BlobRP plugin) {
        if (instance == null) {
            Objects.requireNonNull(plugin, "injected dependency is null");
            instance = new PhatLootsPH(plugin);
        }
        return instance;
    }

    private PhatLootsPH(@NotNull BlobRP plugin) {
        instance = this;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            BlobLib.getAnjoLogger().log("PlaceholderAPI not found, not registering PhatLoots PlaceholderAPI expansion");
            return;
        }
        expansion = new BlobPHExpansion(plugin, "phatloots");
        expansion.putStartsWith("timeRemaining_", (offlinePlayer, identifier) -> {
            String[] split = identifier.split("_");
            if (split.length != 5) {
                return identifier;
            }
            Player player = Bukkit.getPlayer(offlinePlayer.getUniqueId());
            if (player == null) {
                return "Not online";
            }
            String phatLootName = split[0];
            @Nullable PhatLoot phatLoot = PhatLootsAPI.getPhatLoot(phatLootName);
            if (phatLoot == null){
                return "Not a PhatLoot/LootTable: "+phatLootName;
            }
            String worldName = split[1];
            @Nullable World world = Bukkit.getWorld(worldName);
            if (world == null){
                return "Not a world: "+worldName;
            }
            int x = Integer.parseInt(split[2]);
            int y = Integer.parseInt(split[3]);
            int z = Integer.parseInt(split[4]);
            Location location = new Location(world, x, y, z);
            @Nullable PhatLootChest phatLootChest = PhatLootsAPI.getAllPhatLootChests()
                    .stream()
                    .filter(chest->{
                        Location chestLocation = chest.getLocation();
                        return chestLocation.getWorld().getName().equals(location.getWorld().getName()) && chestLocation.toVector().toBlockVector().equals(location.toVector().toBlockVector());
                    })
                    .findFirst()
                    .orElse(null);
            if (phatLootChest == null){
                return "Not a PhatLootChest: "+identifier;
            }
            long remainingMillis = phatLoot.getTimeRemaining(null, phatLootChest);
            String timeRemaining = PhatLootsHolograms.parseTime(remainingMillis);
            return API.getTranslatableSnippet(remainingMillis > 0 ? "PhatLoots.Loot-Looted" : "PhatLoots.Loot-Restocked", player)
                    .modder()
                    .replace("%time%", timeRemaining)
                    .get()
                    .get();
        });
    }
}
