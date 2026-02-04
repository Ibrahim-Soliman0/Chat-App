package org.client.chatapp.ui.controller;

import dto.NotificationDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import model.Notification;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.model.NotificationItem;
import org.client.chatapp.rmi.ClientCallBackImp;
import org.client.chatapp.ui.component.NotificationItemView;
import org.client.chatapp.ui.listener.NotificationListener;
import rmi.NotificationService;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class NotificationScreenController implements NotificationListener {
    private Users user;
    @FXML
    private Group backArrow;
    @FXML
    private Parent root;
    private Stage stage;
    private Scene scene;
    @FXML
    private ListView<NotificationItemView> notificationListView;
    private ObservableList<NotificationItemView> notificationItems;
    @FXML
    private Group emptyStateIcon;
    @FXML
    private Label emptyStateLabel;

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
        ClientCallBackImp.setNotificationScreenListener(this);
        notificationItems = FXCollections.observableArrayList();
        notificationListView.setItems(notificationItems);
    }

    @FXML
    private void onBackArrowClick(MouseEvent mouseEvent) {
        ClientCallBackImp.setNotificationScreenListener(null);
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/org/client/chatapp/home-screen-view.fxml")));

            root = loader.load();
            HomeScreenController homeScreenController = loader.getController();
            homeScreenController.setUser(user);
            homeScreenController.clearNotifications();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().addAll(ClientChatApp.allStyles);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void setUser(Users user) {
        this.user = user;

        onNewNotification();
    }

    @Override
    public void onNewNotification() {
        try {
            NotificationService notificationService =
                    (NotificationService) ClientChatApp.registry.lookup("NotificationService");
            List<NotificationDTO> notifications = notificationService.getNotifications(user);

            List<NotificationItemView> notificationToItemView = notifications.stream()
                    .map(notificationDTO -> {
                        Notification notification = notificationDTO.getNotification();
                        NotificationItem item = new NotificationItem(
                                notification.getId(), notification.getType(), notificationDTO.getName(),
                                notification.getContent(), notification.getCreatedAt().toLocalDateTime(),
                                false);

                        return new NotificationItemView(
                                item, user, notificationDTO.getSender(), notificationDTO.getRoom());
                    })
                    .toList();

            NotificationItemView.setService(notificationService);

            if (!notificationToItemView.isEmpty()) {
                notificationItems.setAll(notificationToItemView);
            }

            if (!notificationListView.getItems().isEmpty()) {
                emptyStateIcon.setVisible(false);
                emptyStateLabel.setVisible(false);
            }
            else {
                emptyStateIcon.setVisible(true);
                emptyStateLabel.setVisible(true);
            }
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}

