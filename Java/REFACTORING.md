# Notes on the refactor

I didn't touch `Item` and `GildedRose` — Goblin rules. The new code lives
next to them, in `com.gildedrose.updaters/` and the `Inventory` class.

## Approach

- `ItemUpdater` — strategy interface, one class per item category.
- `ItemUpdaterRegistry` — maps `name → strategy`. Default fallback is
  `RegularItemUpdater`.
- `Inventory` — new entry point. Same `updateQuality()` signature as the
  legacy class, three lines of body.
- Tests came first. `GildedRoseTest` pins down legacy behaviour;
  `InventoryTest` runs the same scenarios against the new pipeline. Both
  stay green at every commit. The order in `git log` is the real order I
  worked in.

## Conjured

The requirement says "Conjured items decay twice as fast". But Conjured
is a category, not one item. So I added a second method to the registry:
`registerMatching(Predicate<Item>, ItemUpdater)`. Any item with a name
starting with `"Conjured "` now uses the conjured updater. Adding more
conjured items is zero code.

Exact-name registrations still win first. That order is on purpose, and
there is a comment in the file. A future "special" conjured item with
its own rules can override the category rule by registering an exact
name.

## Things to improve later

- Common base for `GildedRoseTest` and `InventoryTest`. Right now they
  duplicate helper methods. The duplication is loud, and I prefer that
  to a clever abstraction for two test classes — but in a real codebase
  I'd extract a base class.
- Per-strategy unit tests. `InventoryTest` covers every rule through
  the pipeline, and each strategy is around five lines, so I skipped
  them. In production code they make sense for a faster failure signal.
- Item names like `"Aged Brie"` are inlined as string literals in the
  registry. Shared constants are cleaner.
