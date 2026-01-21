module org.client.chatapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.common.module;
    requires java.rmi;

    exports org.client.chatapp;
    opens org.client.chatapp to javafx.fxml;
    opens org.client.chatapp.ui.controller to javafx.fxml;
    exports org.client.chatapp.ui.controller;
}