package com.brewcoders.ascend.view.auth;

import com.brewcoders.ascend.controller.MissionManager;
import com.brewcoders.ascend.view.navigation.MainLayoutView;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Control;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

public class SignupCourseView extends StackPane {

    private String registeredUsername = "Guest User";

    public SignupCourseView(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.registeredUsername = username.trim();
        }
        initUI();
    }

    public SignupCourseView() {
        this("Guest User");
    }

    private void initUI() {
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
            Image logoImg = new Image(
                getClass().getResourceAsStream("/images/logo.png")
            );

            logoIcon.setImage(logoImg);
            logoIcon.setFitWidth(40);
            logoIcon.setFitHeight(40);
            logoIcon.setPreserveRatio(true);

        } catch (Exception e) {
            System.err.println(
                "Could not load logo image from /images/logo.png"
            );
        }

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

        Label subtitleLine1 = new Label(
            "Your missions, points, history, and achievements will belong"
        );
        subtitleLine1.getStyleClass().add("hero-subtitle");

        Label subtitleLine2 = new Label(
            "to this course until you begin a new one."
        );
        subtitleLine2.getStyleClass().add("hero-subtitle");

        VBox subtitleBox = new VBox(6);
        subtitleBox.setAlignment(Pos.CENTER);
        subtitleBox.getChildren().addAll(
            subtitleLine1,
            subtitleLine2
        );

        // 4. Main Card Container
        VBox authCard = new VBox(20);
        authCard.getStyleClass().add("auth-card");
        authCard.setMaxWidth(680);
        authCard.setAlignment(Pos.TOP_LEFT);

        // Back Button
        Button backButton = new Button("< Back");
        backButton.getStyleClass().add("back-button");

        backButton.setOnAction(e -> {
            if (this.getScene() != null) {
                this.getScene().setRoot(new LoginView());
            }
        });

        Label cardTitle = new Label("Set up your course");
        cardTitle.getStyleClass().add("card-title-text");

        // 5. Course Name Error Popup
        Label courseErrorLabel = createErrorPopup();

        PauseTransition courseTimer =
            new PauseTransition(Duration.seconds(3));

        courseTimer.setOnFinished(e -> {
            courseErrorLabel.setVisible(false);
            courseErrorLabel.setManaged(false);
        });

        // 6. Course Name Filter
        UnaryOperator<TextFormatter.Change> courseFilter = change -> {

            String addedText = change.getText();
            String newText = change.getControlNewText();

            if (!addedText.isEmpty()) {

                // Maximum 30 characters
                if (newText.length() > 30) {
                    showError(
                        courseErrorLabel,
                        courseTimer,
                        "⚠ Max 30 characters allowed!"
                    );
                    return null;
                }

                // Allow letters, numbers, spaces, hyphens, underscores
                if (!addedText.matches("[\\p{L}\\p{N} _-]+")) {
                    showError(
                        courseErrorLabel,
                        courseTimer,
                        "⚠ Special characters are not allowed!"
                    );
                    return null;
                }
            }

            courseErrorLabel.setVisible(false);
            courseErrorLabel.setManaged(false);
            courseTimer.stop();

            return change;
        };

        // 7. Course Name
        VBox courseBox = new VBox(8);

        Label courseLabel = new Label("Course name");
        courseLabel.getStyleClass().add("field-label");

        TextField courseField = new TextField();
        courseField.setPromptText("e.g. Object-Oriented Programming");
        courseField.getStyleClass().add("retro-input");

        courseField.setTextFormatter(
            new TextFormatter<>(courseFilter)
        );

        courseBox.getChildren().addAll(
            courseLabel,
            courseField,
            courseErrorLabel
        );

        // 8. Focus Cycle
        VBox focusBox = new VBox(6);

        Label focusLabel = new Label("Focus Cycle");
        focusLabel.getStyleClass().add("field-label");

        Label focusSubText = new Label(
            "Pick the first and last day from the calendar."
        );

        focusSubText.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 10px; " +
            "-fx-text-fill: #4a2810;"
        );

        focusSubText.setWrapText(true);

        focusBox.getChildren().addAll(
            focusLabel,
            focusSubText
        );

        // 9. Dates Row
        HBox datesRow = new HBox(16);
        datesRow.setAlignment(Pos.CENTER_LEFT);

        LocalDate today = LocalDate.now();

        // Start Date
        VBox startBox = new VBox(8);

        Label startLabel = new Label("Start date");
        startLabel.getStyleClass().add("field-label");

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setEditable(false);
        startDatePicker.setPromptText("mm/dd/yyyy");
        startDatePicker.getStyleClass().add("retro-date-picker");
        startDatePicker.setMaxWidth(Double.MAX_VALUE);

        startBox.getChildren().addAll(
            startLabel,
            startDatePicker
        );

        HBox.setHgrow(startBox, Priority.ALWAYS);

        // End Date
        VBox endBox = new VBox(8);

        Label endLabel = new Label("End date");
        endLabel.getStyleClass().add("field-label");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setEditable(false);
        endDatePicker.setPromptText("mm/dd/yyyy");
        endDatePicker.getStyleClass().add("retro-date-picker");
        endDatePicker.setMaxWidth(Double.MAX_VALUE);

        endBox.getChildren().addAll(
            endLabel,
            endDatePicker
        );

        HBox.setHgrow(endBox, Priority.ALWAYS);

        datesRow.getChildren().addAll(
            startBox,
            endBox
        );

        // 10. Date Restrictions Setup

        // Prevent selecting past dates on Start Date Picker
        startDatePicker.setDayCellFactory(picker -> new DateCell() {

            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date != null && date.isBefore(today)) {
                    setDisable(true);
                    setStyle(
                        "-fx-background-color: #d1c7b7; " +
                        "-fx-text-fill: #888888;"
                    );
                }
            }
        });

        // Prevent selecting dates before today or Start Date
        endDatePicker.setDayCellFactory(picker -> new DateCell() {

            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                LocalDate minDate =
                    startDatePicker.getValue() != null
                        ? startDatePicker.getValue()
                        : today;

                if (date != null && date.isBefore(minDate)) {
                    setDisable(true);
                    setStyle(
                        "-fx-background-color: #d1c7b7; " +
                        "-fx-text-fill: #888888;"
                    );
                }
            }
        });

        // Update End Date options when Start Date changes
        startDatePicker.valueProperty().addListener(
            (observable, oldValue, newValue) -> {

                if (newValue != null) {

                    if (endDatePicker.getValue() != null &&
                        endDatePicker.getValue().isBefore(newValue)) {

                        endDatePicker.setValue(null);
                    }

                    endDatePicker.setDayCellFactory(
                        picker -> new DateCell() {

                            @Override
                            public void updateItem(
                                LocalDate date,
                                boolean empty
                            ) {
                                super.updateItem(date, empty);

                                if (date != null &&
                                    date.isBefore(newValue)) {

                                    setDisable(true);
                                    setStyle(
                                        "-fx-background-color: #d1c7b7; " +
                                        "-fx-text-fill: #888888;"
                                    );
                                }
                            }
                        }
                    );
                }
            }
        );

        // Clear Error Highlights on User Interaction
        courseField.textProperty().addListener(
            (obs, oldVal, newVal) -> applyInputStyle(courseField)
        );

        startDatePicker.valueProperty().addListener(
            (obs, oldVal, newVal) -> applyInputStyle(startDatePicker)
        );

        endDatePicker.valueProperty().addListener(
            (obs, oldVal, newVal) -> applyInputStyle(endDatePicker)
        );

        // 11. Create Course Button
        Button createCourseBtn = new Button("Create course >");
        createCourseBtn.getStyleClass().add("gold-action-button");
        createCourseBtn.setMaxWidth(Double.MAX_VALUE);

        createCourseBtn.setOnAction(e -> {

            String courseName = courseField.getText().trim();

            // Incomplete Details Check
            boolean hasError = false;

            if (courseName.isEmpty()) {
                highlightInput(courseField);
                hasError = true;
            }

            if (startDatePicker.getValue() == null) {
                highlightInput(startDatePicker);
                hasError = true;
            }

            if (endDatePicker.getValue() == null) {
                highlightInput(endDatePicker);
                hasError = true;
            }

            if (hasError) {
                showCustomAlert(
                    "Course details incomplete",
                    "Please complete all required fields."
                );
                return;
            }

            // Course Name Length Check
            if (courseName.length() > 30) {
                showError(
                    courseErrorLabel,
                    courseTimer,
                    "⚠ Max 30 characters allowed!"
                );
                return;
            }

            // Explicitly sync the username to MissionManager Singleton
            MissionManager.getInstance().setCurrentUsername(registeredUsername);

            // Clear previous session/course mission data
            MissionManager.getInstance().clearMissions();

            DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("MM/dd/yyyy");

            String formattedDates =
                startDatePicker.getValue().format(fmt) +
                " - " +
                endDatePicker.getValue().format(fmt);

            // Route to Main Layout
            if (this.getScene() != null) {
                this.getScene().setRoot(
                    new MainLayoutView(
                        courseName,
                        formattedDates
                    )
                );
            }
        });

        // 12. Add Components
        authCard.getChildren().addAll(
            backButton,
            cardTitle,
            courseBox,
            focusBox,
            datesRow,
            createCourseBtn
        );

        mainContainer.getChildren().addAll(
            logoHeader,
            badgePill,
            heroTitle,
            subtitleBox,
            authCard
        );

        // 13. ScrollPane Settings
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(
            ScrollPane.ScrollBarPolicy.NEVER
        );
        scrollPane.setVbarPolicy(
            ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-background: transparent;"
        );

        scrollPane.setContent(mainContainer);
        this.getChildren().add(scrollPane);
    }

    // Apply Normal Input Style
    private void applyInputStyle(Control input) {
        input.setStyle("");
    }

    // Highlight Invalid / Empty Input
    private void highlightInput(Control input) {
        input.setStyle(
            "-fx-border-color: #e63946; " +
            "-fx-border-width: 3px;"
        );
    }

    // Create Error Popup
    private Label createErrorPopup() {

        Label popup = new Label();

        popup.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 9px; " +
            "-fx-text-fill: #ffffff; " +
            "-fx-background-color: #e63946; " +
            "-fx-border-color: #453018; " +
            "-fx-border-width: 2px; " +
            "-fx-padding: 6px 10px;"
        );

        popup.setVisible(false);
        popup.setManaged(false);

        return popup;
    }

    // Show Error Popup
    private void showError(
        Label errorLabel,
        PauseTransition timer,
        String message
    ) {

        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);

        timer.playFromStart();
    }

    // Custom Alert
    private void showCustomAlert(
        String titleText,
        String messageText
    ) {

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

        okButton.setOnAction(e ->
            this.getChildren().remove(overlay)
        );

        alertCard.getChildren().addAll(
            welcomeText,
            title,
            message,
            buttonContainer
        );

        overlay.getChildren().add(alertCard);
        StackPane.setAlignment(alertCard, Pos.CENTER);

        this.getChildren().add(overlay);
    }
}