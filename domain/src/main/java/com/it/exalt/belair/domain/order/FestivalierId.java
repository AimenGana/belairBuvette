package com.it.exalt.belair.domain.order;

public record FestivalierId(String value) {

    public static FestivalierId of(String value) {
        return new FestivalierId(value);
    }
}
