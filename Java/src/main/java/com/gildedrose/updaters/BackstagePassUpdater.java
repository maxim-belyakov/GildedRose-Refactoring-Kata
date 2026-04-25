package com.gildedrose.updaters;

import com.gildedrose.Item;

public class BackstagePassUpdater implements ItemUpdater {

    @Override
    public void update(Item item) {
        item.sellIn--;

        if (item.sellIn < 0) {
            item.quality = 0;
            return;
        }

        int delta;
        if (item.sellIn < 5)       delta = 3;
        else if (item.sellIn < 10) delta = 2;
        else                       delta = 1;

        item.quality = Math.min(MAX_QUALITY, item.quality + delta);
    }

}
