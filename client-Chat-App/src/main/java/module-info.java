module org.client.chatapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.server.chatapp to javafx.fxml;
    exports org.server.chatapp;
}