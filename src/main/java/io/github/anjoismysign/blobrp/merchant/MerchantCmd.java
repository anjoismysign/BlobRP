package io.github.anjoismysign.blobrp.merchant;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.managers.BlobPlugin;
import io.github.anjoismysign.blobrp.director.RPManagerDirector;
import io.github.anjoismysign.blobrp.inventories.MerchantInventory;

import java.util.ArrayList;
import java.util.List;

public class MerchantCmd implements CommandExecutor, TabCompleter {
    private final RPManagerDirector director;

    public MerchantCmd(RPManagerDirector director) {
        this.director = director;
        BlobPlugin plugin = director.getPlugin();
        plugin.getCommand("blobmerchant").setExecutor(this);
        plugin.getCommand("blobmerchant").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!sender.hasPermission("blobrp.admin")) {
            BlobLibMessageAPI.getInstance()
                    .getMessage("System.No-Permission", sender)
                    .toCommandSender(sender);
            return true;
        }
        if (args.length < 1) {
            debug(sender);
            return true;
        }
        String arg1 = args[0];
        if (!arg1.equalsIgnoreCase("open")) {
            debug(sender);
            return true;
        }
        if (args.length < 2) {
            debug(sender);
            return true;
        }
        String key = args[1];
        if (!director.getMerchantManager().getMerchantKeys().contains(key)) {
            BlobLibMessageAPI.getInstance()
                    .getMessage("Merchant.Not-Found", sender)
                    .toCommandSender(sender);
            return true;
        }
        Player player;
        if (args.length < 3) {
            player = instanceOfPlayer(sender);
            if (player == null) {
                return true;
            }
        } else {
            player = Bukkit.getPlayer(args[2]);
            if (player == null) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("Player.Not-Found", sender)
                        .toCommandSender(sender);
                return true;
            }
        }
        MerchantInventory inventory = director.getMerchantManager().getMerchant(key, player);
        if (inventory == null)
            throw new NullPointerException("MerchantInventory is null");
        inventory.open(player);
        return true;
    }

    private Player instanceOfPlayer(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            Bukkit.getLogger().info("You must be a player to use this command");
            return null;
        }
        return player;
    }

    public void debug(CommandSender sender) {
        if (sender.hasPermission("blobrp.debug")) {
            sender.sendMessage("/blobmerchant open <key> <playerName>");
        }
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("blobmerchant")) {
            List<String> l = new ArrayList<>();
            if (sender.hasPermission("blobrp.admin")) {
                switch (args.length) {
                    case 1 -> l.add("open");
                    case 2 -> l.addAll(director.getMerchantManager().getMerchantKeys());
                    case 3 -> {
                        return null;
                    }
                    default -> {
                        return l;
                    }
                }
                return l;
            }
        }
        return null;
    }
}
