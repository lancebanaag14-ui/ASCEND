package com.brewcoders.ascend.view.profile;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class ProfileMainView extends BorderPane {

    private final StackPane tabContentArea;
    private final Button personalInfoBtn;
    private final Button historyBtn;
    private final Button achievementsBtn;
    private List<Button> tabButtons = new ArrayList<>();

    public ProfileMainView() {

        this.getStyleClass().add("profile-root");

        VBox contentContainer = new VBox(20);
        contentContainer.setPadding(new Insets(70, 80, 80, 80));

        // Apply Pixel Grid Pattern to contentContainer
        ImagePattern checkerboardPattern = createPixelGridPattern();
        Background gridBackground = new Background(new BackgroundFill(checkerboardPattern, CornerRadii.EMPTY, Insets.EMPTY));
        contentContainer.setBackground(gridBackground);

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

    private ImagePattern createPixelGridPattern() {
        int size = 16;
        WritableImage image = new WritableImage(size, size);

        // Retro green checkerboard colors
        Color color1 = Color.web("#a3d95b");
        Color color2 = Color.web("#8ecb45");

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                boolean isEven = (x / 8 + y / 8) % 2 == 0;
                image.getPixelWriter().setColor(x, y, isEven ? color1 : color2);
            }
        }
        return new ImagePattern(image, 0, 0, size, size, false);
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