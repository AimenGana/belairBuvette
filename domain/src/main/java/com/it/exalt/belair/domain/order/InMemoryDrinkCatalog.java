package com.it.exalt.belair.domain.order;

import java.util.HashMap;
import java.util.Map;

public class InMemoryDrinkCatalog implements DrinkCatalog {

    private final Map<String, Integer> stockByDrink = new HashMap<>();

    public InMemoryDrinkCatalog withAvailable(String drinkName) {
        return withStock(drinkName, 1);
    }

    public InMemoryDrinkCatalog withStock(String drinkName, int quantity) {
        stockByDrink.put(drinkName, quantity);
        return this;
    }

    public boolean isAvailable(String drinkName) {
        return availableQuantityOf(drinkName) > 0;
    }

    public int availableQuantityOf(String drinkName) {
        return stockByDrink.getOrDefault(drinkName, 0);
    }

    @Override
    public boolean hasArticle(String drinkName) {
        return stockByDrink.containsKey(drinkName);
    }

    @Override
    public boolean hasSufficientStock(String drinkName, int quantity) {
        return availableQuantityOf(drinkName) >= quantity;
    }

    @Override
    public void decrementStock(String drinkName, int quantity) {
        stockByDrink.computeIfPresent(drinkName, (ignored, current) -> current - quantity);
    }
}
