package io.github.anjoismysign.blobrp.entities.playerserializer;

import org.bukkit.entity.Player;
import io.github.anjoismysign.bloblib.entities.BlobCrudable;

import java.util.function.Consumer;

public interface PlayerSerializer {
    BlobCrudable serialize(Player player);

    void deserialize(Player player, BlobCrudable crudable, Consumer<Player> consumer);

    default void deserialize(Player player, BlobCrudable crudable) {
        deserialize(player, crudable, null);
    }
}
