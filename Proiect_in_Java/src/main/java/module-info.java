module com.example.Proiect_in_Java {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.example.Proiect_in_Java to javafx.fxml;
    exports com.example.Proiect_in_Java;
}