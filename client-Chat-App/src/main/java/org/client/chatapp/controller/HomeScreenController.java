package org.client.chatapp.controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.shape.Line;


public class HomeScreenController {

    @FXML
    private Group profileIcon;

    @FXML
    private Group bellIcon;

    @FXML
    private TextField searchBar;

    @FXML
    private Group searchIconGroup;

    @FXML
    private Circle searchHeadIcon;

    @FXML
    private SVGPath searchLine;

    @FXML
    private StackPane searchContainer;

    @FXML
    private Group addFriend;
    @FXML
    private Label chatLabel;
    @FXML
    private SVGPath chatLogo;
    @FXML
    private Group groupIcon;


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

        SVGPath bell = new SVGPath();
        bell.setContent("M3.262 15.326A1 1 0 0 0 4 17h16a1 1 0 0 0 .74-1.673" +
                "C19.41 13.956 18 12.499 18 8" +
                "A6 6 0 0 0 6 8" +
                "c0 4.499-1.411 5.956-2.738 7.326");
        bell.setStroke(Color.web("BLACK"));
        bell.setFill(Color.TRANSPARENT);
        bell.setStrokeWidth((2));

        SVGPath bellLine = new SVGPath();
        bellLine.setContent("M10.268 21a2 2 0 0 0 3.464 0");
        bellLine.setStroke(Color.web("BLACK"));
        bellLine.setFill(Color.TRANSPARENT);
        bellLine.setStrokeWidth((2));


        bellIcon.getChildren().addAll(bell ,bellLine);

        Circle searchHeadIcon = new Circle(11, 11, 8);
        searchHeadIcon.setFill(Color.TRANSPARENT);
        searchHeadIcon.setStroke(Color.web("BLACK"));
        searchHeadIcon.setStrokeWidth(2);

        SVGPath searchLine = new SVGPath();
        searchLine.setContent("m21 21-4.34-4.34");
        searchLine.setStroke(Color.web("BLACK"));
        searchLine.setStrokeWidth(2);
        searchIconGroup.getChildren().addAll(searchHeadIcon, searchLine);

        searchBar.setPromptText("Search conversations...");
        searchBar.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: rgba(21,0,10,0.68); " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 0 0 0 35px; " +
                        "-fx-prompt-text-fill: #abacad;"
        );

        Parent parent = searchIconGroup.getParent();
        if (parent instanceof StackPane) {
            StackPane stackPane = (StackPane) parent;

            StackPane.setAlignment(searchIconGroup, Pos.CENTER_LEFT);
            StackPane.setMargin(searchIconGroup, new Insets(0, 0, 0, 8));


        }
        Circle groupHeadIcon =new Circle(10,8,5);
        groupHeadIcon.setFill(Color.TRANSPARENT);
        groupHeadIcon.setStroke(Color.web("BLACK"));
        groupHeadIcon.setStrokeWidth(2);

        SVGPath groupBodyIcon=new SVGPath();
        groupBodyIcon.setContent("M22 20c0-3.37-2-6.5-4-8a5 5 0 0 0-.45-8.3");
        groupBodyIcon.setFill(Color.TRANSPARENT);
        groupBodyIcon.setStroke(Color.web("BLACK"));
        groupBodyIcon.setStrokeWidth(2);

        SVGPath otherBodyIcon=new SVGPath();
        otherBodyIcon.setContent("M18 21a8 8 0 0 0-16 0");
        otherBodyIcon.setFill(Color.TRANSPARENT);
        otherBodyIcon.setStroke(Color.web("BLACK"));
        otherBodyIcon.setStrokeWidth(2);
        groupIcon.getChildren().addAll(groupHeadIcon,groupBodyIcon,otherBodyIcon);


        Circle addFriendHead=new Circle(9,7,4);
        addFriendHead.setFill(Color.TRANSPARENT);
        addFriendHead.setStroke(Color.web("BLACK"));
        addFriendHead.setStrokeWidth(2);

        Line vLine=new Line(19,8,19,14);
        vLine.setStroke(Color.web("BLACK"));
        vLine.setStrokeWidth(2);

        Line  hLine=new Line(22,11,16,11);
        hLine.setStroke(Color.web("BLACK"));
        hLine.setStrokeWidth(2);

        SVGPath addFriendBody=new SVGPath();
        addFriendBody.setContent("M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2");
        addFriendBody.setFill(Color.TRANSPARENT);
        addFriendBody.setStroke(Color.web("BLACK"));
        addFriendBody.setStrokeWidth(2);

        addFriend.getChildren().addAll(addFriendHead,vLine,hLine,addFriendBody);
    }

}
