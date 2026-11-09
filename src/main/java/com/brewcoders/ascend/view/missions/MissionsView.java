package com.brewcoders.ascend.view.missions;

import com.brewcoders.ascend.controller.MissionManager;
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
import javafx.scene.shape.Rectangle;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MissionsView extends ScrollPane {

    private final Runnable onCreateMissionClicked;
    private VBox missionsContainer = new VBox(12);
    private VBox emptyStateBox = new VBox(14);
    private VBox mainCardBox = new VBox(); // Outer yellow/cream container box
    private Label pendingCountLabel = new Label("0 Pending missions");

    // Dynamic Header Components
    private ImageView avatarView = new ImageView();
    private Label avatarName = new Label("The Noob");
    private Label totalPointsValueLabel = new Label("TOTAL POINTS : 0");
    private Label nextRankLabel = new Label("NEXT RANK: THE NIGHT CODER");
    private Label pointsCountLabel = new Label("0 / 1,000 points");
    private ProgressBar progressBar = new ProgressBar(0.0);

    public MissionsView(String courseName, Runnable onCreateMissionClicked) {

        this.setBackground(Background.EMPTY);
        this.setStyle("-fx-background-color: transparent;");
        
        this.onCreateMissionClicked = onCreateMissionClicked;

        this.setFitToWidth(true);
        this.setFitToHeight(false);
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
        avatarFrame.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #453018; " +
            "-fx-border-width: 2.5px; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-radius: 12px;"
        );

        // Avatar Image Configuration & Clipping
        avatarView.setFitWidth(70);
        avatarView.setFitHeight(70);
        avatarView.setPreserveRatio(false);

        Rectangle clip = new Rectangle(70, 70);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        avatarView.setClip(clip);

        avatarFrame.getChildren().add(avatarView);

        VBox avatarInfo = new VBox(3);
        avatarInfo.setAlignment(Pos.CENTER_LEFT);
        avatarInfo.setMinWidth(0);
        avatarInfo.setMaxWidth(Double.MAX_VALUE);
        avatarInfo.setFillWidth(true);

        HBox.setHgrow(avatarInfo, Priority.ALWAYS);

        Label currentAvatarBadge = new Label("CURRENT AVATAR");
        currentAvatarBadge.getStyleClass().add("missions-avatar-badge");

        avatarName.getStyleClass().add("missions-avatar-name");
        avatarName.setWrapText(true);
        avatarName.setMinWidth(0);
        avatarName.setMaxWidth(Double.MAX_VALUE);
        avatarName.setTextOverrun(javafx.scene.control.OverrunStyle.CLIP);

        avatarName.maxWidthProperty().bind(avatarInfo.widthProperty());

        totalPointsValueLabel.getStyleClass().add("missions-total-points");
        totalPointsValueLabel.setMinWidth(0);
        totalPointsValueLabel.setMaxWidth(Double.MAX_VALUE);

        avatarInfo.getChildren().addAll(currentAvatarBadge, avatarName, totalPointsValueLabel);

        VBox progressBox = new VBox(6);
        progressBox.setAlignment(Pos.CENTER_LEFT);
        progressBox.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(progressBox, Priority.ALWAYS);

        BorderPane progressHeader = new BorderPane();
        progressHeader.setMaxWidth(Double.MAX_VALUE);

        nextRankLabel.getStyleClass().add("missions-next-rank");

        pointsCountLabel.getStyleClass().add("missions-points-count");

        progressHeader.setLeft(nextRankLabel);
        progressHeader.setRight(pointsCountLabel);

        // Styled Progress Bar with Green Fill (-fx-accent: #6bc832)
        progressBar.getStyleClass().add("missions-progress-bar");
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(18);
        progressBar.setStyle(
            "-fx-accent: #6bc832; " +                   // Retro bright green fill bar
            "-fx-control-inner-background: #453018; " +  // Dark brown background track
            "-fx-border-color: #453018; " +
            "-fx-border-width: 2px;"
        );

        progressBox.getChildren().addAll(progressHeader, progressBar);

        avatarCard.getChildren().setAll(avatarFrame, avatarInfo, progressBox);

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

        emptyCreateBtn.setOnAction(e -> {
            if (this.onCreateMissionClicked != null) {
                this.onCreateMissionClicked.run();
            }
        });

        emptyStateBox.getChildren().addAll(noMissionsText, createSubText, emptyCreateBtn);

        // Setup missions container
        this.missionsContainer.setSpacing(12);
        this.missionsContainer.setMaxWidth(Double.MAX_VALUE);

        missionsSection.getChildren().addAll(sectionHeader, mainCardBox);

        contentContainer.getChildren().addAll(courseHeader, avatarCard, missionsSection);
        this.setContent(contentContainer);

        // Initial view refresh from MissionManager
        refreshMissionsList();
    }

    public MissionsView(String courseName) {
        this(courseName, null);
    }

    public void addMissionCard(Mission mission) {
        if (mission == null) return;

        // Register new active mission in MissionManager store
        MissionManager.getInstance().addMission(mission);
        
        // Refresh active list view
        refreshMissionsList();
    }

    public void refreshMissionsList() {
        this.missionsContainer.getChildren().clear();
        List<Mission> activeMissions = MissionManager.getInstance().getActiveMissions();
        
        // Sort to nearest due date 
        activeMissions.sort(Comparator.comparing(
            Mission::getDueDate,
            Comparator.nullsLast(Comparator.naturalOrder())
        ));

        // 1. Update Avatar Banner & Progress
        updateAvatarProgress();

        // 2. Update Active Missions List
        int count = activeMissions.size();
        pendingCountLabel.setText(count + " Pending missions");

        if (count == 0) {
            mainCardBox.getChildren().clear();
            mainCardBox.getChildren().add(emptyStateBox);
        } else {
            mainCardBox.getChildren().clear();
            mainCardBox.getChildren().add(this.missionsContainer);

            for (Mission mission : activeMissions) {
                this.missionsContainer.getChildren().add(createMissionRowCard(mission));
            }
        }
    }

    private void updateAvatarProgress() {
        int totalPoints = MissionManager.getInstance().getTotalPoints();
        totalPointsValueLabel.setText("TOTAL POINTS : " + totalPoints);

        String imagePath = "/images/avatars/avatar_noob.jpg";
        String rankTitle = "The Noob";
        String nextRankTitle = "NEXT RANK: THE NIGHT CODER";
        int prevThreshold = 0;
        int nextThreshold = 1000;

        if (totalPoints >= 3000) {
            imagePath = "/images/avatars/avatar_wildcard.jpg";
            rankTitle = "The Wildcard";
            nextRankTitle = "MAX RANK REACHED!";
            prevThreshold = 3000;
            nextThreshold = 3000;
        } else if (totalPoints >= 2000) {
            imagePath = "/images/avatars/avatar_grinder.jpg";
            rankTitle = "The Grinder";
            nextRankTitle = "NEXT RANK: THE WILDCARD";
            prevThreshold = 2000;
            nextThreshold = 3000;
        } else if (totalPoints >= 1000) {
            imagePath = "/images/avatars/avatar_nightCoder.jpg";
            rankTitle = "The Night Coder";
            nextRankTitle = "NEXT RANK: THE GRINDER";
            prevThreshold = 1000;
            nextThreshold = 2000;
        }

        try {
            avatarView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.err.println("Could not load avatar image at path: " + imagePath);
        }

        avatarName.setText(rankTitle);
        nextRankLabel.setText(nextRankTitle);

        if (prevThreshold == nextThreshold) {
            pointsCountLabel.setText(String.format("%,d points", totalPoints));
            progressBar.setProgress(1.0);
        } else {
            pointsCountLabel.setText(String.format("%,d / %,d points", totalPoints, nextThreshold));
            double progress = (double) (totalPoints - prevThreshold) / (nextThreshold - prevThreshold);
            progressBar.setProgress(Math.min(1.0, Math.max(0.0, progress)));
        }
    }

    private HBox createMissionRowCard(Mission mission) {
        // Inner Card Container: Lighter cream fill (#fff3cf)
        HBox card = new HBox(18);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setMinHeight(Region.USE_PREF_SIZE);
        card.setStyle(
            "-fx-background-color: #fff3cf; " +
            "-fx-border-width: 0px;"
        );

        // 1. Date Badge (solid blue box with white dot)
        HBox dateBox = new HBox(10);
        dateBox.setPrefHeight(42);
        dateBox.setMinWidth(Region.USE_PREF_SIZE);
        dateBox.setPadding(new Insets(0, 12, 0, 12));
        dateBox.setAlignment(Pos.CENTER);
        dateBox.setStyle(
            "-fx-background-color: #0d9edb; " +
            "-fx-border-color: #453018; " +
            "-fx-border-width: 2.5px;"
        );

        StackPane whiteCircle = new StackPane();
        whiteCircle.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 50%;");
        whiteCircle.setMinSize(14, 14);
        whiteCircle.setMaxSize(14, 14);

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
        Label dateLabel = new Label(mission.getDueDate() != null ? mission.getDueDate().format(dateFmt) : "");
        dateLabel.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 10px; -fx-text-fill: #ffffff;");
        
        dateLabel.setMinWidth(Region.USE_PREF_SIZE);
        dateLabel.setWrapText(false);
    
        dateBox.getChildren().addAll(whiteCircle, dateLabel);

        // 2. Mission Info Column
        VBox infoBox = new VBox(6);
        infoBox.setMinWidth(0);
        infoBox.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(infoBox, Priority.ALWAYS);
                
        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.setMinWidth(0);
        titleRow.setMaxWidth(Double.MAX_VALUE);

        Label nameLabel = new Label(mission.getName());
        nameLabel.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 13px; -fx-text-fill: #1f2d18;");
        nameLabel.setWrapText(true);
        nameLabel.setMinWidth(0);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setTextOverrun(javafx.scene.control.OverrunStyle.CLIP);

        Label diffBadge = new Label(mission.getDifficulty());
        diffBadge.setPadding(new Insets(3, 7, 3, 7));
        diffBadge.setMinWidth(Region.USE_PREF_SIZE);
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
        descLabel.setWrapText(true);
        descLabel.setMinWidth(0);
        descLabel.setMaxWidth(Double.MAX_VALUE);
        descLabel.setTextOverrun(javafx.scene.control.OverrunStyle.CLIP);

        descLabel.maxWidthProperty().bind(infoBox.widthProperty());

        Label catLabel = new Label(mission.getCategory());
        catLabel.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 10px; -fx-text-fill: #006666;");
        catLabel.setWrapText(true);
        catLabel.setMinWidth(0);
        catLabel.setMaxWidth(Double.MAX_VALUE);

        infoBox.getChildren().addAll(titleRow, descLabel, catLabel);

        titleRow.prefWidthProperty().bind(infoBox.widthProperty());

        // 3. Reward Banner & Point Count
        VBox rewardBox = new VBox(3);
        rewardBox.setAlignment(Pos.CENTER_RIGHT);
        rewardBox.setMinWidth(Region.USE_PREF_SIZE);

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
        completeBtn.setMinWidth(Region.USE_PREF_SIZE);

        completeBtn.setOnAction(e -> {
            // Update status in shared store and refresh current view
            MissionManager.getInstance().completeMission(mission);
            refreshMissionsList();
        });

        HBox.setHgrow(infoBox, Priority.ALWAYS);

        card.getChildren().setAll(dateBox, infoBox, rewardBox, completeBtn);

        return card;

    }
}