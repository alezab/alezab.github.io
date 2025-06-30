package pl.umcs.drawingapp;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class DrawingController {
    @FXML
    private Canvas drawingCanvas;
    @FXML
    private ComboBox<ShapeType> shapeComboBox;
    @FXML
    private Button drawButton;

    private ShapeType selectedShape;

    @FXML
    public void initialize() {
        shapeComboBox.getItems().setAll(ShapeType.values());
        shapeComboBox.setOnAction(event -> selectedShape = shapeComboBox.getValue());
    }

    @FXML
    private void onDrawButtonClicked() {
        if (selectedShape != null) {
            drawingCanvas.setOnMouseClicked(this::drawShape);
        }
    }

    private void drawShape(MouseEvent event) {
        GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
        double x = event.getX();
        double y = event.getY();
        double size = 50; // Fixed size for simplicity

        gc.setFill(Color.BLUE); // Set color for drawing

        switch (selectedShape) {
            case CIRCLE:
                gc.fillOval(x - size / 2, y - size / 2, size, size);
                break;
            case RECTANGLE:
                gc.fillRect(x - size / 2, y - size / 2, size, size);
                break;
        }
    }
}