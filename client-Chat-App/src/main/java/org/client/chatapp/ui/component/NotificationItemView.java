package org.client.chatapp.ui.component;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import org.client.chatapp.model.NotificationItem;
import org.client.chatapp.ui.utils.TimeUtils;

public class NotificationItemView extends HBox {

    private final NotificationItem notificationItem;
    private final VBox rightBox = new VBox();
    private StackPane unreadBadge;
    private Timeline timeUpdater;
    private VBox textBox;
    private HBox rightContent;
    private Label time;

    public NotificationItemView(NotificationItem notificationItem) {
        this.notificationItem = notificationItem;
        buildUI();
        registerHandlers();
        startTimeUpdater();
    }

    private void buildUI() {
        setSpacing(15);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("notification-item");

        ImageView avatar = new ImageView(notificationItem.getProfilePic());
        avatar.setFitWidth(60);
        avatar.setFitHeight(60);
        avatar.setPreserveRatio(true);
        Circle clip = new Circle(30, 30, 30); // full circle
        avatar.setClip(clip);

        Label name = new Label(notificationItem.getName());
        name.getStyleClass().add("notification-title");

        Label message = new Label(notificationItem.getMessage());
        message.getStyleClass().add("notification-message");

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

        time = new Label(TimeUtils.formatChatTimestamp(notificationItem.getTime()));
        time.getStyleClass().add("notification-time");
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

}
