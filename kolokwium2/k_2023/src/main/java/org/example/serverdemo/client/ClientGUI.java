package org.example.serverdemo.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.Socket;

public class ClientGUI extends Application {
    private File selectedFile;
    private Label fileLabel = new Label("Nie wybrano pliku");
    private Slider radiusSlider = new Slider(1, 15, 3);
    private Label radiusLabel = new Label("Promień: 3");
    private TextArea logArea = new TextArea();

    @Override
    public void start(Stage stage) {
        Button chooseButton = new Button("Wybierz plik PNG");
        chooseButton.setOnAction(e -> chooseFile(stage));

        radiusSlider.setBlockIncrement(2);
        radiusSlider.setMajorTickUnit(2);
        radiusSlider.setMinorTickCount(0);
        radiusSlider.setSnapToTicks(true);
        radiusSlider.setShowTickLabels(true);
        radiusSlider.setShowTickMarks(true);
        radiusSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int val = (int)Math.round(newVal.doubleValue());
            if (val % 2 == 0) val++;
            if (val > 15) val = 15;
            radiusSlider.setValue(val);
            radiusLabel.setText("Promień: " + val);
        });
        radiusSlider.setValue(3);

        Button sendButton = new Button("Wyślij do serwera");
        sendButton.setOnAction(e -> sendToServer());

        logArea.setEditable(false);
        logArea.setPrefRowCount(6);

        VBox root = new VBox(10, chooseButton, fileLabel, radiusSlider, radiusLabel, sendButton, logArea);
        root.setPadding(new Insets(15));
        stage.setScene(new Scene(root, 400, 320));
        stage.setTitle("Klient PNG - Box Blur");
        stage.show();
    }

    private void chooseFile(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            selectedFile = file;
            fileLabel.setText("Wybrano: " + file.getAbsolutePath());
        }
    }

    private void sendToServer() {
        if (selectedFile == null) {
            log("Nie wybrano pliku!");
            return;
        }
        int radius = (int)radiusSlider.getValue();
        if (radius % 2 == 0) radius++;
        if (radius > 15) radius = 15;
        log("Łączenie z serwerem...");
        int finalRadius = radius;
        new Thread(() -> {
            try {
                Socket socket = Client.connectToServer("localhost", 5000);
                // Wysyłamy najpierw promień filtra
                socket.getOutputStream().write(finalRadius);
                log("Wysyłanie pliku...");
                Client.send(selectedFile.getAbsolutePath(), socket);
                String outputPath = selectedFile.getParent() + File.separator + "output.png";
                log("Odbieranie przetworzonego pliku...");
                Client.receive(socket, outputPath);
                log("Zakończono! Wynik zapisano jako: " + outputPath);
                socket.close();
            } catch (Exception ex) {
                log("Błąd: " + ex.getMessage());
            }
        }).start();
    }

    private void log(String msg) {
        javafx.application.Platform.runLater(() -> logArea.appendText(msg + "\n"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
