package us.mytheria.blobrp.director.manager;

import me.anjoismysign.anjo.crud.CrudManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.mytheria.bloblib.entities.BlobCrudable;
import us.mytheria.bloblib.entities.ComplexEventListener;
import us.mytheria.bloblib.utilities.BlobCrudManagerBuilder;
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
    private final Map<UUID, InventoryDriver> map;
    private final HashSet<UUID> saving;
    protected CrudManager<BlobCrudable> crudManager;
    private PlayerSerializerType serializerType;
    private InventoryDriverType driverType;

    public CloudInventoryManager(RPManagerDirector managerDirector) {
        super(managerDirector);
        this.map = new HashMap<>();
        this.saving = new HashSet<>();
        crudManager = BlobCrudManagerBuilder.PLAYER(getPlugin(), "cloudinventory", crudable -> crudable, true);
        ComplexEventListener cloudInventory = getManagerDirector().getConfigManager().cloudInventory();
        serializerType = Optional.of(cloudInventory.getString("serializer")).map(PlayerSerializerType::valueOf).orElse(PlayerSerializerType.SIMPLE);
        driverType = Optional.of(cloudInventory.getString("driver")).map(InventoryDriverType::valueOf).orElse(InventoryDriverType.DEFAULT);
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
        unload();
        ComplexEventListener cloudInventory = getManagerDirector().getConfigManager().cloudInventory();
        serializerType = Optional.of(cloudInventory.getString("serializer")).map(PlayerSerializerType::valueOf).orElse(PlayerSerializerType.SIMPLE);
        driverType = Optional.of(cloudInventory.getString("driver")).map(InventoryDriverType::valueOf).orElse(InventoryDriverType.DEFAULT);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.PLAYER)
            return;
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        isInventoryDriver(player).ifPresent(driver -> {
            event.setCancelled(driver.isInsideInventoryMenu(slot));
        });
    }

    @EventHandler
    public void onPrejoin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        if (saving.contains(uuid))
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You are already saving your data, please try again in a few seconds.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            if (player == null || !player.isOnline())
                return;
            InventoryDriver applied = generate(crudManager.read(uuid.toString()), serializerType, driverType);
            map.put(uuid, applied);
            CloudInventoryDeserializeEvent deserializeEvent = new CloudInventoryDeserializeEvent(applied, driverType, applied.getInventoryBuilder());
            Bukkit.getPluginManager().callEvent(deserializeEvent);
        });
    }

    @EventHandler
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
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            crudManager.update(serializable.serializeAllAttributes());
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
