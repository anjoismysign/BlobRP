package us.mytheria.blobrp.director.manager;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import us.mytheria.bloblib.entities.inventory.ItemMaterialSelector;
import us.mytheria.bloblib.entities.listeners.BlobChatListener;
import us.mytheria.bloblib.entities.listeners.BlobDropListener;
import us.mytheria.bloblib.entities.listeners.BlobSelectorListener;
import us.mytheria.bloblib.managers.ChatListenerManager;
import us.mytheria.bloblib.managers.DropListenerManager;
import us.mytheria.bloblib.managers.SelectorListenerManager;
import us.mytheria.blobrp.BlobRP;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.ShopArticle;
import us.mytheria.blobrp.inventories.builder.ShopArticleBuilder;
import us.mytheria.blobrp.util.BlobMessageLib;

import java.util.Collections;
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
            case 0 -> chatManager.addChatListener(player, BlobChatListener.build(player, 300,
                    () -> {
                        String input = chatManager.getInput(player);
                        chatManager.removeChatListener(player);
                        Bukkit.getScheduler().runTask(main, () -> {
                            if (player == null || !player.isOnline()) {
                                return;
                            }
                            ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
                            builder.setKey(input);
                        });
                    },
                    () -> {
                        chatManager.removeChatListener(player);
                        getManagerDirector().getLangManager().title(player, "title.Builder.Key-Timeout");
                    }, Collections.singletonList(BlobMessageLib.defaultLangManager("title.Builder.Key"))));
            case 1 -> dropListenerManager.addDropListener(player, BlobDropListener.build(player, () -> {
                ItemStack input = dropListenerManager.getInput(player);
                dropListenerManager.removeDropListener(player);
                Bukkit.getScheduler().runTask(main, () -> {
                    if (player == null || !player.isOnline()) {
                        return;
                    }
                    ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
                    builder.setDisplay(input);
                });
            }, Collections.singletonList(BlobMessageLib.defaultLangManager("title.Builder.ItemStack"))));
            case 2 -> chatManager.addChatListener(player, BlobChatListener.build(player, 300,
                    () -> {
                        String input = chatManager.getInput(player);
                        chatManager.removeChatListener(player);
                        Bukkit.getScheduler().runTask(main, () -> {
                            if (player == null || !player.isOnline()) {
                                return;
                            }
                            ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
                            builder.setCustomModelData(input);
                        });
                    },
                    () -> {
                        chatManager.removeChatListener(player);
                        getManagerDirector().getLangManager().title(player, "title.Builder.CustomModelData-Timeout");
                    }, Collections.singletonList(BlobMessageLib.defaultLangManager("title.Builder.CustomModelData"))));
            case 3 -> selectorListenerManager.addSelectorListener(player, BlobSelectorListener.build(player, () -> {
                        Material input = (Material) selectorListenerManager.getInput(player);
                        selectorListenerManager.removeSelectorListener(player);
                        Bukkit.getScheduler().runTask(main, () -> {
                            if (player == null || !player.isOnline()) {
                                return;
                            }
                            ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
                            builder.setMaterial(input);
                        });
                    }, Collections.singletonList(BlobMessageLib.defaultLangManager("title.Builder.Material")),
                    ItemMaterialSelector.build(player.getUniqueId())));
            case 4 -> chatManager.addChatListener(player, BlobChatListener.build(player, 300,
                    () -> {
                        String input = chatManager.getInput(player);
                        chatManager.removeChatListener(player);
                        Bukkit.getScheduler().runTask(main, () -> {
                            if (player == null || !player.isOnline()) {
                                return;
                            }
                            ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
                            builder.setBuyPrice(input);
                        });
                    },
                    () -> {
                        chatManager.removeChatListener(player);
                        getManagerDirector().getLangManager().title(player, "title.Builder.BuyPrice-Timeout");
                    }, Collections.singletonList(BlobMessageLib.defaultLangManager("title.Builder.BuyPrice"))));
            case 5 -> chatManager.addChatListener(player, BlobChatListener.build(player, 300,
                    () -> {
                        String input = chatManager.getInput(player);
                        chatManager.removeChatListener(player);
                        Bukkit.getScheduler().runTask(main, () -> {
                            if (player == null || !player.isOnline()) {
                                return;
                            }
                            ShopArticleBuilder builder = getOrDefault(player.getUniqueId());
                            builder.setSellPrice(input);
                        });
                    },
                    () -> {
                        chatManager.removeChatListener(player);
                        getManagerDirector().getLangManager().title(player, "title.Builder.SellPrice-Timeout");
                    }, Collections.singletonList(BlobMessageLib.defaultLangManager("title.Builder.SellPrice"))));
        }
    }
}
