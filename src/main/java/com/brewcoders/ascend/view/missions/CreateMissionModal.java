package com.brewcoders.ascend.view.missions;

import com.brewcoders.ascend.model.Mission;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class CreateMissionModal {

    public static void display(
            Stage ownerStage,
            LocalDate cycleStart,
            LocalDate cycleEnd,
            Consumer<Mission> onMissionCreated
    ) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ownerStage);
        dialog.initStyle(StageStyle.TRANSPARENT);

        // 1. Large Card Container matching Figma dimensions
        VBox card = new VBox(22);
        card.setPadding(new Insets(40, 48, 40, 48));
        card.setStyle(
            "-fx-background-color: #fce8ac; " +
            "-fx-border-color: #453018; " +
            "-fx-border-width: 4px; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.45), 16, 0, 8, 8);"
        );

        card.setMinWidth(820);
        card.setPrefWidth(820);
        card.setMaxWidth(820);
        card.setMaxHeight(Region.USE_PREF_SIZE);

        // Header Section
        Label badge = new Label("NEW MISSION");
        badge.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 11px; -fx-text-fill: #455d38;");

        Label title = new Label("Create a mission");
        title.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 26px; -fx-text-fill: #1f2d18;");

        VBox headerBox = new VBox(10, badge, title);

        // Error Popups Setup
        Label nameErrorLabel = createErrorPopup();
        Label descErrorLabel = createErrorPopup();

        PauseTransition nameTimer = new PauseTransition(Duration.seconds(3));
        nameTimer.setOnFinished(e -> nameErrorLabel.setVisible(false));

        PauseTransition descTimer = new PauseTransition(Duration.seconds(3));
        descTimer.setOnFinished(e -> descErrorLabel.setVisible(false));

        // Inputs: Mission Name( Max 50 chars, no special chars & spaces )
        Label nameLabel = createFieldLabel("Mission name");
        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter mission name");
        nameInput.setPrefHeight(48);
        applyInputStyle(nameInput);

        UnaryOperator<TextFormatter.Change> nameFilter = change -> {
            String addedText = change.getText();
            String newText = change.getControlNewText();

            // Check if user is typing/pasting invalid content
            if (!addedText.isEmpty()) {
                if (newText.length() > 50) {
                    showError(nameErrorLabel, nameTimer, "⚠ Max 50 characters allowed!");
                    return null;
                }
                if (!addedText.matches("[a-zA-Z0-9 _]*")) {
                    showError(nameErrorLabel, nameTimer, "⚠ Special character is not allowed!");
                    return null;
                }
            }

            nameErrorLabel.setVisible(false);
            return change;
        };
        nameInput.setTextFormatter(new TextFormatter<>(nameFilter));

        // Inputs: Short Note / Description (Max 62 chars)
        Label descLabel = createFieldLabel("Short note / description (62 character max)");
        TextArea descInput = new TextArea();
        descInput.setPromptText("What needs to be done?");
        descInput.setPrefRowCount(4);
        descInput.setPrefHeight(130);
        descInput.setWrapText(true);

        UnaryOperator<TextFormatter.Change> descFilter = change -> {
            String addedText = change.getText();
            String newText = change.getControlNewText();

            if (!addedText.isEmpty() && newText.length() > 62) {
                showError(descErrorLabel, descTimer, "⚠ Max 62 characters allowed!");
                return null;
            }

            descErrorLabel.setVisible(false);
            return change;
        };
        descInput.setTextFormatter(new TextFormatter<>(descFilter));

        descInput.setStyle(
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 11px; " +
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #453018; " +
            "-fx-border-width: 3px; " +
            "-fx-control-inner-background: #ffffff;"
        );

        // Expand line spacing and inner padding once rendered
        Platform.runLater(() -> {
            Node scrollPane = descInput.lookup(".scroll-pane");
            if (scrollPane != null) {
                scrollPane.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-padding: 0;");
            }

            Node content = descInput.lookup(".content");
            if (content != null) {
                content.setStyle(
                    "-fx-background-color: #ffffff; " +
                    "-fx-background-insets: 0; " +
                    "-fx-border-width: 0; " +
                    "-fx-padding: 16px 14px 16px 14px; " +
                    "-fx-line-spacing: 12px;"
                );
            }

            for (Node n : descInput.lookupAll(".text")) {
                n.setStyle("-fx-line-spacing: 12px; -fx-bounds-type: logical;");
            }
        });

        // Containers with popups attached directly beneath inputs
        VBox nameBox = new VBox(6, nameLabel, nameInput, nameErrorLabel);
        VBox descBox = new VBox(6, descLabel, descInput, descErrorLabel);

        // Due Date & Category Row
        Label dateLabel = createFieldLabel("Due date");
        DatePicker datePicker = new DatePicker();
        datePicker.setEditable(false);
        datePicker.setPromptText("mm/dd/yyyy");
        datePicker.setMaxWidth(Double.MAX_VALUE);
        datePicker.setPrefHeight(48);
        applyInputStyle(datePicker);

        // Keep field empty so placeholder "mm/dd/yyyy" shows up
        datePicker.setValue(null);

        // Navigate view to cycleStart month on open without pre-filling text
        datePicker.setOnShowing(e -> {
            if (datePicker.getValue() == null && cycleStart != null) {
                datePicker.setValue(cycleStart);
                Platform.runLater(() -> datePicker.setValue(null));
            }
        });

        // Dynamically filter calendar dates by focus cycle range
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setDisable(true);
                    return;
                }

                boolean isBeforeStart = (cycleStart != null) && item.isBefore(cycleStart);
                boolean isAfterEnd = (cycleEnd != null) && item.isAfter(cycleEnd);
                boolean isOutOfRange = isBeforeStart || isAfterEnd;

                if (isOutOfRange) {
                    setDisable(true);
                    setMouseTransparent(true);
                    setFocusTraversable(false);
                    setStyle(
                        "-fx-background-color: #d9d9d9; " +
                        "-fx-text-fill: #999999; " +
                        "-fx-opacity: 0.5; " +
                        "-fx-cursor: default;"
                    );
                } else {
                    setDisable(false);
                    setMouseTransparent(false);
                    setFocusTraversable(true);
                    setStyle(
                        "-fx-background-color: #fce8ac; " +
                        "-fx-text-fill: #1f2d18; " +
                        "-fx-font-family: 'Press Start 2P'; " +
                        "-fx-font-size: 10px;"
                    );
                }
            }
        });

        Label catLabel = createFieldLabel("Category");
        ComboBox<String> catCombo = new ComboBox<>();
        catCombo.getItems().addAll("Study", "Practice", "Build");
        catCombo.setPromptText("Choose a category");
        catCombo.setMaxWidth(Double.MAX_VALUE);
        catCombo.setPrefHeight(48);
        applyInputStyle(catCombo);

        VBox dateBox = new VBox(10, dateLabel, datePicker);
        VBox catBox = new VBox(10, catLabel, catCombo);

        dateBox.setMaxWidth(Double.MAX_VALUE);
        catBox.setMaxWidth(Double.MAX_VALUE);

        HBox dateCatRow = new HBox(20, dateBox, catBox);
        HBox.setHgrow(dateBox, Priority.ALWAYS);
        HBox.setHgrow(catBox, Priority.ALWAYS);

        // Lock columns to exact equal width (50% each)
        dateBox.prefWidthProperty().bind(dateCatRow.widthProperty().subtract(20).divide(2));
        catBox.prefWidthProperty().bind(dateCatRow.widthProperty().subtract(20).divide(2));

        // Difficulty Row
        Label diffLabel = createFieldLabel("Difficulty level");
        ComboBox<String> diffCombo = new ComboBox<>();
        diffCombo.getItems().addAll("Easy", "Medium", "Hard");
        diffCombo.setPromptText("Choose a level");
        diffCombo.setMaxWidth(Double.MAX_VALUE);
        diffCombo.setPrefHeight(48);
        applyInputStyle(diffCombo);

        VBox diffBox = new VBox(10, diffLabel, diffCombo);

        // Reward Banner
        HBox rewardBox = new HBox();
        rewardBox.setAlignment(Pos.CENTER_LEFT);
        rewardBox.setPadding(new Insets(18, 24, 18, 24));
        rewardBox.setStyle("-fx-background-color: #f7d168; -fx-border-color: #453018; -fx-border-width: 3px;");

        Label rewardText = new Label("Reward for completion");
        rewardText.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 11px; -fx-text-fill: #453018;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label rewardPoints = new Label("+ - points");
        rewardPoints.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 11px; -fx-text-fill: #453018;");

        // Dynamically update points based on selected difficulty
        diffCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.toLowerCase()) {
                    case "easy":
                        rewardPoints.setText("+ 20 points");
                        break;
                    case "medium":
                        rewardPoints.setText("+ 50 points");
                        break;
                    case "hard":
                        rewardPoints.setText("+ 70 points");
                        break;
                    default:
                        rewardPoints.setText("+ - points");
                        break;
                }
            } else {
                rewardPoints.setText("+ - points");
            }
        });
        rewardBox.getChildren().addAll(rewardText, spacer, rewardPoints);

        // Clear error highlights on user interaction
        nameInput.textProperty().addListener((obs, oldVal, newVal) -> applyInputStyle(nameInput));
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> applyInputStyle(datePicker));
        catCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyInputStyle(catCombo));
        diffCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyInputStyle(diffCombo));

        // Action Buttons
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 11px; -fx-background-color: #ff6b6b; -fx-text-fill: #ffffff; -fx-border-color: #453018; -fx-border-width: 3px; -fx-padding: 14 28; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> dialog.close());

        Button createBtn = new Button("+ Create mission");
        createBtn.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 11px; -fx-background-color: #f7bb43; -fx-text-fill: #1f2d18; -fx-border-color: #453018; -fx-border-width: 3px; -fx-padding: 14 28; -fx-cursor: hand;");
        createBtn.setOnAction(e -> {
            String name = nameInput.getText() != null ? nameInput.getText().trim() : "";
            String desc = descInput.getText() != null ? descInput.getText().trim() : "";
            LocalDate dueDate = datePicker.getValue();
            String category = catCombo.getValue();
            String difficulty = diffCombo.getValue();

            // Validate mandatory fields
            boolean hasError = false;
            if (name.isEmpty()) {
                nameInput.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 11px; -fx-background-color: #ffffff; -fx-border-color: #ff4d4d; -fx-border-width: 3px; -fx-padding: 12;");
                hasError = true;
            }
            if (dueDate == null) {
                datePicker.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 11px; -fx-background-color: #ffffff; -fx-border-color: #ff4d4d; -fx-border-width: 3px; -fx-padding: 12;");
                hasError = true;
            }
            if (category == null) {
                catCombo.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 11px; -fx-background-color: #ffffff; -fx-border-color: #ff4d4d; -fx-border-width: 3px; -fx-padding: 12;");
                hasError = true;
            }
            if (difficulty == null) {
                diffCombo.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 11px; -fx-background-color: #ffffff; -fx-border-color: #ff4d4d; -fx-border-width: 3px; -fx-padding: 12;");
                hasError = true;
            }

            if (hasError) {
                showIncompleteDetailsPopup(dialog);
                return;
            }

            int points = 0;
            switch (difficulty.toLowerCase()) {
                case "easy": points = 20; break;
                case "medium": points = 50; break;
                case "hard": points = 70; break;
            }

            Mission newMission = new Mission(name, desc, dueDate, category, difficulty, points);

            if (onMissionCreated != null) {
                onMissionCreated.accept(newMission);
            }

            dialog.close();
        });

        HBox actionRow = new HBox(16, cancelBtn, createBtn);
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        actionRow.setPadding(new Insets(12, 0, 0, 0));

        card.getChildren().addAll(
            headerBox,
            nameBox,
            descBox,
            dateCatRow,
            diffBox,
            rewardBox,
            actionRow
        );

        // Full Window Overlay
        StackPane overlay = new StackPane(card);
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        dialog.setX(ownerStage.getX());
        dialog.setY(ownerStage.getY());
        dialog.setWidth(ownerStage.getWidth());
        dialog.setHeight(ownerStage.getHeight());

        Scene scene = new Scene(overlay);
        scene.setFill(Color.TRANSPARENT);

        URL cssUrl = CreateMissionModal.class.getResource("/css/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    // Show Mission details incomplete
    private static void showIncompleteDetailsPopup(Stage owner) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.initStyle(StageStyle.TRANSPARENT);

        // Alert card
        VBox alertBox = new VBox(16);
        alertBox.setAlignment(Pos.TOP_LEFT);
        alertBox.setPrefWidth(600);
        alertBox.setMaxWidth(600);
        alertBox.getStyleClass().add("alert-box");
        // Fallback inline style in case CSS is not loaded
        alertBox.setStyle(
            "-fx-background-color: #fbe6af; " +
            "-fx-border-color: #4a2810; " +
            "-fx-border-width: 4px; " +
            "-fx-padding: 36px 40px; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.45), 14, 0, 6, 6);"
        );
        alertBox.setMaxHeight(VBox.USE_PREF_SIZE);

        // "NEW MISSION" badge
        Label badge = new Label("NEW MISSION");
        badge.getStyleClass().add("alert-welcome");
        badge.setStyle(
            "-fx-text-fill: #1b5230; " +
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 10px;"
        );

        // Title
        Label title = new Label("Mission details incomplete");
        title.getStyleClass().add("alert-title");
        title.setStyle(
            "-fx-text-fill: #4a2810; " +
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 18px; " +
            "-fx-padding: 4px 0 6px 0; " +
            "-fx-wrap-text: true; " +
            "-fx-text-alignment: left;"
        );
       title.setWrapText(true);

        // Message
        Label message = new Label("Please complete all mission details before creating the mission.");
        message.getStyleClass().add("alert-message");
        message.setStyle(
            "-fx-text-fill: #4a2810; " +
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 10px; " +
            "-fx-line-spacing: 10px; " +
            "-fx-wrap-text: true; " +
            "-fx-text-alignment: left;"
        );
        title.setWrapText(true);

        // OK button
        Button okBtn = new Button("OK");
        okBtn.getStyleClass().add("alert-ok-button");
        okBtn.setStyle(
            "-fx-background-color: #ffc23d; " +
            "-fx-text-fill: #4a2810; " +
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 13px; " +
            "-fx-border-color: #4a2810; " +
            "-fx-border-width: 3px; " +
            "-fx-padding: 10px 28px; " +
            "-fx-background-radius: 0; " +
            "-fx-border-radius: 0; " +
            "-fx-cursor: hand;"
        );
        okBtn.setOnMouseEntered(e -> okBtn.setStyle(
            "-fx-background-color: #f7b322; " +
            "-fx-text-fill: #4a2810; " +
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 13px; " +
            "-fx-border-color: #4a2810; " +
            "-fx-border-width: 3px; " +
            "-fx-padding: 10px 28px; " +
            "-fx-background-radius: 0; " +
            "-fx-border-radius: 0; " +
            "-fx-cursor: hand;"
        ));
        okBtn.setOnMouseExited(e -> okBtn.setStyle(
            "-fx-background-color: #ffc23d; " +
            "-fx-text-fill: #4a2810; " +
            "-fx-font-family: 'Press Start 2P'; " +
            "-fx-font-size: 13px; " +
            "-fx-border-color: #4a2810; " +
            "-fx-border-width: 3px; " +
            "-fx-padding: 10px 28px; " +
            "-fx-background-radius: 0; " +
            "-fx-border-radius: 0; " +
            "-fx-cursor: hand;"
        ));
        okBtn.setOnAction(e -> popup.close());

        HBox buttonRow = new HBox(okBtn);
        buttonRow.setAlignment(Pos.CENTER_RIGHT);
        buttonRow.setMaxWidth(Double.MAX_VALUE);
        buttonRow.setMinWidth(0);
        buttonRow.setPadding(new Insets(12, 0, 0, 0));

        alertBox.getChildren().addAll(
            badge,
            title,
            message,
            buttonRow
        );
        
        // Overlay
        StackPane overlay = new StackPane(alertBox);
        overlay.setAlignment(Pos.CENTER);
        overlay.getStyleClass().add("modal-overlay");
        overlay.setStyle("-fx-background-color: rgba(15, 35, 35, 0.65);");

        Scene scene = new Scene(overlay);
        scene.setFill(Color.TRANSPARENT);

        // Load the same CSS if available
        URL cssUrl = CreateMissionModal.class.getResource("/css/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        // Cover the whole owner window
        popup.setX(owner.getX());
        popup.setY(owner.getY());
        popup.setWidth(owner.getWidth());
        popup.setHeight(owner.getHeight());

        popup.setScene(scene);
        popup.showAndWait();
    }

    private static Label createFieldLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 10px; -fx-text-fill: #1f2d18;");
        return lbl;
    }

    private static Label createErrorPopup() {
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
        popup.setManaged(false); // Keeps layout compact until visible
        return popup;
    }

    private static void showError(Label errorLabel, PauseTransition timer, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true); 
        timer.playFromStart();
    }

    private static void applyInputStyle(Control control) {
        control.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 11px; -fx-background-color: #ffffff; -fx-border-color: #453018; -fx-border-width: 3px; -fx-padding: 12;");
    }
}