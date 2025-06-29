package io.github.anjoismysign.blobrp.listeners;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.entities.ComplexEventListener;
import io.github.anjoismysign.bloblib.utilities.TextColor;
import io.github.anjoismysign.blobrp.director.manager.ConfigManager;

public class DiscordCmd extends RPListener implements CommandExecutor {
    private ComplexEventListener discordCmd;
    private String message, url;
    private TextComponent textComponent;

    public DiscordCmd(ConfigManager configManager) {
        super(configManager);
    }

    public void reload() {
        getConfigManager().discordCmd().ifRegister(complexEventListener -> {
            message = complexEventListener.getString("Message");
            url = complexEventListener.getString("URL");
            message = TextColor.PARSE(message);
            textComponent = new TextComponent(message);
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                    ChatColor.stripColor(url)));
            getConfigManager().getPlugin().getCommand("discord")
                    .setExecutor(DiscordCmd.this);
        });
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int length = args.length;
        if (length < 1) {
            if (!(sender instanceof Player player)) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("System.Console-Not-Allowed-Command", sender)
                        .toCommandSender(sender);
                return true;
            }
            player.spigot().sendMessage(textComponent);
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            BlobLibMessageAPI.getInstance()
                    .getMessage("Player.Not-Found", sender)
                    .toCommandSender(sender);
            return true;
        }
        target.spigot().sendMessage(textComponent);
        return true;
    }
}
