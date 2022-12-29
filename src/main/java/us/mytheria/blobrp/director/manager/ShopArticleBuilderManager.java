package us.mytheria.blobrp.director.manager;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import us.mytheria.bloblib.BlobLibDevAPI;
import us.mytheria.bloblib.entities.inventory.ItemMaterialSelector;
import us.mytheria.bloblib.managers.ChatListenerManager;
import us.mytheria.bloblib.managers.DropListenerManager;
import us.mytheria.bloblib.managers.SelectorListenerManager;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.inventories.builder.ShopArticleBuilder;

import java.util.HashMap;
import java.util.UUID;

public class ShopArticleBuilderManager extends RPManager implements Listener {
    private BlobRP main;
    private String title;
    private HashMap<UUID, ShopArticleBuilder> builders;
    private ChatListenerManager chatManager;
    private DropListenerManager dropListenerManager;
    private ShopArticleManager shopArticleManager;
    private SelectorListenerManager selectorListenerManager;

    public ShopArticleBuilderManager(RPManagerDirector managerDirector) {
        super(managerDirector);
    }

    @Override
    public void loadInConstructor() {
        main = BlobRP.getInstance();
        Bukkit.getPluginManager().registerEvents(this, main);
        chatManager = getManagerDirector().getChatListenerManager();
        dropListenerManager = getManagerDirector().getDropListenerManager();
        shopArticleManager = getManagerDirector().getDropsManager();
        selectorListenerManager = getManagerDirector().getSelectorManager();
        update();
        this.builders = new HashMap<>();
    }

    @Override
    public void reload() {
        update();
        this.builders = new HashMap<>();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        String invname = e.getView().getTitle();
        if (!invname.equals(title)) {
            return;
        }
        int slot = e.getRawSlot();
        Player player = (Player) e.getWhoClicked();
        ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
        if (slot >= builder.getSize()) {
            return;
        }
        e.setCancelled(true);
        if (builder.isKeyButton(slot)) {
            addListener(player, 0);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }
        if (builder.isDisplayButton(slot)) {
            addListener(player, 1);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }
        if (builder.isMatchDisplayButton(slot)) {
            builder.matchDisplay();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }
        if (builder.isHasCustomModelDataButton(slot)) {
            builder.setHasCustomModelData(!builder.hasCustomModelData());
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }
        if (builder.isCustomModelDataButton(slot)) {
            addListener(player, 2);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }
        if (builder.isMaterialButton(slot)) {
            addListener(player, 3);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }
        if (builder.isBuyPriceButton(slot)) {
            addListener(player, 4);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }
        if (builder.isSellPriceButton(slot)) {
            addListener(player, 5);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            return;
        }
        if (builder.isBuildButton(slot)) {
            ShopArticle build = builder.build();
            if (build == null)
                return;
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1.35f);
            player.closeInventory();
            build.saveToFile();
            shopArticleManager.addShopArticle(build);
            removeBuilder(player);
        }
    }

    public void update() {
        FileManager fileManager = getManagerDirector().getFileManager();
        YamlConfiguration inventories = fileManager.getYml(fileManager.inventoriesFile());
        this.title = ChatColor.translateAlternateColorCodes('&',
                inventories.getString("ShopArticleBuilder.Title"));
    }

    public ShopArticleBuilder getOrDefault(UUID uuid) {
        ShopArticleBuilder builder = builders.get(uuid);
        if (builder == null) {
            builder = ShopArticleBuilder.build(uuid);
            builders.put(uuid, builder);
        }
        return builder;
    }

    public ShopArticleBuilder getOrDefault(Player player) {
        return getOrDefault(player.getUniqueId());
    }

    public void removeBuilder(UUID uuid) {
        builders.remove(uuid);
    }

    public void removeBuilder(Player player) {
        removeBuilder(player.getUniqueId());
    }

    /**
     * @param player The player to add the listener
     * @param type   0 = key, 1 = display, 2 = customModelData,
     *               3 = material, 4 = buyPrice, 5 = sellPrice
     */
    private void addListener(Player player, int type) {
        player.closeInventory();
        switch (type) {
            case 0 -> BlobLibDevAPI.addChatListener(player, 300, input -> {
                ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
                builder.setKey(input);
            }, "Builder.Key-Timeout", "Builder.Key");
            case 1 -> BlobLibDevAPI.addDropListener(player, input -> {
                ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
                builder.setDisplay(input);
            }, "Builder.ItemStack");
            case 2 -> BlobLibDevAPI.addChatListener(player, 300, input -> {
                ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
                builder.setCustomModelData(input);
            }, "Builder.CustomModelData-Timeout", "Builder.CustomModelData");
            case 3 -> BlobLibDevAPI.addSelectorListener(player, input -> {
                ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
                builder.setMaterial(input);
            }, "Builder.Material", ItemMaterialSelector.build(player.getUniqueId()));
            case 4 -> BlobLibDevAPI.addChatListener(player, 300, input -> {
                ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
                builder.setBuyPrice(input);
            }, "Builder.BuyPrice-Timeout", "Builder.BuyPrice");
            case 5 -> BlobLibDevAPI.addChatListener(player, 300, input -> {
                ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
                builder.setSellPrice(input);
            }, "Builder.SellPrice-Timeout", "Builder.SellPrice");
        }
    }
}
