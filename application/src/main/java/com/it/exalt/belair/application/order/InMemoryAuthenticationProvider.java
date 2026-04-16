package com.it.exalt.belair.application.order;

public class InMemoryAuthenticationProvider implements AuthenticationProvider {

    private String authenticatedFestivalierId;

    public void authenticate(String festivalierId) {
        this.authenticatedFestivalierId = festivalierId;
    }

    public void clear() {
        this.authenticatedFestivalierId = null;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticatedFestivalierId != null && !authenticatedFestivalierId.isBlank();
    }
}
