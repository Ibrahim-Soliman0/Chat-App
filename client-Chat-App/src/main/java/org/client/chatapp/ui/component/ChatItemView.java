package org.client.chatapp.ui.component;

import dto.ChatRoomDTO;
import dto.MessageStatusDTO;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Message;
import model.Room;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.model.ChatItem;
import org.client.chatapp.rmi.ClientCallBackImp;
import org.client.chatapp.ui.controller.ChatRoomController;
import org.client.chatapp.ui.utils.ImageUtil;
import org.client.chatapp.ui.utils.TimeUtils;
import rmi.GetMessageService;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Objects;

public class ChatItemView extends HBox {

    private ChatRoomDTO dataToBeUsedInChat;
    private final ChatItem chatItem;
    private Users me;
    private Room chatRoom;
    private Label time;
    private VBox rightBox;
    private StackPane unreadBadge;

    public ChatItemView(ChatItem chatItem, Users me, Room chatRoom, ChatRoomDTO dataToBeUsedInChat) {
        this.chatItem = chatItem;
        this.me = me;
        this.chatRoom = chatRoom;
        this.dataToBeUsedInChat = dataToBeUsedInChat;

        time = new Label(TimeUtils.formatChatTimestamp(chatItem.getMessageTime()));
        time.getStyleClass().add("chat-time");

        buildUI();
        registerHandlers();
    }

    private void buildUI() {
        getStyleClass().add("chat-item");
        int characterLimit = 50;

        Image profileImage = ImageUtil.getImageFromByteArray(
                dataToBeUsedInChat.getOther() != null ?
                        dataToBeUsedInChat.getOther().getPictureBytes() :
                        chatRoom.getPictureBytes());

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

        Label name = new Label(chatItem.getName());
        name.getStyleClass().add("chat-name");

        String messageText = (chatItem.isIncoming() ? "" : "You: ") + chatItem.getLastMessage();
        Label lastMessage = new Label(messageText.length() <= characterLimit ? messageText
                : messageText.substring(0, characterLimit + 1) + "...");
        lastMessage.getStyleClass().add("chat-last-message");

        VBox textBox = new VBox(name, lastMessage);
        textBox.setSpacing(4);

        unreadBadge = new StackPane();
        if (!chatItem.getUnreadMessageIds().isEmpty()) {
            Label unread = new Label(!chatItem.getUnreadMessageIds().isEmpty()
                    ? String.valueOf(chatItem.getUnreadMessageIds().size()) : "");

            int count = chatItem.getUnreadMessageIds().size();
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
        if (!chatItem.getUnreadMessageIds().isEmpty()) {
            rightBox.getChildren().add(unreadBadge);
        }

        rightBox.setSpacing(3);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(rightBox, Priority.ALWAYS);
        getChildren().addAll(avatar, textBox, rightBox);
    }

    private void registerHandlers() {
        setOnMouseClicked(mouseEvent -> {

            try {
                ClientCallBackImp.setHomeScreenListener(null);
                ClientCallBackImp.setNotificationScreenListener(null);
                GetMessageService getMessageService =
                        (GetMessageService) ClientChatApp.registry.lookup("GetMessageService");

                rightBox.getChildren().remove(unreadBadge);

                for (Long messageId : chatItem.getUnreadMessageIds()) {
                    Message message = new Message();
                    message.setId(messageId);
                    MessageStatusDTO messageStatusDTO = new MessageStatusDTO(dataToBeUsedInChat.getMe(),
                            message, LocalDateTime.now(), dataToBeUsedInChat.getRoom().getId());
                    getMessageService.setMessageStatusAsSeen(messageStatusDTO);
                }

                chatItem.getUnreadMessageIds().clear();
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }

            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(
                        Objects.requireNonNull(getClass().getResource("/org/client/chatapp/chat-room-view.fxml")));

                root = loader.load();
                ChatRoomController chatRoomController = loader.getController();
                chatRoomController.initializeChat(dataToBeUsedInChat);
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

