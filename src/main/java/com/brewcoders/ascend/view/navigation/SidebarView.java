package com.brewcoders.ascend.view.navigation;

import com.brewcoders.ascend.view.auth.LoginView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class SidebarView extends VBox {
    private java.time.LocalDate currentCycleStart;
    private java.time.LocalDate currentCycleEnd;
    private Button missionsBtn;
    private Button profileBtn;

    public SidebarView(String courseName, String focusCycleDates, Runnable onMissionsSelected, Runnable onProfileSelected){
        // 1. Dynamically parse incoming focus cycle dates
        parseAndSetCycleDates(focusCycleDates);

        this.setPrefWidth(260);
        this.setMinWidth(260);
        this.setMaxWidth(260);
        this.getStyleClass().add("sidebar-root");
        this.setAlignment(Pos.TOP_LEFT);
        this.setSpacing(20);

        // 1. Top Brand (Logo + ASCEND)
        HBox brandHeader = new HBox(12);
        brandHeader.setAlignment(Pos.CENTER);
        brandHeader.setMaxWidth(Double.MAX_VALUE);

        ImageView logoIcon = new ImageView();
        try {
            Image logoImg = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoIcon.setImage(logoImg);
            logoIcon.setFitWidth(32);
            logoIcon.setFitHeight(32);
            logoIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Could not load logo image from /images/logo.png");
        }

        StackPane logoBox = new StackPane(logoIcon);
        logoBox.getStyleClass().add("sidebar-logo-box");

        Label brandName = new Label("ASCEND");
        brandName.getStyleClass().add("sidebar-brand-text");

        brandHeader.getChildren().addAll(logoBox, brandName);

        // 2. Current Course Box
        VBox courseCard = new VBox(10);
        courseCard.getStyleClass().add("sidebar-course-card");
        courseCard.setMinWidth(0);
        courseCard.setMaxWidth(Double.MAX_VALUE);
        courseCard.setAlignment(Pos.CENTER);
        courseCard.setFillWidth(true);

        Label courseBadge = new Label("CURRENT COURSE");
        courseBadge.getStyleClass().add("sidebar-course-badge");
        courseBadge.setAlignment(Pos.CENTER);
        courseBadge.setMaxWidth(Double.MAX_VALUE);

        Label courseTitle = new Label(courseName != null && !courseName.isEmpty() ? courseName : "Course Title Here");
        courseTitle.getStyleClass().add("sidebar-course-title");
        courseTitle.setWrapText(true);
        courseTitle.setTextOverrun(javafx.scene.control.OverrunStyle.CLIP);
        courseTitle.setAlignment(Pos.CENTER);
        courseTitle.setTextAlignment(TextAlignment.CENTER);
        courseTitle.setMinWidth(0);
        courseTitle.setMaxWidth(Double.MAX_VALUE);
        
        String dateVal = (focusCycleDates != null && !focusCycleDates.isEmpty()) ? focusCycleDates : "11/04/2026 - 11/19/2026";
        Label courseDates = new Label("FOCUS CYCLE:\n" + dateVal);
        courseDates.getStyleClass().add("sidebar-course-dates");
        courseDates.setWrapText(true);
        courseDates.setAlignment(Pos.CENTER);
        courseDates.setMaxWidth(Double.MAX_VALUE);

        courseCard.getChildren().addAll(courseBadge, courseTitle, courseDates);

        // 3. Navigation Links Section
        VBox navBox = new VBox(12);
        Label navTitle = new Label("MAIN MENU");
        navTitle.getStyleClass().add("sidebar-menu-header");

        missionsBtn = new Button("Missions");
        missionsBtn.getStyleClass().add("sidebar-nav-btn-active");
        missionsBtn.setMaxWidth(Double.MAX_VALUE);

        profileBtn = new Button("Profile");
        profileBtn.getStyleClass().add("sidebar-nav-btn-inactive");
        profileBtn.setMaxWidth(Double.MAX_VALUE);

        // Selection Logic
        missionsBtn.setOnAction(e -> {
            setActiveTab(true);
            if (onMissionsSelected != null) onMissionsSelected.run();
        });

        profileBtn.setOnAction(e -> {
            setActiveTab(false);
            if (onProfileSelected != null) onProfileSelected.run();
        });

        navBox.getChildren().addAll(navTitle, missionsBtn, profileBtn);

        // 4. Back to Front Page Button (Configured to full width)
        Button backToFrontBtn = new Button("< Back to Front Page");
        backToFrontBtn.getStyleClass().add("sidebar-back-btn");
        backToFrontBtn.setMaxWidth(Double.MAX_VALUE);
        backToFrontBtn.setOnAction(e -> {
            if (this.getScene() != null) {
                this.getScene().setRoot(new LoginView());
            }
        });

        // Spacer to push footer to the very bottom
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // 5. Footer Text
        Label footerLabel = new Label("BY BREW CODERS SINCE 2026");
        footerLabel.getStyleClass().add("sidebar-footer-text");
        footerLabel.setTextAlignment(TextAlignment.CENTER);
        footerLabel.setAlignment(Pos.CENTER);
        footerLabel.setMaxWidth(Double.MAX_VALUE);

        HBox footerContainer = new HBox(footerLabel);
        footerContainer.setAlignment(Pos.CENTER);
        footerContainer.setMaxWidth(Double.MAX_VALUE);

        this.getChildren().addAll(brandHeader, courseCard, navBox, backToFrontBtn, spacer, footerLabel);
    }

    public void setActiveTab(boolean isMissions) {
        if (isMissions) {
            missionsBtn.getStyleClass().setAll("sidebar-nav-btn-active");
            profileBtn.getStyleClass().setAll("sidebar-nav-btn-inactive");
        } else {
            profileBtn.getStyleClass().setAll("sidebar-nav-btn-active");
            missionsBtn.getStyleClass().setAll("sidebar-nav-btn-inactive");
        }
    }

    // Method to parse date strings dynamically
    private void parseAndSetCycleDates(String rawDateString) {
        try {
            if (rawDateString != null && rawDateString.contains("-")) {
                String[] parts = rawDateString.split("-");
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
                
                this.currentCycleStart = java.time.LocalDate.parse(parts[0].trim(), formatter);
                this.currentCycleEnd = java.time.LocalDate.parse(parts[1].trim(), formatter);
                return;
            }
        } catch (Exception e) {
            // Fallback if format parsing fails
        }
        // Default fallback if string is empty or invalid
        this.currentCycleStart = java.time.LocalDate.now();
        this.currentCycleEnd = this.currentCycleStart.plusDays(28);
    }

    // Getters used by MainLayoutView
    public java.time.LocalDate getFocusCycleStart() {
        return this.currentCycleStart;
    }

    public java.time.LocalDate getFocusCycleEnd() {
        return this.currentCycleEnd;
    }
}