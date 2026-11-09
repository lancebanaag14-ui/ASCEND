package com.brewcoders.ascend;

import com.brewcoders.ascend.controller.MissionManager;
import com.brewcoders.ascend.view.auth.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Load custom pixel font into global JavaFX Font registry
        try {
            Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 12);
            if (pixelFont != null) {
                System.out.println("Loaded font family: " + pixelFont.getFamily());
            } else {
                System.err.println("Failed to load PressStart2P font from resources.");
            }
        } catch (Exception e) {
            System.err.println("Error loading PressStart2P font: " + e.getMessage());
        }

        // 2. Ensure initial state in MissionManager singleton starts clean
        MissionManager.getInstance().setCurrentUsername("Guest User");

        // 3. Initialize entry point with LoginView
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView, 1280, 800);

        // 4. Load stylesheet globally
        if (getClass().getResource("/css/styles.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        } else {
            System.err.println("Could not find /css/styles.css in resources!");
        }

        // 5. Configure primary window stage settings
        primaryStage.setTitle("ASCEND - Gamified Productivity System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);

        primaryStage.show();
    }

    public static void main(String[] args) {
        // Enable system DPI scaling adaptation across high-res displays
        System.setProperty("prism.allowhidpi", "true");
        launch(args);
    }
}