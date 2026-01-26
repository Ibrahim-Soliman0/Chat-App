package org.client.chatapp.ui.component;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Room;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.model.ChatItem;
import org.client.chatapp.ui.utils.TimeUtils;

import java.io.IOException;
import java.util.Objects;

public class ChatItemView extends HBox {

    private final ChatItem chatItem;
    private Users me;
    private Room chatRoom;
    private Label time;
    private VBox rightBox;
    private StackPane unreadBadge;

    public ChatItemView(ChatItem chatItem, Users me, Room chatRoom) {
        this.chatItem = chatItem;
        this.me = me;
        this.chatRoom = chatRoom;

        time = new Label(TimeUtils.formatChatTimestamp(chatItem.getMessageTime()));
        time.getStyleClass().add("chat-time");

        buildUI();
        registerHandlers();
    }

    private void buildUI() {
        getStyleClass().add("chat-item");
        int characterLimit = 50;

        ImageView avatar = new ImageView(chatItem.getProfilePic());
        avatar.setFitWidth(60);
        avatar.setFitHeight(60);
        avatar.setPreserveRatio(true);

        Circle clip = new Circle(30, 30, 24);
        avatar.setClip(clip);

        Label name = new Label(chatItem.getName());
        name.getStyleClass().add("chat-name");

        Label lastMessage = new Label(chatItem.getLastMessage().length() <= characterLimit
                ? chatItem.getLastMessage() :
                chatItem.getLastMessage().substring(0, characterLimit + 1) + "...");
        lastMessage.getStyleClass().add("chat-last-message");

        VBox textBox = new VBox(name, lastMessage);
        textBox.setSpacing(4);

        unreadBadge = new StackPane();
        if (chatItem.getUnreadMessageCount() > 0) {

            Label unread = new Label(chatItem.getUnreadMessageCount() > 0
                    ? String.valueOf(chatItem.getUnreadMessageCount()) : "");

            int count = chatItem.getUnreadMessageCount();
            unread.setText(count > 99 ? "99+" : String.valueOf(count));
            unread.setPrefSize(26, 26);
            unreadBadge.setMinSize(26, 26);
            unreadBadge.setMaxSize(26, 26);
            unread.getStyleClass().add("unread-message-count");

            Circle badgeCircle = new Circle(14);
            badgeCircle.setFill(Color.web("#00ab8a"));

            unreadBadge.getChildren().addAll(badgeCircle, unread);
            unreadBadge.setPrefSize(26, 26);
            unreadBadge.setMinSize(26, 26);
            unreadBadge.setMaxSize(26, 26);
            unreadBadge.setAlignment(Pos.CENTER_RIGHT);
        }

        rightBox = new VBox(time);
        if (chatItem.getUnreadMessageCount() > 0) {
            rightBox.getChildren().add(unreadBadge);
        }

        rightBox.setSpacing(3);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(rightBox, Priority.ALWAYS);
        getChildren().addAll(avatar, textBox, rightBox);
    }

    private void registerHandlers() {
        setOnMouseClicked(mouseEvent -> {

            chatItem.setUnreadMessageCount(0);
            rightBox.getChildren().remove(unreadBadge);

            System.out.println("Open chat: " + chatItem.getName());

            Parent root = null;
            try {
                root = FXMLLoader.load(
                        Objects.requireNonNull(getClass().getResource("chat-room-view.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(ClientChatApp.allStyles);
            stage.setScene(scene);
            stage.show();
        });
    }

    public ChatItem getChatItem() {
        return chatItem;
    }

    public void updateTimestamp() {
        time.setText(TimeUtils.formatChatTimestamp(chatItem.getMessageTime()));
    }

    public Users getMe() {
        return me;
    }

    public void setMe(Users me) {
        this.me = me;
    }

    public Room getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(Room chatRoom) {
        this.chatRoom = chatRoom;
    }
}

