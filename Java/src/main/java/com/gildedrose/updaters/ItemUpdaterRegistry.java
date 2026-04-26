package com.gildedrose.updaters;

import com.gildedrose.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ItemUpdaterRegistry {

    private final Map<String, ItemUpdater> byName = new HashMap<>();

    private final List<MatcherEntry> matchers = new ArrayList<>();

    private final ItemUpdater defaultUpdater;

    public ItemUpdaterRegistry(ItemUpdater defaultUpdater) {
        this.defaultUpdater = defaultUpdater;
    }

    public ItemUpdaterRegistry register(String name, ItemUpdater updater) {
        byName.put(name, updater);
        return this;
    }

    public ItemUpdaterRegistry registerMatching(Predicate<Item> when, ItemUpdater updater) {
        matchers.add(new MatcherEntry(when, updater));
        return this;
    }

    public ItemUpdater updaterFor(Item item) {
        ItemUpdater exact = byName.get(item.name);
        if (exact != null) return exact;
        for (MatcherEntry entry : matchers) {
            if (entry.when.test(item)) return entry.updater;
        }
        return defaultUpdater;
    }


    public static ItemUpdaterRegistry defaults() {
        return new ItemUpdaterRegistry(new RegularItemUpdater())
                .register("Aged Brie", new AgedBrieUpdater())
                .register("Sulfuras, Hand of Ragnaros", new SulfurasUpdater())
                .register("Backstage passes to a TAFKAL80ETC concert", new BackstagePassUpdater())
                .registerMatching(item -> item.name.startsWith("Conjured "), new ConjuredUpdater());
    }

    private static final class MatcherEntry {
        final Predicate<Item> when;
        final ItemUpdater updater;
        MatcherEntry(Predicate<Item> when, ItemUpdater updater) {
            this.when = when;
            this.updater = updater;
        }
    }
}
