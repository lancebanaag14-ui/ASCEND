package com.brewcoders.ascend.view.profile;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HistoryView extends VBox {
    public HistoryView() {
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(new Label("History View - Assigned to Developer B"));
    }
}