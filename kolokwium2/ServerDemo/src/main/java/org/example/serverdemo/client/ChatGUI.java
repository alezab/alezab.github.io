package org.example.serverdemo.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatGUI extends Application {
    private Client client;
    private TextArea chatArea = new TextArea();
    private TextField inputField = new TextField();
    private Button sendButton = new Button("Wyślij");
    private String login;

    @Override
    public void start(Stage stage) throws Exception {
        login = promptLogin();
        client = new Client("localhost", 3000);
        client.setDisplay(msg -> Platform.runLater(() -> chatArea.appendText(msg + "\n")));
        client.introduce(login);
        new Thread(client).start();

        chatArea.setEditable(false);
        sendButton.setOnAction(e -> sendMessage());
        inputField.setOnAction(e -> sendMessage());

        HBox bottomPanel = new HBox(inputField, sendButton);
        BorderPane pane = new BorderPane();
        pane.setCenter(chatArea);
        pane.setBottom(bottomPanel);

        Scene scene = new Scene(pane, 600, 400);
        stage.setTitle("Chat GUI");
        stage.setScene(scene);
        stage.show();
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            client.send(message);
            inputField.clear();
        }
    }

    private String promptLogin() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Login");
        dialog.setHeaderText("Podaj swój login:");
        dialog.setContentText("Login:");
        return dialog.showAndWait().orElse("user");
    }

    public static void main(String[] args) {
        launch();
    }
}
