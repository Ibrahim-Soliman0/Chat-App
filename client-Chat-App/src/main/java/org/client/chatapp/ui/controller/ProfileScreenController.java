package org.client.chatapp.ui.controller;

import dto.GetMyFriendsListDTO;
import dto.StatusDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Users;
import model.enums.Gender;
import model.enums.Status;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.ui.utils.ImageUtil;
import rmi.*;
import org.client.chatapp.config.ConfigManager;
import org.client.chatapp.config.UserConfig;
import org.client.chatapp.ui.utils.SavedUserUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.client.chatapp.config.ConfigManager.loadConfig;

public class ProfileScreenController {

    private Users user = new Users();

    @FXML
    private Group profileIcon, chatsIcon, editIcon, passwordIcon;

    @FXML
    private Circle profileImage, profileHeadIconTop;

    @FXML
    private Label initialsLabel, fullNameLabel, emailLabel, genderLabel,
            countryLabel, dobLabel, bioLabel;
    @FXML
    ScrollPane scrollPane;

    @FXML
    private TextField fullNameField, emailField;

    @FXML
    private ComboBox<String> genderComboBox, countryComboBox;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private TextArea bioField;

    @FXML
    private Button logoutButton;

    @FXML
    private Group imageActionButton;

    @FXML
    private ComboBox<StatusItem> statusComboBox;

    private byte[] newSelectedImageBytes = null;
    private Image previousImage = null;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private boolean isEditMode = false;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private File file;

    public record StatusItem(String text, Color color) {}
    @FXML
    public void initialize() throws RemoteException {
        // Initialize bottom navigation icons
        initializeNavigationIcons();

        // Initialize edit icon (pen icon)
        initializeEditIcon();

        // Initialize password icon
        initializePasswordIcon();

        // Initialize ComboBoxes and DatePicker
        initializeFormControls();

        // Auto scroll to top appropriately
        Node content = scrollPane.getContent();
        content.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            if (newBounds.getHeight() > 0) {
                Platform.runLater(() -> scrollPane.setVvalue(0.0));
            }
        });

        statusComboBox.getItems().addAll(
                new StatusItem("Online", Color.web("#25D366")),
                new StatusItem("Away", Color.web("#FFA500")),
                new StatusItem("Busy", Color.web("#FF3B30")),
                new StatusItem("Offline", Color.web("#9E9E9E"))
        );

        statusComboBox.getSelectionModel().selectFirst();
        statusComboBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(StatusItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(createStatusItem(item.text(), item.color()));
                }
            }
        });

        statusComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(StatusItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(createStatusItem(item.text(), item.color()));
                }
            }
        });

        statusComboBox.setOnAction(event -> {
            StatusItem selected = statusComboBox.getValue();

            if (selected == null) {
                return;
            }

            Status status = Status.valueOf(selected.text.toUpperCase());
            StatusDTO statusDTO = new StatusDTO(user.getId(), status);

            try {
                GetUserService getUserService =
                        (GetUserService) ClientChatApp.registry.lookup("GetUserService");
                getUserService.updateStatus(statusDTO);
            } catch (NotBoundException | RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private HBox createStatusItem(String text, Color color) {
        Circle icon = new Circle(6, color);
        Label label = new Label(text);
        label.setTextFill(Color.BLACK);

        HBox box = new HBox(8, icon, label);
        box.setPadding(new Insets(6, 10, 6, 10));

        return box;
    }

    private void initializeNavigationIcons() {
        // Profile Icon (selected state)
        Circle profileHeadIcon = new Circle(12, 7, 4);
        profileHeadIcon.getStyleClass().add("selectedIcon");

        SVGPath profileBodyIcon = new SVGPath();
        profileBodyIcon.setContent("M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2");
        profileBodyIcon.getStyleClass().add("selectedIcon");

        profileIcon.getChildren().addAll(profileHeadIcon, profileBodyIcon);
        profileIcon.setScaleX(1.75);
        profileIcon.setScaleY(1.75);

        // Chats Icon
        SVGPath chatsSvg = new SVGPath();
        chatsSvg.setContent("M2.992 16.342a2 2 0 0 1 .094 1.167l-1.065 3.29a1 1 0 0 0 1.236 1.168l3.413-.998a2 2 0 0 1 1.099.092 10 10 0 1 0-4.777-4.719");
        chatsSvg.getStyleClass().add("icon");

        chatsIcon.getChildren().add(chatsSvg);
        chatsIcon.setScaleX(1.5);
        chatsIcon.setScaleY(1.5);

        chatsIcon.setOnMouseEntered(e -> chatsSvg.getStyleClass().setAll("onIconHover"));
        chatsIcon.setOnMouseExited(e -> chatsSvg.getStyleClass().setAll("icon"));
    }

    private void initializeEditIcon() {
        editIcon.getChildren().clear();
        // Pen icon for editing
        SVGPath penPath = new SVGPath();
        penPath.setContent("M12 20h9M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z");
        penPath.getStyleClass().add("icon");

        editIcon.getChildren().add(penPath);
        editIcon.setScaleX(1.1);
        editIcon.setScaleY(1.1);

        editIcon.setOnMouseEntered(e -> penPath.getStyleClass().setAll("onIconHover"));
        editIcon.setOnMouseExited(e -> penPath.getStyleClass().setAll("icon"));
    }

    private void initializePasswordIcon() {
        // Key icon for password
        SVGPath keyPath = new SVGPath();
        keyPath.setContent("M21 2l-2 2m-7.61 7.61a5.5 5.5 0 1 1-7.778 7.778 5.5 5.5 0 0 1 7.777-7.777zm0 0L15.5 7.5m0 0l3 3L22 7l-3-3m-3.5 3.5L19 4");
        keyPath.getStyleClass().add("icon");
        keyPath.setStyle("-fx-stroke: #25D366; -fx-stroke-width: 2; -fx-fill: transparent;");

        passwordIcon.getChildren().add(keyPath);
        passwordIcon.setScaleX(0.8);
        passwordIcon.setScaleY(0.8);
    }

    private void initializeFormControls() {
        // Initialize Gender ComboBox
        genderComboBox.getItems().addAll("Male", "Female");

        // Initialize Country ComboBox
        countryComboBox.getItems().addAll(
                "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Argentina", "Armenia",
                "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Belarus",
                "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana",
                "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon",
                "Canada", "Chad", "Chile", "China", "Colombia", "Costa Rica", "Croatia", "Cuba",
                "Cyprus", "Czech Republic", "Denmark", "Dominica", "Dominican Republic", "Ecuador",
                "Egypt", "El Salvador", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland", "France",
                "Gabon", "Gambia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea",
                "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran",
                "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan",
                "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho",
                "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Madagascar", "Malawi",
                "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius",
                "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco",
                "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand",
                "Nicaragua", "Niger", "Nigeria", "North Korea", "North Macedonia", "Norway", "Oman",
                "Pakistan", "Palau", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru",
                "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda",
                "Saint Kitts and Nevis", "Saint Lucia", "Samoa", "San Marino", "Saudi Arabia",
                "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia",
                "Solomon Islands", "Somalia", "South Africa", "South Korea", "South Sudan", "Spain",
                "Sri Lanka", "Sudan", "Suriname", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan",
                "Tanzania", "Thailand", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",
                "Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom",
                "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela",
                "Vietnam", "Yemen", "Zambia", "Zimbabwe");

        // Initialize DatePicker with the date cell factory to disable future dates
        dobPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #EEEEEE;");
                }
            }
        });
    }

    private void loadProfileData(Users user) {
        // Load from the database through RMI
        fullNameLabel.setText(user.getName());
        emailLabel.setText(user.getEmail());
        genderLabel.setText(user.getGender().toString().equals("MALE") ? "Male" : "Female");
        countryLabel.setText(user.getCountry());
        dobLabel.setText(user.getDob().toString());
        bioLabel.setText(user.getBio());
        Image image = ImageUtil.getImageFromByteArray(user.getPictureBytes());
        profileImage.setFill(new ImagePattern(image));

        // Set initial values for form controls
        fullNameField.setText(fullNameLabel.getText());
        emailField.setText(emailLabel.getText());
        genderComboBox.setValue(genderLabel.getText());
        countryComboBox.setValue(countryLabel.getText());
        dobPicker.setValue(LocalDate.parse(dobLabel.getText()));
        bioField.setText(bioLabel.getText());
    }

    @FXML
    private void onEditIconClick(MouseEvent event) throws NotBoundException, RemoteException {
        if (!isEditMode) {
            // Switch to edit mode
            enterEditMode();
        } else {
            // Save changes and exit edit mode
            saveProfileChanges(user);
            exitEditMode();
        }
    }

    private void enterEditMode() {
        isEditMode = true;

        // Hide labels and show form controls
        fullNameLabel.setVisible(false);
        fullNameLabel.setManaged(false);
        fullNameField.setVisible(true);
        fullNameField.setManaged(true);

        emailLabel.setVisible(false);
        emailLabel.setManaged(false);
        emailField.setVisible(true);
        emailField.setManaged(true);

        genderLabel.setVisible(false);
        genderLabel.setManaged(false);
        genderComboBox.setVisible(true);
        genderComboBox.setManaged(true);

        countryLabel.setVisible(false);
        countryLabel.setManaged(false);
        countryComboBox.setVisible(true);
        countryComboBox.setManaged(true);

        dobLabel.setVisible(false);
        dobLabel.setManaged(false);
        dobPicker.setVisible(true);
        dobPicker.setManaged(true);

        bioLabel.setVisible(false);
        bioLabel.setManaged(false);
        bioField.setVisible(true);
        bioField.setManaged(true);

        // Change edit icon to a checkmark
        editIcon.getChildren().clear();
        SVGPath checkPath = new SVGPath();
        checkPath.setContent("M20 6L9 17l-5-5");
        checkPath.getStyleClass().add("icon");
        editIcon.getChildren().add(checkPath);
        editIcon.setOnMouseEntered(e -> checkPath.getStyleClass().setAll("onIconHover"));
        editIcon.setOnMouseExited(e -> checkPath.getStyleClass().setAll("icon"));

        previousImage = ((ImagePattern) profileImage.getFill()).getImage();
        showCameraIcon();
    }

    private void exitEditMode() {
        isEditMode = false;

        // Show labels and hide form controls
        fullNameLabel.setVisible(true);
        fullNameLabel.setManaged(true);
        fullNameField.setVisible(false);
        fullNameField.setManaged(false);

        emailLabel.setVisible(true);
        emailLabel.setManaged(true);
        emailField.setVisible(false);
        emailField.setManaged(false);

        genderLabel.setVisible(true);
        genderLabel.setManaged(true);
        genderComboBox.setVisible(false);
        genderComboBox.setManaged(false);

        countryLabel.setVisible(true);
        countryLabel.setManaged(true);
        countryComboBox.setVisible(false);
        countryComboBox.setManaged(false);

        dobLabel.setVisible(true);
        dobLabel.setManaged(true);
        dobPicker.setVisible(false);
        dobPicker.setManaged(false);

        bioLabel.setVisible(true);
        bioLabel.setManaged(true);
        bioField.setVisible(false);
        bioField.setManaged(false);

        hideImageActionButton();
        newSelectedImageBytes = null;
        previousImage = null;

        // Change checkmark back to edit icon
        initializeEditIcon();
    }

    private void saveProfileChanges(Users user) throws NotBoundException, RemoteException {
        // Update in the database through RMI
        user.setName(fullNameField.getText());
        user.setEmail(emailField.getText());
        user.setGender(Gender.valueOf(genderComboBox.getValue().toUpperCase()));
        user.setCountry(countryComboBox.getValue());
        user.setDob(dobPicker.getValue());
        user.setBio(bioField.getText());
        if (newSelectedImageBytes != null) {
            user.setPictureBytes(newSelectedImageBytes);
            // TODO: Ahmed Ramadan should handle this
            // user.setPicturePath(file.getAbsolutePath());
        }

        GetUserService getUserService = (GetUserService) ClientChatApp.registry.lookup("GetUserService");
        getUserService.updateUser(user);

        // Update the current controller
        loadProfileData(user);
    }

    @FXML
    private void onChangePasswordClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/org/client/chatapp/change-password-view.fxml")));
            root = loader.load();
            ChangePasswordController changePasswordController = loader.getController();
            changePasswordController.setUser(user);
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
    private void onLogoutClick() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Logout Confirmation");
        confirmAlert.setHeaderText("Are you sure you want to logout?");
        confirmAlert.setContentText("You will need to login again to access your account.");

        ButtonType yesButton = new ButtonType("Yes, Logout");
        ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        confirmAlert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            // Navigate to login screen
            try {
                LoginService loginService = (LoginService) ClientChatApp.registry.lookup("LoginService");
                loginService.logout(user.getPhoneNumber());
                ConfigManager.logoutUser(user.getPhoneNumber());
                this.user = null;

                root = FXMLLoader.load(
                        Objects.requireNonNull(getClass().getResource(
                                "/org/client/chatapp/login-view.fxml")));

                stage = (Stage) logoutButton.getScene().getWindow();
                scene = new Scene(root);
                scene.getStylesheets().addAll(ClientChatApp.allStyles);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void onChatsIconClick(MouseEvent event) {
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
    private void onImageActionClicked(MouseEvent event) {
        if (newSelectedImageBytes == null) {
            chooseNewProfileImage();
        } else {
            removeSelectedImage();
        }
    }

    private void chooseNewProfileImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Profile Picture");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        file = chooser.showOpenDialog(profileImage.getScene().getWindow());
        if (file != null) {
            try {
                Image img = new Image(file.toURI().toString());
                profileImage.setFill(new ImagePattern(img));
                newSelectedImageBytes = Files.readAllBytes(file.toPath());
                showRemoveIcon(); // switch camera → cross
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeSelectedImage() {
        profileImage.setFill(new ImagePattern(previousImage));
        newSelectedImageBytes = null;
        showCameraIcon(); // back to camera
    }

    private void showCameraIcon() {
        imageActionButton.getChildren().clear();

        Circle bg = new Circle(18, javafx.scene.paint.Color.web("#00ab8a"));

        SVGPath camera = new SVGPath();
        camera.setContent(
                "M13.997 4a2 2 0 0 1 1.76 1.05l.486.9A2 2 0 0 0 18.003 7H20a2 2 0 0 1 2 2v9a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V9a2 2 0 0 1 2-2h1.997a2 2 0 0 0 1.759-1.048l.489-.904A2 2 0 0 1 10.004 4z"
        );
        camera.setFill(Color.TRANSPARENT);
        camera.setStroke(Color.WHITE);
        camera.setStrokeWidth(2);
        camera.setStyle("-fx-fill: transparent;");
        camera.setScaleX(1.1);

        Circle bgInside = new Circle(3, Color.TRANSPARENT);
        bgInside.setStroke(Color.WHITE);
        bgInside.setStrokeWidth(2);

        StackPane iconWrapper = new StackPane(bg, camera, bgInside);
        iconWrapper.setPrefSize(36, 36);

        imageActionButton.getChildren().add(iconWrapper);
        imageActionButton.setVisible(true);
        imageActionButton.setManaged(true);
    }


    private void showRemoveIcon() {
        imageActionButton.getChildren().clear();

        Line l1 = new Line(-6, -6, 6, 6);
        Line l2 = new Line(-6, 6, 6, -6);
        l1.setStrokeWidth(2);
        l2.setStrokeWidth(2);
        l1.setStroke(javafx.scene.paint.Color.WHITE);
        l2.setStroke(javafx.scene.paint.Color.WHITE);

        Circle bg = new Circle(14, javafx.scene.paint.Color.web("#ff4444"));

        imageActionButton.getChildren().addAll(bg, l1, l2);
        imageActionButton.setVisible(true);
        imageActionButton.setManaged(true);
    }

    private void hideImageActionButton() {
        imageActionButton.setVisible(false);
        imageActionButton.setManaged(false);
    }

    public void setUser(Users user) {
        this.user = user;
        loadProfileData(user);
    }
}