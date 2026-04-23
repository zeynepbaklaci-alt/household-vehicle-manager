package com.example.household_app.ui.util;

import com.example.household_app.ui.session.SessionStore;
import org.json.JSONArray;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.logging.Logger;

public final class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private static JSONArray reminderCache;

    private ApiClient() {
    }

    private static HttpRequest.Builder baseRequest(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json");

        String jwt = SessionStore.getJwt();

        if (jwt != null && !jwt.isBlank()) {
            builder.header("Authorization", "Bearer " + jwt);
        }

        return builder;
    }

    public static String post(String path, String body) throws Exception {
        HttpRequest request = baseRequest(path)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = CLIENT
                .send(request, HttpResponse.BodyHandlers.ofString());

        logResponse("POST", path, response);

        if (response.statusCode() >= 400) {
            throw new RuntimeException(
                    "HTTP " + response.statusCode() + " → " + response.body()
            );
        }

        return response.body();
    }

    public static String get(String path) throws Exception {
        HttpRequest request = baseRequest(path)
                .GET()
                .build();

        HttpResponse<String> response = CLIENT
                .send(request, HttpResponse.BodyHandlers.ofString());

        logResponse("GET", path, response);

        if (response.statusCode() >= 400) {
            throw new RuntimeException(
                    "HTTP " + response.statusCode() + " → " + response.body()
            );
        }

        return response.body();
    }

    private static final Logger LOGGER =
            Logger.getLogger(ApiClient.class.getName());

    private static void logResponse(
            String method,
            String path,
            HttpResponse<String> response
    ) {
        LOGGER.fine(() ->
                method + " " + path +
                        " → " + response.statusCode() +
                        "\n" + response.body()
        );
    }


    public static void delete(String path) throws Exception {
        HttpRequest request = baseRequest(path)
                .DELETE()
                .build();

        HttpResponse<String> response =
                CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse("DELETE", path, response);

        if (response.statusCode() >= 400) {
            throw new RuntimeException(
                    "HTTP " + response.statusCode() + " → " + response.body()
            );
        }
    }

    public static String put(String path, String body) throws Exception {
        HttpRequest request = baseRequest(path)
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response =
                CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        logResponse("PUT", path, response);

        if (response.statusCode() >= 400) {
            throw new RuntimeException(
                    "HTTP " + response.statusCode() + " → " + response.body()
            );
        }

        return response.body();
    }

    public static void dismissReminder(UUID reminderId) {
        try {
            post("/reminders/" + reminderId + "/dismiss", "");
            reminderCache = null;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to dismiss reminder " + reminderId,
                    e
            );
        }
    }

    public static void dismissAllReminders() {
        try {
            post("/reminders/dismiss-all", "");
            reminderCache = null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to dismiss all reminders", e);
        }
    }

    public static JSONArray getCachedReminders() {
        try {
            if (reminderCache == null) {
                reminderCache =
                        new JSONArray(get("/reminders/dashboard"));
            }
            return reminderCache;
        } catch (Exception e) {
            return new JSONArray();
        }
    }


}