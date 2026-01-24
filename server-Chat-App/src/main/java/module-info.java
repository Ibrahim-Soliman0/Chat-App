module org.server.chatapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires org.common.module;
    requires java.rmi;
    requires jbcrypt;

    opens org.server.chatapp.ui.controller to javafx.fxml;
    opens org.server.chatapp to javafx.fxml;
    exports org.server.chatapp;
}