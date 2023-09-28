package us.mytheria.blobrp.merchant;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.managers.BlobPlugin;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.inventories.MerchantInventory;

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
            BlobLibMessageAPI.getInstance().getMessage("System.No-Permission").toCommandSender(sender);
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
        if (!director.getMerchantManager().getMerchants().containsKey(key)) {
            BlobLibMessageAPI.getInstance().getMessage("Merchant.Not-Found").toCommandSender(sender);
            return true;
        }
        MerchantInventory inventory = director.getMerchantManager().getMerchants().get(key);
        Player player;
        if (args.length < 3) {
            player = instanceOfPlayer(sender);
            if (player == null) {
                return true;
            }
        } else {
            player = Bukkit.getPlayer(args[2]);
            if (player == null) {
                BlobLibMessageAPI.getInstance().getMessage("Player.Not-Found").toCommandSender(sender);
                return true;
            }
        }
        player.openInventory(inventory.getInventory());
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
                    case 2 -> l.addAll(director.getMerchantManager().getMerchants().keySet());
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
