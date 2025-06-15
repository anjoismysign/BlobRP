package io.github.anjoismysign.blobrp.entities.blocktype;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum DefaultBlockType {
    VANILLA,
    BLOB_DESIGN;

    private static final Map<String, DefaultBlockType> byName = new HashMap<>();

    static {
        for (DefaultBlockType blockType : values()) {
            byName.put(blockType.name(), blockType);
        }
    }

    @Nullable
    public static DefaultBlockType byName(@NotNull String name) {
        Objects.requireNonNull(name, "'name' cannot be null");
        return byName.get(name);
    }


}
