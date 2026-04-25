package com.gildedrose.updaters;

import com.gildedrose.Item;


public interface ItemUpdater {
    int MAX_QUALITY = 50;
    int MIN_QUALITY = 0;

    void update(Item item);
}
