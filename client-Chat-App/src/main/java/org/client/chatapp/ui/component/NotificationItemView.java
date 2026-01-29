package org.client.chatapp.ui.component;

import dto.BidirectionalFriendStatusDTO;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import model.Room;
import model.Users;
import model.enums.FriendStatus;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.model.NotificationItem;
import org.client.chatapp.ui.utils.ImageUtil;
import org.client.chatapp.ui.utils.TimeUtils;
import rmi.FriendRequestService;
import rmi.GetUserService;
import rmi.NotificationService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class NotificationItemView extends HBox {

    private final NotificationItem notificationItem;
    private Users me, sender;
    private Room chatRoom;
    private StackPane unreadBadge;
    private Timeline timeUpdater;
    private VBox textBox;
    private Label time;
    private Button acceptFriendRequestButton, rejectFriendRequestButton;
    private static NotificationService service;
    @FXML
    private static ListView<NotificationItemView> notificationListView;


    public NotificationItemView(NotificationItem notificationItem, Users me, Users sender, Room chatRoom) {
        this.notificationItem = notificationItem;
        this.sender = sender;
        this.me = me;
        this.chatRoom = chatRoom;

        buildUI();
        registerHandlers();
        startTimeUpdater();
    }

    private void buildUI() {
        getStyleClass().add("chat-item");
        int characterLimit = 40;

        Image profileImage = ImageUtil.getImageFromByteArray(sender != null ?
                sender.getPictureBytes() : chatRoom.getPictureBytes());

        ImageView avatar = new ImageView(profileImage);
        avatar.setFitWidth(60);
        avatar.setFitHeight(60);
//        avatar.setPreserveRatio(true);
        avatar.setSmooth(true);

        Circle clip = new Circle();
        clip.centerXProperty().bind(avatar.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(avatar.fitHeightProperty().divide(2));
        clip.radiusProperty().bind(avatar.fitWidthProperty().divide(2));
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

        VBox rightContent = new VBox(6);
        rightContent.setAlignment(Pos.CENTER_RIGHT);

        HBox topRow = new HBox(6);
        topRow.setAlignment(Pos.CENTER_RIGHT);

        HBox bottomRow = new HBox(6);
        bottomRow.setAlignment(Pos.CENTER_RIGHT);

        SVGPath binIcon = new SVGPath();
        binIcon.setContent("M3 6h18 M8 6v12 M16 6v12 M5 6l1 14c0 1 1 2 2 2h8c1 0 2-1 2-2l1-14");
        binIcon.getStyleClass().add("icon");
        binIcon.setOnMouseEntered(e -> binIcon.getStyleClass().setAll("onIconHover"));
        binIcon.setOnMouseExited(e -> binIcon.getStyleClass().setAll("icon"));
        binIcon.setOnMouseClicked(e -> handleDelete());

        time = new Label(TimeUtils.formatChatTimestamp(notificationItem.getTime()));
        time.getStyleClass().add("chat-time");
        if (!notificationItem.isRead()) {
            Label badgeLabel = new Label("●");
            badgeLabel.setStyle("-fx-text-fill: rgba(0,255,51,0.55); -fx-font-size: 18px;");
            unreadBadge = new StackPane(badgeLabel);
            topRow.getChildren().add(unreadBadge);
        }

        topRow.getChildren().addAll(time, binIcon);

        if (chatRoom == null) {
            binIcon.setVisible(false);

            acceptFriendRequestButton = new Button();
            acceptFriendRequestButton.getStyleClass().add("icon-button");
            acceptFriendRequestButton.setGraphic(getAcceptIcon());
            acceptFriendRequestButton.setOnAction(evet -> acceptFriendRequest());

            rejectFriendRequestButton = new Button();
            rejectFriendRequestButton.getStyleClass().add("icon-button");
            rejectFriendRequestButton.setGraphic(getRejectIcon());
            rejectFriendRequestButton.setOnAction(evet -> rejectFriendRequest());

            bottomRow.getChildren().addAll(acceptFriendRequestButton, rejectFriendRequestButton);
        }

        rightContent.getChildren().addAll(topRow, bottomRow);
        HBox.setHgrow(rightContent, Priority.ALWAYS);

        getChildren().addAll(avatar, textBox, rightContent);
    }

    private void registerHandlers() {
        setOnMouseClicked(e -> {
            notificationItem.setRead(true);
            if (unreadBadge != null) {
                unreadBadge.setVisible(false);
                unreadBadge = null;
            }

            // TODO: fix later so the icon changes in the database without having to continue here
            if (chatRoom == null) {
                return;
            }

            try {
                service.markNotificationAsRead(notificationItem.getId());
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private Group getAcceptIcon() {
        SVGPath bodyIcon = new SVGPath();
        bodyIcon.setContent("M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2");
        bodyIcon.getStyleClass().add("icon-black");

        Circle headIcon = new Circle(9, 7, 4);
        headIcon.getStyleClass().add("icon-black");

        SVGPath correctMarkIcon = new SVGPath();
        correctMarkIcon.setContent("m16 11 2 2 4-4");
        correctMarkIcon.getStyleClass().add("icon-black");

        Group acceptRequestIcon = new Group();
        acceptRequestIcon.getChildren().addAll(headIcon, bodyIcon, correctMarkIcon);

        return acceptRequestIcon;
    }

    private Group getRejectIcon() {
        SVGPath bodyIcon = new SVGPath();
        bodyIcon.setContent("M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2");
        bodyIcon.getStyleClass().add("icon-black");

        Circle headIcon = new Circle(9, 7, 4);
        headIcon.getStyleClass().add("icon-black");

        Line firstLine = new Line(17, 8, 22, 13);
        firstLine.getStyleClass().add("icon-black");

        Line secondLine = new Line(22, 8, 17, 13);
        secondLine.getStyleClass().add("icon-black");

        Group rejectRequestIcon = new Group();
        rejectRequestIcon.getChildren().addAll(headIcon, bodyIcon, firstLine, secondLine);

        return rejectRequestIcon;
    }

    public void acceptFriendRequest() {
        try {
            FriendRequestService friendRequestService = (FriendRequestService)
                    ClientChatApp.registry.lookup("FriendRequestService");

            friendRequestService.acceptFriendRequest(me, sender);
            service.markNotificationAsRead(notificationItem.getId());
            notificationListView.getItems().remove(this);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void rejectFriendRequest() {
        try {
            FriendRequestService friendRequestService = (FriendRequestService)
                    ClientChatApp.registry.lookup("FriendRequestService");

            friendRequestService.rejectFriendRequest(sender, me);
            service.markNotificationAsRead(notificationItem.getId());
            notificationListView.getItems().remove(this);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
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

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }

    public Room getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(Room chatRoom) {
        this.chatRoom = chatRoom;
    }
}
