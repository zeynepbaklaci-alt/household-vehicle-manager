package com.example.household_app.ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import com.example.household_app.ui.util.ApiClient;

import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.util.UUID;

import static javax.swing.UIManager.getColor;

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

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), remindAt);

        setStyle("""
                    -fx-border-radius: 6;
                    -fx-background-radius: 6;
                    -fx-padding: 10;
                    -fx-background-color: %s;
                """.formatted(getColor(daysLeft)));


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
        long daysLeft =
                java.time.temporal.ChronoUnit.DAYS
                        .between(LocalDate.now(), remindAt);

        String daysText;

        if (daysLeft == 0) {
            daysText = "today";
        } else if (daysLeft == 1) {
            daysText = "tomorrow";
        } else {
            daysText = "in " + daysLeft + " days";
        }

        return switch (type) {
            case "ITV" ->
                    vehicleLabel + " - ITV expires " + daysText +
                            " (" + remindAt + ")";
            case "INSURANCE" ->
                    vehicleLabel + " - Insurance expires " + daysText +
                            " (" + remindAt + ")";
            case "MAINTENANCE" ->
                    vehicleLabel + " - Maintenance " + daysText;
            default ->
                    vehicleLabel;
        };
    }

    private String getColor(long daysLeft) {
        if (daysLeft <= 7) {
            return "#ffcccc"; // red
        } else if (daysLeft <= 30) {
            return "#fff0cc"; // orange
        } else {
            return "#e6ffec"; // green
        }
    }

}

