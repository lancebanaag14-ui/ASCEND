package com.brewcoders.ascend.view.profile;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AchievementsView extends VBox {
    public AchievementsView() {
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(new Label("Achievements View - Assigned to Developer B"));
    }
}