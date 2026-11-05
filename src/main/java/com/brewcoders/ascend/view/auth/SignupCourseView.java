package com.brewcoders.ascend.view.auth;

import com.brewcoders.ascend.view.navigation.MainLayoutView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class SignupCourseView extends StackPane {

    public SignupCourseView() {
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

        // Wrapper to apply the original logo border styling 
        StackPane logoBox = new StackPane(logoIcon);
        logoBox.getStyleClass().add("brand-logo-image-box");

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
        Label heroTitle = new Label("Create your current course.");
        heroTitle.getStyleClass().add("hero-title");

        // Split into two distinct labels inside a dedicated VBox to guarantee full height rendering
        Label subtitleLine1 = new Label("Your missions, points, history, and achievements will belong");
        subtitleLine1.getStyleClass().add("hero-subtitle");

        Label subtitleLine2 = new Label("to this course until you begin a new one.");
        subtitleLine2.getStyleClass().add("hero-subtitle");

        VBox subtitleBox = new VBox(6); 
        subtitleBox.setAlignment(Pos.CENTER);
        subtitleBox.getChildren().addAll(subtitleLine1, subtitleLine2);

        // 4. Main Card Container 
        VBox authCard = new VBox(20);
        authCard.getStyleClass().add("auth-card");
        authCard.setMaxWidth(680);
        authCard.setAlignment(Pos.TOP_LEFT);

        // Back Button
        Button backButton = new Button("< Back");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> this.getScene().setRoot(new LoginView()));

        Label cardTitle = new Label("Set up your course");
        cardTitle.getStyleClass().add("card-title-text");

        // Course Name
        VBox courseBox = new VBox(8);
        Label courseLabel = new Label("Course name");
        courseLabel.getStyleClass().add("field-label");
        TextField courseField = new TextField();
        courseField.setPromptText("e.g. Object-Oriented Programming");
        courseField.getStyleClass().add("retro-input");
        courseBox.getChildren().addAll(courseLabel, courseField);

        // Focus Cycle
        VBox focusBox = new VBox(6);
        Label focusLabel = new Label("Focus Cycle");
        focusLabel.getStyleClass().add("field-label");
        
        Label focusSubText = new Label("Pick the first and last day from the calendar.");
        focusSubText.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 10px; -fx-text-fill: #4a2810;");
        focusSubText.setWrapText(true);
        
        focusBox.getChildren().addAll(focusLabel, focusSubText);

        // Dates Row
        HBox datesRow = new HBox(16);
        datesRow.setAlignment(Pos.CENTER_LEFT);

        LocalDate today = LocalDate.now();

        VBox startBox = new VBox(8);
        Label startLabel = new Label("Start date");
        startLabel.getStyleClass().add("field-label");
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setEditable(false); // Disables manual typing
        startDatePicker.setPromptText("mm/dd/yyyy");
        startDatePicker.getStyleClass().add("retro-date-picker");
        startDatePicker.setMaxWidth(Double.MAX_VALUE);
        startBox.getChildren().addAll(startLabel, startDatePicker);
        HBox.setHgrow(startBox, Priority.ALWAYS);

        VBox endBox = new VBox(8);
        Label endLabel = new Label("End date");
        endLabel.getStyleClass().add("field-label");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setEditable(false); // Disables manual typing
        endDatePicker.setPromptText("mm/dd/yyyy");
        endDatePicker.getStyleClass().add("retro-date-picker");
        endDatePicker.setMaxWidth(Double.MAX_VALUE);
        endBox.getChildren().addAll(endLabel, endDatePicker);
        HBox.setHgrow(endBox, Priority.ALWAYS);

        datesRow.getChildren().addAll(startBox, endBox);

        // --- Date Restrictions Setup ---
        
        // 1. Prevent selecting past dates on Start Date Picker
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date != null && date.isBefore(today)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d1c7b7; -fx-text-fill: #888888;");
                }
            }
        });

        // 2. Prevent selecting dates before today (or before Start Date) on End Date Picker
        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate minDate = startDatePicker.getValue() != null ? startDatePicker.getValue() : today;
                if (date != null && date.isBefore(minDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d1c7b7; -fx-text-fill: #888888;");
                }
            }
        });

        // 3. Listener to update End Date options when Start Date changes
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (endDatePicker.getValue() != null && endDatePicker.getValue().isBefore(newValue)) {
                    endDatePicker.setValue(null);
                }
                
                endDatePicker.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        if (date != null && date.isBefore(newValue)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #d1c7b7; -fx-text-fill: #888888;");
                        }
                    }
                });
            }
        });

        // Action Button
        Button createCourseBtn = new Button("Create course >");
        createCourseBtn.getStyleClass().add("gold-action-button");
        createCourseBtn.setMaxWidth(Double.MAX_VALUE);

        createCourseBtn.setOnAction(e -> {
            if (courseField.getText().trim().isEmpty() || startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
                showCustomAlert("Course details incomplete", "Please enter a course name and select both\nstart and end dates.");
            } else {
                String courseName = courseField.getText().trim();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy");
                String formattedDates = startDatePicker.getValue().format(fmt) + " - " + endDatePicker.getValue().format(fmt);

                if (this.getScene() != null) {
                    this.getScene().setRoot(new MainLayoutView(
                        courseName,
                        formattedDates
                    ));
                }
            }
        });

        authCard.getChildren().addAll(backButton, cardTitle, courseBox, focusBox, datesRow, createCourseBtn);
        mainContainer.getChildren().addAll(logoHeader, badgePill, heroTitle, subtitleBox, authCard);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

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