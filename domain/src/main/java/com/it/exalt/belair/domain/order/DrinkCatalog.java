package com.it.exalt.belair.domain.order;

public interface DrinkCatalog {

    boolean hasArticle(String drinkName);

    boolean hasSufficientStock(String drinkName, int quantity);

    void decrementStock(String drinkName, int quantity);
}
