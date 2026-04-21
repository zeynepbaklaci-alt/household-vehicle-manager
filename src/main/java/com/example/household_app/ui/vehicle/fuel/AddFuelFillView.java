package com.example.household_app.ui.vehicle.fuel;

import com.example.household_app.ui.util.ApiClient;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.time.LocalDate;

public class AddFuelFillView {

    /* ===== PUBLIC API ===== */
    public static void showCreate(Stage owner, String vehicleId, Runnable onSuccess) {
        showInternal(owner, vehicleId, null, onSuccess);
    }

    public static void showEdit(Stage owner, String vehicleId, FuelDto fill, Runnable onSuccess) {
        showInternal(owner, vehicleId, fill, onSuccess);
    }

    /* ===== INTERNAL FORM ===== */
    private static void showInternal(
            Stage owner,
            String vehicleId,
            FuelDto fill,
            Runnable onSuccess
    ) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(fill == null ? "Add Fuel Fill" : "Edit Fuel Fill");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField odometerField = new TextField();
        TextField litersField = new TextField();
        TextField priceField = new TextField();
        TextField stationField = new TextField();

        Label totalLabel = new Label("Total: 0.00 €");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        if (fill != null) {
            datePicker.setValue(LocalDate.parse(fill.getDate()));
            odometerField.setText(String.valueOf(fill.getOdometerKm()));
            litersField.setText(String.valueOf(fill.getLiters()));
            priceField.setText(String.valueOf(fill.getPricePerLiter()));
            stationField.setText(fill.getStation());
            totalLabel.setText("Total: " + String.format("%.2f", fill.getTotalCost()) + " €");
        }

        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");

        grid.add(new Label("Date"), 0, 0); grid.add(datePicker, 1, 0);
        grid.add(new Label("Odometer (km)"), 0, 1); grid.add(odometerField, 1, 1);
        grid.add(new Label("Liters"), 0, 2); grid.add(litersField, 1, 2);
        grid.add(new Label("Price / Liter"), 0, 3); grid.add(priceField, 1, 3);
        grid.add(new Label("Station"), 0, 4); grid.add(stationField, 1, 4);
        grid.add(totalLabel, 1, 5);
        grid.add(saveBtn, 0, 6); grid.add(cancelBtn, 1, 6);
        grid.add(errorLabel, 0, 7, 2, 1);

        // live total calculation
        Runnable recalc = () -> {
            try {
                double l = Double.parseDouble(litersField.getText());
                double p = Double.parseDouble(priceField.getText());
                totalLabel.setText("Total: " + String.format("%.2f", l * p) + " €");
            } catch (Exception ignore) {}
        };
        litersField.textProperty().addListener((a,b,c)->recalc.run());
        priceField.textProperty().addListener((a,b,c)->recalc.run());

        saveBtn.setOnAction(e -> {

            if (datePicker.getValue() == null ||
                    odometerField.getText().isBlank() ||
                    litersField.getText().isBlank() ||
                    priceField.getText().isBlank()) {

                errorLabel.setText("All fields are required");
                return;
            }

            int km;
            double liters;
            double price;

            try {
                km = Integer.parseInt(odometerField.getText());
                liters = Double.parseDouble(litersField.getText());
                price = Double.parseDouble(priceField.getText());

                if (km <= 0 || liters <= 0 || price <= 0) {
                    errorLabel.setText("Values must be greater than zero");
                    return;
                }
            } catch (NumberFormatException e2) {
                errorLabel.setText("Please enter valid numbers");
                return;
            }

            try {
                JSONObject body = new JSONObject();
                body.put("date", datePicker.getValue().toString());
                body.put("odometerKm", km);
                body.put("liters", liters);
                body.put("pricePerLiter", price);
                body.put("station", stationField.getText().trim());

                if (fill == null) {
                    ApiClient.post("/vehicles/" + vehicleId + "/fuel-fills", body.toString());
                } else {
                    ApiClient.put("/fuel-fills/" + fill.getId(), body.toString());
                }

                stage.close();
                onSuccess.run();

            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        cancelBtn.setOnAction(e -> stage.close());

        stage.setScene(new Scene(grid));
        stage.showAndWait();
    }
}
