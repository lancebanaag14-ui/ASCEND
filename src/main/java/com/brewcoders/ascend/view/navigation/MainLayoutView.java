package com.brewcoders.ascend.view.navigation;

import javafx.scene.layout.BorderPane;

public class MainLayoutView extends BorderPane {
    public MainLayoutView() {
        // Sets the sidebar on the left side of the main app container
        this.setLeft(new SidebarView());
    }
}