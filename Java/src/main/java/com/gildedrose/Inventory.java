package com.gildedrose;

import com.gildedrose.updaters.ItemUpdaterRegistry;


public class Inventory {

    private final Item[] items;
    private final ItemUpdaterRegistry registry;

    public Inventory(Item[] items) {
        this(items, ItemUpdaterRegistry.defaults());
    }

    public Inventory(Item[] items, ItemUpdaterRegistry registry) {
        this.items = items;
        this.registry = registry;
    }

    public void updateQuality() {
        for (Item item : items) {
            registry.updaterFor(item).update(item);
        }
    }
}
