package com.brewcoders.ascend.view.profile;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ProfileMainView extends BorderPane {

    private final StackPane tabContentArea;
    private final Button personalInfoBtn;
    private final Button historyBtn;
    private final Button achievementsBtn;
    private final List<Button> tabButtons = new ArrayList<>();
    private PersonalInfoView personalInfoView;

    public ProfileMainView() {

        // Use the exact same root style class as your main layout
        this.getStyleClass().add("main-content-area");

        VBox contentContainer = new VBox(20);
        contentContainer.setPadding(new Insets(30, 40, 40, 40));
        contentContainer.setMaxWidth(Double.MAX_VALUE);

        // Header Section
        Label badgePill = new Label("USER PROFILE");
        badgePill.getStyleClass().add("profile-badge-pill");

        HBox pillWrapper = new HBox(badgePill);
        pillWrapper.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Profile");
        title.getStyleClass().add("profile-main-title");

        Label subtitle = new Label("View your information, current-course history, and achievements.");
        subtitle.getStyleClass().add("profile-subtitle");
        subtitle.setWrapText(true);

        VBox titleAndSubtitleGroup = new VBox(15);
        titleAndSubtitleGroup.getChildren().addAll(title, subtitle);

        VBox headerBox = new VBox(32);
        headerBox.getChildren().addAll(pillWrapper, titleAndSubtitleGroup);

        // Navigation Tabs
        HBox tabsRow = new HBox(16);

        personalInfoBtn = createTabButton("• Personal Information", true);
        historyBtn = createTabButton("• History", false);
        achievementsBtn = createTabButton("• Achievements", false);

        tabsRow.getChildren().addAll(personalInfoBtn, historyBtn, achievementsBtn);

        // Initialize PersonalInfoView
        this.personalInfoView = new PersonalInfoView();

        // Dynamic Content Area
        tabContentArea = new StackPane();
        tabContentArea.getChildren().add(personalInfoView);
        VBox.setVgrow(tabContentArea, Priority.ALWAYS);

        // Tab Switching Actions
        personalInfoBtn.setOnAction(e -> {
            if (personalInfoView == null) {
                personalInfoView = new PersonalInfoView();
            } else {
                personalInfoView.refreshUserData();
            }
            switchTab(personalInfoBtn, personalInfoView);
        });

        historyBtn.setOnAction(e -> switchTab(historyBtn, new HistoryView()));
        achievementsBtn.setOnAction(e -> switchTab(achievementsBtn, new AchievementsView()));

        contentContainer.getChildren().addAll(headerBox, tabsRow, tabContentArea);

        // ScrollPane Setup with JavaFX internal viewport fixes
        ScrollPane scrollPane = new ScrollPane(contentContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("transparent-scroll-pane");

        this.setCenter(scrollPane);

        // Ensure user data is refreshed upon creation
        refreshData();
    }

    /**
     * Refreshes user details and point totals in PersonalInfoView.
     * Call this when navigating to ProfileMainView from MainLayoutView.
     */
    public void refreshData() {
        if (personalInfoView != null) {
            personalInfoView.refreshUserData();
        }
    }

    private Button createTabButton(String text, boolean isActive) {
        Button btn = new Button(text);
        btn.getStyleClass().add("profile-tab-button");

        if (isActive) {
            btn.getStyleClass().add("active");
        }

        tabButtons.add(btn);
        return btn;
    }

    private void switchTab(Button selectedButton, Node newContentView) {
        for (Button btn : tabButtons) {
            btn.getStyleClass().remove("active");
        }
        selectedButton.getStyleClass().add("active");
        tabContentArea.getChildren().setAll(newContentView);
    }
}