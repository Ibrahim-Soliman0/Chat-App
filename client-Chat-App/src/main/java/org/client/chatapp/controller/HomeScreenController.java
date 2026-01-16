package org.client.chatapp.controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;

public class HomeScreenController {

    @FXML
    private Group profileIcon;

    public void initialize() {
        Circle profileHeadIcon = new Circle(12, 7, 4);
        profileHeadIcon.setStroke(Color.web("#abacad"));
        profileHeadIcon.setFill(Color.TRANSPARENT);
        profileHeadIcon.setStrokeWidth(2);

        SVGPath profileBodyIcon = new SVGPath();
        profileBodyIcon.setContent("M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2");
        profileBodyIcon.setStroke(Color.web("#abacad"));
        profileBodyIcon.setFill(Color.TRANSPARENT);
        profileBodyIcon.setStrokeWidth(2);

        profileIcon.getChildren().addAll(profileHeadIcon, profileBodyIcon);
        profileIcon.setScaleX(1.5);
        profileIcon.setScaleY(1.5);
    }
}
