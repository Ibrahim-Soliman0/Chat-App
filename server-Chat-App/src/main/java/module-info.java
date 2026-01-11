module org.server.chatapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;


    opens org.server.chatapp to javafx.fxml;
    exports org.server.chatapp;
}