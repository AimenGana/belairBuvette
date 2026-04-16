package com.it.exalt.belair.domain.order;

public record PlaceDrinkOrderCommand(FestivalierId festivalierId, String drinkName, int quantity) {
}
