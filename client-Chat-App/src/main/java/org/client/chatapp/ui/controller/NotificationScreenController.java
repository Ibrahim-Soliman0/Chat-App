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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.enums.NotificationType;
import org.client.chatapp.model.NotificationItem;
import org.client.chatapp.ui.component.ChatItemView;
import org.client.chatapp.ui.component.NotificationItemView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public class NotificationScreenController {
    @FXML
    private Group backArrow;
    @FXML
    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    private ListView<NotificationItemView> notificationListView;

    private final ObservableList<NotificationItemView> notifications = FXCollections.observableArrayList();

   public void initialize() {

        SVGPath arrowHead=new SVGPath();
        arrowHead.setContent("m12 19-7-7 7-7");
        arrowHead.getStyleClass().add("icon");

        SVGPath arrowTail=new SVGPath();
        arrowTail.setContent("M19 12H5");
        arrowTail.getStyleClass().add("icon");
        backArrow.getChildren().addAll(arrowHead,arrowTail);
        backArrow.setOnMouseEntered(e -> {
           arrowHead.getStyleClass().setAll("onIconHover");
            arrowTail.getStyleClass().setAll("onIconHover");
        });
        backArrow.setOnMouseExited(e -> {
            arrowHead.getStyleClass().setAll("icon");
            arrowTail.getStyleClass().setAll("icon");
        });
       notifications.addAll( new NotificationItemView(new NotificationItem(NotificationType.MESSAGE,
               "Sarah Miller",
               "Hey! Are you free for a call later?",
                       LocalDateTime.now().minusMinutes(5),
               true
       )),
               new NotificationItemView(new NotificationItem(NotificationType.MESSAGE,
                       "noor",
                       "David Park wants to connect with you",
                       LocalDateTime.now().minusMinutes(10),
                       true
               )),
               new NotificationItemView(new NotificationItem(NotificationType.MESSAGE,
                       "Emily Chen",
                       "The project deadline is tomorrow",
                       LocalDateTime.now().minusMinutes(2), false
               ))
               );
       notifications.sort(Comparator.comparing(
               n -> n.getNotificationItem().getTime(),
               Comparator.reverseOrder()
       ));

       notificationListView.setItems(notifications);
   }

   @FXML
    private void onBackArrowClick(MouseEvent mouseEvent){
       try {
           root = FXMLLoader.load(
                   Objects.requireNonNull(getClass().getResource("/org/client/chatapp/home-screen-view.fxml")));
       } catch (IOException e) {
           e.printStackTrace();
       }

       stage =(Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.setResizable(false);
       stage.show();
    }
}

