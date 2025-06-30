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
                    // Oczekiwany format: /sendfile login_odbiorcy rozmiar "nazwa_pliku"
                    String regex = "/sendfile (\\S+) (\\d+) \"(.+)\"";
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
                    java.util.regex.Matcher matcher = pattern.matcher(message);
                    if (matcher.matches()) {
                        String recipientName = matcher.group(1);
                        String fileSizeStr = matcher.group(2);
                        String fileName = matcher.group(3);
                        long fileSize = Long.parseLong(fileSizeStr);
                        // Przygotuj katalog uploads
                        File uploadsDir = new File("uploads");
                        if (!uploadsDir.exists()) uploadsDir.mkdirs();
                        String serverFileName = login + "_" + System.currentTimeMillis() + "_" + fileName;
                        File serverFile = new File(uploadsDir, serverFileName);
                        // Odbierz plik i zapisz na serwerze
                        try (DataInputStream fileIn = new DataInputStream(socket.getInputStream());
                             FileOutputStream fileOut = new FileOutputStream(serverFile)) {
                            byte[] buffer = new byte[4096];
                            long totalRead = 0;
                            int count;
                            while (totalRead < fileSize && (count = fileIn.read(buffer, 0, (int)Math.min(buffer.length, fileSize-totalRead))) > 0) {
                                fileOut.write(buffer, 0, count);
                                totalRead += count;
                            }
                        }
                        send("Plik zapisany na serwerze jako: " + serverFile.getName());
                        // Wyślij plik do odbiorcy
                        ClientHandler recipient = server.findHandlerByLogin(recipientName);
                        if (recipient != null) {
                            recipient.send(fileSizeStr); // najpierw rozmiar
                            try (FileInputStream fileIn = new FileInputStream(serverFile);
                                 DataOutputStream fileOut = new DataOutputStream(recipient.getSocket().getOutputStream())) {
                                byte[] buffer = new byte[4096];
                                int count;
                                while ((count = fileIn.read(buffer)) > 0) {
                                    fileOut.write(buffer, 0, count);
                                }
                            }
                            send("File sent to " + recipientName);
                            recipient.send("File received from " + getLogin());
                        } else {
                            send("Recipient not found");
                        }
                    } else {
                        send("Nieprawidłowa składnia komendy /sendfile. Użyj: /sendfile login_odbiorcy rozmiar \"nazwa_pliku\"");
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
