package com.brewcoders.ascend.view.missions;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        contentContainer.setMaxWidth(Double.MAX_VALUE);// Align items left inside the container


        // --- 1. Course Header Section ---
        VBox courseHeader = new VBox(6);
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
        HBox avatarCard = new HBox(20);
        avatarCard.getStyleClass().add("missions-avatar-card");
        avatarCard.setAlignment(Pos.CENTER_LEFT);
        avatarCard.setMaxWidth(Double.MAX_VALUE);

        // Left Side: Rounded Avatar Frame
        ImageView avatarView = new ImageView();
        try {
            Image avatarImg = new Image(getClass().getResourceAsStream("/images/avatars/avatar_noob.png"));
            avatarView.setImage(avatarImg);
            avatarView.setFitWidth(76);
            avatarView.setFitHeight(76);
            avatarView.setPreserveRatio(true);
        } catch (Exception e) {
            try {
                Image avatarImg = new Image(getClass().getResourceAsStream("/images/avatars/avatar1.png"));
                avatarView.setImage(avatarImg);
                avatarView.setFitWidth(76);
                avatarView.setFitHeight(76);
                avatarView.setPreserveRatio(true);
            } catch (Exception ex) {
                System.err.println("Could not load avatar image");
            }
        }

        StackPane avatarFrame = new StackPane(avatarView);
        avatarFrame.getStyleClass().add("missions-avatar-frame");

        // Middle Column: Title & Total Points
        VBox middleInfoBox = new VBox(6);
        middleInfoBox.setAlignment(Pos.CENTER_LEFT);

        Label avatarBadge = new Label("CURRENT AVATAR");
        avatarBadge.getStyleClass().add("missions-avatar-badge");

        Label rankName = new Label("The Noob");
        rankName.getStyleClass().add("missions-rank-name");

        Label totalPointsLabel = new Label("TOTAL POINTS :");
        totalPointsLabel.getStyleClass().add("missions-total-points-label");

        middleInfoBox.getChildren().addAll(avatarBadge, rankName, totalPointsLabel);

        // Right Column: Next Rank, Points Count, and Progress Bar
        VBox rightProgressBox = new VBox(6);
        rightProgressBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(rightProgressBox, Priority.ALWAYS);

        HBox progressTextRow = new HBox();
        progressTextRow.setAlignment(Pos.BOTTOM_LEFT);

        Label nextRankLabel = new Label("NEXT RANK: THE NIGHT CODER");
        nextRankLabel.getStyleClass().add("missions-next-rank-text");

        Region textSpacer = new Region();
        HBox.setHgrow(textSpacer, Priority.ALWAYS);

        Label pointsCount = new Label("0 / 1,000 points");
        pointsCount.getStyleClass().add("missions-points-count");

        progressTextRow.getChildren().addAll(nextRankLabel, textSpacer, pointsCount);

        ProgressBar xpProgressBar = new ProgressBar(0.0);
        xpProgressBar.getStyleClass().add("missions-xp-bar");
        xpProgressBar.setMaxWidth(Double.MAX_VALUE);

        rightProgressBox.getChildren().addAll(progressTextRow, xpProgressBar);

        // Assemble card horizontal layout
        avatarCard.getChildren().addAll(avatarFrame, middleInfoBox, rightProgressBox);

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