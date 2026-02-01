package org.client.chatapp.rmi;

import dto.ChatRoomDTO;
import dto.NotificationDTO;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.web.WebView;
import javafx.stage.Window;
import javafx.util.Duration;
import org.client.chatapp.ui.listener.NotificationListener;
import org.controlsfx.control.Notifications;
import rmi.ClientCallBack;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.client.chatapp.ui.controller.ChatRoomController;

public class ClientCallBackImp extends UnicastRemoteObject implements ClientCallBack {

    public static NotificationListener homeScreenListener, notificationScreenListener;

    public ClientCallBackImp() throws RemoteException {
    }

    @Override
    public void receiveAnnouncement(String title, String htmlContent) throws RemoteException {
        Platform.runLater(() -> {
            String readonlyHtml = htmlContent.replace("contenteditable=\"true\"", "contenteditable=\"false\"");

            WebView webView = new WebView();
            webView.getEngine().loadContent(readonlyHtml);
            webView.setPrefSize(400, 300);
            webView.setContextMenuEnabled(false);
            Window currentWindow = Window.getWindows().stream()
                    .filter(Window::isShowing)
                    .findFirst()
                    .orElse(null);

            Notifications notificationBuilder = Notifications.create()
                    .title(title)
                    .text("Click here to see more...")
                    .graphic(null)
                    .owner(currentWindow)
                    .hideAfter(Duration.seconds(7))
                    .position(Pos.BOTTOM_RIGHT)
                    .onAction(e -> {
                        Alert detailAlert = new Alert(Alert.AlertType.INFORMATION);
                        detailAlert.setTitle(title);
                        detailAlert.setHeaderText(null);
                        detailAlert.getDialogPane().setContent(webView);
                        detailAlert.showAndWait();
                    });

            notificationBuilder.showInformation();
        });
    }

    @Override
    public void receiveMessage(ChatRoomDTO chatRoomDTO) throws RemoteException {
        Platform.runLater(() -> {
            ChatRoomController chatRoomController = new ChatRoomController();
            chatRoomController.initializeChat(chatRoomDTO);
            chatRoomController.loadMessages();
        });
    }

    @Override
    public void receiveNotification() throws RemoteException {
        Platform.runLater(() -> {
            if (homeScreenListener != null) {
                homeScreenListener.onNewNotification();
            }
            else if (notificationScreenListener != null) {
                notificationScreenListener.onNewNotification();
            }
        });
    }

    public static NotificationListener getHomeScreenListener() {
        return homeScreenListener;
    }

    public static void setHomeScreenListener(NotificationListener homeScreenListener) {
        ClientCallBackImp.homeScreenListener = homeScreenListener;
    }

    public static NotificationListener getNotificationScreenListener() {
        return notificationScreenListener;
    }

    public static void setNotificationScreenListener(NotificationListener notificationScreenListener) {
        ClientCallBackImp.notificationScreenListener = notificationScreenListener;
    }
}
