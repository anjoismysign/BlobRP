package us.mytheria.blobrp.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Shulker;

import java.util.Collection;

public interface Ship {
    boolean addPart(Shulker shulker, Entity entity);

    boolean addPart(ShipPart<?> shipPart);

    void remove();


    void move(Location location, double distancePerCall,
              long callDelay, long callPeriod);

    Collection<Shulker> getParts();
}
