package com.example.household_app.ui.vehicle;

import com.example.household_app.ui.session.SessionStore;
import com.example.household_app.ui.util.ApiClient;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

public class AddVehicleView {

    public static void showCreate(Stage owner, Runnable onSuccess) {
        showInternal(owner, null, onSuccess);
    }

    public static void showEdit(
            Stage owner,
            VehicleDto vehicle,
            Runnable onSuccess
    ) {
        showInternal(owner, vehicle, onSuccess);
    }

    private static void showInternal(
            Stage owner,
            VehicleDto vehicle,
            Runnable onSuccess
    ) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(vehicle == null ? "Add Vehicle" : "Edit Vehicle");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        TextField plateField = new TextField();
        TextField brandField = new TextField();
        TextField modelField = new TextField();

        ComboBox<String> fuelTypeBox = new ComboBox<>();
        fuelTypeBox.getItems().addAll(
                "GASOLINE", "DIESEL", "HYBRID", "ELECTRIC"
        );

        TextField odometerField = new TextField();
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        if (vehicle != null) {
            plateField.setText(vehicle.getPlate());
            brandField.setText(vehicle.getBrand());
            modelField.setText(vehicle.getModel());
            fuelTypeBox.getSelectionModel().select(vehicle.getFuelType());
            odometerField.setText(
                    vehicle.getInitialOdometer() != null
                            ? vehicle.getInitialOdometer().toString()
                            : ""
            );
            plateField.setDisable(true);
        } else {
            fuelTypeBox.getSelectionModel().selectFirst();
        }


        Button saveBtn = new Button("Save");
        Button cancelBtn = new Button("Cancel");
        Button deleteBtn = new Button("Delete");

        deleteBtn.setStyle("-fx-text-fill: red;");
        deleteBtn.setVisible(vehicle != null); // ✅ only in edit mode

        HBox actionButtons = new HBox(10, saveBtn, cancelBtn);
        if (vehicle != null) {
            actionButtons.getChildren().add(deleteBtn);
        }

        grid.add(new Label("Plate"), 0, 0); grid.add(plateField, 1, 0);
        grid.add(new Label("Brand"), 0, 1); grid.add(brandField, 1, 1);
        grid.add(new Label("Model"), 0, 2); grid.add(modelField, 1, 2);
        grid.add(new Label("Fuel Type"), 0, 3); grid.add(fuelTypeBox, 1, 3);
        grid.add(new Label("Initial Odometer"), 0, 4); grid.add(odometerField, 1, 4);

        grid.add(actionButtons, 0, 5, 2, 1);
        grid.add(errorLabel, 0, 6, 2, 1);

        /* ===== SAVE ===== */
        saveBtn.setOnAction(e -> {

            if (plateField.getText().isBlank()
                    || brandField.getText().isBlank()
                    || modelField.getText().isBlank()
                    || odometerField.getText().isBlank()
                    || fuelTypeBox.getValue() == null) {

                showError("All fields must be filled");
                return;
            }

            try {
                JSONObject body = new JSONObject();
                body.put("plate", plateField.getText().trim());
                body.put("brand", brandField.getText().trim());
                body.put("model", modelField.getText().trim());
                body.put("fuelType", fuelTypeBox.getValue());
                body.put(
                        "initialOdometer",
                        odometerField.getText().isBlank()
                                ? 0
                                : Integer.parseInt(odometerField.getText())
                );

                if (vehicle == null) {
                    ApiClient.post(
                            "/households/" +
                                    SessionStore.getHouseholdId() +
                                    "/vehicles",
                            body.toString()
                    );
                } else {

                    ApiClient.put(
                            "/households/" +
                                    SessionStore.getHouseholdId() +
                                    "/vehicles/" +
                                    vehicle.getId(),
                            body.toString()
                    );

                }

                stage.close();
                onSuccess.run();

            } catch (Exception ex) {
                ex.printStackTrace();
                errorLabel.setText("Failed to save vehicle");
            }
        });

        /* ===== DELETE ===== */
        deleteBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Vehicle");
            alert.setHeaderText("Delete " + vehicle.getPlate());
            alert.setContentText("Are you sure?");

            alert.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    try {
                        ApiClient.delete(
                                "/households/" +
                                        SessionStore.getHouseholdId() +
                                        "/vehicles/" +
                                        vehicle.getId()
                        )
                        ;
                        stage.close();
                        onSuccess.run();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        errorLabel.setText("Failed to delete vehicle");
                    }
                }
            });
        });

        cancelBtn.setOnAction(e -> stage.close());

        stage.setScene(new Scene(grid));
        stage.showAndWait();
    }
    private static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
