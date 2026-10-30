package com.brewcoders.ascend.view.profile;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PersonalInfoView extends VBox {
    public PersonalInfoView() {
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(new Label("Personal Information View - Assigned to Developer B"));
    }
}