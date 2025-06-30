module org.example.serverdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;




    opens org.example.serverdemo to javafx.fxml;
    exports org.example.serverdemo;
}