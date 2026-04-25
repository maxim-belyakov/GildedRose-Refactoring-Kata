package com.gildedrose.updaters;

import com.gildedrose.Item;


public class AgedBrieUpdater implements ItemUpdater {

    @Override
    public void update(Item item) {
        item.sellIn--;
        int delta = item.sellIn < 0 ? 2 : 1;
        item.quality = Math.min(MAX_QUALITY, item.quality + delta);
    }

}
