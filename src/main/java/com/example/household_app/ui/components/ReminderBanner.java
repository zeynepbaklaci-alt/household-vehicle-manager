package com.example.household_app.ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import com.example.household_app.ui.util.ApiClient;

import java.time.LocalDate;
import java.util.UUID;

public class ReminderBanner extends HBox {

    private final UUID reminderId;

    public ReminderBanner(
            UUID reminderId,
            String vehicleLabel,
            String type,
            LocalDate remindAt,
            Runnable onDismiss
    ) {
        this.reminderId = reminderId;

        // Layout
        setSpacing(12);
        setPadding(new Insets(10));
        setStyle("""
            -fx-border-color: lightgray;
            -fx-border-radius: 6;
            -fx-background-radius: 6;
        """);

        // Icon
        Label iconLabel = new Label(getIcon(type));
        iconLabel.setStyle("-fx-font-size: 18px;");

        // Message
        Label messageLabel = new Label(buildMessage(type, vehicleLabel, remindAt));
        messageLabel.setStyle("-fx-font-size: 13px;");

        // Dismiss button
        Button dismissButton = new Button("✖");
        dismissButton.setStyle("""
            -fx-background-color: transparent;
            -fx-font-size: 14px;
        """);

        dismissButton.setOnAction(e -> {
            ApiClient.dismissReminder(reminderId);
            onDismiss.run();
        });

        getChildren().addAll(iconLabel, messageLabel, dismissButton);
    }

    private String getIcon(String type) {
        return switch (type) {
            case "ITV" -> "🚗";
            case "INSURANCE" -> "🛡";
            case "MAINTENANCE" -> "🔧";
            default -> "🔔";
        };
    }

    private String buildMessage(
            String type,
            String vehicleLabel,
            LocalDate remindAt
    ) {
        return switch (type) {
            case "ITV" ->
                    vehicleLabel + " - ITV vence el " + remindAt;
            case "INSURANCE" ->
                    vehicleLabel + " - Seguro vence el " + remindAt;
            case "MAINTENANCE" ->
                    vehicleLabel + " - Mantenimiento pendiente el " + remindAt;
            default ->
                    vehicleLabel;
        };
    }
}

