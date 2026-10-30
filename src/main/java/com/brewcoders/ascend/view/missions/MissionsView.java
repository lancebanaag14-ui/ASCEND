package com.brewcoders.ascend.view.missions;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MissionsView extends VBox {
    public MissionsView() {
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(new Label("Missions View - Assigned to Developer B"));
    }
}