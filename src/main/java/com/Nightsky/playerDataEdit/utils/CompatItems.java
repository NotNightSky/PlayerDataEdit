package com.Nightsky.playerDataEdit.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public final class CompatItems {

    private CompatItems() {
    }

    public static Material resolveMaterial(String modernName, String legacyName) {
        Material material = usableMaterial(modernName);
        return material != null ? material : usableMaterial(legacyName);
    }

    public static ItemStack create(String modernName, String legacyName) {
        return new ItemStack(resolveMaterialOrThrow(modernName, legacyName));
    }

    public static ItemStack create(String modernName, String legacyName, short legacyData) {
        Material material = resolveMaterialOrThrow(modernName, legacyName);
        return material.name().equals(legacyName)
                ? new ItemStack(material, 1, legacyData)
                : new ItemStack(material);
    }

    private static Material resolveMaterialOrThrow(String modernName, String legacyName) {
        return Objects.requireNonNull(resolveMaterial(modernName, legacyName),
                "No usable material for modern=" + modernName + ", legacy=" + legacyName);
    }

    private static Material usableMaterial(String name) {
        Material material = Material.matchMaterial(name);
        if (material == null || material.name().startsWith("LEGACY_")) {
            return null;
        }
        return material;
    }
}
