package io.github.anjoismysign.blobrp.entities;

import org.jetbrains.annotations.NotNull;

public enum IngredientType {
    MATERIAL("MATERIAL-"),
    TRANSLATABLE_ITEM("TRANSLATABLE-ITEM-");

    private final String startsWith;

    IngredientType(@NotNull String startsWith) {
        this.startsWith = startsWith;
    }

    public String getStartsWith() {
        return startsWith;
    }
}