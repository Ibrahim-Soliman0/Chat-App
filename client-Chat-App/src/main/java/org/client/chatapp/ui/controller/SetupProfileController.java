package org.client.chatapp.ui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Users;
import model.enums.Gender;
import rmi.RegisterService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;

public class SetupProfileController {


    @FXML
    private ImageView profileImageView;
    private File selectedImageFile;

    @FXML
    private Group profilePlaceholder;

    @FXML
    private TextField phoneField;
    private String phoneNumber;

    @FXML
    ScrollPane scrollPaneForm;
    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label nameError;

    @FXML
    private Label emailError;

    @FXML
    private Label passwordError;

    @FXML
    private Label confirmPasswordError;

    @FXML
    private Button createAccountBtn;
    @FXML
    private RadioButton maleRadio;
    @FXML
    private RadioButton femaleRadio;
    @FXML
    private ToggleGroup genderGroup;
    @FXML
    private Label genderError;

    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private Label countryError;

    @FXML
    private DatePicker dobPicker;
    @FXML
    private Label dobError;

    @FXML
    private TextArea bioField;
    @FXML
    private Label bioCounter;
    @FXML
    private Label bioError;
    @FXML
    private Label clearImageLbl;
    private static final int BIO_MAX_LENGTH = 150;

    @FXML
    public void initialize() {
        dobPicker.setEditable(false);
        Platform.runLater(() -> {
            scrollPaneForm.getScene().getStylesheets().add(
                    getClass().getResource("/css/date-picker.css").toExternalForm()
            );
        });
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

        bioField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() > BIO_MAX_LENGTH) {
                bioField.setText(oldText);
            }
            bioCounter.setText(bioField.getText().length() + "/" + BIO_MAX_LENGTH);
        });

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

    private boolean isValidGender() {
        return genderGroup.getSelectedToggle() != null;
    }

    private boolean isValidCountry() {
        return countryComboBox.getValue() != null && !countryComboBox.getValue().isEmpty();
    }

    private boolean isValidDOB() {
        if (dobPicker.getValue() == null) return false;
        return !dobPicker.getValue().isAfter(LocalDate.now());
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        phoneField.setText("+2" + phoneNumber);
    }


    public void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files", "*.png", "*.jpg", "*.jpeg"
                )
        );

        File file = fileChooser.showOpenDialog(
                profileImageView.getScene().getWindow()
        );

        if (file != null) {
            selectedImageFile = file;

            Image image = new Image(
                    file.toURI().toString(),
                    100, 100,
                    false,
                    true
            );

            profileImageView.setImage(image);

            profilePlaceholder.setVisible(false);
            clearImageLbl.setVisible(true);
            clearImageLbl.setManaged(true);
        }
    }


    private boolean isValidName(String name) {
        return name != null && name.trim().length() >= 3;
    }


    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(regex);
    }

    private boolean isValidBio() {
        return bioField.getText() == null || bioField.getText().length() <= BIO_MAX_LENGTH;
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    private void clearErrors() {
        nameError.setVisible(false);
        emailError.setVisible(false);
        passwordError.setVisible(false);
        confirmPasswordError.setVisible(false);
        genderError.setVisible(false);
        countryError.setVisible(false);
        dobError.setVisible(false);
        bioError.setVisible(false);
    }

    public void handleCreateAccount() {
        clearErrors();

        boolean valid = true;

        valid &= showErrorIfInvalid(
                isValidName(nameField.getText()),
                nameError,
                "Name must be at least 3 characters"
        );

        valid &= showErrorIfInvalid(
                isValidEmail(emailField.getText()),
                emailError,
                "Please enter a valid email address"
        );

        valid &= showErrorIfInvalid(
                isValidPassword(passwordField.getText()),
                passwordError,
                "Password must be at least 6 characters"
        );

        valid &= showErrorIfInvalid(
                !confirmPasswordField.getText().isEmpty()
                        && passwordField.getText().equals(confirmPasswordField.getText()),
                confirmPasswordError,
                "Passwords do not match"
        );

        valid &= showErrorIfInvalid(isValidGender(), genderError, "Please select a gender");
        valid &= showErrorIfInvalid(isValidCountry(), countryError, "Please select a country");
        valid &= showErrorIfInvalid(isValidDOB(), dobError, "Date of birth cannot be in the future");
        valid &= showErrorIfInvalid(isValidBio(), bioError, "Bio cannot exceed 150 characters");

        if (!valid) {
            return;
        }
        registerUser();
    }

    private void registerUser() {
        try {
            createAccountBtn.setDisable(true);
            createAccountBtn.setText("Creating Account...");
            Users newUser = new Users();
            newUser.setPhoneNumber(phoneNumber);
            newUser.setName(nameField.getText().trim());
            newUser.setEmail(emailField.getText().trim());
            newUser.setPassword(passwordField.getText());
            if (maleRadio.isSelected()) {
                newUser.setGender(Gender.MALE);
            } else {
                newUser.setGender(Gender.FEMALE);
            }
            newUser.setCountry(countryComboBox.getValue());
            newUser.setDob(dobPicker.getValue());
            newUser.setBio(bioField.getText() == null || bioField.getText().isEmpty()
                    ? null
                    : bioField.getText().trim());

            if (selectedImageFile != null) {
//                newUser.setPicturePath(selectedImageFile.getAbsolutePath());
                try {
                    byte[] imageBytes = Files.readAllBytes(selectedImageFile.toPath());
                    newUser.setPictureBytes(imageBytes);
                } catch (IOException e) {
                    showErrorAlert("Could not read image file: " + e.getMessage());
                    return;
                }
            } else {
                newUser.setPicturePath(null);
            }
            Registry registry = LocateRegistry.getRegistry("localhost", 5000);
            RegisterService registerService = (RegisterService) registry.lookup("RegisterService");
            Users registeredUser = registerService.register(newUser);
            showSuccessAlert("Account created successfully!\nWelcome, " + registeredUser.getName() + "!");
            System.out.println("Account Created Successfully");
            moveToLogin();
        } catch (RemoteException e) {
            String errorMessage = e.getMessage();
            if (e.getCause() != null) {
                errorMessage = e.getCause().getMessage();
            }
            if (errorMessage.contains("Email already exists")) {
                showErrorAlert("This email is already registered!\nPlease use a different email.");
            } else if (errorMessage.contains("Phone number already registered")) {
                showErrorAlert("This phone number is already registered!");
            } else {
                showErrorAlert("Registration failed: " + errorMessage);
            }

        } catch (NotBoundException e) {
            showErrorAlert("Could not connect to server.\nPlease make sure the server is running.");
        } catch (IOException e) {
            showErrorAlert("An error occurred during registration.\nPlease try again.");
            e.printStackTrace();

        } finally {
            createAccountBtn.setDisable(false);
            createAccountBtn.setText("Create Account →");

        }
    }

    private void moveToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/client/chatapp/login-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) genderError.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showErrorIfInvalid(boolean condition,
                                       Label errorLabel,
                                       String errorMessage) {
        if (!condition) {
            errorLabel.setText(errorMessage);
            errorLabel.setVisible(true);
            return false;
        }
        errorLabel.setVisible(false);
        return true;
    }

    @FXML
    public void handleBackAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/client/chatapp/register-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) genderError.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void handleRemovePhoto(){
        selectedImageFile = null;
        profileImageView.setImage(null);
        profilePlaceholder.setVisible(true);

        clearImageLbl.setVisible(false);
        clearImageLbl.setManaged(false);
    }
    @FXML
    public void handleBackToLogin() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/client/chatapp/login-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) genderError.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
