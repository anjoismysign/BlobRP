package io.github.anjoismysign.blobrp.entity;

import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.holoworld.asset.DataAsset;
import io.github.anjoismysign.holoworld.asset.IdentityGenerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

public record RoleplayKit(@NotNull String identifier,
                          @NotNull Set<Item> equipment,
                          @NotNull Permission permission,
                          int priority) implements DataAsset {

    public void apply(@NotNull Player player){
        var inventory = player.getInventory();
        var accumulator = new ArrayList<ItemStack>();
        equipment.stream().sorted(Comparator.comparingInt(Item::getIndex)).forEach(item -> {
            @Nullable var translatableItem = TranslatableItem.by(item.identifier);
            if (translatableItem == null){
                return;
            }
            translatableItem = translatableItem.localize(player.getLocale());
            if (translatableItem == null){
                return;
            }
            var stack = translatableItem.getClone();
            stack.setAmount(item.amount);
            int index = item.getIndex();
            if (index >= 0 && index < inventory.getSize()) {
                var current = inventory.getItem(index);
                if (current == null || current.getType().isAir()) {
                    inventory.setItem(index, stack);
                } else {
                    accumulator.add(stack);
                }
            } else {
                accumulator.add(stack);
            }
        });
        if (accumulator.isEmpty()) {
            return;
        }
        player.give(accumulator);
    }

    public static class Item {
        private int index;
        private String identifier;
        private int amount;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }

    public static class Info implements IdentityGenerator<RoleplayKit> {
        private Set<Item> equipment;
        private int priority;

        private static final PluginManager PLUGIN_MANAGER = Bukkit.getPluginManager();

        @Override
        public @NotNull RoleplayKit generate(@NotNull String identifier) {
            var permission = new Permission("blobrp.kit."+identifier, "Access to kit "+identifier);
            try {
                PLUGIN_MANAGER.addPermission(permission);
            } catch (IllegalArgumentException ignored){
            }
            return new RoleplayKit(identifier, equipment, permission, priority);
        }

        public Set<Item> getEquipment() {
            return equipment;
        }

        public void setEquipment(Set<Item> equipment) {
            this.equipment = equipment;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }
    }
}
