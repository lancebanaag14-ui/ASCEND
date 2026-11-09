package com.brewcoders.ascend.view.profile;

import com.brewcoders.ascend.controller.MissionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

public class AchievementsView extends VBox {

    // Achievement Data Model
    private static class Achievement {
        final String name;
        final int requiredPoints;
        final String imagePath;

        Achievement(String name, int requiredPoints, String imagePath) {
            this.name = name;
            this.requiredPoints = requiredPoints;
            this.imagePath = imagePath;
        }
    }

    public AchievementsView() {
        this.setSpacing(24);
        this.setAlignment(Pos.TOP_LEFT);

        // Fetch user total points dynamically from MissionManager
        int totalPoints = MissionManager.getInstance().getTotalPoints();

        // 1. Top Banner: Total Points in this Course
        HBox topBanner = new HBox();
        topBanner.getStyleClass().add("retro-points-banner");
        topBanner.setAlignment(Pos.CENTER_LEFT);
        topBanner.setPadding(new Insets(20, 28, 20, 28));
        topBanner.setStyle(
            "-fx-background-color: #167a47; " + 
            "-fx-border-color: #4a2810; " +     
            "-fx-border-width: 3px;"
        );

        Label bannerText = new Label("TOTAL POINTS IN THIS COURSE");
        bannerText.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 11px; " +
            "-fx-text-fill: #ffffff;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label pointsLabel = new Label(String.valueOf(totalPoints));
        pointsLabel.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 32px; " +
            "-fx-text-fill: #f7ce68;" 
        );

        topBanner.getChildren().addAll(bannerText, spacer, pointsLabel);

        // 2. Achievements Grid Row
        HBox cardsRow = new HBox(16);
        cardsRow.setAlignment(Pos.CENTER_LEFT);

        Achievement[] achievements = new Achievement[] {
            new Achievement("The Noob", 0, "/images/avatars/avatar_noob.jpg"),
            new Achievement("The Night Coder", 1000, "/images/avatars/avatar_nightCoder.jpg"),
            new Achievement("The Grinder", 2000, "/images/avatars/avatar_grinder.jpg"),
            new Achievement("The Wildcard", 3000, "/images/avatars/avatar_wildcard.jpg")
        };

        for (Achievement ach : achievements) {
            boolean isUnlocked = totalPoints >= ach.requiredPoints;
            VBox card = createAchievementCard(ach, isUnlocked);
            HBox.setHgrow(card, Priority.ALWAYS);
            cardsRow.getChildren().add(card);
        }

        this.getChildren().addAll(topBanner, cardsRow);
    }

    private VBox createAchievementCard(Achievement ach, boolean isUnlocked) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(24, 16, 24, 16));

        // Style differences for Unlocked vs Locked state
        if (isUnlocked) {
            card.setStyle(
                "-fx-background-color: #fce4b3; " + 
                "-fx-border-color: #4a2810; " +
                "-fx-border-width: 3px;"
            );
        } else {
            card.setStyle(
                "-fx-background-color: #cfd8bd; " + 
                "-fx-border-color: #4a2810; " +
                "-fx-border-width: 2.5px; " +
                "-fx-opacity: 0.85;"
            );
        }

        // Image Frame
        StackPane imageFrame = new StackPane();
        imageFrame.setPrefSize(120, 120);
        imageFrame.setMinSize(120, 120);
        imageFrame.setMaxSize(120, 120);
        imageFrame.setStyle(
            "-fx-background-color: " + (isUnlocked ? "#ffffff;" : "#e2e9d5;") +
            "-fx-border-color: #4a2810; " +
            "-fx-border-width: 2.5px; " +
            "-fx-background-radius: 16px; " +
            "-fx-border-radius: 16px;"
        );

        try {
            ImageView imgView = new ImageView(new Image(getClass().getResourceAsStream(ach.imagePath)));
            
            // Set dimensions to fill frame completely without aspect ratio padding
            imgView.setFitWidth(115);
            imgView.setFitHeight(115);
            imgView.setPreserveRatio(false);

            // Clip image corners to fit smoothly inside the rounded border frame
            Rectangle clip = new Rectangle(115, 115);
            clip.setArcWidth(24);
            clip.setArcHeight(24);
            imgView.setClip(clip);

            if (!isUnlocked) {
                imgView.setOpacity(0.55);
            }

            imageFrame.getChildren().add(imgView);
        } catch (Exception e) {
            // Fallback placeholder
        }

        // Status Label (UNLOCKED vs LOCKED)
        Label statusLabel = new Label(isUnlocked ? "UNLOCKED" : "LOCKED");
        statusLabel.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 8px; " +
            "-fx-text-fill: " + (isUnlocked ? "#7c4a16;" : "#6b7a59;")
        );

        // Achievement Title
        Label titleLabel = new Label(ach.name);
        titleLabel.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 13px; " +
            "-fx-text-fill: " + (isUnlocked ? "#3e2723;" : "#4b573a;")
        );

        // Points Subtitle
        String pointsText = ach.requiredPoints == 0 
                ? "0 required points" 
                : String.format("%,d required points", ach.requiredPoints);

        Label reqPointsLabel = new Label(pointsText);
        reqPointsLabel.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 9px; " +
            "-fx-text-fill: " + (isUnlocked ? "#5d4037;" : "#6b7a59;")
        );

        card.getChildren().addAll(imageFrame, statusLabel, titleLabel, reqPointsLabel);
        return card;
    }
}