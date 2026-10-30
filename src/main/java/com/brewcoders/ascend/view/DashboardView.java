package com.brewcoders.ascend.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DashboardView {

    private BorderPane rootLayout;

    public DashboardView() {
        rootLayout = new BorderPane();
        
        // 1. Sidebar Navigation
        VBox sidebar = buildSidebar();
        rootLayout.setLeft(sidebar);

        // 2. Center Content Area (Missions Board)
        VBox mainContent = buildMainContent();
        rootLayout.setCenter(mainContent);
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(200);

        Label title = new Label("ASCEND");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        Button navMissions = new Button("Missions");
        Button navProfile = new Button("Profile");
        navMissions.setMaxWidth(Double.MAX_VALUE);
        navProfile.setMaxWidth(Double.MAX_VALUE);

        sidebar.getChildren().addAll(title, navMissions, navProfile);
        return sidebar;
    }

    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Header Section
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label headerTitle = new Label("Active Missions");
        headerTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        
        Button addMissionBtn = new Button("+ New Mission");
        addMissionBtn.getStyleClass().add("button-primary");

        header.getChildren().addAll(headerTitle, addMissionBtn);
        HBox.setHgrow(headerTitle, Priority.ALWAYS);

        content.getChildren().addAll(header);
        return content;
    }

    public BorderPane getView() {
        return rootLayout;
    }
}