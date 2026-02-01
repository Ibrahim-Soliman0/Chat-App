package org.server.chatapp.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import model.ServerAnnouncement;
import org.server.chatapp.dao.dao.ServerAnnouncementDao;
import org.server.chatapp.dao.implement.ServerAnnouncementImpl;
import org.server.chatapp.rmi.LoginServiceImpl;
import rmi.LoginService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;

public class AnnouncementController {
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    TextField titleField;
    private LoginService loginService;
    private final ServerAnnouncementDao announcementDao = new ServerAnnouncementImpl();

    @FXML
    public void initialize() {
        try {
            Registry registry = LocateRegistry.getRegistry(5000);
            this.loginService = (LoginService) registry.lookup("LoginService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleSendAnnouncement() {
        String htmlContent = htmlEditor.getHtmlText();
        String title = titleField.getText().trim();
        String plainText = htmlContent.replaceAll("<[^>]*>", "").trim();
        if (title.isEmpty() || plainText.isEmpty()) {
            showWarningAlert("Input Error", "Make sure to enter both a title and content for the announcement.");
            return;
        }
        try {
            ServerAnnouncement announcement = new ServerAnnouncement();
            announcement.setTitle(title);
            announcement.setContent(htmlContent);
            announcement.setCreatedBy("Admin");
            announcement.setCreatedAt(LocalDateTime.now());
            announcement.setExpireAt(LocalDateTime.now().plusDays(7));
            announcement.setActive(true);

            long result = announcementDao.insert(announcement);

            if (result > 0) {
                loginService.broadcastAnnouncement(title, htmlContent);
                titleField.clear();
                htmlEditor.setHtmlText("");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleClearButton() {
        htmlEditor.setHtmlText("");
    }
}
