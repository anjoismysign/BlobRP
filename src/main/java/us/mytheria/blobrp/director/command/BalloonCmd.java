package us.mytheria.blobrp.director.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import us.mytheria.bloblib.BlobLibAssetAPI;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.entities.Balloon;
import us.mytheria.blobrp.entities.ShipPart;

import java.util.ArrayList;
import java.util.List;

public class BalloonCmd implements CommandExecutor, TabCompleter {
    private final BlobRP main;

    public BalloonCmd() {
        this.main = BlobRP.getInstance();
        main.getCommand("blobballoon").setExecutor(this);
        main.getCommand("blobballoon").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("blobrp.admin")) {
            BlobLibAssetAPI.getMessage("System.No-Permission").toCommandSender(sender);
            return true;
        }
        Player player = instanceOfPlayer(sender);
        if (player == null) {
            BlobLibAssetAPI.getMessage("System.Console-Not-Allowed-Command").toCommandSender(sender);
            return true;
        }
        Balloon balloon = Balloon.build();
        balloon.addPart(ShipPart.fallingBlock(player
                .getLocation().clone().add(0, -1, 0), Material.OAK_PLANKS));
        balloon.move(new Vector(0, 85, 0).toLocation(player.getWorld()),
                1, 40L, 5L);
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
            sender.sendMessage("/blobballoon hand");
            sender.sendMessage("/blobballoon offhand");
            sender.sendMessage("/blobballoon inventory");
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("blobballoon")) {
            if (sender.hasPermission("blobrp.admin")) {
                List<String> l = new ArrayList<>();
                if (args.length == 1) {
                    l.add("spawn");
                }
                return l;
            }
        }
        return null;
    }
}
