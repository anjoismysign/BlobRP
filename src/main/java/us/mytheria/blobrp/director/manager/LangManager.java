package us.mytheria.blobrp.director.manager;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;

import java.util.HashMap;

public class LangManager extends RPManager {
    private String msg;
    private String actionbar;
    private String title;
    private String menu;

    private HashMap<String, String> lang;

    public LangManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    public static void sub(Player player, String subtitle) {
        player.sendTitle(" ", ChatColor.translateAlternateColorCodes('&', subtitle), 10, 60, 10);
    }

    public void actionBar(Player player, String key, Sound sound, float volume, float pitch) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getLang(key)));
        player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
    }

    public void actionBar(Player player, String key) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getLang(key)));
    }

    public String[] getTitle(String key) {
        return getLang(key).split("%split%");
    }

    public void sendActionBar(Player player, String key) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getLang(key)));
    }

    public void doorAdded(Player player) {
        String[] title = getTitle("title.Door.Added");
        player.sendTitle(title[0], title[1], 10, 40, 10);
    }

    public void doorRemoved(Player player) {
        String[] title = getTitle("title.Door.Removed");
        player.sendTitle(title[0], title[1], 10, 40, 10);
    }

    public void containerAdded(Player player) {
        String[] title = getTitle("title.Container.Added");
        player.sendTitle(title[0], title[1], 10, 40, 10);
    }

    public void containerRemoved(Player player) {
        String[] title = getTitle("title.Container.Removed");
        player.sendTitle(title[0], title[1], 10, 40, 10);
    }

    /**
     * Will send a title to player with a default of
     * 10 ticks fade in, 40 ticks stay, 10 ticks fade out
     *
     * @param player player to send title
     * @param key    key to get title from
     */
    public void title(Player player, String key) {
        title(player, key, 10, 40, 10);
    }

    public void replaceSubtitle(Player player, String key, String regex, String replacement) {
        String[] title = getTitle(key);
        player.sendTitle(title[0], title[1].replace(regex, replacement), 10, 40, 10);
    }

    public void replaceSubtitle(Player player, String key, String regex, String replacement, int fadeIn, int stay, int fadeOut) {
        String[] title = getTitle(key);
        player.sendTitle(title[0], title[1].replace(regex, replacement), fadeIn, stay, fadeOut);
    }

    public void title(Player player, String key, int fadeIn, int stay, int fadeOut) {
        String[] title = getTitle(key);
        player.sendTitle(title[0], title[1], fadeIn, stay, fadeOut);
    }

    @Override
    public void loadInConstructor() {
        FileManager fileManager = getManagerDirector().getFileManager();
        msg = "Message.";
        actionbar = "Action-Bar.";
        title = "Title.";
        menu = "Menu.";
        lang = new HashMap<>();
        YamlConfiguration langYml = fileManager.getYml(fileManager.langFile());
        ConfigurationSection msgSection = langYml.getConfigurationSection(msg);
        if (msgSection != null)
            msgSection.getKeys(true).forEach(key -> {
                if (msgSection.isConfigurationSection(key))
                    return;
                lang.put("msg." + key, ChatColor.translateAlternateColorCodes('&', langYml.getString(msg + key)));
            });
        ConfigurationSection actionbarSection = langYml.getConfigurationSection(actionbar);
        if (actionbarSection != null)
            actionbarSection.getKeys(true).forEach(key -> {
                if (actionbarSection.isConfigurationSection(key))
                    return;
                lang.put("actionbar." + key, ChatColor.translateAlternateColorCodes('&', langYml.getString(actionbar + key)));
            });
        ConfigurationSection titleSection = langYml.getConfigurationSection(title);
        if (titleSection != null)
            titleSection.getKeys(true).forEach(key -> {
                String value = langYml.getString(title + key);
                if (titleSection.isConfigurationSection(value))
                    return;
                lang.put("title." + key, ChatColor.translateAlternateColorCodes('&', langYml.getString(title + key)));
            });
        ConfigurationSection menuSection = langYml.getConfigurationSection(menu);
        if (menuSection != null)
            menuSection.getKeys(true).forEach(key -> {
                if (menuSection.isConfigurationSection(key))
                    return;
                lang.put("menu." + key, ChatColor.translateAlternateColorCodes('&', langYml.getString(menu + key)));
            });
    }

    @Override
    public void reload() {
        loadInConstructor();
    }

    public String noPermission() {
        return lang.get("msg.No-Permission");
    }

    public String getLang(String key) {
        return lang.get(key);
    }
}
