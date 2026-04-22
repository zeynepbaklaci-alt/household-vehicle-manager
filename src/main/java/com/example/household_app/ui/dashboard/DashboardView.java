package com.example.household_app.ui.dashboard;

import com.example.household_app.ui.components.ReminderBanner;
import com.example.household_app.ui.util.ApiClient;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;

public class DashboardView extends VBox {

    public DashboardView() {
        setSpacing(10);
        setPadding(new Insets(10));

        loadReminders();
    }

    private void loadReminders() {
        getChildren().clear();

        Label title = new Label("🔔 Recordatorios");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        getChildren().add(title);

        try {
            String response = ApiClient.get("/reminders/dashboard");
            JSONArray reminders = new JSONArray(response);

            if (reminders.isEmpty()) {
                Label empty = new Label("No hay avisos pendientes");
                empty.setStyle("-fx-text-fill: gray;");
                getChildren().add(empty);
                return;
            }

            for (int i = 0; i < reminders.length(); i++) {
                JSONObject r = reminders.getJSONObject(i);

                ReminderBanner banner = new ReminderBanner(
                        java.util.UUID.fromString(r.getString("id")),
                        r.getString("vehicleLabel"),
                        r.getString("type"),
                        LocalDate.parse(r.getString("remindAt")),
                        this::loadReminders
                );

                getChildren().add(banner);
            }

        } catch (Exception e) {
            Label error = new Label("⚠️ Error cargando recordatorios");
            error.setStyle("-fx-text-fill: red;");
            getChildren().add(error);
            e.printStackTrace();
        }
    }
}