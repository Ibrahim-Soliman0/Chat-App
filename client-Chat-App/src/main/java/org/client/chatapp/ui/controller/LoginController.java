package org.client.chatapp.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.config.ConfigManager;
import org.client.chatapp.config.UserConfig;
import org.client.chatapp.rmi.ClientCallBackImp;
import org.client.chatapp.ui.utils.EncryptionUtil;
import org.client.chatapp.ui.utils.SavedUserUtil;
import rmi.LoginService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.Optional;

public class LoginController {
    private Parent root;
    private Stage stage;
    private Scene scene;
    private static ClientCallBackImp client;


    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;
    @FXML
    private VBox savedAccountsContainer;
    @FXML
    private HBox accountsList;
    @FXML
    private ScrollPane accountsScrollPane;

    @FXML
    public void initialize() {
        // Initialize any default values or listeners here
        errorLabel.setVisible(false);

        UserConfig config = ConfigManager.loadConfig();
        if (config != null && !config.getUsers().isEmpty()) {
            savedAccountsContainer.setVisible(true);
            savedAccountsContainer.setManaged(true);

            accountsList.getChildren().clear();

            for (SavedUserUtil savedUser : config.getUsers()) {
                VBox userCard = createUserCard(savedUser);
                accountsList.getChildren().add(userCard);
            }
        }
        // Add an input validation listener for phone number (digits only)
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            // Limit to 10 digits for Egyptian phone numbers
            if (newValue.length() > 10) {
                phoneField.setText(newValue.substring(0, 10));
            }
        });


        accountsScrollPane.getStyleClass().add("accounts-scroll-pane");

        setupDragScrolling();
    }

    private void setupDragScrolling() {
        final double[] startX = new double[1];
        final double[] startHvalue = new double[1];

        accountsList.setOnMousePressed(e -> {
            startX[0] = e.getSceneX();
            startHvalue[0] = accountsScrollPane.getHvalue();
        });

        accountsList.setOnMouseDragged(e -> {
            double delta = startX[0] - e.getSceneX();
            double width = accountsList.getBoundsInLocal().getWidth();
            accountsScrollPane.setHvalue(startHvalue[0] + delta / width);
        });
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException, NotBoundException {
        // Hide previous error messages
        errorLabel.setVisible(false);

        String phone = phoneField.getText().trim();
        String password = passwordField.getText();

        // Validation
        if (phone.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        if (phone.length() != 10) {
            showError("Phone number must be 10 digits");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }

        // Construct a full phone number with country code
        String fullPhone = "0" + phone;

        LoginService loginService = (LoginService) ClientChatApp.registry.lookup("LoginService");
        client = new ClientCallBackImp();
        Users success = loginService.login(fullPhone, password, client);
        if (success != null) {
            String encrypted = EncryptionUtil.encrypt(password);
            SavedUserUtil newUser = new SavedUserUtil(success.getName(), success.getPhoneNumber(), encrypted);
            ConfigManager.addUser(newUser);

            moveToMainApp(event, success);
        } else {
            showError("The phone number and the password don't match");
        }
    }

    private void moveToMainApp(ActionEvent event, Users user) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/org/client/chatapp/home-screen-view.fxml")));
            root = loader.load();
            HomeScreenController homeScreenController = loader.getController();
            homeScreenController.setUser(user);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().addAll(ClientChatApp.allStyles);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        System.out.println("Forgot password clicked");
        // TODO: Navigate to forgot password screen
        showError("Forgot password feature coming soon");
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/client/chatapp/register-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private VBox createUserCard(SavedUserUtil user) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("account-card");

        StackPane avatarStack = new StackPane();

        Group avatarGroup = new Group();

        Circle backgroundCircle = new Circle(35, 35, 35);
        backgroundCircle.setFill(Color.web("#ECEFF1"));
        backgroundCircle.setStroke(Color.WHITE);
        backgroundCircle.setStrokeWidth(2);
        backgroundCircle.getStyleClass().add("avatar-circle");

        SVGPath userIcon = new SVGPath();
        userIcon.setContent("M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2 M12 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8z");
        userIcon.setFill(Color.TRANSPARENT);
        userIcon.setStroke(Color.web("#5e6468"));
        userIcon.setStrokeWidth(1.5);
        userIcon.setLayoutX(23);
        userIcon.setLayoutY(25);
        userIcon.setScaleX(1.1);
        userIcon.setScaleY(1.1);

        Circle imageCircle = new Circle(35, 35, 35);
        imageCircle.setCursor(Cursor.HAND);
        imageCircle.getStyleClass().add("circle");

        try {
            LoginService loginService = (LoginService) ClientChatApp.registry.lookup("LoginService");
            byte[] imageBytes = loginService.getUserProfilePicture(user.getPhoneNumber());

            if (imageBytes != null && imageBytes.length > 0) {
                Image img = new Image(new ByteArrayInputStream(imageBytes));
                imageCircle.setFill(new ImagePattern(img));
                userIcon.setVisible(false);
            } else {
                imageCircle.setFill(Color.TRANSPARENT);
            }
        } catch (Exception e) {
            imageCircle.setFill(Color.TRANSPARENT);
        }

        avatarGroup.getChildren().addAll(backgroundCircle, userIcon, imageCircle);

        Button removeBtn = new Button("x");
        removeBtn.getStyleClass().add("remove-account-btn");
        StackPane.setAlignment(removeBtn, Pos.TOP_RIGHT);

        removeBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Saved Account");
            alert.setHeaderText("Remove " + user.getName() + "?");
            alert.setContentText("Are you sure you want to remove this account from remember me?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                ConfigManager.removeUser(user.getPhoneNumber());
                accountsList.getChildren().remove(card);
                if (accountsList.getChildren().isEmpty()) {
                    savedAccountsContainer.setVisible(false);
                    savedAccountsContainer.setManaged(false);
                }
            }
            e.consume();
        });

        avatarStack.getChildren().addAll(avatarGroup, removeBtn);
        imageCircle.setOnMouseClicked(event -> {
            try {
                String decryptedPassword = EncryptionUtil.decrypt(user.getEncryptedPassword());
                autoLogin(user.getPhoneNumber(), decryptedPassword);
            } catch (Exception e) {
                showError("Auto-login failed. Please sign in manually.");
            }
        });

        Label nameLabel = new Label(user.getName());
        nameLabel.getStyleClass().add("account-name-label");

        card.getChildren().addAll(avatarStack, nameLabel);

        return card;
    }

    private void autoLogin(String phone, String password) {
        try {
            LoginService loginService = (LoginService) ClientChatApp.registry.lookup("LoginService");
            client = new ClientCallBackImp();

            Users success = loginService.login(phone, password, client);
            if (success != null) {
                moveToMainApp(new ActionEvent(accountsList, null), success);
            } else {
                showError("Saved session expired. Please login again.");
            }
        } catch (Exception e) {
            showError("Connection error.");
        }
    }
}