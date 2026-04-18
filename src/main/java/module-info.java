module com.template.demikopu {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.demikopi.ui to javafx.fxml;
    exports com.demikopi.ui;
}