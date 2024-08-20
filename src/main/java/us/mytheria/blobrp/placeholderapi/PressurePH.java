package us.mytheria.blobrp.placeholderapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.mytheria.bloblib.BlobLib;
import us.mytheria.bloblib.api.BlobLibTranslatableAPI;
import us.mytheria.bloblib.entities.BlobPHExpansion;
import us.mytheria.bloblib.utilities.Formatter;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.BlobRPAPI;
import us.mytheria.blobrp.pressure.PlayerPressure;

import java.util.Objects;
import java.util.UUID;

public class PressurePH {
    private static PressurePH instance;
    private BlobPHExpansion expansion;

    @NotNull
    public static PressurePH getInstance(@NotNull BlobRP plugin) {
        if (instance == null) {
            Objects.requireNonNull(plugin, "injected dependency is null");
            instance = new PressurePH(plugin);
        }
        return instance;
    }

    private PressurePH(@NotNull BlobRP plugin) {
        instance = this;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            BlobLib.getAnjoLogger().log("PlaceholderAPI not found, not registering Pressure PlaceholderAPI expansion");
            return;
        }
        BlobPHExpansion expansion = new BlobPHExpansion(plugin, "pressure");
        this.expansion = expansion;
        expansion.putSimple("isAffected", offlinePlayer -> {
            Player player = Bukkit.getPlayer(offlinePlayer.getUniqueId());
            if (player == null)
                return "Not online";
            UUID uuid = player.getUniqueId();
            @Nullable PlayerPressure pressure = BlobRPAPI.getInstance().getPressure(uuid);
            if (pressure == null)
                return "Not in cache";
            if (pressure.isAffected())
                return BlobLibTranslatableAPI.getInstance().getTranslatableSnippet("BlobRP.Pressure-Is-Affected", player).get();
            return BlobLibTranslatableAPI.getInstance().getTranslatableSnippet("BlobRP.Pressure-Is-Not-Affected", player).get();
        });
        expansion.putSimple("pressure", offlinePlayer -> {
            Player player = Bukkit.getPlayer(offlinePlayer.getUniqueId());
            if (player == null)
                return "Not online";
            UUID uuid = player.getUniqueId();
            @Nullable PlayerPressure pressure = BlobRPAPI.getInstance().getPressure(uuid);
            if (pressure == null)
                return "Not in cache";
            return Formatter.getInstance().formatAll("%psi%", pressure.getPressure());
        });
        expansion.putSimple("depthRating", offlinePlayer -> {
            Player player = Bukkit.getPlayer(offlinePlayer.getUniqueId());
            if (player == null)
                return "Not online";
            UUID uuid = player.getUniqueId();
            @Nullable PlayerPressure pressure = BlobRPAPI.getInstance().getPressure(uuid);
            if (pressure == null)
                return "Not in cache";
            return Formatter.getInstance().formatAll("%psi%", pressure.getDepthRating());
        });
    }

    public BlobPHExpansion getExpansion() {
        return expansion;
    }
}
