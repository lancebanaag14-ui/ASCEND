package com.brewcoders.ascend.view.profile;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

public class PersonalInfoView extends HBox {

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
        Label pointsValue = new Label("0");
        pointsValue.getStyleClass().add("profile-card-value");
        VBox pointsBox = new VBox(6, pointsHeader, pointsValue);
        pointsBox.setAlignment(Pos.CENTER);

        userInfoCard.getChildren().addAll(avatarFrame, usernameBox, pointsBox);

        // 2. Delete Card (Fixed width & text alignment)
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
        
        try {
            ImageView rankImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/avatars/avatar_noob.jpg")));
            rankImageView.setFitWidth(110);
            rankImageView.setFitHeight(110);
            rankImageView.setPreserveRatio(true);
            rankImageFrame.getChildren().add(rankImageView);
        } catch (Exception e) {
            // Fallback image frame
        }

        Label rankTitle = new Label("The Noob");
        rankTitle.getStyleClass().add("profile-rank-title");

        // About Box
        VBox aboutCard = new VBox(16);
        aboutCard.getStyleClass().add("profile-about-card");
        aboutCard.setPadding(new Insets(20, 24, 20, 24));

        Label quoteLabel = new Label("\"Hello World! (And goodbye to my syntax errors.)\"");
        quoteLabel.getStyleClass().add("profile-quote");
        quoteLabel.setWrapText(true);
        quoteLabel.setTextAlignment(TextAlignment.CENTER);

        Label descLabel = new Label("Your journey into coding begins here. You are learning the ropes, setting up your first development cycles, and discovering that debugging takes twice as long as writing the code in the first place.");
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
    }
}