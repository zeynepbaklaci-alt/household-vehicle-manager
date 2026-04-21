package com.example.household_app.ui.login;

import com.example.household_app.ui.session.SessionStore;
import com.example.household_app.ui.util.ApiClient;
import com.example.household_app.ui.vehicle.VehicleListView;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

@RequiredArgsConstructor
public class LoginView extends VBox {

    private final Stage stage;

    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label errorLabel = new Label();

    {
        setPadding(new Insets(20));
        setSpacing(10);

        emailField.setPromptText("Email");
        passwordField.setPromptText("Password");

        Button loginButton = new Button("LOGIN");
        loginButton.setOnAction(e -> onLogin());

        getChildren().addAll(
                new Label("Login"),
                emailField,
                passwordField,
                loginButton,
                errorLabel
        );
    }

    private void onLogin() {
        try {
            /* ===== LOGIN ===== */
            String body = """
                {
                  "email": "%s",
                  "password": "%s"
                }
            """.formatted(
                    emailField.getText().trim(),
                    passwordField.getText()
            );

            String loginResponse = ApiClient.post("/auth/login", body);
            JSONObject loginJson = new JSONObject(loginResponse);

            // ✅ JWT
            SessionStore.setJwt(loginJson.getString("accessToken"));

            // ===== GET HOUSEHOLDS =====
            String householdsResponse = ApiClient.get("/households");

// ✅ RESPONSE ARRAY
            JSONArray households = new JSONArray(householdsResponse);

            if (households.isEmpty()) {
                errorLabel.setText("No household found for this user");
                return;
            }

            String householdId =
                    households.getJSONObject(0).getString("id");

            SessionStore.setHouseholdId(householdId);
            System.out.println("✅ UI householdId set to: " + householdId);

// ===== GO TO VEHICLE LIST =====
            stage.setScene(
                    new Scene(
                            new VehicleListView(stage),
                            600,
                            400
                    )
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            errorLabel.setText("Login failed");
        }
    }
}