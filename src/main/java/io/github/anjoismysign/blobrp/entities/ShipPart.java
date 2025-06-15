package io.github.anjoismysign.blobrp.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Shulker;

public record ShipPart<T extends Entity>(Shulker shulker, T entity) {

    public static ShipPart<FallingBlock>
    fallingBlock(Location location, Material material) {
        Shulker shulker = (Shulker) location.getWorld()
                .spawnEntity(location, EntityType.SHULKER);
        FallingBlock fallingBlock = location.getWorld()
                .spawnFallingBlock(location, material.createBlockData());
        return new ShipPart<>(shulker, fallingBlock);
    }
}
