package com.gildedrose;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryTest {

    private static final String AGED_BRIE = "Aged Brie";
    private static final String SULFURAS = "Sulfuras, Hand of Ragnaros";

    private static Item afterOneDay(String name, int sellIn, int quality) {
        return afterDays(name, sellIn, quality, 1);
    }

    private static Item afterDays(String name, int sellIn, int quality, int days) {
        Item item = new Item(name, sellIn, quality);
        Inventory shop = new Inventory(new Item[]{item});
        for (int d = 0; d < days; d++) {
            shop.updateQuality();
        }
        return item;
    }

    // regular items

    @Test
    @DisplayName("regular item loses one quality and one day each tick")
    void regularItemTicks() {
        Item it = afterOneDay("+5 Dexterity Vest", 8, 17);
        assertEquals(7, it.sellIn);
        assertEquals(16, it.quality);
    }

    @Test
    @DisplayName("regular item past expiry decays twice as fast")
    void regularItemExpiredDecaysFaster() {
        Item it = afterOneDay("+5 Dexterity Vest", 0, 10);
        assertEquals(-1, it.sellIn);
        assertEquals(8, it.quality);
    }

    @Test
    @DisplayName("regular item quality never falls below zero")
    void regularItemQualityFloor() {
        Item it = afterDays("+5 Dexterity Vest", -5, 1, 7);
        assertEquals(0, it.quality);
    }

    @ParameterizedTest(name = "regular ({0},{1}) -> ({2},{3})")
    @CsvSource({
        " 7, 25,  6, 24",
        " 2,  3,  1,  2",
        "-1, 12, -2, 10",
        " 4,  0,  3,  0"
    })
    void regularItemDegradationTable(int sellIn, int quality, int expSellIn, int expQuality) {
        Item it = afterOneDay("+5 Dexterity Vest", sellIn, quality);
        assertEquals(expSellIn, it.sellIn);
        assertEquals(expQuality, it.quality);
    }

    // aged brie

    @Test
    @DisplayName("aged brie gains one quality each tick")
    void brieRipens() {
        Item it = afterOneDay(AGED_BRIE, 5, 12);
        assertEquals(4, it.sellIn);
        assertEquals(13, it.quality);
    }

    @Test
    @DisplayName("aged brie past expiry ripens twice as fast")
    void brieRipensFasterAfterExpiry() {
        Item it = afterOneDay(AGED_BRIE, 0, 10);
        assertEquals(-1, it.sellIn);
        assertEquals(12, it.quality);
    }

    @Test
    @DisplayName("aged brie quality never exceeds fifty even after a long stretch")
    void brieCeiling() {
        Item it = afterDays(AGED_BRIE, 30, 35, 30);
        assertEquals(50, it.quality);
    }

    @ParameterizedTest(name = "brie ({0},{1}) -> ({2},{3})")
    @CsvSource({
        " 8,  7,  7,  8",
        "-2, 41, -3, 43",
        " 9, 50,  8, 50",
        "-1, 49, -2, 50"
    })
    void brieRipeningTable(int sellIn, int quality, int expSellIn, int expQuality) {
        Item it = afterOneDay(AGED_BRIE, sellIn, quality);
        assertEquals(expSellIn, it.sellIn);
        assertEquals(expQuality, it.quality);
    }

    // sulfuras

    @Test
    @DisplayName("sulfuras never moves: quality and sellIn stay put")
    void sulfurasIsImmutable() {
        Item it = afterDays(SULFURAS, 6, 80, 10);
        assertEquals(6, it.sellIn);
        assertEquals(80, it.quality);
    }

    @ParameterizedTest(name = "sulfuras at ({0},{1}) is unchanged")
    @CsvSource({
        " 12, 80",
        "  0, 80",
        " -3, 80"
    })
    void sulfurasUnchangedRegardlessOfState(int sellIn, int quality) {
        Item it = afterOneDay(SULFURAS, sellIn, quality);
        assertEquals(sellIn, it.sellIn);
        assertEquals(quality, it.quality);
    }

    // backstage tests are added together with BackstagePassUpdater in the next commit
}
