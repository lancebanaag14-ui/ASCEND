package com.brewcoders.ascend.view.profile;

import com.brewcoders.ascend.controller.MissionManager;
import com.brewcoders.ascend.model.Mission;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HistoryView extends VBox {

    public HistoryView() {
        this.setSpacing(12);
        this.setMaxWidth(Double.MAX_VALUE);

        // Header Label
        Label sortLabel = new Label("Sorted by earliest dates");
        sortLabel.getStyleClass().add("history-sort-label");

        // Outer Retro Container (Yellow Box)
        VBox historyCardContainer = new VBox(16);
        historyCardContainer.getStyleClass().add("history-card-container");
        historyCardContainer.setPadding(new Insets(16)); // Balanced 16px padding on all sides

        // Load finished/failed missions dynamically from MissionManager
        List<Mission> historyItems = MissionManager.getInstance().getHistoryMissions();

        if (historyItems.isEmpty()) {
            Label emptyLabel = new Label("No mission history yet.");
            emptyLabel.getStyleClass().add("history-empty-text");
            emptyLabel.setPadding(new Insets(30, 0, 30, 0));

            VBox emptyWrapper = new VBox(emptyLabel);
            emptyWrapper.setAlignment(Pos.CENTER);
            historyCardContainer.getChildren().add(emptyWrapper);
        } else {
            for (Mission item : historyItems) {
                historyCardContainer.getChildren().add(createHistoryItemRow(item));
            }
        }

        this.getChildren().addAll(sortLabel, historyCardContainer);
    }

    private HBox createHistoryItemRow(Mission item) {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("history-item-row");
        row.setPadding(new Insets(14, 20, 14, 20));

        boolean isCompleted = item.isCompleted();

        // Status Icon Box (Green Checkmark or Red X)
        StackPane iconBox = new StackPane();
        iconBox.getStyleClass().add(isCompleted ? "status-icon-box-success" : "status-icon-box-failed");

        Label iconLabel = new Label(isCompleted ? "✓" : "✕");
        iconLabel.getStyleClass().add(isCompleted ? "status-icon-text-success" : "status-icon-text-failed");
        iconBox.getChildren().add(iconLabel);

        // Details Column (Name & Category/Due Date)
        VBox detailsBox = new VBox(4);
        Label titleLabel = new Label(item.getName());
        titleLabel.getStyleClass().add("history-item-title");

        Label subLabel = new Label(item.getCategory() + " - Due " + item.getFormattedDueDate());
        subLabel.getStyleClass().add("history-item-subtext");

        detailsBox.getChildren().addAll(titleLabel, subLabel);
        HBox.setHgrow(detailsBox, Priority.ALWAYS);

        // Points Label
        Label pointsLabel = new Label(isCompleted ? "+ " + item.getPoints() + " points" : "No points");
        pointsLabel.getStyleClass().add("history-item-points");

        // Status Badge Pill (Completed / Failed)
        Label statusBadge = new Label(isCompleted ? "Completed" : "Failed");
        statusBadge.getStyleClass().add(isCompleted ? "status-badge-completed" : "status-badge-failed");

        row.getChildren().addAll(iconBox, detailsBox, pointsLabel, statusBadge);
        return row;
    }
}