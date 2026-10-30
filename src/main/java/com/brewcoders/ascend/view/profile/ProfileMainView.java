package com.brewcoders.ascend.view.profile;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ProfileMainView extends VBox {
    public ProfileMainView() {
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(new Label("Profile Main Container - Assigned to Developer B"));
    }
}