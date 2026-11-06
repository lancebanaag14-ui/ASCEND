package com.brewcoders.ascend.view.profile;

import com.brewcoders.ascend.controller.MissionManager;
import com.brewcoders.ascend.view.auth.LoginView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class PersonalInfoView extends HBox {

    private Label pointsValue;
    private ImageView rankImageView;
    private Label rankTitle;
    private Label quoteLabel;
    private Label descLabel;

    public PersonalInfoView() {
        this.setSpacing(24);
        this.setAlignment(Pos.TOP_LEFT);

        // --- LEFT COLUMN ---
        VBox leftColumn = new VBox(20);
        leftColumn.setMinWidth(460);
        leftColumn.setPrefWidth(460);
        leftColumn.setMaxWidth(460);

        // 1. User Info Card
        VBox userInfoCard = new VBox(20);
        userInfoCard.getStyleClass().add("profile-card");
        userInfoCard.setAlignment(Pos.CENTER);
        userInfoCard.setPadding(new Insets(32, 24, 32, 24));

        StackPane avatarFrame = new StackPane();
        avatarFrame.getStyleClass().add("profile-initials-avatar-frame");
        Label avatarInitial = new Label("R");
        avatarInitial.getStyleClass().add("profile-avatar-initial");
        avatarFrame.getChildren().add(avatarInitial);

        Label usernameHeader = new Label("Username");
        usernameHeader.getStyleClass().add("profile-card-header");
        Label usernameValue = new Label("red");
        usernameValue.getStyleClass().add("profile-card-value");
        VBox usernameBox = new VBox(6, usernameHeader, usernameValue);
        usernameBox.setAlignment(Pos.CENTER);

        Label pointsHeader = new Label("Total Points");
        pointsHeader.getStyleClass().add("profile-card-header");
        
        // Dynamic Total Points Label
        pointsValue = new Label("0");
        pointsValue.getStyleClass().add("profile-card-value");
        VBox pointsBox = new VBox(6, pointsHeader, pointsValue);
        pointsBox.setAlignment(Pos.CENTER);

        userInfoCard.getChildren().addAll(avatarFrame, usernameBox, pointsBox);

        // 2. Delete Card
        HBox deleteCard = new HBox(12);
        deleteCard.getStyleClass().add("profile-card");
        deleteCard.setAlignment(Pos.CENTER_LEFT);
        deleteCard.setPadding(new Insets(16, 20, 16, 20));

        VBox deleteTextGroup = new VBox(4);
        Label deleteTitle = new Label("Course and Account");
        deleteTitle.getStyleClass().add("profile-delete-title");
        Label deleteSubtitle = new Label("Delete all current progress");
        deleteSubtitle.getStyleClass().add("profile-delete-subtitle");
        deleteTextGroup.getChildren().addAll(deleteTitle, deleteSubtitle);

        Region deleteSpacer = new Region();
        HBox.setHgrow(deleteSpacer, Priority.ALWAYS);

        Button deleteBtn = new Button("Delete");
        deleteBtn.getStyleClass().add("profile-delete-btn");

        // Open Confirmation Modal
        deleteBtn.setOnAction(e -> showDeleteConfirmationModal());

        deleteCard.getChildren().addAll(deleteTextGroup, deleteSpacer, deleteBtn);

        leftColumn.getChildren().addAll(userInfoCard, deleteCard);

        // --- RIGHT COLUMN ---
        VBox rightColumn = new VBox(20);
        HBox.setHgrow(rightColumn, Priority.ALWAYS);

        VBox avatarInfoCard = new VBox(24);
        avatarInfoCard.getStyleClass().add("profile-card");
        avatarInfoCard.setAlignment(Pos.TOP_CENTER);
        avatarInfoCard.setPadding(new Insets(32, 28, 32, 28));

        StackPane rankImageFrame = new StackPane();
        rankImageFrame.getStyleClass().add("profile-rank-image-frame");
        
        rankImageView = new ImageView();
        rankImageView.setFitWidth(110);
        rankImageView.setFitHeight(110);
        rankImageView.setPreserveRatio(false);

        Rectangle clip = new Rectangle(110, 110);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        rankImageView.setClip(clip);

        rankImageFrame.getChildren().add(rankImageView);

        rankTitle = new Label("The Noob");
        rankTitle.getStyleClass().add("profile-rank-title");

        // About Box
        VBox aboutCard = new VBox(16);
        aboutCard.getStyleClass().add("profile-about-card");
        aboutCard.setPadding(new Insets(20, 24, 20, 24));

        quoteLabel = new Label("\"Hello World! (And goodbye to my syntax errors.)\"");
        quoteLabel.getStyleClass().add("profile-quote");
        quoteLabel.setWrapText(true);
        quoteLabel.setTextAlignment(TextAlignment.CENTER);

        descLabel = new Label("Your journey into coding begins here. You are learning the ropes, setting up your first development cycles, and discovering that debugging takes twice as long as writing the code in the first place.");
        descLabel.getStyleClass().add("profile-desc");
        descLabel.setWrapText(true);
        descLabel.setTextAlignment(TextAlignment.CENTER);

        aboutCard.getChildren().addAll(quoteLabel, descLabel);

        Label aboutHeader = new Label("About");
        aboutHeader.getStyleClass().add("profile-about-header");

        VBox rightContentGroup = new VBox(14, rankTitle, aboutHeader, aboutCard);
        rightContentGroup.setAlignment(Pos.CENTER);

        avatarInfoCard.getChildren().addAll(rankImageFrame, rightContentGroup);
        rightColumn.getChildren().add(avatarInfoCard);

        this.getChildren().addAll(leftColumn, rightColumn);

        // Synchronize dynamic user point totals and rank visuals
        refreshUserData();
    }

    public void refreshUserData() {
        int totalPoints = MissionManager.getInstance().getTotalPoints();
        
        // Update Total Points label
        pointsValue.setText(String.valueOf(totalPoints));

        // Dynamic threshold checks for current avatar rank display
        String imagePath = "/images/avatars/avatar_noob.jpg";
        String title = "The Noob";
        String quote = "\"Hello World! (And goodbye to my syntax errors.)\"";
        String desc = "Your journey into coding begins here. You are learning the ropes, setting up your first development cycles, and discovering that debugging takes twice as long as writing the code in the first place.";

        if (totalPoints >= 3000) {
            imagePath = "/images/avatars/avatar_wildcard.jpg";
            title = "The Wildcard";
            quote = "\"Predictable? Never. Effective? Always.\"";
            desc = "Master of unexpected solutions and out-of-the-box thinking. You write code that surprises everyone, including yourself, and somehow it runs with zero errors.";
        } else if (totalPoints >= 2000) {
            imagePath = "/images/avatars/avatar_grinder.jpg";
            title = "The Grinder";
            quote = "\"Consistency is key. One line at a time.\"";
            desc = "Pushing through endless mission logs and refactoring legacy code without flinching. Your discipline turns complex problems into clean, functional algorithms.";
        } else if (totalPoints >= 1000) {
            imagePath = "/images/avatars/avatar_nightCoder.jpg";
            title = "The Night Coder";
            quote = "\"Powered by dark mode and late night breakthroughs.\"";
            desc = "Thriving when the sun goes down. The quiet hours of the night are when your best logic comes to life and unit tests pass effortlessly.";
        }

        try {
            rankImageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.err.println("Could not load rank image: " + imagePath);
        }

        rankTitle.setText(title);
        quoteLabel.setText(quote);
        descLabel.setText(desc);
    }

    private void showDeleteConfirmationModal() {
        Scene scene = this.getScene();
        if (scene == null) return;

        if (!(scene.getRoot() instanceof StackPane)) {
            Pane currentRoot = (Pane) scene.getRoot();
            StackPane wrapperRoot = new StackPane();
            scene.setRoot(wrapperRoot);
            wrapperRoot.getChildren().add(currentRoot);
        }

        StackPane rootStackPane = (StackPane) scene.getRoot();

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.45);");
        overlay.setAlignment(Pos.CENTER);

        VBox alertCard = new VBox(20);
        alertCard.setMaxWidth(540);
        alertCard.setMaxHeight(VBox.USE_PREF_SIZE);
        alertCard.setAlignment(Pos.TOP_LEFT);
        alertCard.setPadding(new Insets(32, 36, 32, 36));
        alertCard.setStyle(
            "-fx-background-color: #fde8b4; " + 
            "-fx-border-color: #4a2810; " +     
            "-fx-border-width: 3.5px;"
        );

        Label categoryHeader = new Label("COURSE AND ACCOUNT");
        categoryHeader.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 10px; " +
            "-fx-text-fill: #1b5e20;"
        );

        Label title = new Label("Delete all progress?");
        title.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 18px; " +
            "-fx-text-fill: #3e2723;"
        );
        title.setWrapText(true);

        Label message = new Label("Are you sure you want to delete all your current-course progress? This action cannot be undone.");
        message.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 10px; " +
            "-fx-text-fill: #4a2810; " +
            "-fx-line-spacing: 5px;"
        );
        message.setWrapText(true);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 11px; " +
            "-fx-background-color: #f7be3b; " +
            "-fx-text-fill: #1f2d18; " +
            "-fx-border-color: #4a2810; " +
            "-fx-border-width: 2.5px; " +
            "-fx-padding: 12 24; " +
            "-fx-cursor: hand;"
        );
        cancelBtn.setOnAction(e -> rootStackPane.getChildren().remove(overlay));

        Button confirmDeleteBtn = new Button("Delete all progress");
        confirmDeleteBtn.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 11px; " +
            "-fx-background-color: #ff585d; " +
            "-fx-text-fill: #ffffff; " +
            "-fx-border-color: #4a2810; " +
            "-fx-border-width: 2.5px; " +
            "-fx-padding: 12 24; " +
            "-fx-cursor: hand;"
        );
        confirmDeleteBtn.setOnAction(e -> {
            MissionManager.getInstance().clearMissions();
            rootStackPane.getChildren().remove(overlay);
            scene.setRoot(new LoginView());
        });

        HBox buttonContainer = new HBox(16, cancelBtn, confirmDeleteBtn);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setPadding(new Insets(10, 0, 0, 0));

        alertCard.getChildren().addAll(categoryHeader, title, message, buttonContainer);
        overlay.getChildren().add(alertCard);

        rootStackPane.getChildren().add(overlay);
    }
}