package com.brewcoders.ascend.view.navigation;

import com.brewcoders.ascend.view.missions.CreateMissionModal;
import com.brewcoders.ascend.view.missions.MissionsView;
import com.brewcoders.ascend.view.profile.ProfileMainView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

public class MainLayoutView extends BorderPane {

    private SidebarView sidebarView;
    private Label breadcrumbLabel;
    private Button newMissionBtn;
    private VBox centerContainer;
    private MissionsView missionsView;
    private ProfileMainView profileMainView;

    private String courseName;
    private String focusCycleDates;

    public MainLayoutView(String courseName, String focusCycleDates) {
        this.courseName = (courseName != null && !courseName.isEmpty()) ? courseName : "oop";
        this.focusCycleDates = (focusCycleDates != null && !focusCycleDates.isEmpty()) ? focusCycleDates : "11/02/2026 - 11/26/2026";

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

        // Apply Pixel Grid Background Pattern
        ImagePattern checkerboardPattern = createPixelGridPattern();
        Background gridBackground = new Background(new BackgroundFill(checkerboardPattern, CornerRadii.EMPTY, Insets.EMPTY));
        centerContainer.setBackground(gridBackground);

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

        if (this.missionsView == null) {
            this.missionsView = new MissionsView(courseName, this::openCreateMissionPage);
        } else {
            this.missionsView.refreshMissionsList();
        }
        centerContainer.getChildren().setAll(this.missionsView);
    }

    public void showProfileView() {
        breadcrumbLabel.setText("ASCEND  >  PROFILE");
        sidebarView.setActiveTab(false);
        
        // Hide top-right button on Profile view
        newMissionBtn.setVisible(false);
        newMissionBtn.setManaged(false);

        if (this.profileMainView == null) {
            this.profileMainView = new ProfileMainView();
        }
        
        // Trigger explicit refresh for username and point stats from MissionManager
        this.profileMainView.refreshData();
        centerContainer.getChildren().setAll(this.profileMainView);
    }

    private void openCreateMissionPage() {
        Stage ownerStage = (Stage) this.getScene().getWindow();

        java.time.LocalDate cycleStart = sidebarView.getFocusCycleStart();
        java.time.LocalDate cycleEnd = sidebarView.getFocusCycleEnd();

        // Pass the callback to handle mission creation
        CreateMissionModal.display(ownerStage, cycleStart, cycleEnd, newMission -> {
            if (this.missionsView != null && newMission != null) {
                this.missionsView.addMissionCard(newMission);
            }
        });
    }

    private ImagePattern createPixelGridPattern() {
        int tileSize = 20; 
        Canvas canvas = new Canvas(tileSize * 2, tileSize * 2);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Color c1 = Color.web("#b4e380"); // Light background square
        Color c2 = Color.web("#abe076"); // Horizontal/Vertical band tint
        Color c3 = Color.web("#abe076"); // Horizontal/Vertical band tint
        Color c4 = Color.web("#a3da6f"); // Dark intersection square

        // Top-left
        gc.setFill(c1);
        gc.fillRect(0, 0, tileSize, tileSize);

        // Top-right
        gc.setFill(c2);
        gc.fillRect(tileSize, 0, tileSize, tileSize);

        // Bottom-left
        gc.setFill(c3);
        gc.fillRect(0, tileSize, tileSize, tileSize);

        // Bottom-right (Intersection)
        gc.setFill(c4);
        gc.fillRect(tileSize, tileSize, tileSize, tileSize);

        Image patternImage = canvas.snapshot(null, null);
        return new ImagePattern(patternImage, 0, 0, tileSize * 2, tileSize * 2, false);
    }

    public MainLayoutView() {
        this("oop", "11/02/2026 - 11/26/2026");
    }
}