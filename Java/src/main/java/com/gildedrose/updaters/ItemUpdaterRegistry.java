package com.gildedrose.updaters;

import com.gildedrose.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemUpdaterRegistry {

    private final Map<String, ItemUpdater> byName = new HashMap<>();
    private final ItemUpdater defaultUpdater;

    public ItemUpdaterRegistry(ItemUpdater defaultUpdater) {
        this.defaultUpdater = defaultUpdater;
    }

    public ItemUpdaterRegistry register(String name, ItemUpdater updater) {
        byName.put(name, updater);
        return this;
    }

    public ItemUpdater updaterFor(Item item) {
        return byName.getOrDefault(item.name, defaultUpdater);
    }


    public static ItemUpdaterRegistry defaults() {
        return new ItemUpdaterRegistry(new RegularItemUpdater())
                .register("Aged Brie", new AgedBrieUpdater())
                .register("Sulfuras, Hand of Ragnaros", new SulfurasUpdater());
    }
}
