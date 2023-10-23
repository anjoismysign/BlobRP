package us.mytheria.blobrp.director.manager;

import me.anjoismysign.anjo.crud.CrudManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.mytheria.bloblib.api.BlobLibInventoryAPI;
import us.mytheria.bloblib.api.BlobLibMessageAPI;
import us.mytheria.bloblib.entities.BlobCrudable;
import us.mytheria.bloblib.entities.ComplexEventListener;
import us.mytheria.bloblib.entities.inventory.InventoryBuilderCarrier;
import us.mytheria.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import us.mytheria.bloblib.entities.inventory.MetaInventoryButton;
import us.mytheria.bloblib.utilities.BlobCrudManagerFactory;
import us.mytheria.blobrp.SoulAPI;
import us.mytheria.blobrp.director.RPManager;
import us.mytheria.blobrp.director.RPManagerDirector;
import us.mytheria.blobrp.entities.inventorydriver.DefaultInventoryDriver;
import us.mytheria.blobrp.entities.inventorydriver.InventoryDriver;
import us.mytheria.blobrp.entities.inventorydriver.InventoryDriverType;
import us.mytheria.blobrp.entities.playerserializer.PlayerSerializerType;
import us.mytheria.blobrp.events.CloudInventoryDeserializeEvent;
import us.mytheria.blobrp.events.CloudInventorySerializeEvent;

import java.util.*;

public class CloudInventoryManager extends RPManager implements Listener {
    private String welcomeMessage;
    private boolean soulInventory;
    private final Map<UUID, InventoryDriver> map;
    private final HashSet<UUID> saving;
    private String reference;
    protected CrudManager<BlobCrudable> crudManager;
    private PlayerSerializerType serializerType;
    private InventoryDriverType driverType;

    public CloudInventoryManager(RPManagerDirector director) {
        super(director);
        this.reference = "WelcomeInventory";
        this.map = new HashMap<>();
        this.saving = new HashSet<>();
        this.crudManager = BlobCrudManagerFactory.PLAYER(getPlugin(), "alternativesaving", crudable -> crudable, true);
        reload();
    }

    private InventoryDriver generate(BlobCrudable crudable,
                                     PlayerSerializerType serializerType,
                                     InventoryDriverType driverType) {
        if (driverType != InventoryDriverType.DEFAULT)
            throw new IllegalArgumentException("Driver type not supported yet");
        return new DefaultInventoryDriver(crudable, serializerType);
    }

    @Override
    public void unload() {
        saveAll();
    }

    @Override
    public void reload() {
        soulInventory = false;
        HandlerList.unregisterAll(this);
        ComplexEventListener alternativeSaving = getManagerDirector().getConfigManager().alternativeSaving();
        alternativeSaving.ifRegister(eventListener -> {
            Bukkit.getPluginManager().registerEvents(this, getPlugin());
            ConfigurationSection welcomePlayers = alternativeSaving.getConfigurationSection("Welcome-Players");
            if (welcomePlayers.getBoolean("Register")) {
                welcomeMessage = welcomePlayers.getString("Message");
                soulInventory = welcomePlayers.getBoolean("Inventory-Is-Soul");
            } else
                welcomeMessage = null;
            serializerType = Optional.of(alternativeSaving.getString("Player-Serializer")).map(PlayerSerializerType::valueOf).orElse(PlayerSerializerType.SIMPLE);
            driverType = Optional.of(alternativeSaving.getString("Inventory-Driver")).map(InventoryDriverType::valueOf).orElse(InventoryDriverType.DEFAULT);
        });
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null ||
                event.getClickedInventory().getType() != InventoryType.PLAYER) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        isInventoryDriver(player).ifPresent(driver -> {
            event.setCancelled(driver.isInsideInventoryMenu(slot));
            MetaInventoryButton inventoryButton = driver.getInventoryBuilder().getKeys().stream()
                    .map(key -> driver.getInventoryBuilder().getButton(key))
                    .filter(button -> button.containsSlot(slot))
                    .findFirst().orElse(null);
            if (inventoryButton == null) {
                return;
            }
            String meta = inventoryButton.getMeta().toLowerCase();
            String subMeta = inventoryButton.getSubMeta();
            if (subMeta == null) {
                return;
            }
            subMeta = subMeta.toLowerCase();
            switch (meta) {
                case "blobrp#player" -> {
                    player.closeInventory();
                    player.performCommand(subMeta.replace("%player%", player.getName()));
                }
                case "blobrp#console" -> {
                    player.closeInventory();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), subMeta.replace("%player%", player.getName()));
                }
                case "blobrp#player_null_cursor" -> {
                    event.setCursor(null);
                    player.closeInventory();
                    player.performCommand(subMeta.replace("%player%", player.getName()));
                }
                case "blobrp#console_null_cursor" -> {
                    event.setCursor(null);
                    player.closeInventory();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), subMeta.replace("%player%", player.getName()));
                }
                default -> {
                }
            }
        });
    }

    @EventHandler
    public void onPrejoin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        if (saving.contains(uuid))
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You are already saving your data, please try again in a few seconds.");
    }

    @EventHandler
    public void onLocale(PlayerLocaleChangeEvent event) {
        Player player = event.getPlayer();
        InventoryDriver driver = map.get(player.getUniqueId());
        if (driver == null)
            return; // it seems that on join event this is called
        DefaultInventoryDriver defaultInventoryDriver = (DefaultInventoryDriver) driver;
        defaultInventoryDriver.updateLocale(player, event.getLocale());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            if (player == null || !player.isOnline())
                return;
            BlobCrudable crudable = crudManager.read(uuid.toString());
            boolean hasPlayedBefore = crudable.hasBoolean("HasPlayedBefore").orElse(false);
            InventoryDriver applied = generate(crudable, serializerType, driverType);
            if (!hasPlayedBefore) {
                if (welcomeMessage != null) {
                    BlobLibMessageAPI.getInstance()
                            .getMessage(welcomeMessage, player)
                            .modder()
                            .replace("%player%", player.getName())
                            .get()
                            .handle(player);
                    InventoryBuilderCarrier<MetaInventoryButton> carrier = BlobLibInventoryAPI
                            .getInstance().getMetaInventoryBuilderCarrier(reference, player);
                    MetaBlobPlayerInventoryBuilder.fromInventoryBuilderCarrier
                            (carrier, player.getUniqueId());
                    if (soulInventory)
                        Bukkit.getScheduler().runTask(getPlugin(), () -> {
                            if (player == null || !player.isOnline())
                                return;
                            SoulAPI.getInstance().setSoul(player);
                        });
                }
            }
            map.put(uuid, applied);
            CloudInventoryDeserializeEvent deserializeEvent = new CloudInventoryDeserializeEvent(applied,
                    driverType, applied.getInventoryBuilder(), hasPlayedBefore);
            Bukkit.getPluginManager().callEvent(deserializeEvent);
            deserializeEvent.fetchAsynchronous().forEach(consumer ->
                    consumer.accept(player));
            deserializeEvent.fetch().forEach(consumer ->
                    Bukkit.getScheduler().runTask(getPlugin(), () -> {
                        if (player == null || !player.isOnline())
                            return;
                        consumer.accept(player);
                    }));
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        Optional<InventoryDriver> optional = isInventoryDriver(uuid);
        if (optional.isEmpty())
            return;
        InventoryDriver serializable = optional.get();
        CloudInventorySerializeEvent quitEvent = new CloudInventorySerializeEvent(serializable, driverType, serializable.getInventoryBuilder());
        Bukkit.getPluginManager().callEvent(quitEvent);
        saving.add(uuid);
        BlobCrudable crudable = serializable.serializeAllAttributes();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            crudManager.update(crudable);
            removeObject(uuid);
            saving.remove(uuid);
        });
    }

    public void addObject(UUID key, InventoryDriver serializable) {
        map.put(key, serializable);
    }

    public void addObject(Player player, InventoryDriver serializable) {
        addObject(player.getUniqueId(), serializable);
    }

    public void removeObject(UUID key) {
        map.remove(key);
    }

    public void removeObject(Player player) {
        removeObject(player.getUniqueId());
    }

    public Optional<InventoryDriver> isInventoryDriver(UUID uuid) {
        return Optional.ofNullable(map.get(uuid));
    }

    public Optional<InventoryDriver> isInventoryDriver(Player player) {
        return isInventoryDriver(player.getUniqueId());
    }

    public void saveAll() {
        map.values().forEach(serializable -> crudManager.update(serializable.serializeAllAttributes()));
    }
}
