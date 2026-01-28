package org.client.chatapp.ui.controller;

import dto.NotificationDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import model.Notification;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.model.NotificationItem;
import org.client.chatapp.ui.component.NotificationItemView;
import rmi.NotificationService;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class NotificationScreenController {
    private Users user;
    @FXML
    private Group backArrow;
    @FXML
    private Parent root;
    private Stage stage;
    private Scene scene;
    @FXML
    private ListView<NotificationItemView> notificationListView;

    public void initialize() {

        SVGPath arrowHead = new SVGPath();
        arrowHead.setContent("m12 19-7-7 7-7");
        arrowHead.getStyleClass().add("icon");

        SVGPath arrowTail = new SVGPath();
        arrowTail.setContent("M19 12H5");
        arrowTail.getStyleClass().add("icon");

        backArrow.getChildren().addAll(arrowHead, arrowTail);

        backArrow.setOnMouseEntered(e -> {
            arrowHead.getStyleClass().setAll("onIconHover");
            arrowTail.getStyleClass().setAll("onIconHover");
        });

        backArrow.setOnMouseExited(e -> {
            arrowHead.getStyleClass().setAll("icon");
            arrowTail.getStyleClass().setAll("icon");
        });

        NotificationItemView.setNotificationListView(notificationListView);
        // TODO: to be removed later after getting passed the actual logged in user
        user = new Users();
        user.setId(2L);
        try {

            NotificationService notificationService =
                    (NotificationService) ClientChatApp.registry.lookup("NotificationService");
            NotificationItemView.setService(notificationService);
            List<NotificationDTO> notifications = notificationService.getNotifications(user);

            List<NotificationItemView> notificationToItemView = notifications.stream()
                    .map(notificationDTO -> {
                        Notification notification = notificationDTO.getNotification();
                        NotificationItem item = new NotificationItem(notification.getId(),notification.getType(), notificationDTO.getName(),
                                // TODO: get the real picture to show later



                                notification.getContent(), notification.getCreatedAt().toLocalDateTime(), false);
                        return new NotificationItemView(item);
                    })
                    .toList();

            notificationListView.setItems(FXCollections.observableArrayList(notificationToItemView));
           // System.out.println( notificationToItemView.size());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onBackArrowClick(MouseEvent mouseEvent) {
        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/org/client/chatapp/home-screen-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().addAll(ClientChatApp.allStyles);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void setUser(Users user) {
        this.user = user;
    }
}

