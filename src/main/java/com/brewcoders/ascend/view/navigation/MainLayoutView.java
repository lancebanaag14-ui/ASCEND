package com.brewcoders.ascend.view.navigation;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainLayoutView extends BorderPane {

    private VBox centerContainer;

    public MainLayoutView() {
        // 1. Initialize empty center container for dynamic sub-views
        centerContainer = new VBox();
        
        // 2. Preserve responsiveness rules (growth priority & infinite max dimensions)
        VBox.setVgrow(centerContainer, Priority.ALWAYS);
        centerContainer.setMaxWidth(Double.MAX_VALUE);
        centerContainer.setMaxHeight(Double.MAX_VALUE);

        // 3. Set center layout
        this.setCenter(centerContainer);
    }

    //Call this method whenever you want to switch the view in the center container!
     
    public VBox getCenterContainer() {
        return centerContainer;
    }
}