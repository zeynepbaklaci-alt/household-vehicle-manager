package com.example.household_app.ui.vehicle.fuel;

import com.example.household_app.ui.util.ApiClient;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FuelTabView extends VBox {

    private final String vehicleId;

    private final TableView<FuelDto> table = new TableView<>();

    public FuelTabView(String vehicleId) {
        this.vehicleId = vehicleId;

        setPadding(new Insets(10));
        setSpacing(10);

        Label title = new Label("Fuel Records");

        // === Columns ===
        TableColumn<FuelDto, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getDate()));

        TableColumn<FuelDto, Number> kmCol = new TableColumn<>("Km");
        kmCol.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getOdometerKm()));

        TableColumn<FuelDto, Number> litersCol = new TableColumn<>("Liters");
        litersCol.setCellValueFactory(d ->
                new SimpleDoubleProperty(d.getValue().getLiters()));

        TableColumn<FuelDto, Number> costCol = new TableColumn<>("Cost (€)");
        costCol.setCellValueFactory(d ->
                new SimpleDoubleProperty(d.getValue().getTotalCost()));

        table.getColumns().addAll(dateCol, kmCol, litersCol, costCol);
        table.setPlaceholder(new Label("No fuel records"));

        Button addBtn = new Button("Add Fuel");
        addBtn.setOnAction(e -> {
            reload();
        });

        getChildren().addAll(title, table, addBtn);

        reload();
    }

    // ✅ EN ÖNEMLİ METOD
    private void reload() {
        new Thread(() -> {
            try {
                String response =
                        ApiClient.get("/vehicles/" + vehicleId + "/fuel-fills");

                JSONObject page = new JSONObject(response);
                JSONArray arr = page.getJSONArray("content");

                List<FuelDto> list = new ArrayList<>();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);

                    FuelDto dto = new FuelDto();
                    dto.setDate(o.getString("date"));
                    dto.setOdometerKm(o.getInt("odometerKm"));
                    dto.setLiters(o.getDouble("liters"));
                    dto.setTotalCost(o.getDouble("totalCost"));

                    list.add(dto);
                }

                Platform.runLater(() -> {
                    ObservableList<FuelDto> items =
                            FXCollections.observableArrayList(list);
                    table.setItems(items);
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() ->
                        table.setPlaceholder(
                                new Label("Could not load fuel data")
                        )
                );
            }
        }).start();
    }
}