package com.brewcoders.ascend.view.missions;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MissionsView extends ScrollPane {

    private final Runnable onCreateMissionClicked;

    public MissionsView(String courseName, Runnable onCreateMissionClicked) {
        this.onCreateMissionClicked = onCreateMissionClicked;

        this.setFitToWidth(true);
        this.setFitToHeight(true);
        this.getStyleClass().add("missions-scroll-pane");

        // Inside MissionsView.java constructor:
        VBox contentContainer = new VBox(24);
        contentContainer.getStyleClass().add("missions-root");
        contentContainer.setMaxWidth(Double.MAX_VALUE);

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

        // 1. Avatar Icon Square (Fixed size, rounded corners)
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

        // 2. Avatar Info Column
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

        // 3. Right Progress Container 
        VBox progressBox = new VBox(6);
        progressBox.setAlignment(Pos.CENTER_LEFT);
        progressBox.setPrefWidth(1200); 
        progressBox.setMaxWidth(1200);

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

        // Assemble card 
        avatarCard.getChildren().setAll(avatarFrame, avatarInfo, cardSpacer, progressBox);

        // --- 3. Active Missions Section & Empty State Card ---
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

        Label pendingCount = new Label("0 Pending missions");
        pendingCount.getStyleClass().add("missions-pending-count");

        sectionHeader.getChildren().addAll(sectionTitleBox, headerSpacer, pendingCount);

        // Empty State Box
        VBox emptyCard = new VBox(14);
        emptyCard.getStyleClass().add("missions-empty-card");
        emptyCard.setAlignment(Pos.CENTER);
        emptyCard.setMaxWidth(Double.MAX_VALUE);

        StackPane emptyIconFrame = new StackPane();
        emptyIconFrame.getStyleClass().add("missions-empty-icon-frame");

        Region innerDot = new Region();
        innerDot.getStyleClass().add("missions-empty-icon-dot");
        emptyIconFrame.getChildren().add(innerDot);

        Label emptyTitle = new Label("No active missions");
        emptyTitle.getStyleClass().add("missions-empty-title");

        Label emptySub = new Label("Create a mission to begin earning points");
        emptySub.getStyleClass().add("missions-empty-sub");

        Button createBtn = new Button("+ Create Mission");
        createBtn.getStyleClass().add("gold-action-button");
        createBtn.setOnAction(e -> {
            if (this.onCreateMissionClicked != null) {
                this.onCreateMissionClicked.run();
            }
        });

        emptyCard.getChildren().addAll(emptyIconFrame, emptyTitle, emptySub, createBtn);
        missionsSection.getChildren().addAll(sectionHeader, emptyCard);

                contentContainer.getChildren().addAll(courseHeader, avatarCard, missionsSection);
                this.setContent(contentContainer);
            }
}