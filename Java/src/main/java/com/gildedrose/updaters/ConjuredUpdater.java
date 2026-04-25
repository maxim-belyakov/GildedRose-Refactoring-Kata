package com.gildedrose.updaters;

import com.gildedrose.Item;

public class ConjuredUpdater implements ItemUpdater {

    @Override
    public void update(Item item) {
        item.sellIn--;
        int delta = item.sellIn < 0 ? 4 : 2;
        item.quality = Math.max(MIN_QUALITY, item.quality - delta);
    }
}
