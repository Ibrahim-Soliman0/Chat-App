package org.server.chatapp.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import model.Users;
import model.enums.Gender;
import model.enums.Role;
import model.enums.Status;
import org.server.chatapp.dao.dao.UsersDao;
import org.server.chatapp.dao.implement.UsersImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserManagementController {

    @FXML
    private TableView<Users> usersTable;
    @FXML
    private TableColumn<Users, String> colPhone, colName, colEmail, colCountry;
    @FXML
    private TableColumn<Users, Status> colStatus;
    @FXML
    private TextField searchField;
    @FXML
    private Label lblTotalUsers;
    @FXML
    private TableColumn<Users, Gender> colGender;
    private final UsersDao usersDao = new UsersImpl();
    private ObservableList<Users> usersMasterList;
    private final Set<Users> modifiedUsers = new HashSet<>();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadUsersData();
        setupSearch();
    }

    private void setupTableColumns() {
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        makeColumnEditable(colName, "name");
//        makeColumnEditable(colEmail, "email");
        makeColumnEditable(colCountry, "country");


        colGender.setCellFactory(ComboBoxTableCell.forTableColumn(Gender.values()));

        colGender.setOnEditCommit(event -> {
            Users user = event.getRowValue();
            Gender newValue = event.getNewValue();
            if (user.getRole() == Role.MASTER_ADMIN) {
                showAlert("Action Denied", "Master Admin details cannot be modified.");
                usersTable.refresh();
                return;
            }

            if (newValue != null) {
                user.setGender(newValue);
                modifiedUsers.add(user);
            }
        });
    }

    private void makeColumnEditable(TableColumn<Users, String> column, String fieldName) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            Users user = event.getRowValue();
            String newValue = event.getNewValue();
            if (user.getRole() == model.enums.Role.MASTER_ADMIN) {
                showAlert("Permission Denied", "You cannot edit Master Admin's data.");
                usersTable.refresh();
                return;
            }
            switch (fieldName) {
                case "name" -> user.setName(newValue);
                case "country" -> user.setCountry(newValue);
            }
            modifiedUsers.add(user);

        });
    }

    private void loadUsersData() {
        List<Users> allUsers = usersDao.getAll();
        usersMasterList = FXCollections.observableArrayList(allUsers);
        usersTable.setItems(usersMasterList);
        lblTotalUsers.setText("Total Users: " + usersMasterList.size());
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                usersTable.setItems(usersMasterList);
            } else {
                String filter = newVal.toLowerCase();
                ObservableList<Users> filteredList = usersMasterList.filtered(u ->
                        u.getName().toLowerCase().contains(filter) ||
                                u.getPhoneNumber().contains(filter)
                );
                usersTable.setItems(filteredList);
            }
        });
    }
    @FXML
    private void handleApplyChanges() {
        if (modifiedUsers.isEmpty()) {
            showInfo("No changes to apply.");
            return;
        }

        int totalSuccess = 0;
        for (Users user : modifiedUsers) {
            totalSuccess += usersDao.update(user);
        }

        if (totalSuccess == modifiedUsers.size()) {
            showInfo("Success", "All changes applied to database.");
            modifiedUsers.clear();
        } else {
            showAlert("Warning", "Some changes might not have been saved.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    private void showInfo(String message) {
        showInfo("Information", message);
    }
    @FXML
    private void handleCancelChanges() {
        if (modifiedUsers.isEmpty()) {
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Discard Changes");
        confirm.setHeaderText("Are you sure?");
        confirm.setContentText("This will revert all unapplied changes in the table.");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            modifiedUsers.clear();
            loadUsersData();
            showInfo("Changes discarded successfully.");
        }
    }
}