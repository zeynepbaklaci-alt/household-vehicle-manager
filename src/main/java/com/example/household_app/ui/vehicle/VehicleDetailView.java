package com.example.household_app.ui.vehicle;

import com.example.household_app.ui.util.ApiClient;
import com.example.household_app.ui.vehicle.fuel.AddFuelFillView;
import com.example.household_app.ui.vehicle.fuel.FuelDto;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

        /* ===== TAB PANE ===== */
        TabPane tabs = new TabPane(detailsTab, fuelTab, reportsTab, reminderTab);

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
}
