package com.brewcoders.ascend.view.profile;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class ProfileMainView extends BorderPane {

    private final StackPane tabContentArea;
    private final Button personalInfoBtn;
    private final Button historyBtn;
    private final Button achievementsBtn;

    public ProfileMainView() {
        this.getStyleClass().add("profile-root");

        VBox contentContainer = new VBox(20);
        contentContainer.setPadding(new Insets(70, 80, 80, 80));


        // 1. Header Section
        Label badgePill = new Label("USER PROFILE");
        badgePill.getStyleClass().add("profile-badge-pill");

        HBox pillWrapper = new HBox(badgePill);
        pillWrapper.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Profile");
        title.getStyleClass().add("profile-main-title");

        Label subtitle = new Label("View your information, current-course history, and achievements.");
        subtitle.getStyleClass().add("profile-subtitle");

        VBox titleAndSubtitleGroup = new VBox(15);
        titleAndSubtitleGroup.getChildren().addAll(title, subtitle);

        VBox headerBox = new VBox(32);
        headerBox.getChildren().addAll(pillWrapper, titleAndSubtitleGroup);

        // 2. Navigation Tabs
        HBox tabsRow = new HBox(12);
        
        personalInfoBtn = createTabButton("● Personal Information", true);
        historyBtn = createTabButton("● History", false);
        achievementsBtn = createTabButton("● Achievements", false);
        
        tabsRow.getChildren().addAll(personalInfoBtn, historyBtn, achievementsBtn);

        // 3. Dynamic Content Area (Loads default sub-view)
        tabContentArea = new StackPane();
        tabContentArea.getChildren().add(new PersonalInfoView());

        // Tab Switching Actions
        personalInfoBtn.setOnAction(e -> switchTab(personalInfoBtn, new PersonalInfoView()));
        historyBtn.setOnAction(e -> switchTab(historyBtn, new HistoryView()));
        achievementsBtn.setOnAction(e -> switchTab(achievementsBtn, new AchievementsView()));

        contentContainer.getChildren().addAll(headerBox, tabsRow, tabContentArea);

        // ScrollPane Wrapper for dynamic window scaling
        ScrollPane scrollPane = new ScrollPane(contentContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        this.setCenter(scrollPane);
    }

    private Button createTabButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.getStyleClass().add("profile-tab-btn");
        if (active) {
            btn.getStyleClass().add("profile-tab-btn-active");
        }
        return btn;
    }

    private void switchTab(Button selectedBtn, Pane view) {
        personalInfoBtn.getStyleClass().remove("profile-tab-btn-active");
        historyBtn.getStyleClass().remove("profile-tab-btn-active");
        achievementsBtn.getStyleClass().remove("profile-tab-btn-active");

        selectedBtn.getStyleClass().add("profile-tab-btn-active");
        tabContentArea.getChildren().setAll(view);
    }
}