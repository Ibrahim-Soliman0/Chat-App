package org.client.chatapp.ui.controller;

import dto.GetMyFriendsListDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import model.Users;
import org.client.chatapp.ClientChatApp;
import rmi.GetUserService;
import rmi.LoadFriendsListService;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

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

    private Parent root;
    private Stage stage;
    private Scene scene;
    private boolean isEditMode = false;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

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

//        try {
//            GetUserService getUserService = (GetUserService) ClientChatApp.registry.lookup("GetUserService");
//            user = getUserService.getUser(1L);
//
//        } catch (RemoteException | NotBoundException ex) {
//            throw new RuntimeException(ex);
//        }
//        loadProfileData(user);
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

        // Initialize DatePicker with date cell factory to disable future dates
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
        // Placeholder data - in a real app, this would load from a database or service
        fullNameLabel.setText(user.getName());
        emailLabel.setText(user.getEmail());
        genderLabel.setText(user.getGender().toString());
        countryLabel.setText(user.getCountry());
        dobLabel.setText(user.getDob().toString());
        bioLabel.setText(user.getBio());

        // Set initial values for form controls
        fullNameField.setText(fullNameLabel.getText());
        emailField.setText(emailLabel.getText());
        genderComboBox.setValue(genderLabel.getText());
        countryComboBox.setValue(countryLabel.getText());

        // Parse and set date
        try {
            LocalDate dob = LocalDate.parse(dobLabel.getText(), dateFormatter);
            dobPicker.setValue(dob);
        } catch (Exception e) {
            dobPicker.setValue(LocalDate.of(1990, 1, 1));
        }

        bioField.setText(bioLabel.getText());

        // Set initials
        updateInitials(fullNameLabel.getText());
    }

    private void updateInitials(String fullName) {
        if (fullName != null && !fullName.trim().isEmpty()) {
            String[] nameParts = fullName.trim().split("\\s+");
            String initials = "";
            if (nameParts.length >= 2) {
                initials = String.valueOf(nameParts[0].charAt(0)) +
                        String.valueOf(nameParts[1].charAt(0));
            } else if (nameParts.length == 1) {
                initials = String.valueOf(nameParts[0].charAt(0));
            }
            initialsLabel.setText(initials.toUpperCase());
        }
    }

    @FXML
    private void onEditIconClick(MouseEvent event) {
        if (!isEditMode) {
            // Switch to edit mode
            enterEditMode();
        } else {
            // Save changes and exit edit mode
            saveProfileChanges();
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

        // Change checkmark back to edit icon
        initializeEditIcon();
    }

    private void saveProfileChanges() {
        // Update labels with new values
        fullNameLabel.setText(fullNameField.getText());
        emailLabel.setText(emailField.getText());
        genderLabel.setText(genderComboBox.getValue());
        countryLabel.setText(countryComboBox.getValue());

        // Format and update date of birth
        if (dobPicker.getValue() != null) {
            dobLabel.setText(dobPicker.getValue().format(dateFormatter));
        }

        bioLabel.setText(bioField.getText());

        // Update initials
        updateInitials(fullNameField.getText());

        // Here, save to database/service
        System.out.println("Profile updated successfully");
    }

    @FXML
    private void onChangePasswordClick(MouseEvent event) {
        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/change-password-view.fxml")));

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

    public void setUser(Users user) {
        this.user = user;
        loadProfileData(user);
    }
}