module com.example.lab4map {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;
    requires junit;

    //requires org.controlsfx.controls;
    //requires com.dlsc.formsfx;

    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.example.lab4map to javafx.fxml;
    exports com.example.lab4map;
}