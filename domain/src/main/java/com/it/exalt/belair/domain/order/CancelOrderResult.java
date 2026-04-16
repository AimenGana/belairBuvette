package com.it.exalt.belair.domain.order;

public final class CancelOrderResult {
    private final boolean successful;

    public CancelOrderResult(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
