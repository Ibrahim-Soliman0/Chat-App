package org.server.chatapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.server.chatapp.util.RMIUtil;
import org.server.chatapp.rmi.*;

import java.io.IOException;

public class ServerChatApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServerChatApp.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String css = this.getClass().getResource("/css/admin.css").toExternalForm();

        scene.getStylesheets().add(css);

        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setOnCloseRequest(event -> {
            try {
                if (RMIUtil.isRunning()) {
                    RMIUtil.stopServices();
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Failed to stop services during exit: " + e.getMessage());
            }
            System.exit(0);
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
