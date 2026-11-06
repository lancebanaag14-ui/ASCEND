package com.brewcoders.ascend.view.auth;

import com.brewcoders.ascend.view.navigation.MainLayoutView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


import java.util.function.UnaryOperator;

public class LoginView extends StackPane {

    private boolean isLoginMode = true;

    public LoginView() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("auth-scroll-pane");

        VBox mainContainer = new VBox(24);
        mainContainer.getStyleClass().add("auth-root");
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle("-fx-padding: 60px 20px;");

        // 1. Header
        HBox logoHeader = new HBox(16);
        logoHeader.setAlignment(Pos.CENTER);

        ImageView logoIcon = new ImageView();
        try {
            Image logoImg = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoIcon.setImage(logoImg);
            logoIcon.setFitWidth(40);
            logoIcon.setFitHeight(40);
            logoIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Could not load logo image from /images/logo.png");
        }

        // Wrapper to apply the original logo border styling (#facc15 border)
        StackPane logoBox = new StackPane(logoIcon);
        logoBox.getStyleClass().add("brand-logo-image-box");

        Label brandName = new Label("ASCEND");
        brandName.getStyleClass().add("brand-text");

        logoHeader.getChildren().addAll(logoBox, brandName);

        // 2. Badge Pill
        HBox badgePill = new HBox();
        badgePill.setAlignment(Pos.CENTER);
        badgePill.getStyleClass().add("badge-pill");
        badgePill.setMaxWidth(VBox.USE_PREF_SIZE);

        Label badgeText = new Label("GAMIFIED PRODUCTIVITY SYSTEM");
        badgeText.getStyleClass().add("badge-text");
        badgePill.getChildren().add(badgeText);

        // 3. Hero Titles
        Label heroTitle = new Label("Turn every mission into progress.");
        heroTitle.getStyleClass().add("auth-headline");
        heroTitle.setWrapText(true);
        heroTitle.setAlignment(Pos.CENTER);
        heroTitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        // Bind width so text wraps dynamically as the parent resizes
        heroTitle.prefWidthProperty().bind(mainContainer.widthProperty().subtract(40));

        // Subtitle
        Label heroSubtitle = new Label("Create missions, earn points, and advance your avatar throughout the cycle.");
        heroSubtitle.getStyleClass().add("auth-subheadline");
        heroSubtitle.setWrapText(true);
        heroSubtitle.setAlignment(Pos.CENTER);
        heroSubtitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        heroSubtitle.prefWidthProperty().bind(mainContainer.widthProperty().subtract(60));

        // 4. Main Card Container (Width set to 680px)
        VBox authCard = new VBox(22);
        authCard.getStyleClass().add("auth-card");
        authCard.setMaxWidth(680);
        authCard.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("WELCOME TO ASCEND");
        welcomeLabel.getStyleClass().add("card-welcome-text");

        Label cardTitle = new Label("Login to continue");
        cardTitle.getStyleClass().add("card-title-text");

        // Tab Switcher
        HBox tabBar = new HBox();
        tabBar.getStyleClass().add("tab-bar");

        Button loginTab = new Button("Login");
        loginTab.getStyleClass().add("tab-button-active");
        loginTab.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(loginTab, Priority.ALWAYS);

        Button signupTab = new Button("Signup");
        signupTab.getStyleClass().add("tab-button-inactive");
        signupTab.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(signupTab, Priority.ALWAYS);

        tabBar.getChildren().addAll(loginTab, signupTab);

        // Username Filter: Alphanumeric & underscores, max 30 chars, no whitespace
        UnaryOperator<TextFormatter.Change> usernameFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= 30 && !change.getText().contains(" ")) {
                return change;
            }
            return null; // Reject change
        };

        // Password Filter: Max 64 chars, no whitespace
        UnaryOperator<TextFormatter.Change> passwordFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= 64 && !change.getText().contains(" ")) {
                return change;
            }
            return null; // Reject change
        };

        // Username Field
        VBox usernameBox = new VBox(8);
        usernameBox.setAlignment(Pos.CENTER_LEFT);
        Label usernameLabel = new Label("Username");
        usernameLabel.getStyleClass().add("field-label");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("retro-input");
        usernameField.setTextFormatter(new TextFormatter<>(usernameFilter));
        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        // Password Field
        VBox passwordBox = new VBox(8);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("field-label");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("retro-input");
        passwordField.setTextFormatter(new TextFormatter<>(passwordFilter));
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Action Button
        Button actionButton = new Button("Login >");
        actionButton.getStyleClass().add("gold-action-button");
        actionButton.setMaxWidth(Double.MAX_VALUE);

        // Tab Button Alignment & Expansion
        loginTab.setAlignment(Pos.CENTER);
        signupTab.setAlignment(Pos.CENTER);
        loginTab.setMaxWidth(Double.MAX_VALUE);
        signupTab.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(loginTab, Priority.ALWAYS);
        HBox.setHgrow(signupTab, Priority.ALWAYS);
        
        // Tab Switch Logic
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

        // Action Execution & Validation
        actionButton.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText();

            // 1. Incomplete Details Check
            if (user.isEmpty() || pass.isEmpty()) {
                if (isLoginMode) {
                    showCustomAlert("Login details incomplete", "Please complete both the username and password\nbefore logging in.");
                } else {
                    showCustomAlert("Signup details incomplete", "Please complete both the username and password\nbefore signing up.");
                }
                return;
            }

    

            // 2. Signup Rules Check
            if (isLoginMode || !isLoginMode) {
                if (user.length() < 3) {
                    showCustomAlert("Invalid Username", "Username must be at least 3 characters long.");
                    return;
                }
                 if (!user.matches("^[a-zA-Z0-9_]+$")) {
                showCustomAlert("Invalid Username", "Username can only contain letters, numbers, and underscores.");
                return;
                }
                if (pass.length() < 8) {
                    showCustomAlert("Weak Password", "Password must be at least 8 characters long.");
                    return;
                }
            }

            // Route to next view
            if (this.getScene() != null) {
                if (!isLoginMode) {
                    this.getScene().setRoot(new SignupCourseView());
                } else {
                    this.getScene().setRoot(new MainLayoutView("oop", "Nov 2, 2026 - Nov 26, 2026"));
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

        VBox alertCard = new VBox(16);
        alertCard.getStyleClass().add("alert-box");
        alertCard.setMaxWidth(600);
        alertCard.setMaxHeight(VBox.USE_PREF_SIZE);
        alertCard.setAlignment(Pos.TOP_LEFT);

        Label welcomeText = new Label("WELCOME TO ASCEND");
        welcomeText.getStyleClass().add("alert-welcome");

        Label title = new Label(titleText);
        title.getStyleClass().add("alert-title");
        title.setWrapText(true);

        Label message = new Label(messageText);
        message.getStyleClass().add("alert-message");
        message.setWrapText(true);

        Button okButton = new Button("OK");
        okButton.getStyleClass().add("alert-ok-button");

        HBox buttonContainer = new HBox(okButton);
        buttonContainer.setAlignment(Pos.BOTTOM_RIGHT);
        buttonContainer.setStyle("-fx-padding: 12px 0 0 0;");

        okButton.setOnAction(e -> this.getChildren().remove(overlay));

        alertCard.getChildren().addAll(welcomeText, title, message, buttonContainer);
        overlay.getChildren().add(alertCard);
        StackPane.setAlignment(alertCard, Pos.CENTER);

        this.getChildren().add(overlay);
    }
}