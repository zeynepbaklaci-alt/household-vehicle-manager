package com.example.household_app.ui.vehicle;

import com.example.household_app.ui.util.ApiClient;
import com.example.household_app.ui.vehicle.fuel.AddFuelFillView;
import com.example.household_app.ui.vehicle.fuel.FuelDto;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VehicleDetailView extends VBox {

    private final Stage stage;
    private final VehicleDto vehicle;
    private Tab insuranceTab;
    private Tab itvTab;

    private static final Logger LOGGER =
            Logger.getLogger(VehicleDetailView.class.getName());

    public VehicleDetailView(Stage stage, VehicleDto vehicle) {
        this.stage = stage;
        this.vehicle = vehicle;

        setPadding(new Insets(20));
        setSpacing(10);

        /* ===== HEADER (TITLE + REMINDER BADGE) ===== */
        Label title = new Label("Vehicle Detail");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label reminderBadge = new Label();
        reminderBadge.setStyle("""
                    -fx-background-color: #ff4444;
                    -fx-text-fill: white;
                    -fx-padding: 4 8;
                    -fx-background-radius: 10;
                """);
        reminderBadge.setVisible(false);

        HBox headerBox = new HBox(10, title, reminderBadge);
        headerBox.setPadding(new Insets(0, 0, 10, 0));

        loadReminderBadge(vehicle.getId(), reminderBadge);

        /* ===== DETAILS TAB ===== */
        VBox detailsBox = new VBox(8);
        detailsBox.getChildren().addAll(
                new Label("Plate: " + vehicle.getPlate()),
                new Label("Brand: " + vehicle.getBrand()),
                new Label("Model: " + vehicle.getModel())
        );

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e ->
                stage.setScene(
                        new Scene(new VehicleListView(stage), 600, 400)
                )
        );
        detailsBox.getChildren().add(backBtn);

        Tab detailsTab = new Tab("Details", detailsBox);
        detailsTab.setClosable(false);

        /* ===== FUEL TAB ===== */
        VBox fuelBox = new VBox(10);
        ListView<FuelDto> fuelList = new ListView<>();

        Button addFuelBtn = new Button("➕ Add Fuel");
        Button deleteFuelBtn = new Button("🗑 Delete Fuel");
        deleteFuelBtn.setStyle("-fx-text-fill: red;");

        fuelBox.getChildren().addAll(addFuelBtn, deleteFuelBtn, fuelList);

        Runnable loadFuel = () -> {
            fuelList.getItems().clear();
            try {
                String resp =
                        ApiClient.get("/vehicles/" + vehicle.getId() + "/fuel-fills");

                JSONObject page = new JSONObject(resp);
                JSONArray arr = page.getJSONArray("content");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    FuelDto dto = new FuelDto();
                    dto.setId(o.getString("id"));
                    dto.setDate(o.getString("date"));
                    dto.setOdometerKm(o.getInt("odometerKm"));
                    dto.setLiters(o.getDouble("liters"));
                    dto.setPricePerLiter(o.getDouble("pricePerLiter"));
                    dto.setTotalCost(o.getDouble("totalCost"));
                    dto.setStation(o.optString("station", ""));
                    fuelList.getItems().add(dto);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Fuel loading error", e);
                fuelList.setPlaceholder(new Label("No fuel records"));
            }
        };

        addFuelBtn.setOnAction(e ->
                AddFuelFillView.showCreate(
                        stage,
                        vehicle.getId(),
                        loadFuel
                )
        );

        deleteFuelBtn.setOnAction(e -> {
            FuelDto selected = fuelList.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Fuel");
            alert.setHeaderText("Delete fuel entry");
            alert.setContentText("Are you sure?");

            alert.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    try {
                        ApiClient.delete(
                                "/vehicles/" +
                                        vehicle.getId() +
                                        "/fuel-fills/" +
                                        selected.getId()
                        );
                        loadFuel.run();
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, "Fuel delete error", ex);
                    }
                }
            });
        });

        fuelList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(FuelDto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(
                            item.getDate() +
                                    " • " + item.getLiters() + "L • " +
                                    String.format("%.2f €", item.getTotalCost())
                    );
                }
            }
        });

        fuelList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                FuelDto selected = fuelList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    AddFuelFillView.showEdit(
                            stage,
                            vehicle.getId(),
                            selected,
                            loadFuel
                    );
                }
            }
        });

        loadFuel.run();

        Tab fuelTab = new Tab("Fuel", fuelBox);
        fuelTab.setClosable(false);

        /* ===== REPORTS TAB ===== */
        Tab reportsTab = new Tab("Reports", createReportsTab());
        reportsTab.setClosable(false);

        //---- REMINDER TAB ---
        Tab reminderTab = new Tab("Reminders", createReminderHistoryTab());
        reminderTab.setClosable(false);


        insuranceTab = new Tab("Insurance");
        insuranceTab.setContent(createInsuranceTab());
        insuranceTab.setClosable(false);

        itvTab = new Tab("ITV");
        itvTab.setContent(createItvTab());
        itvTab.setClosable(false);

        TabPane tabs = new TabPane(
                detailsTab,
                fuelTab,
                reportsTab,
                reminderTab,
                insuranceTab,
                itvTab
        );

        getChildren().addAll(headerBox, tabs);
    }

    /* ===== REPORT TAB CONTENT ===== */
    private VBox createReportsTab() {

        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        ComboBox<String> periodBox = new ComboBox<>();
        periodBox.getItems().addAll(
                "2026-01", "2026-02", "2026-03", "2026-04", "2026-05"
        );
        periodBox.getSelectionModel().selectFirst();

        Label kmLabel = new Label();
        Label totalLabel = new Label();
        Label costKmLabel = new Label();
        Label consumptionLabel = new Label();

        Runnable loadReport = () -> {
            try {
                String period = periodBox.getValue();
                String resp = ApiClient.get(
                        "/reports/costs?vehicleId=" +
                                vehicle.getId() +
                                "&period=" + period
                );

                JSONObject o = new JSONObject(resp);

                kmLabel.setText("Km driven: " + o.getInt("kmDriven"));
                totalLabel.setText(
                        "Total cost: " +
                                String.format("%.2f €", o.getDouble("totalCost"))
                );
                costKmLabel.setText(
                        "Cost / km: " +
                                String.format("%.3f €", o.getDouble("costPerKm"))
                );
                consumptionLabel.setText(
                        "Avg consumption: " +
                                String.format(
                                        "%.2f L/100km",
                                        o.getDouble("avgConsumption")
                                )
                );

            } catch (Exception e) {
                kmLabel.setText("No data");
                totalLabel.setText("");
                costKmLabel.setText("");
                consumptionLabel.setText("");
            }
        };

        periodBox.setOnAction(e -> loadReport.run());
        loadReport.run();

        box.getChildren().addAll(
                new Label("Period"),
                periodBox,
                kmLabel,
                totalLabel,
                costKmLabel,
                consumptionLabel
        );

        return box;
    }

    private VBox createReminderHistoryTab() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        ListView<String> list = new ListView<>();

        try {
            String resp = ApiClient.get(
                    "/vehicles/" + vehicle.getId() + "/reminders/history"
            );

            JSONArray arr = new JSONArray(resp);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject r = arr.getJSONObject(i);
                list.getItems().add(
                        r.getString("type") +
                                " – " +
                                r.getString("remindAt")
                );
            }

        } catch (Exception e) {
            list.setPlaceholder(new Label("No reminders"));
        }

        box.getChildren().add(list);
        return box;
    }

    /* ===== REMINDER BADGE LOADER ===== */
    private void loadReminderBadge(
            String vehicleId,
            Label badge
    ) {
        try {
            JSONArray arr = ApiClient.getCachedReminders();

            long count = 0;
            for (int i = 0; i < arr.length(); i++) {
                JSONObject r = arr.getJSONObject(i);
                if (vehicleId.equals(r.getString("vehicleId"))) {
                    count++;
                }
            }

            if (count > 0) {
                badge.setText("🔔 " + count);
                badge.setVisible(true);
            } else {
                badge.setVisible(false);
            }

        } catch (Exception e) {
            badge.setVisible(false);
        }
    }

    private VBox createInsuranceTab() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // ✅ Add button
        Button addButton = new Button("Add Insurance");
        addButton.setOnAction(e -> openAddInsuranceDialog());

        ListView<String> listView = new ListView<>();

        try {
            String response = ApiClient.get(
                    "/vehicles/" + vehicle.getId() + "/insurance-policies"
            );

            JSONArray arr = new JSONArray(response);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject p = arr.getJSONObject(i);

                String line =
                        p.getString("provider") + " | " +
                                p.getString("policyNumber") + " | " +
                                p.getString("startDate") + " → " +
                                p.getString("endDate");

                listView.getItems().add(line);
            }

            if (arr.isEmpty()) {
                Label empty = new Label("No insurance policies yet");
                empty.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
                listView.setPlaceholder(empty);

            }

        } catch (Exception e) {
            listView.setPlaceholder(
                    new Label("Error loading insurance policies")
            );
        }

        root.getChildren().addAll(addButton, listView);
        return root;
    }


    private void openAddInsuranceDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add Insurance");

        // Fields
        TextField providerField = new TextField();
        providerField.setPromptText("Provider");

        TextField policyNumberField = new TextField();
        policyNumberField.setPromptText("Policy number");

        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        TextField premiumField = new TextField();
        premiumField.setPromptText("Premium");

        ComboBox<String> periodicityBox = new ComboBox<>();
        periodicityBox.getItems().addAll("ANNUAL", "MONTHLY", "QUARTERLY");
        periodicityBox.setValue("ANNUAL");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label("Provider:"), providerField);
        grid.addRow(1, new Label("Policy Number:"), policyNumberField);
        grid.addRow(2, new Label("Start Date:"), startDatePicker);
        grid.addRow(3, new Label("End Date:"), endDatePicker);
        grid.addRow(4, new Label("Premium:"), premiumField);
        grid.addRow(5, new Label("Periodicity:"), periodicityBox);

        dialog.getDialogPane().setContent(grid);

        // Buttons
        ButtonType saveButtonType =
                new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(
                saveButtonType,
                ButtonType.CANCEL
        );

        dialog.setResultConverter(button -> {
            if (button == saveButtonType) {
                if (providerField.getText().isBlank()
                        || policyNumberField.getText().isBlank()
                        || startDatePicker.getValue() == null
                        || endDatePicker.getValue() == null
                        || premiumField.getText().isBlank()) {
                    showError("All fields must be filled");
                    return null;
                }
                try {
                    JSONObject body = new JSONObject();
                    body.put("provider", providerField.getText());
                    body.put("policyNumber", policyNumberField.getText());
                    body.put("startDate", startDatePicker.getValue().toString());
                    body.put("endDate", endDatePicker.getValue().toString());
                    body.put("premium", Double.parseDouble(premiumField.getText()));
                    body.put("periodicity", periodicityBox.getValue());

                    ApiClient.post(
                            "/vehicles/" + vehicle.getId() + "/insurance-policies",
                            body.toString()
                    );

                    refreshInsuranceTab();

                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setHeaderText(null);
                    info.setContentText("Insurance saved successfully");
                    info.showAndWait();

                } catch (Exception ex) {
                    showError("Failed to save insurance");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void refreshInsuranceTab() {
        insuranceTab.setContent(createInsuranceTab());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private VBox createItvTab() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // ✅ Add button
        Button addButton = new Button("Add ITV");
        addButton.setOnAction(e -> openAddItvDialog());

        ListView<String> listView = new ListView<>();

        try {
            String response = ApiClient.get(
                    "/vehicles/" + vehicle.getId() + "/itv"
            );

            JSONArray arr = new JSONArray(response);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject itv = arr.getJSONObject(i);

                String line =
                        "Valid until: " + itv.getString("validUntil") +
                                " | Passed: " + itv.getBoolean("passed") +
                                " | Cost: " + itv.getDouble("cost");

                listView.getItems().add(line);
            }

            if (arr.isEmpty()) {
                Label empty = new Label("No insurance policies yet");
                empty.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
                listView.setPlaceholder(empty);
            }

        } catch (Exception e) {
            listView.setPlaceholder(
                    new Label("Error loading ITV records")
            );
        }

        root.getChildren().addAll(addButton, listView);
        return root;
    }
    private void openAddItvDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add ITV");

        DatePicker datePicker = new DatePicker();
        DatePicker validUntilPicker = new DatePicker();

        TextField costField = new TextField();
        costField.setPromptText("Cost");

        CheckBox passedCheck = new CheckBox("Passed");

        TextField noteField = new TextField();
        noteField.setPromptText("Note (optional)");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label("Date:"), datePicker);
        grid.addRow(1, new Label("Valid Until:"), validUntilPicker);
        grid.addRow(2, new Label("Cost:"), costField);
        grid.addRow(3, new Label("Result:"), passedCheck);
        grid.addRow(4, new Label("Note:"), noteField);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveButtonType =
                new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(
                saveButtonType,
                ButtonType.CANCEL
        );

        dialog.setResultConverter(button -> {
            if (button == saveButtonType) {
                if (datePicker.getValue() == null
                        || validUntilPicker.getValue() == null
                        || costField.getText().isBlank()) {

                    showError("All required fields must be filled");
                    return null;
                }

                if (validUntilPicker.getValue().isBefore(datePicker.getValue())) {
                    showError("Valid until must be after inspection date");
                    return null;
                }

                try {
                    JSONObject body = new JSONObject();
                    body.put("date", datePicker.getValue().toString());
                    body.put("validUntil", validUntilPicker.getValue().toString());
                    body.put("cost", Double.parseDouble(costField.getText()));
                    body.put("passed", passedCheck.isSelected());
                    body.put("note", noteField.getText());

                    ApiClient.post(
                            "/vehicles/" + vehicle.getId() + "/itv",
                            body.toString()
                    );
                    refreshItvTab();

                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setHeaderText(null);
                    info.setContentText("ITV saved successfully");
                    info.showAndWait();

                } catch (Exception ex) {
                    showError("Failed to save ITV");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
    private void refreshItvTab() {
        itvTab.setContent(createItvTab());
    }

}
