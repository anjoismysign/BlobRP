package io.github.anjoismysign.blobrp.ui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import io.github.anjoismysign.bloblib.api.BlobLibInventoryAPI;
import io.github.anjoismysign.bloblib.api.BlobLibListenerAPI;
import io.github.anjoismysign.bloblib.entities.ReloadableUI;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.blobrp.BlobRPAPI;
import io.github.anjoismysign.blobrp.entities.RoleplayWarp;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class RoleplayWarpsUI implements ReloadableUI {
    private static RoleplayWarpsUI instance;
    private final Plugin plugin = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("BlobRP"), "BlobRP not found in PluginManager");

    public static RoleplayWarpsUI getInstance() {
        return instance;
    }

    protected RoleplayWarpsUI() {
        instance = this;
    }

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
                    },
                    null,
                    null,
                    null);
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
                                    String display = ChatColor.stripColor(warp.getPositionable().localize(player).getDisplay()).toLowerCase(Locale.ROOT);
                                    return display.contains(input.toLowerCase(Locale.ROOT));
                                })
                                .toList());
                    },
                    "System.Search-Timeout",
                    "System.Search");
        });
    }
}
