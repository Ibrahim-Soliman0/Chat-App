package org.client.chatapp.ui.controller;

import dto.ChatRoomDTO;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Message;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.ui.utils.ImageUtil;
import rmi.FileTransferService;
import rmi.GetMessageService;
import rmi.GetUserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatRoomController {

    @FXML
    public SVGPath videoCallButton;

    @FXML
    public SVGPath phoneCallButton;

    @FXML
    public Group ellipsisButton;

    @FXML
    public Circle profileImage;
    public SVGPath attachmentButton;

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
    private static ExecutorService backgroundThreads = Executors.newFixedThreadPool(2);

    public record UserRoomKey(long userId, long roomId) {}
    public static Map<UserRoomKey, ChatRoomController> activeControllers = new ConcurrentHashMap<>();

    public void initializeChat(ChatRoomDTO chatRoomDTO) {
        this.currentUser = chatRoomDTO.getMe();
        this.chatRoomDTO = chatRoomDTO;
        this.otherUser = chatRoomDTO.getOther();

        // Set another user's profile picture
        updateProfilePicture();

        // Save the current object in the active controllers map to be used in the callback
        UserRoomKey key = new UserRoomKey(chatRoomDTO.getMe().getId(), chatRoomDTO.getRoom().getId());
        activeControllers.putIfAbsent(key, this);
//        activeControllers.put(chatRoomDTO.getRoom().getId(), this);

        // Set the other user's name
        chatUserName.setText(otherUser == null ? chatRoomDTO.getRoom().getName() : otherUser.getName());

        // Set user status
        updateUserStatus();

        // Load messages
        loadMessages();

        // Auto-scroll to bottom
        messagesScrollPane.setFitToWidth(true);
        messagesContainer.heightProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> messagesScrollPane.setVvalue(messagesScrollPane.getVmax()));
        });

        Platform.runLater(() -> {
            Stage currentStage = (Stage) messagesScrollPane.getScene().getWindow();
            if (currentStage != null) {
                currentStage.setOnCloseRequest(event -> {
                    activeControllers.remove(new UserRoomKey(chatRoomDTO.getMe().getId(), chatRoomDTO.getRoom().getId()));
                    Platform.exit();
                    System.exit(0);
                });
            }
        });

        // Icons Hovering
        hoverAllIcons();

        // Listener to the message input
        listenMessageInput();
    }

    private void listenMessageInput() {
        messageInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                // Text entered - activate button
                sendButton.setStyle("-fx-background-color: #00ab8a; -fx-opacity: 1; -fx-background-radius: 18; -fx-text-fill: white; -fx-cursor: hand;");
                ((Group) sendButton.getGraphic()).getChildren().forEach(node -> {
                    if (node instanceof SVGPath) {
                        node.setStyle("-fx-stroke: #000000; -fx-stroke-width: 2; -fx-stroke-linecap: round; -fx-stroke-linejoin: round; -fx-fill: transparent;");
                    }
                });
            } else {
                // Text cleared - deactivate button
                sendButton.setStyle("-fx-background-color: #00ab8a; -fx-opacity: 0.3; -fx-background-radius: 18; -fx-text-fill: white; -fx-cursor: hand;");
                ((Group) sendButton.getGraphic()).getChildren().forEach(node -> {
                    if (node instanceof SVGPath) {
                        node.setStyle("-fx-stroke: #808080; -fx-stroke-width: 2; -fx-stroke-linecap: round; -fx-stroke-linejoin: round; -fx-fill: transparent;");
                    }
                });
            }
        });
    }

    private void hoverAllIcons() {
        // Back button
        backButton.getChildren().forEach(node -> node.getStyleClass().add("icon"));
        backButton.setOnMouseEntered(e ->
                backButton.getChildren().forEach(node ->
                        node.getStyleClass().setAll("onIconHover")
                )
        );
        backButton.setOnMouseExited(e ->
                backButton.getChildren().forEach(node ->
                        node.getStyleClass().setAll("icon")
                )
        );

        // Video Call Button
        videoCallButton.getStyleClass().add("icon");
        videoCallButton.setOnMouseEntered(e -> videoCallButton.getStyleClass().setAll("onIconHover"));
        videoCallButton.setOnMouseExited(e -> videoCallButton.getStyleClass().setAll("icon"));

        // Phone Call Button
        phoneCallButton.getStyleClass().add("icon");
        phoneCallButton.setOnMouseEntered(e -> phoneCallButton.getStyleClass().setAll("onIconHover"));
        phoneCallButton.setOnMouseExited(e -> phoneCallButton.getStyleClass().setAll("icon"));

        // Ellipsis Button
        ellipsisButton.getChildren().forEach(node -> node.getStyleClass().add("icon"));
        ellipsisButton.setOnMouseEntered(e ->
                ellipsisButton.getChildren().forEach(node ->
                        node.getStyleClass().setAll("onIconHover")
                )
        );
        ellipsisButton.setOnMouseExited(e ->
                ellipsisButton.getChildren().forEach(node ->
                        node.getStyleClass().setAll("icon")
                )
        );

        // Attachment Button
        attachmentButton.getStyleClass().add("icon");
        attachmentButton.setOnMouseEntered(e -> attachmentButton.getStyleClass().setAll("onIconHover"));
        attachmentButton.setOnMouseExited(e -> attachmentButton.getStyleClass().setAll("icon"));
    }

    private void updateProfilePicture() {
        Image image = ImageUtil.getImageFromByteArray(
                chatRoomDTO.getOther() != null ?
                        chatRoomDTO.getOther().getPictureBytes() :
                        chatRoomDTO.getRoom().getPictureBytes());
        profileImage.setFill(new ImagePattern(image));
    }

    private void updateUserStatus() {
        String status = String.valueOf(otherUser == null ? "Group Chat" : otherUser.getStatus());
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
                case "Group Chat":
                    userStatus.setText("Group Chat");
                    userStatus.setTextFill(Color.web("#25D366"));
                    break;
                default:
                    userStatus.setText("Unknown");
                    userStatus.setTextFill(Color.web("#808080"));
            }
        }
    }

    public void loadMessages() {
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
        boolean isSentByMe = Objects.equals(message.getSenderId(), currentUser.getId());
        Users sender;
        String senderName = "";
        try{
            GetUserService getUserService = (GetUserService) ClientChatApp.registry.lookup("GetUserService");
            sender = getUserService.getUser(message.getSenderId());
            senderName = sender.getName();
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            showError("Failed to load user");
        }

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
        // Add sender name to the bubble if the message is not sent by me
        messageBubble.setSpacing(5);
        if (!isSentByMe && chatRoomDTO.getOther() == null) {
            Text senderNameText = new Text(senderName);
            senderNameText.setFill(Color.BLACK);
            senderNameText.setFont(Font.font("System", FontWeight.BOLD, 16));
            messageBubble.getChildren().add(senderNameText);
        }

        if (message.getAttachedFile() == null) {
            // Message text
            TextFlow messageText = new TextFlow();
            Text text = new Text(message.getText());
            text.setFill(isSentByMe ? Color.WHITE : Color.BLACK);
            text.setFont(Font.font("System", 14));
            messageText.getChildren().add(text);
            messageBubble.getChildren().add(messageText);
        } else {
            // Attachment file message
            VBox attachmentBox = new VBox(5);

            Label loadingLabel = new Label("Downloading attachment...");
            attachmentBox.getChildren().clear();
            attachmentBox.getChildren().add(loadingLabel);
            messageBubble.getChildren().add(attachmentBox);

            Task<File> downloadTask = new Task<>() {
                @Override
                protected File call() throws Exception {
                    File attachedFile = new File(FileTransferService.CLIENT_MESSAGE_PATH,
                            message.getAttachedFile());

                    File parentDir = new File(FileTransferService.CLIENT_MESSAGE_PATH);

                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    if (!attachedFile.exists()) {

                        FileTransferService service =
                                (FileTransferService) ClientChatApp.registry.lookup("FileTransferService");

                        byte[] data = service.downloadFileFromServer(message.getAttachedFile());

                        Files.write(attachedFile.toPath(), data);
                    }

                    return attachedFile;
                }
            };

            downloadTask.setOnSucceeded(e -> {
                File attachedFile = downloadTask.getValue();
                attachmentBox.getChildren().clear();

                setAttachmentUI(attachedFile, attachmentBox);
            });

            downloadTask.setOnFailed(e -> {
                attachmentBox.getChildren().clear();
                attachmentBox.getChildren().add(new Label("Failed to download attachment"));
            });

            backgroundThreads.submit(downloadTask);
        }

        // Timestamp
        Label timestamp = new Label(formatTimestamp(message.getSentAt()));
        timestamp.setFont(Font.font("System", 10));
        timestamp.setTextFill(isSentByMe ? Color.web("#E0E0E0") : Color.web("#808080"));
        timestamp.setAlignment(Pos.CENTER_RIGHT);

        messageBubble.getChildren().add(timestamp);
        messageBox.getChildren().add(messageBubble);

        messagesContainer.getChildren().add(messageBox);
    }

    private static void setAttachmentUI(File attachedFile, VBox attachmentBox) {
        String fileName = attachedFile.getName();

        if (fileName.endsWith(".png") || fileName.endsWith(".jpg") ||
                fileName.endsWith(".jpeg") || fileName.endsWith(".gif")) {
            try {
                Image image = new Image(attachedFile.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(300);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setCache(true);

                attachmentBox.getChildren().add(imageView);
            } catch (Exception e) {
                Label fileLabel = getDefaultFileLabel(fileName, attachedFile);

                attachmentBox.getChildren().add(fileLabel);
            }
        } else if (fileName.endsWith(".mp4") || fileName.endsWith(".mov") ||
                fileName.endsWith(".m4v")) {
            Platform.runLater(() -> {
                try {
                    Media media = new Media(attachedFile.toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    MediaView mediaView = new MediaView(mediaPlayer);

                    mediaView.setFitWidth(300);
                    mediaView.setPreserveRatio(true);

                    Button playPauseBtn = new Button("▶");
                    playPauseBtn.setStyle("-fx-background-color: rgba(0,0,0,0.5); " +
                            "-fx-text-fill: white; -fx-font-size: 24px; " +
                            "-fx-background-radius: 30px;");

                    playPauseBtn.setOpacity(0.7);

                    Slider seekSlider = new Slider();
                    seekSlider.setMin(0);
                    seekSlider.setMax(100);
                    seekSlider.setValue(0);
                    seekSlider.setPrefWidth(300);

                    mediaPlayer.setOnReady(() -> {
                        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                            if (!seekSlider.isValueChanging()) {
                                double progress = newTime.toMillis() / media.getDuration().toMillis() * 100;
                                seekSlider.setValue(progress);
                            }
                        });

                        seekSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
                            if (!isChanging) {
                                mediaPlayer.seek(media.getDuration().multiply(seekSlider.getValue() / 100.0));
                            }
                        });

                        playPauseBtn.setOnAction(e -> {
                            e.consume();
                            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                                mediaPlayer.pause();
                                playPauseBtn.setText("▶");
                            } else {
                                mediaPlayer.play();
                                playPauseBtn.setText("⏸");
                            }
                        });
                    });

                    StackPane videoPane = new StackPane();
                    videoPane.getChildren().addAll(mediaView, playPauseBtn);

                    VBox videoBox = new VBox(5);
                    videoBox.getChildren().addAll(videoPane, seekSlider);

                    attachmentBox.getChildren().add(videoBox);

                    mediaPlayer.setOnEndOfMedia(() -> {
                        mediaPlayer.stop();
                        playPauseBtn.setText("▶");
                        seekSlider.setValue(0);
                    });
                } catch (Exception e) {
                    Label fileLabel = getDefaultFileLabel(fileName, attachedFile);

                    attachmentBox.getChildren().add(fileLabel);
                }
            });
        } else if (fileName.endsWith(".mp3") || fileName.endsWith(".wav") ||
                fileName.endsWith(".aac")) {
            Platform.runLater(() -> {
                try {
                    Media media = new Media(attachedFile.toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);

                    Button playPauseBtn = new Button("▶");
                    playPauseBtn.setPrefWidth(40);
                    playPauseBtn.setMinWidth(40);
                    playPauseBtn.setMaxWidth(100);

                    Slider seekSlider = new Slider();
                    seekSlider.setMin(0);
                    seekSlider.setMax(100);
                    seekSlider.setValue(0);
                    seekSlider.setPrefWidth(300);

                    mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                        if (!seekSlider.isValueChanging()) {
                            double progress = newTime.toMillis() / media.getDuration().toMillis() * 100;
                            seekSlider.setValue(progress);
                        }
                    });

                    seekSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
                        if (!isChanging) {
                            mediaPlayer.seek(media.getDuration().multiply(seekSlider.getValue() / 100.0));
                        }
                    });

                    playPauseBtn.setOnAction(e -> {
                        e.consume();
                        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                            mediaPlayer.pause();
                            playPauseBtn.setText("▶");
                        } else {
                            mediaPlayer.play();
                            playPauseBtn.setText("⏸");
                        }
                    });

                    mediaPlayer.setOnEndOfMedia(() -> {
                        mediaPlayer.stop();
                        playPauseBtn.setText("▶");
                        seekSlider.setValue(0);
                    });

                    HBox audioBox = new HBox(10, playPauseBtn, seekSlider);
                    audioBox.setAlignment(Pos.CENTER_LEFT);
                    attachmentBox.getChildren().add(audioBox);
                } catch (Exception e) {
                    Label fileLabel = getDefaultFileLabel(fileName, attachedFile);

                    attachmentBox.getChildren().add(fileLabel);
                }
            });
        } else {
            Label fileLabel = getDefaultFileLabel(fileName, attachedFile);

            attachmentBox.getChildren().add(fileLabel);
        }
    }

    private static Label getDefaultFileLabel(String fileName, File attachedFile) {
        Label fileLabel = new Label(fileName);
        fileLabel.setTextFill(Color.BLUE);
        fileLabel.setUnderline(true);
        fileLabel.setCursor(Cursor.HAND);

        fileLabel.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            fileChooser.setInitialFileName(attachedFile.getName());

            File destination = fileChooser.showSaveDialog(fileLabel.getScene().getWindow());

            if (destination != null) {
                try {
                    Files.copy(
                            attachedFile.toPath(),
                            destination.toPath(),
                            StandardCopyOption.REPLACE_EXISTING
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return fileLabel;
    }

    private String formatTimestamp(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(formatter);
    }

    @FXML
    private void onSendButtonClick() {
        String messageText = messageInput.getText().trim();
        if (!messageText.isEmpty()) {
            messageInput.clear();
            GetMessageService getMessageService = null;
            try {
                Message message = new Message();
                message.setSenderId(currentUser.getId());
                message.setText(messageText);
                message.setRoomId(chatRoomDTO.getRoom().getId());
                message.setSentAt(LocalDateTime.now());
                getMessageService = (GetMessageService) ClientChatApp.registry.lookup("GetMessageService");
                getMessageService.sendMessage(message);
                addMessageToUI(message);
                getMessageService.updateOthersGUI(chatRoomDTO);
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
                showError("Failed to send a message");
            }
        }
    }

    @FXML
    private void onBackButtonClick(MouseEvent event) {
        try {
            activeControllers.remove(new UserRoomKey(chatRoomDTO.getMe().getId(), chatRoomDTO.getRoom().getId()));
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

    public void onAttachmentButtonClick(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Your File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("Image Files",
                        "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new ExtensionFilter("Audio Files",
                        "*.wav", "*.mp3", "*.aac"),
                new ExtensionFilter("Video Files",
                        "*.mp4", "*.mkv", "*.mov", "*.wmv"),
                new ExtensionFilter("All Files", "*.*"));

        File chosenFile =
                fileChooser.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());

        if (chosenFile == null || !chosenFile.exists()) {
            return;
        }

        String name = chosenFile.getName(), extension = "";

        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < name.length() - 1) {
            extension = name.substring(dotIndex + 1).toLowerCase();
        }

        LocalDateTime sentAt = LocalDateTime.now();
        String filePath = currentUser.getId() + "_" +
                sentAt.toString().replaceAll(":", "-") + "_" +
                chatRoomDTO.getRoom().getId() + "." + extension;

        try {
            GetMessageService getMessageService =
                    (GetMessageService) ClientChatApp.registry.lookup("GetMessageService");
            FileTransferService fileTransferService =
                    (FileTransferService) ClientChatApp.registry.lookup("FileTransferService");

            Message message = new Message();
            message.setSenderId(currentUser.getId());
            message.setRoomId(chatRoomDTO.getRoom().getId());
            message.setSentAt(sentAt);
            message.setText("Sent an Attachment");
            message.setFileType(extension);
            message.setAttachedFile(filePath);

            backgroundThreads.submit(() -> {
                try {
                    byte[] selectedFileBytes = Files.readAllBytes(chosenFile.toPath());
                    fileTransferService.uploadFileToServer(selectedFileBytes, filePath);
                    getMessageService.sendMessage(message);
                    Platform.runLater(() -> {
                        try {
                            addMessageToUI(message);
                            getMessageService.updateOthersGUI(chatRoomDTO);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            showError("Failed to send a message");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to upload a file");
        }
    }

    public void onPhoneCallButtonClick(MouseEvent mouseEvent) {
        // TODO: Implement Phone Call
    }

    public void onVideoCallButtonClick(MouseEvent mouseEvent) {
        // TODO: Implement Video Call
    }

    public void onEllipsisButtonClick(MouseEvent mouseEvent) {
        // TODO: Implement Options Button
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