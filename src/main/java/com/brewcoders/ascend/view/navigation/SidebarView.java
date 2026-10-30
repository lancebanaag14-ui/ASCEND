package com.brewcoders.ascend.view.navigation;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SidebarView extends VBox {
    public SidebarView() {
        this.setSpacing(15);
        this.getChildren().addAll(
            new Label("ASCEND Navigation"),
            new Label("• Missions"),
            new Label("• Profile")
        );
    }
}