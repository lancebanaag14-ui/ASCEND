package com.brewcoders.ascend.view.missions;

import com.brewcoders.ascend.model.Mission;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MissionsView extends ScrollPane {

    private final Runnable onCreateMissionClicked;
    private VBox missionsContainer = new VBox(12);
    private VBox emptyStateBox = new VBox(14);
    private VBox mainCardBox = new VBox(); // Outer yellow/cream container box
    private Label pendingCountLabel = new Label("0 Pending missions");
    private int pendingCount = 0;

    public MissionsView(String courseName, Runnable onCreateMissionClicked) {

        this.setBackground(Background.EMPTY);
        this.setStyle("-fx-background-color: transparent;");
        
        this.onCreateMissionClicked = onCreateMissionClicked;

        this.setFitToWidth(true);
        this.setFitToHeight(true);
        this.getStyleClass().add("missions-scroll-pane");

        VBox contentContainer = new VBox(24);
        contentContainer.setPadding(new Insets(70, 80, 80, 80));

        // --- 1. Course Header Section ---
        VBox courseHeader = new VBox(15);
        courseHeader.setAlignment(Pos.TOP_LEFT);
        courseHeader.setMaxWidth(Double.MAX_VALUE);

        Label badgeLabel = new Label("CURRENT COURSE");
        badgeLabel.getStyleClass().add("missions-course-badge");

        Label courseTitle = new Label(courseName != null && !courseName.isEmpty() ? courseName : "oop");
        courseTitle.getStyleClass().add("missions-course-title");
        courseTitle.setWrapText(true);

        Label courseSubtext = new Label("Complete missions before their due dates to earn points.");
        courseSubtext.getStyleClass().add("missions-course-subtext");

        courseHeader.getChildren().addAll(badgeLabel, courseTitle, courseSubtext);

        // --- 2. Avatar Progress Card ---
        HBox avatarCard = new HBox(16);
        avatarCard.getStyleClass().add("missions-avatar-card");
        avatarCard.setAlignment(Pos.CENTER_LEFT);
        avatarCard.setMaxWidth(Double.MAX_VALUE);
        avatarCard.setPadding(new Insets(16, 20, 16, 16));

        StackPane avatarFrame = new StackPane();
        avatarFrame.getStyleClass().add("missions-avatar-frame");
        avatarFrame.setMinWidth(76);
        avatarFrame.setMinHeight(76);
        avatarFrame.setPrefSize(76, 76);
        avatarFrame.setMaxSize(76, 76);

        ImageView avatarView = new ImageView();
        try {
            Image avatarImg = new Image(getClass().getResourceAsStream("/images/avatars/avatar_noob.png"));
            avatarView.setImage(avatarImg);
            avatarView.setFitWidth(68);
            avatarView.setFitHeight(68);
            avatarView.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Could not load avatar image");
        }
        avatarFrame.getChildren().add(avatarView);

        VBox avatarInfo = new VBox(3);
        avatarInfo.setAlignment(Pos.CENTER_LEFT);
        avatarInfo.setMinWidth(150);

        Label currentAvatarBadge = new Label("CURRENT AVATAR");
        currentAvatarBadge.getStyleClass().add("missions-avatar-badge");

        Label avatarName = new Label("The Noob");
        avatarName.getStyleClass().add("missions-avatar-name");

        Label totalPointsLabel = new Label("TOTAL POINTS :");
        totalPointsLabel.getStyleClass().add("missions-total-points");

        avatarInfo.getChildren().addAll(currentAvatarBadge, avatarName, totalPointsLabel);

        VBox progressBox = new VBox(6);
        progressBox.setAlignment(Pos.CENTER_LEFT);
        progressBox.setPrefWidth(1000);
        progressBox.setMaxWidth(1000);

        BorderPane progressHeader = new BorderPane();
        progressHeader.setMaxWidth(Double.MAX_VALUE);

        Label nextRankLabel = new Label("NEXT RANK: THE NIGHT CODER");
        nextRankLabel.getStyleClass().add("missions-next-rank");

        Label pointsCountLabel = new Label("0 / 1,000 points");
        pointsCountLabel.getStyleClass().add("missions-points-count");

        progressHeader.setLeft(nextRankLabel);
        progressHeader.setRight(pointsCountLabel);

        ProgressBar progressBar = new ProgressBar(0.0);
        progressBar.getStyleClass().add("missions-progress-bar");
        progressBar.setMaxWidth(Double.MAX_VALUE);

        progressBox.getChildren().addAll(progressHeader, progressBar);

        Region cardSpacer = new Region();
        HBox.setHgrow(cardSpacer, Priority.ALWAYS);

        avatarCard.getChildren().setAll(avatarFrame, avatarInfo, cardSpacer, progressBox);

        // --- 3. Active Missions Section ---
        VBox missionsSection = new VBox(14);
        missionsSection.setMaxWidth(Double.MAX_VALUE);

        HBox sectionHeader = new HBox();
        sectionHeader.setAlignment(Pos.BOTTOM_LEFT);
        sectionHeader.setMaxWidth(Double.MAX_VALUE);

        VBox sectionTitleBox = new VBox(4);
        Label sectionTitle = new Label("Active Missions");
        sectionTitle.getStyleClass().add("missions-section-title");

        Label sectionSub = new Label("Sorted by nearest due date");
        sectionSub.getStyleClass().add("missions-section-sub");
        sectionTitleBox.getChildren().addAll(sectionTitle, sectionSub);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        pendingCountLabel.getStyleClass().add("missions-pending-count");

        sectionHeader.getChildren().addAll(sectionTitleBox, headerSpacer, pendingCountLabel);

        // Configure main yellow retro container box
        mainCardBox.setStyle(
            "-fx-background-color: #fce8ac; " +
            "-fx-border-color: #453018; " +
            "-fx-border-width: 3px; " +
            "-fx-padding: 24;"
        );
        mainCardBox.setMaxWidth(Double.MAX_VALUE);

        // Configure Empty State View
        emptyStateBox.setAlignment(Pos.CENTER);
        emptyStateBox.setPadding(new Insets(40, 20, 40, 20));

        StackPane emptyIconFrame = new StackPane();
        emptyIconFrame.setStyle("-fx-background-color: #72bf36; -fx-border-color: #453018; -fx-border-width: 2px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        emptyIconFrame.setMinSize(56, 56);
        emptyIconFrame.setMaxSize(56, 56);

        Label circleDot = new Label("●");
        circleDot.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px;");
        emptyIconFrame.getChildren().add(circleDot);

        Label noMissionsText = new Label("No active missions");
        noMissionsText.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 13px; -fx-text-fill: #1f2d18;");

        Label createSubText = new Label("Create a mission to begin earning points");
        createSubText.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 10px; -fx-text-fill: #555555;");

        Button emptyCreateBtn = new Button("+ Create Mission");
        emptyCreateBtn.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 10px; " +
            "-fx-background-color: #f7ce68; " +
            "-fx-text-fill: #1f2d18; " +
            "-fx-border-color: #453018; " +
            "-fx-border-width: 2px; " +
            "-fx-padding: 10 18; " +
            "-fx-cursor: hand;"
        );

        // Call the Runnable here to clear the unused field warning:
        emptyCreateBtn.setOnAction(e -> {
            if (this.onCreateMissionClicked != null) {
                this.onCreateMissionClicked.run();
            }
        });

        emptyStateBox.getChildren().addAll(emptyIconFrame, noMissionsText, createSubText, emptyCreateBtn);

        // Setup missions container
        this.missionsContainer.setSpacing(12);
        this.missionsContainer.setMaxWidth(Double.MAX_VALUE);

        // Default layout: empty state box inside outer main card box
        mainCardBox.getChildren().add(emptyStateBox);

        missionsSection.getChildren().addAll(sectionHeader, mainCardBox);

        contentContainer.getChildren().addAll(courseHeader, avatarCard, missionsSection);
        this.setContent(contentContainer);
    }

        public MissionsView(String courseName) {
            this(courseName, null);
        }

    public void addMissionCard(Mission mission) {
        if (mission == null) return;

        // Hide empty state on first card creation
        if (pendingCount == 0) {
            mainCardBox.getChildren().clear();
            mainCardBox.getChildren().add(this.missionsContainer);
        }

        // Inner Card Container: Lighter cream fill (#fff3cf) with NO inner border lines
        HBox card = new HBox(18);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
            "-fx-background-color: #fff3cf; " +
            "-fx-border-width: 0px;"
        );

        // 1. Date Badge (solid blue box with white dot)
        HBox dateBox = new HBox(10);
        dateBox.setAlignment(Pos.CENTER);
        dateBox.setPrefHeight(46);
        dateBox.setPadding(new Insets(0, 16, 0, 16));
        dateBox.setStyle(
            "-fx-background-color: #0d9edb; " +
            "-fx-border-color: #453018; " +
            "-fx-border-width: 2.5px;"
        );

        StackPane whiteCircle = new StackPane();
        whiteCircle.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 50%;");
        whiteCircle.setMinSize(14, 14);
        whiteCircle.setMaxSize(14, 14);

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        Label dateLabel = new Label(mission.getDueDate() != null ? mission.getDueDate().format(dateFmt) : "");
        dateLabel.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 10px; -fx-text-fill: #ffffff;");
        dateBox.getChildren().addAll(whiteCircle, dateLabel);

        // 2. Mission Info Column
        VBox infoBox = new VBox(6);
        
        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(mission.getName());
        nameLabel.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 13px; -fx-text-fill: #1f2d18;");

        Label diffBadge = new Label(mission.getDifficulty());
        diffBadge.setPadding(new Insets(3, 7, 3, 7));
        diffBadge.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 8px; " +
            "-fx-background-color: #f7be3b; " +
            "-fx-border-color: #453018; " +
            "-fx-border-width: 1.5px; " +
            "-fx-text-fill: #1f2d18;"
        );
        titleRow.getChildren().addAll(nameLabel, diffBadge);

        Label descLabel = new Label(mission.getDescription());
        descLabel.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 10px; -fx-text-fill: #444444;");

        Label catLabel = new Label(mission.getCategory());
        catLabel.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 10px; -fx-text-fill: #006666;");

        infoBox.getChildren().addAll(titleRow, descLabel, catLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 3. Reward Banner & Point Count
        VBox rewardBox = new VBox(4);
        rewardBox.setAlignment(Pos.CENTER_RIGHT);

        Label rewardTitle = new Label("REWARD");
        rewardTitle.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 8px; -fx-text-fill: #453018;");

        Label rewardValue = new Label("+ " + mission.getPoints());
        rewardValue.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 12px; -fx-text-fill: #1f2d18;");
        rewardBox.getChildren().addAll(rewardTitle, rewardValue);

        // 4. Complete Action Button
        Button completeBtn = new Button("Complete");
        completeBtn.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 12px; " +
            "-fx-background-color: #6bc832; " +
            "-fx-text-fill: #ffffff; " +
            "-fx-border-color: #453018; " +
            "-fx-border-width: 2.5px; " +
            "-fx-padding: 12 20; " +
            "-fx-cursor: hand;"
        );

        completeBtn.setOnAction(e -> {
            this.missionsContainer.getChildren().remove(card);
            pendingCount--;
            pendingCountLabel.setText(pendingCount + " Pending missions");

            if (pendingCount == 0) {
                mainCardBox.getChildren().clear();
                mainCardBox.getChildren().add(emptyStateBox);
            }
        });

        card.getChildren().addAll(dateBox, infoBox, spacer, rewardBox, completeBtn);

        this.missionsContainer.getChildren().add(card);
        pendingCount++;
        pendingCountLabel.setText(pendingCount + " Pending missions");
    }
}