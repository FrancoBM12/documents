package com.francobm.documents.cache;

import org.bukkit.Material;

import java.util.List;

public class InventoryData {
    private final String name;
    private final Material item;
    private final int slot;
    private final List<String> lore;

    public InventoryData(String name, Material item, int slot, List<String> lore) {
        this.name = name;
        this.item = item;
        this.slot = slot;
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public Material getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }

    public List<String> getLore() {
        return lore;
    }
}
