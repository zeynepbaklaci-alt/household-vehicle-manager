package com.example.household_app.ui.session;

import lombok.Getter;
import lombok.Setter;

public final class SessionStore {

    private SessionStore() {
        // static holder
    }

    @Getter @Setter
    private static String jwt;

    @Getter @Setter
    private static String householdId;

    public static void setHouseholdId(String id) {
        householdId = id;
    }

    public static boolean isLoggedIn() {
        return jwt != null && !jwt.isBlank();
    }
}