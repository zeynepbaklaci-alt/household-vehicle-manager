package com.example.household_app.ui.vehicle;

import com.example.household_app.ui.session.SessionStore;
import com.example.household_app.ui.util.ApiClient;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

public class VehicleListView extends VBox {

    private final Stage stage;
    private final ListView<VehicleDto> listView = new ListView<>();

    public VehicleListView(Stage stage) {
        this.stage = stage;

        setPadding(new Insets(20));
        setSpacing(10);

        Label title = new Label("Vehicles");

        /* ===== BUTTONS ===== */
        Button addBtn = new Button("➕ Add Vehicle");
        Button editBtn = new Button("✏️ Edit");
        Button deleteBtn = new Button("🗑 Delete");
        deleteBtn.setStyle("-fx-text-fill: red;");

        editBtn.setDisable(true);
        deleteBtn.setDisable(true);

        HBox buttons = new HBox(10, addBtn, editBtn, deleteBtn);

        /* ===== BUTTON ACTIONS ===== */

        // ADD
        addBtn.setOnAction(e ->
                AddVehicleView.showCreate(stage, this::loadVehicles)
        );

        // EDIT
        editBtn.setOnAction(e -> {
            VehicleDto selected =
                    listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                AddVehicleView.showEdit(
                        stage,
                        selected,
                        this::loadVehicles
                );
            }
        });

        // DELETE
        deleteBtn.setOnAction(e -> {
            VehicleDto selected =
                    listView.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Vehicle");
            alert.setHeaderText("Delete " + selected.getPlate());
            alert.setContentText("Are you sure? This action cannot be undone.");

            alert.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    try {

                        ApiClient.delete(
                                "/households/" +
                                        SessionStore.getHouseholdId() +
                                        "/vehicles/" +
                                        selected.getId()
                        )
                        ;
                        loadVehicles();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        });

        /* ===== ENABLE / DISABLE EDIT & DELETE ===== */
        listView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldV, selected) -> {
                    boolean hasSelection = selected != null;
                    editBtn.setDisable(!hasSelection);
                    deleteBtn.setDisable(!hasSelection);
                });

        /* ===== LIST VIEW ===== */
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(VehicleDto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(
                            item.getBrand() + " " +
                                    item.getModel() +
                                    " (" + item.getPlate() + ")"
                    );
                }
            }
        });

        /* ===== DOUBLE CLICK → DETAIL (READ-ONLY) ===== */
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                VehicleDto selected =
                        listView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    stage.setScene(
                            new Scene(
                                    new VehicleDetailView(stage, selected),
                                    800,
                                    600
                            )
                    );
                }
            }
        });

        loadVehicles();

        getChildren().addAll(title, buttons, listView);
    }

    // 🔁 Load / Reload vehicles
    private void loadVehicles() {
        listView.getItems().clear();

        try {
            String response = ApiClient.get(
                    "/households/" +
                            SessionStore.getHouseholdId() +
                            "/vehicles"
            );

            if (response == null || response.isBlank()) {
                listView.setPlaceholder(new Label("No vehicles found"));
                return;
            }

            JSONArray array = new JSONArray(response);

            for (int i = 0; i < array.length(); i++) {
                JSONObject v = array.getJSONObject(i);

                VehicleDto dto = new VehicleDto();
                dto.setId(v.getString("id"));
                dto.setPlate(v.getString("plate"));
                dto.setBrand(v.getString("brand"));
                dto.setModel(v.getString("model"));
                dto.setFuelType(v.getString("fuelType"));
                dto.setInitialOdometer(v.getInt("initialOdometer"));


                listView.getItems().add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            listView.setPlaceholder(new Label("Error loading vehicles"));
        }
    }
}