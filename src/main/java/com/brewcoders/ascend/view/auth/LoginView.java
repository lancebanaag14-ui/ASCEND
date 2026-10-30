package com.brewcoders.ascend.view.auth;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LoginView extends VBox {
    public LoginView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.getChildren().add(new Label("Login Page"));
    }
}