package us.mytheria.blobrp.director.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.SoulAPI;

import java.util.ArrayList;
import java.util.List;

public class SoulCmd implements CommandExecutor, TabCompleter {
    private final BlobRP main;

    public SoulCmd() {
        this.main = BlobRP.getInstance();
        main.getCommand("blobsoul").setExecutor(this);
        main.getCommand("blobsoul").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("blobrp.admin")) {
            BlobLibMessageAPI.getInstance().getMessage("System.No-Permission").toCommandSender(sender);
            return true;
        }
        if (args.length < 1) {
            debug(sender);
            return true;
        }
        String arg1 = args[0];
        String lowercased = arg1.toLowerCase();
        switch (lowercased) {
            case "hand" -> {
                Player player = instanceOfPlayer(sender);
                if (player == null) return true;
                PlayerInventory inventory = player.getInventory();
                ItemStack itemStack = inventory.getItemInMainHand();
                boolean success = SoulAPI.getInstance().setSoul(itemStack);
                if (success)
                    BlobLibMessageAPI.getInstance().getMessage("Soul.Hand-Success").toCommandSender(sender);
                else
                    BlobLibMessageAPI.getInstance().getMessage("Soul.Fail").toCommandSender(sender);
                return true;
            }
            case "offhand" -> {
                Player player = instanceOfPlayer(sender);
                if (player == null) return true;
                PlayerInventory inventory = player.getInventory();
                ItemStack itemStack = inventory.getItemInOffHand();
                boolean success = SoulAPI.getInstance().setSoul(itemStack);
                if (success)
                    BlobLibMessageAPI.getInstance().getMessage("Soul.Offhand-Success").toCommandSender(sender);
                else
                    BlobLibMessageAPI.getInstance().getMessage("Soul.Fail").toCommandSender(sender);
                return true;
            }
            case "inventory" -> {
                Player player = instanceOfPlayer(sender);
                if (player == null) return true;
                PlayerInventory inventory = player.getInventory();
                for (ItemStack itemStack : inventory.getContents()) {
                    SoulAPI.getInstance().setSoul(itemStack);
                }
                BlobLibMessageAPI.getInstance().getMessage("Soul.Inventory").toCommandSender(sender);
                return true;
            }
            default -> {
                debug(sender);
                return true;
            }
        }
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
            sender.sendMessage("/blobsoul hand");
            sender.sendMessage("/blobsoul offhand");
            sender.sendMessage("/blobsoul inventory");
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("blobsoul")) {
            if (sender.hasPermission("blobrp.admin")) {
                List<String> l = new ArrayList<>();
                if (args.length == 1) {
                    l.add("hand");
                    l.add("offhand");
                    l.add("inventory");
                }
                return l;
            }
        }
        return null;
    }
}
