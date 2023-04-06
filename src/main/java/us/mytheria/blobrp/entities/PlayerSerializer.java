package us.mytheria.blobrp.entities;

import org.bukkit.entity.Player;
import us.mytheria.bloblib.entities.BlobCrudable;

public interface PlayerSerializer {
    BlobCrudable serialize(Player player);

    void deserialize(Player player, BlobCrudable crudable);
}
