module pl.umcs.drawingapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.umcs.drawingapp to javafx.fxml;
    exports pl.umcs.drawingapp;
}