package org.client.chatapp.ui.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.model.ChatItem;
import org.client.chatapp.ui.component.ChatItemView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class HomeScreenController {

    @FXML
    private Group profileIcon, chatsIcon;
    @FXML
    private ListView<ChatItemView> chatsList;
    private Parent root;
    private Stage stage;
    private Scene scene;

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
        profileHeadIcon.getStyleClass().add("icon");

        SVGPath profileBodyIcon = new SVGPath();
        profileBodyIcon.setContent("M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2");
        profileBodyIcon.getStyleClass().add("icon");

        profileIcon.getChildren().addAll(profileHeadIcon, profileBodyIcon);
        profileIcon.setScaleX(1.75);
        profileIcon.setScaleY(1.75);

        profileIcon.setOnMouseEntered(e -> {
            profileHeadIcon.getStyleClass().setAll("onIconHover");
            profileBodyIcon.getStyleClass().setAll("onIconHover");
        });

        profileIcon.setOnMouseExited(e -> {
            profileHeadIcon.getStyleClass().setAll("icon");
            profileBodyIcon.getStyleClass().setAll("icon");
        });

        SVGPath chatsSvg = new SVGPath();
        chatsSvg.setContent("M2.992 16.342a2 2 0 0 1 .094 1.167l-1.065 3.29a1 1 0 0 0 1.236 1.168l3.413-.998a2 2 0 0 1 1.099.092 10 10 0 1 0-4.777-4.719");
        chatsSvg.getStyleClass().add("selectedIcon");

        chatsIcon.getChildren().add(chatsSvg);
        chatsIcon.setScaleX(1.5);
        chatsIcon.setScaleY(1.5);

        ObservableList<ChatItemView> chats = FXCollections.observableArrayList();

        chats.add(new ChatItemView(new ChatItem(
                "Alice",
                "Hey!",
                true,
                LocalDateTime.now().minusDays(7),
                3
        )));

        chats.add(new ChatItemView(new ChatItem(
                "Alice",
                "Heyyy",
                true,
                LocalDateTime.now().minusDays(7),
                3
        )));

        chats.add(new ChatItemView(new ChatItem(
                "Bob",
                "See you later",
                false,
                LocalDateTime.now().minusMinutes(5),
                0
        )));

        chats.add(new ChatItemView(new ChatItem(
                "Ibrahim",
                "See you later ajsdlkja lasd jaslkdj alkslk asd asd asdas as",
                false,
                LocalDateTime.now().minusMinutes(5),
                90
        )));

        chats.add(new ChatItemView(new ChatItem(
                "Ibrahim",
                "See you later ajsdlkja lasd jaslkdj alkslk asd asd asdas as",
                true,
                LocalDateTime.now(),
                100
        )));

        chatsList.setItems(chats);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(60),
                e -> chatsList.getItems().forEach(ChatItemView::updateTimestamp)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        SVGPath bell = new SVGPath();
        bell.setContent("M3.262 15.326A1 1 0 0 0 4 17h16a1 1 0 0 0 .74-1.673" +
                "C19.41 13.956 18 12.499 18 8" +
                "A6 6 0 0 0 6 8" +
                "c0 4.499-1.411 5.956-2.738 7.326");
        bell.getStyleClass().add("icon");

        SVGPath bellLine = new SVGPath();
        bellLine.setContent("M10.268 21a2 2 0 0 0 3.464 0");
        bellLine.getStyleClass().add("icon");

        bellIcon.getChildren().addAll(bell, bellLine);
        bellIcon.setScaleX(1.1);
        bellIcon.setScaleY(1.1);

        bellIcon.setOnMouseEntered(e -> {
            bell.getStyleClass().setAll("onIconHover");
            bellLine.getStyleClass().setAll("onIconHover");
        });

        bellIcon.setOnMouseExited(e -> {
            bell.getStyleClass().setAll("icon");
            bellLine.getStyleClass().setAll("icon");

        });


        Circle searchHeadIcon = new Circle(11, 11, 8);
        searchHeadIcon.setFill(Color.TRANSPARENT);
        searchHeadIcon.setStroke(Color.web("#abacad"));
        searchHeadIcon.setStrokeWidth(2);

        SVGPath searchLine = new SVGPath();
        searchLine.setContent("m21 21-4.34-4.34");
        searchLine.setStroke(Color.web("#abacad"));
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
        StackPane.setAlignment(searchIconGroup, Pos.CENTER_LEFT);
        StackPane.setMargin(searchIconGroup, new Insets(0, 0, 0, 8));

        Circle groupHeadIcon = new Circle(10, 8, 5);
        groupHeadIcon.getStyleClass().add("icon");

        SVGPath groupBodyIcon = new SVGPath();
        groupBodyIcon.setContent("M22 20c0-3.37-2-6.5-4-8a5 5 0 0 0-.45-8.3");
        groupBodyIcon.getStyleClass().add("icon");

        SVGPath otherBodyIcon = new SVGPath();
        otherBodyIcon.setContent("M18 21a8 8 0 0 0-16 0");
        otherBodyIcon.getStyleClass().add("icon");
        groupIcon.getChildren().addAll(groupHeadIcon, groupBodyIcon, otherBodyIcon);
        groupIcon.setScaleX(1.1);
        groupIcon.setScaleY(1.1);
        groupIcon.setOnMouseEntered(e -> {
            groupHeadIcon.getStyleClass().setAll("onIconHover");
            groupBodyIcon.getStyleClass().setAll("onIconHover");
            otherBodyIcon.getStyleClass().setAll("onIconHover");

        });
        groupIcon.setOnMouseExited(e -> {
            groupHeadIcon.getStyleClass().setAll("icon");
            groupBodyIcon.getStyleClass().setAll("icon");
            otherBodyIcon.getStyleClass().setAll("icon");

        });


        Circle addFriendHead = new Circle(9, 7, 4);
        addFriendHead.getStyleClass().add("icon");

        Line vLine = new Line(19, 8, 19, 14);
        vLine.getStyleClass().add("icon");

        Line hLine = new Line(22, 11, 16, 11);
        hLine.getStyleClass().add("icon");

        SVGPath addFriendBody = new SVGPath();
        addFriendBody.setContent("M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2");
        addFriendBody.getStyleClass().add("icon");

        addFriend.getChildren().addAll(addFriendHead, vLine, hLine, addFriendBody);
        addFriend.setScaleX(1.1);
        addFriend.setScaleY(1.1);
        addFriend.setOnMouseEntered(e -> {
            addFriendHead.getStyleClass().setAll("onIconHover");
            vLine.getStyleClass().setAll("onIconHover");
            hLine.getStyleClass().setAll("onIconHover");
            addFriendBody.getStyleClass().setAll("onIconHover");

        });

        addFriend.setOnMouseExited(e -> {
            addFriendHead.getStyleClass().setAll("icon");
            vLine.getStyleClass().setAll("icon");
            hLine.getStyleClass().setAll("icon");
            addFriendBody.getStyleClass().setAll("icon");

        });

    }

    @FXML
    private void onProfileIconClick(MouseEvent actionEvent) {

        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/profile-screen-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().addAll(ClientChatApp.allStyles);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void onChatsIconClick(MouseEvent actionEvent) {

        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/home-screen-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().addAll(ClientChatApp.allStyles);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void onBellIconClick(MouseEvent actionEvent) {

        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/notification-screen-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().addAll(ClientChatApp.allStyles);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void onGroupIconClick(MouseEvent actionEvent) {

        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/group-screen-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().addAll(ClientChatApp.allStyles);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void onAddFriendIconClick(MouseEvent actionEvent) {

        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/friends-list-screen-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().addAll(ClientChatApp.allStyles);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
