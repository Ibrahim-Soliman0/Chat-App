package org.client.chatapp.ui.controller;

import dto.ChatRoomDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.Users;
import model.Message;
import org.client.chatapp.ClientChatApp;
import rmi.GetMessageService;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class ChatRoomController {

    @FXML
    private Group backButton;

    @FXML
    private SVGPath backArrow1;

    @FXML
    private SVGPath backArrow2;

    @FXML
    private Label chatUserName;

    @FXML
    private Label userStatus;

    @FXML
    private ScrollPane messagesScrollPane;

    @FXML
    private VBox messagesContainer;

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendButton;

    private Users currentUser;
    private Users otherUser;
    private ChatRoomDTO chatRoomDTO;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void initializeChat(ChatRoomDTO chatRoomDTO) {
        this.currentUser = chatRoomDTO.getMe();
        this.chatRoomDTO = chatRoomDTO;
        this.otherUser = chatRoomDTO.getOther();

        // Set the other user's name
        chatUserName.setText(otherUser.getName());

        // Set user status
        updateUserStatus();

        // Load messages
        loadMessages();

        // Auto-scroll to bottom
        Platform.runLater(() -> messagesScrollPane.setVvalue(1.0));
    }

    private void updateUserStatus() {
        String status = String.valueOf(otherUser.getStatus());
        if (status != null) {
            switch (status) {
                case "ONLINE":
                    userStatus.setText("Online");
                    userStatus.setTextFill(Color.web("#25D366"));
                    break;
                case "AWAY":
                    userStatus.setText("Away");
                    userStatus.setTextFill(Color.web("#FFA500"));
                    break;
                case "BUSY":
                    userStatus.setText("Busy");
                    userStatus.setTextFill(Color.web("#FF0000"));
                    break;
                case "OFFLINE":
                    userStatus.setText("Offline");
                    userStatus.setTextFill(Color.web("#808080"));
                    break;
                default:
                    userStatus.setText("Unknown");
                    userStatus.setTextFill(Color.web("#808080"));
            }
        }
    }

    private void loadMessages() {
        try {
            GetMessageService getMessageService =
                    (GetMessageService) ClientChatApp.registry.lookup("GetMessageService");
            List<Message> messages = getMessageService.getRoomMessages(chatRoomDTO.getRoom().getId());

            messagesContainer.getChildren().clear();

            for (Message message : messages) {
                addMessageToUI(message);
            }

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            showError("Failed to load messages");
        }
    }

    private void addMessageToUI(Message message) {
        boolean isSentByMe = message.getSenderId() == currentUser.getId();

        // Create message bubble
        HBox messageBox = new HBox();
        messageBox.setPadding(new Insets(5, 10, 5, 10));
        messageBox.setAlignment(isSentByMe ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        // Message content
        VBox messageBubble = new VBox(5);
        messageBubble.setMaxWidth(300);
        messageBubble.setPadding(new Insets(10));
        messageBubble.setStyle(
                isSentByMe
                        ? "-fx-background-color: #00ab8a; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
                        : "-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        // Message text
        TextFlow messageText = new TextFlow();
        Text text = new Text(message.getText());
        text.setFill(isSentByMe ? Color.WHITE : Color.BLACK);
        text.setFont(Font.font("System", 14));
        messageText.getChildren().add(text);

        // Timestamp
        Label timestamp = new Label(formatTimestamp(message.getSentAt()));
        timestamp.setFont(Font.font("System", 10));
        timestamp.setTextFill(isSentByMe ? Color.web("#E0E0E0") : Color.web("#808080"));
        timestamp.setAlignment(Pos.CENTER_RIGHT);

        messageBubble.getChildren().addAll(messageText, timestamp);
        messageBox.getChildren().add(messageBubble);

        messagesContainer.getChildren().add(messageBox);
    }

    private String formatTimestamp(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(formatter);
    }

    @FXML
    private void onSendButtonClick() {
        String messageText = messageInput.getText().trim();
        if (!messageText.isEmpty()) {
            // TODO: Implement sending message via RMI
            // For now, just clear the input
            messageInput.clear();

            // You would call something like:
            // SendMessageService sendMessageService =
            //     (SendMessageService) ClientChatApp.registry.lookup("SendMessageService");
            // Message newMessage = sendMessageService.sendMessage(
            //     currentUser.getId(), chatRoomDTO.getRoom().getId(), messageText);
            // addMessageToUI(newMessage);

            showInfo("Message sending not yet implemented");
        }
    }

    @FXML
    private void onBackButtonClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/org/client/chatapp/home-screen-view.fxml")));
            root = loader.load();
            HomeScreenController homeScreenController = loader.getController();
            homeScreenController.setUser(currentUser);

            stage = (Stage) backButton.getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().addAll(ClientChatApp.allStyles);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to return to home screen");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}