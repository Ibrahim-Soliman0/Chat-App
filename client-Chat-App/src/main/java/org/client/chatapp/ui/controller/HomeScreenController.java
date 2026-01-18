package org.client.chatapp.ui.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    }

    @FXML
    private void onProfileIconClick(ActionEvent actionEvent) {

        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("profile-screen-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void onChatsIconClick(ActionEvent actionEvent) {

        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("home-screen-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
