package com.brewcoders.ascend.view.navigation;

import com.brewcoders.ascend.view.missions.MissionsView;
import com.brewcoders.ascend.view.profile.ProfileMainView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MainLayoutView extends BorderPane {

    private SidebarView sidebarView;
    private Label breadcrumbLabel;
    private Button newMissionBtn;
    private VBox centerContainer;

    private String courseName;
    private String focusCycleDates;

    public MainLayoutView(String courseName, String focusCycleDates) {
        this.courseName = (courseName != null && !courseName.isEmpty()) ? courseName : "oop";
        this.focusCycleDates = (focusCycleDates != null && !focusCycleDates.isEmpty()) ? focusCycleDates : "Nov 2, 2026 - Nov 26, 2026";

        this.getStyleClass().add("main-layout-root");

        // 1. Sidebar View Setup
        sidebarView = new SidebarView(
            this.courseName,
            this.focusCycleDates,
            this::showMissionsView,
            this::showProfileView
        );
        this.setLeft(sidebarView);

        // 2. Right Side Layout (Top Header + Content Area)
        VBox rightLayout = new VBox();
        HBox.setHgrow(rightLayout, Priority.ALWAYS);

        // Top Navigation / Breadcrumb Header
        HBox topBar = new HBox();
        topBar.getStyleClass().add("main-top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);

        breadcrumbLabel = new Label("ASCEND  >  MISSIONS");
        breadcrumbLabel.getStyleClass().add("main-breadcrumb-text");

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        newMissionBtn = new Button("+ New Mission");
        newMissionBtn.getStyleClass().add("gold-action-button");
        newMissionBtn.setOnAction(e -> openCreateMissionPage());

        topBar.getChildren().addAll(breadcrumbLabel, headerSpacer, newMissionBtn);

        // Main Content Area Container
        centerContainer = new VBox();
        VBox.setVgrow(centerContainer, Priority.ALWAYS);

        rightLayout.getChildren().addAll(topBar, centerContainer);
        this.setCenter(rightLayout);

        // Default to Missions View on initial load
        showMissionsView();
    }

    public void showMissionsView() {
    breadcrumbLabel.setText("ASCEND  >  MISSIONS");
    sidebarView.setActiveTab(true);
    
    // Show top-right button on Missions view
    newMissionBtn.setVisible(true);
    newMissionBtn.setManaged(true);

    MissionsView missionsView = new MissionsView(courseName, this::openCreateMissionPage);
    centerContainer.getChildren().setAll(missionsView);
}

public void showProfileView() {
    breadcrumbLabel.setText("ASCEND  >  PROFILE");
    sidebarView.setActiveTab(false);
    
    // Hide top-right button on Profile view
    newMissionBtn.setVisible(false);
    newMissionBtn.setManaged(false);

    ProfileMainView profileView = new ProfileMainView();
    centerContainer.getChildren().setAll(profileView);
}
    private void openCreateMissionPage() {
        System.out.println("Navigating to Create New Mission Page...");
        // TODO: Route to CreateMissionView when created
    }

    public MainLayoutView() {
    this("oop", "Nov 2, 2026 - Nov 26, 2026");
    }

}