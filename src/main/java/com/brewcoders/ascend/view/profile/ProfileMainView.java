package com.brewcoders.ascend.view.profile;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.Node;

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
        HBox tabsRow = new HBox(16);
        
        personalInfoBtn = createTabButton("• Personal Information", true);
        historyBtn = createTabButton("• History", false);
        achievementsBtn = createTabButton("• Achievements", false);
        
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

    private List<Button> tabButtons = new ArrayList<>();

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
        // 1. Reset 'active' class on all tab buttons
        for (Button btn : tabButtons) {
            btn.getStyleClass().remove("active");
        }
        
        // 2. Mark clicked button as active
        selectedButton.getStyleClass().add("active");
        
        // 3. Swap the content area view
        tabContentArea.getChildren().setAll(newContentView);
    }
}