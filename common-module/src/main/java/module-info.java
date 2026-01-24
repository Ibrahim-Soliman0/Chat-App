module org.common.module {
    requires java.sql;
    requires java.rmi;

    exports dto;
    exports rmi;
    exports model;
    exports model.enums;
}