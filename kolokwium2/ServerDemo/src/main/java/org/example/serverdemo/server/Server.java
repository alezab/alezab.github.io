package org.example.serverdemo.server;

import java.io.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// Klasa serwera obsługująca wielu klientów i komunikację między nimi
public class Server {
    private ServerSocket serverSocket;
    private final List<ClientHandler> handlers = new ArrayList<>();

    // Tworzy serwer nasłuchujący na porcie 3000
    public Server() throws IOException {
        serverSocket = new ServerSocket(3000);
    }

    // Rozpoczyna nasłuchiwanie i obsługę klientów w osobnych wątkach
    public void listen() throws IOException {
        System.out.println("Server started");
        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket, this);
            Thread thread = new Thread(handler);
            thread.start();
            handlers.add(handler);
        }
    }

    // Rozsyła wiadomość do wszystkich klientów
    public void broadcast(String message) {
        handlers.forEach(handler -> handler.send(message));
    }

    // Usuwa handler zakończonego klienta
    public void removeHandler(ClientHandler handler) {
        handlers.remove(handler);
    }

    // Wysyła wiadomość do wszystkich klientów i czyści listę handlerów (zamyka połączenia)
    public void disconnectHandlers() {
        handlers.forEach(handler -> handler.send("Bye!"));
        handlers.clear();
    }

    // Przesyła plik od jednego klienta do drugiego
    public void transferFile(String fileSize, ClientHandler sender, ClientHandler recipient) throws IOException {
        DataInputStream fileIn = new DataInputStream(sender.getSocket().getInputStream());
        DataOutputStream fileOut = new DataOutputStream(recipient.getSocket().getOutputStream());
        byte[] buffer = new byte[64];
        int count;
        recipient.send(fileSize); // najpierw rozmiar pliku
//        while ((count = fileIn.read(buffer)) > 0) {
//            fileOut.write(buffer, 0, count);
//        }
        long totalRead = 0;
        long expectedSize = Long.parseLong(fileSize);
        while (totalRead < expectedSize && (count = fileIn.read(buffer)) > 0) {
            fileOut.write(buffer, 0, count);
            totalRead += count;
        }
    }

    // Znajduje handler klienta po loginie
    public ClientHandler findHandlerByLogin(String login) {
        for (ClientHandler handler : handlers) {
            if (handler.getLogin() != null && handler.getLogin().equals(login)) {
                return handler;
            }
        }
        return null;
    }

    // Zwraca listę loginów online
    public String getOnlineList() {
        return handlers.stream()
                .map(ClientHandler::getLogin)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }

    // Główna metoda uruchamiająca serwer i obsługująca zamknięcie
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        Runtime.getRuntime().addShutdownHook(new Thread(server::disconnectHandlers));
        server.listen();
    }
}
