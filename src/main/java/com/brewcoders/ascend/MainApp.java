package com.brewcoders.ascend;

import com.brewcoders.ascend.view.DashboardView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DashboardView dashboardView = new DashboardView();
        Scene scene = new Scene(dashboardView.getView(), 1100, 700);

        // Attach CSS stylesheet if available
        if (getClass().getResource("/css/styles.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        }

        primaryStage.setTitle("ASCEND - Developer Productivity System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
