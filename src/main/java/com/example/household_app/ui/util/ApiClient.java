package com.example.household_app.ui.util;

import com.example.household_app.ui.session.SessionStore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public final class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private ApiClient() {}

    private static HttpRequest.Builder baseRequest(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json");

        String jwt = SessionStore.getJwt();
        System.out.println("JWT in session: " + SessionStore.getJwt());

        if (jwt != null && !jwt.isBlank()) {
            builder.header("Authorization", "Bearer " + jwt);
        } else {
            System.out.println("⚠️ No JWT for request: " + path);
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

    private static void logResponse(
            String method,
            String path,
            HttpResponse<String> response
    ) {
        System.out.println("========== API CALL ==========");
        System.out.println(method + " " + path);
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body:");
        System.out.println(response.body());
        System.out.println("================================");
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
        } catch (Exception e) {
            throw new RuntimeException("Failed to dismiss all reminders", e);
        }
    }


}