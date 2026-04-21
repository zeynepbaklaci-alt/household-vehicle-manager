package com.example.household_app.ui;

import com.example.household_app.ui.login.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        LoginView loginView = new LoginView(stage);

        Scene scene = new Scene(loginView, 900, 600);

        stage.setTitle("Household Vehicle Manager");
        stage.setScene(scene);

        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setResizable(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
