package org.example.serverdemo.client;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.util.function.Consumer;

// Klasa klienta obsługująca połączenie z serwerem i komunikację
public class Client implements Runnable {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private Consumer<String> display;

    // Tworzy klienta i łączy z serwerem
    public Client(String address, int port) throws IOException {
        socket = new Socket(address, port);
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(input));
        writer = new PrintWriter(output, true);
    }

    // Ustawia konsumenta do wyświetlania wiadomości
    public void setDisplay(Consumer<String> display) {
        this.display = display;
    }

    // Wysyła wiadomość do serwera lub obsługuje komendę wysyłania pliku
    public void send(String message) {
        if (message.startsWith("/sendfile ")) {
            // Oczekiwany format: /sendfile login_odbiorcy "ścieżka/do/pliku.jpg"
            String regex = "/sendfile (\\S+) \"(.+)\"";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(message);
            if (matcher.matches()) {
                String recipient = matcher.group(1);
                String filePath = matcher.group(2);
                File file = new File(filePath);
                if (!file.exists() || !file.isFile()) {
                    if (display != null) display.accept("Plik nie istnieje: " + filePath);
                    else System.out.println("Plik nie istnieje: " + filePath);
                    return;
                }
                long fileSize = file.length();
                // Wyślij komendę do serwera
                writer.println("/sendfile " + recipient + " " + fileSize);
                writer.flush();
                // Wyślij binarne dane pliku
                try (FileInputStream fileIn = new FileInputStream(file);
                     DataOutputStream fileOut = new DataOutputStream(socket.getOutputStream())) {
                    byte[] buffer = new byte[64];
                    int count;
                    while ((count = fileIn.read(buffer)) > 0) {
                        fileOut.write(buffer, 0, count);
                    }
                } catch (IOException e) {
                    if (display != null) display.accept("Błąd wysyłania pliku: " + e.getMessage());
                    else e.printStackTrace();
                }
                if (display != null) display.accept("Wysłano plik do " + recipient);
                else System.out.println("Wysłano plik do " + recipient);
                return;
            }
        }
        // Domyślnie wyślij tekst
        writer.println(message);
    }

    // Wysyła plik do serwera
    public void sendFile(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        // Wysyłamy rozmiar pliku jako pierwszą linię
        // writer.println(fileSize);
        FileInputStream fileIn = new FileInputStream(file);
        DataOutputStream fileOut = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[64];
        int count;
        while ((count = fileIn.read(buffer)) > 0) {
            fileOut.write(buffer, 0, count);
        }
        fileIn.close();
    }

    // Odbiera plik z serwera i zapisuje do pliku tymczasowego
    public void receiveFile(String size) throws IOException {
        long fileSize = Long.parseLong(size);
        File file = new File(String.valueOf(java.nio.file.Path.of(System.getProperty("java.io.tmpdir")).resolve("result.bin")));
        DataInputStream fileIn = new DataInputStream(socket.getInputStream());
        FileOutputStream fileOut = new FileOutputStream(file);
        byte[] buffer = new byte[64];
        int count, receivedSize = 0;
        while ((count = fileIn.read(buffer)) > 0) {
            receivedSize += count;
            System.out.print("\r" + (receivedSize * 100 / fileSize) + "%");
            fileOut.write(buffer, 0, count);
            if (receivedSize >= fileSize) break;
        }
        fileOut.close();
        System.out.println("\nPlik zapisany: " + file.getAbsolutePath());
    }

    // Wysyła swoją nazwę jako pierwszą linię po połączeniu
    public void introduce(String name) {
        writer.println(name);
    }

    // Wysyła plik do innego klienta przez serwer
    public void sendFileTo(String recipient, String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        writer.println("SEND_FILE " + recipient + " " + fileSize);
        sendFile(filePath);
    }

    // Odbiera wiadomości od serwera i przekazuje do konsumenta lub na konsolę
    @Override
    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                if (message.matches("\\d+")) { // jeśli to rozmiar pliku
                    receiveFile(message);
                } else if (display != null) {
                    display.accept(message);
                } else {
                    System.out.println(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Zwraca socket klienta (do przesyłania plików)
    public Socket getSocket() {
        return socket;
    }
}
