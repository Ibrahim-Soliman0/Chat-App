module org.server.chatapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires org.common.module;
    requires java.rmi;

    opens org.server.chatapp to javafx.fxml;
    exports org.server.chatapp;
}