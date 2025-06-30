package org.example.serverdemo.server;

import java.io.*;
import java.net.Socket;

// Klasa obsługująca pojedynczego klienta w osobnym wątku
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server server;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private String name; // Nazwa klienta
    private String login;

    // Konstruktor inicjalizuje strumienie i referencję do serwera
    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(input));
        writer = new PrintWriter(output, true);
    }

    // Wysyła wiadomość do klienta
    public void send(String message) {
        writer.println(message);
    }

    // Zamyka połączenie i usuwa handler z serwera
    private void close() throws IOException {
        socket.close();
        server.removeHandler(this);
    }

    // Obsługuje komunikację z klientem (odbiór i broadcast wiadomości)
    @Override
    public void run() {
        System.out.println("Client connected");
        String message;
        try {
            // Pierwsza linia od klienta to login
            login = reader.readLine();
            if (login == null) {
                close();
                return;
            }
            server.broadcast(login + " dołączył do czatu!");
            while ((message = reader.readLine()) != null) {
                if (message.startsWith("/sendfile ")) {
                    String[] parts = message.split(" ");
                    String recipientName = parts[1];
                    String fileSize = parts[2];
                    ClientHandler recipient = server.findHandlerByLogin(recipientName);
                    if (recipient != null) {
                        server.transferFile(fileSize, this, recipient);
                        send("File sent to " + recipientName);
                        recipient.send("File received from " + getLogin());
                    } else {
                        send("Recipient not found");
                    }
                } else if (message.startsWith("/w ")) {
                    // /w odbiorca wiadomość
                    String[] parts = message.split(" ", 3);
                    if (parts.length >= 3) {
                        String recipient = parts[1];
                        String msg = parts[2];
                        ClientHandler target = server.findHandlerByLogin(recipient);
                        if (target != null) {
                            target.send("[whisper od " + login + "]: " + msg);
                            send("[whisper do " + recipient + "]: " + msg);
                        } else {
                            send("Użytkownik " + recipient + " nie jest online.");
                        }
                    }
                } else if (message.equals("/online")) {
                    send("Online: " + server.getOnlineList());
                } else {
                    server.broadcast(login + ": " + message);
                }
            }
            close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.broadcast(login + " opuścił czat.");
        System.out.println("Client disconnected");
    }

    // Zwraca socket klienta (do przesyłania plików)
    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }
}
