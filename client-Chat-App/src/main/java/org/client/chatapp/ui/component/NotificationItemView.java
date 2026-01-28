package org.client.chatapp.ui.component;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import model.Notification;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.model.NotificationItem;
import org.client.chatapp.ui.utils.TimeUtils;
import rmi.NotificationService;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class NotificationItemView extends HBox {

    private final NotificationItem notificationItem;
    private final VBox rightBox = new VBox();
    private StackPane unreadBadge;
    private Timeline timeUpdater;
    private VBox textBox;
    private HBox rightContent;
    private Label time;
    private static NotificationService service;
    @FXML
    private static ListView<NotificationItemView> notificationListView;


    public NotificationItemView(NotificationItem notificationItem) {
        this.notificationItem = notificationItem;

        buildUI();
        registerHandlers();
        startTimeUpdater();
    }

    private void buildUI() {
        getStyleClass().add("chat-item");
        int characterLimit = 40;

        ImageView avatar = new ImageView(notificationItem.getProfilePic());
        avatar.setFitWidth(60);
        avatar.setFitHeight(60);
        avatar.setPreserveRatio(true);
        Circle clip = new Circle(30, 30, 30);
        avatar.setClip(clip);

        Label name = new Label(notificationItem.getName());
        name.getStyleClass().add("chat-name");

        Label message = new Label(notificationItem.getMessage().length() <= characterLimit
                ? notificationItem.getMessage() :
                notificationItem.getMessage().substring(0, characterLimit + 1) + "...");
        message.getStyleClass().add("chat-last-message");

        textBox = new VBox();
        textBox.setSpacing(4);
        textBox.getChildren().addAll(name, message);


        rightContent = new HBox(8);
        rightContent.setAlignment(Pos.CENTER_RIGHT);

        SVGPath binIcon = new SVGPath();
        binIcon.setContent("M3 6h18 M8 6v12 M16 6v12 M5 6l1 14c0 1 1 2 2 2h8c1 0 2-1 2-2l1-14");
        binIcon.getStyleClass().add("icon");
        binIcon.setOnMouseEntered(e -> binIcon.getStyleClass().setAll("onIconHover"));
        binIcon.setOnMouseExited(e -> binIcon.getStyleClass().setAll("icon"));
        binIcon.setOnMouseClicked(e -> handleDelete());


        time = new Label(TimeUtils.formatChatTimestamp(notificationItem.getTime()));
        time.getStyleClass().add("chat-time");
        if (notificationItem.isUnread()) {
            Label badgeLabel = new Label("●");
            badgeLabel.setStyle("-fx-text-fill: rgba(0,255,51,0.55); -fx-font-size: 18px;");
            unreadBadge = new StackPane(badgeLabel);
            rightContent.getChildren().add(0, unreadBadge);
        }
        rightContent.getChildren().addAll(time, binIcon);
        rightBox.getChildren().add(rightContent);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

        getChildren().addAll(avatar, textBox, rightBox);
    }

    private void registerHandlers() {
        setOnMouseClicked(e -> {
            notificationItem.setRead(true);
            if (unreadBadge != null) {
                rightContent.getChildren().remove(unreadBadge);
                unreadBadge = null;
            }
        });
    }

    public NotificationItem getNotificationItem() {
        return notificationItem;
    }

    private void startTimeUpdater() {
        timeUpdater = new Timeline(
                new KeyFrame(Duration.seconds(60), e -> updateTimestamp())
        );
        timeUpdater.setCycleCount(Timeline.INDEFINITE);
        timeUpdater.play();
    }

    private void updateTimestamp() {
        if (time != null) {
            time.setText(TimeUtils.formatChatTimestamp(notificationItem.getTime()));
        }
    }

    public void stopTimeUpdater() {
        if (timeUpdater != null) {
            timeUpdater.stop();
        }
    }
    public void handleDelete() {
        stopTimeUpdater();

        try {
            service.deleteNotification(notificationItem.getId());
            notificationListView.getItems().remove(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public static void setService(NotificationService notificationService) {
        service = notificationService;
    }

    public static void setNotificationListView(ListView<NotificationItemView> notificationListView) {
        NotificationItemView.notificationListView = notificationListView;
    }
}
