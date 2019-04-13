module database {
    requires javafx.controls;
    requires javafx.fxml;
    requires sqlite.jdbc;
    requires cryptlib;
    requires java.sql;
    exports home.david.database.crypted;
}