package com.brewcoders.ascend.view.auth;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginView extends StackPane {

    private boolean isLoginMode = true;

    public LoginView() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("auth-scroll-pane");

        VBox mainContainer = new VBox(20);
        mainContainer.getStyleClass().add("auth-root");
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle("-fx-padding: 50px 20px;");

        // 1. Logo Header
        HBox logoHeader = new HBox(12);
        logoHeader.setAlignment(Pos.CENTER);

        Label logoBox = new Label("A");
        logoBox.getStyleClass().add("brand-logo-box");

        Label brandName = new Label("ASCEND");
        brandName.getStyleClass().add("brand-text");

        logoHeader.getChildren().addAll(logoBox, brandName);

        // 2. Badge
        HBox badgePill = new HBox();
        badgePill.setAlignment(Pos.CENTER);
        badgePill.getStyleClass().add("badge-pill");
        badgePill.setMaxWidth(VBox.USE_PREF_SIZE);

        Label badgeText = new Label("GAMIFIED PRODUCTIVITY SYSTEM");
        badgeText.getStyleClass().add("badge-text");
        badgePill.getChildren().add(badgeText);

        // 3. Hero Headlines
        Label heroTitle = new Label("Turn every mission into progress.");
        heroTitle.getStyleClass().add("hero-title");

        Label heroSubtitle = new Label("Create missions, earn points, and advance your\navatar throughout the cycle.");
        heroSubtitle.getStyleClass().add("hero-subtitle");
        heroSubtitle.setStyle("-fx-text-alignment: center;");

        // 4. Main Card Container (Width set to 520px for scaled display)
        VBox authCard = new VBox(18);
        authCard.getStyleClass().add("auth-card");
        authCard.setMaxWidth(520);
        authCard.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("WELCOME TO ASCEND");
        welcomeLabel.getStyleClass().add("card-welcome-text");

        Label cardTitle = new Label("Log in to continue");
        cardTitle.getStyleClass().add("card-title-text");

        // Tab Switcher
        HBox tabBar = new HBox();
        tabBar.getStyleClass().add("tab-bar");

        Button loginTab = new Button("Log In");
        loginTab.getStyleClass().add("tab-button-active");
        loginTab.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(loginTab, Priority.ALWAYS);

        Button signupTab = new Button("Sign up");
        signupTab.getStyleClass().add("tab-button-inactive");
        signupTab.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(signupTab, Priority.ALWAYS);

        tabBar.getChildren().addAll(loginTab, signupTab);

        // Username Field
        VBox usernameBox = new VBox(6);
        usernameBox.setAlignment(Pos.CENTER_LEFT);
        Label usernameLabel = new Label("Username");
        usernameLabel.getStyleClass().add("field-label");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("retro-input");
        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        // Password Field
        VBox passwordBox = new VBox(6);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("field-label");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("retro-input");
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Action Button
        Button actionButton = new Button("Login >");
        actionButton.getStyleClass().add("gold-action-button");
        actionButton.setMaxWidth(Double.MAX_VALUE);

        // Mode Switching
        loginTab.setOnAction(e -> {
            if (!isLoginMode) {
                isLoginMode = true;
                loginTab.getStyleClass().setAll("tab-button-active");
                signupTab.getStyleClass().setAll("tab-button-inactive");
                cardTitle.setText("Log in to continue");
                actionButton.setText("Login >");
            }
        });

        signupTab.setOnAction(e -> {
            if (isLoginMode) {
                isLoginMode = false;
                signupTab.getStyleClass().setAll("tab-button-active");
                loginTab.getStyleClass().setAll("tab-button-inactive");
                cardTitle.setText("Create your Account");
                actionButton.setText("Signup >");
            }
        });

        // Validation & Action Execution
        actionButton.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                if (isLoginMode) {
                    showCustomAlert("Login details incomplete", "Please complete both the username and password\nbefore logging in.");
                } else {
                    showCustomAlert("Signup details incomplete", "Please complete both the username and password\nbefore signing up.");
                }
            } else {
                if (!isLoginMode) {
                    this.getScene().setRoot(new SignupCourseView());
                }
            }
        });

        authCard.getChildren().addAll(welcomeLabel, cardTitle, tabBar, usernameBox, passwordBox, actionButton);
        mainContainer.getChildren().addAll(logoHeader, badgePill, heroTitle, heroSubtitle, authCard);

        scrollPane.setContent(mainContainer);
        this.getChildren().add(scrollPane);
    }

    private void showCustomAlert(String titleText, String messageText) {
        StackPane overlay = new StackPane();
        overlay.getStyleClass().add("modal-overlay");

        VBox alertCard = new VBox(12);
        alertCard.getStyleClass().add("alert-box");
        alertCard.setMaxWidth(500); // Increased width from 440 to 500 to accommodate pixel text
        alertCard.setMaxHeight(VBox.USE_PREF_SIZE);
        alertCard.setAlignment(Pos.TOP_LEFT);

        Label welcomeText = new Label("WELCOME TO ASCEND");
        welcomeText.getStyleClass().add("alert-welcome");

        Label title = new Label(titleText);
        title.getStyleClass().add("alert-title");
        title.setWrapText(true); // Ensures long titles break into two lines if needed
        title.setMaxWidth(450);

        Label message = new Label(messageText);
        message.getStyleClass().add("alert-message");
        message.setWrapText(true);
        message.setMaxWidth(450);

        Button okButton = new Button("OK");
        okButton.getStyleClass().add("alert-ok-button");

        HBox buttonContainer = new HBox(okButton);
        buttonContainer.setAlignment(Pos.BOTTOM_RIGHT);
        buttonContainer.setStyle("-fx-padding: 8px 0 0 0;");

        okButton.setOnAction(e -> this.getChildren().remove(overlay));

        alertCard.getChildren().addAll(welcomeText, title, message, buttonContainer);
        overlay.getChildren().add(alertCard);
        StackPane.setAlignment(alertCard, Pos.CENTER);

        this.getChildren().add(overlay);
    }
}