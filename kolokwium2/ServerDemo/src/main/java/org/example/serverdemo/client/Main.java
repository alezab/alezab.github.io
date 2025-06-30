package org.example.serverdemo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Klasa uruchamiająca klienta i obsługująca komunikację z serwerem
public class Main {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 3000);
        new Thread(client).start();
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        // Przykład: wyświetlanie wiadomości w oknie dialogowym (można zamienić na konsolę)
        client.setDisplay(message -> System.out.println("[SERVER]: " + message));
        while (true) {
            String message = consoleReader.readLine();
            client.send(message);
        }
    }
}
