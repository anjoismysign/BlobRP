package us.mytheria.blobrp.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.api.BlobLibInventoryAPI;
import us.mytheria.bloblib.api.BlobLibListenerAPI;
import us.mytheria.bloblib.entities.ReloadableUI;
import us.mytheria.bloblib.entities.translatable.TranslatableItem;
import us.mytheria.blobrp.BlobRPAPI;
import us.mytheria.blobrp.entities.RoleplayWarp;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RoleplayWarpsUI implements ReloadableUI {
    private static RoleplayWarpsUI instance;

    protected RoleplayWarpsUI() {
        instance = this;
    }

    public static RoleplayWarpsUI getInstance() {
        return instance;
    }

    private final Plugin plugin = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("BlobRP"), "BlobRP not found in PluginManager");

    public void open(@NotNull Player player) {
        open(player, BlobRPAPI.getInstance().getWarps(player));
    }

    private void open(@NotNull Player player,
                      @NotNull List<RoleplayWarp> query) {
        Objects.requireNonNull(player, "'player' cannot be null");
        UUID uuid = player.getUniqueId();
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (player != Bukkit.getPlayer(uuid))
                return;
            BlobLibInventoryAPI.getInstance().customSelector(
                    "RoleplayWarps",
                    player,
                    "Warps",
                    "Warp",
                    () -> query,
                    roleplayWarp -> {
                        player.closeInventory();
                        roleplayWarp.warp(player);
                    },
                    roleplayWarp -> {
                        return TranslatableItem.by("BlobRP.Warp").localize(player)
                                .modder()
                                .replace("%display%", roleplayWarp.getPositionable().localize(player).getDisplay())
                                .get()
                                .get();
                    }
            );
        });
    }

    @Override
    public void reload(@NotNull BlobLibInventoryAPI inventoryAPI) {
        var roleplayWarpsRegistry = inventoryAPI.getInventoryDataRegistry("RoleplayWarps");
        roleplayWarpsRegistry.onClick("Reset-Search", blobInventoryClickEvent -> {
            Player player = (Player) blobInventoryClickEvent.getWhoClicked();
            open(player, BlobRPAPI.getInstance().getWarps(player));
        });
        roleplayWarpsRegistry.onClick("Search", blobInventoryClickEvent -> {
            Player player = (Player) blobInventoryClickEvent.getWhoClicked();
            player.closeInventory();
            BlobLibListenerAPI.getInstance().addChatListener(
                    player,
                    300,
                    input -> {
                        open(player, BlobRPAPI.getInstance().getWarps(player).stream()
                                .filter(warp -> {
                                    String display = ChatColor.stripColor(warp.getPositionable().localize(player).getDisplay()).toLowerCase();
                                    return display.contains(input.toLowerCase());
                                })
                                .toList());
                    },
                    "BlobTycoon.PlotHelper-Search-Timeout",
                    "BlobTycoon.PlotHelper-Search");
        });
    }
}
