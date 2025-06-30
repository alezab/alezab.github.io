package org.example.serverdemo.server;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;

// Klasa serwera obsługująca wielu klientów i komunikację między nimi
public class Server {
    private static final int PORT = 5000;
    private static final String IMAGES_DIR = "images";
    private static final String DB_PATH = IMAGES_DIR + "/index.db";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    // Ustaw promień filtra tutaj lub pobierz z GUI
    private static final int filterRadius = 3; // domyślnie 3, tylko nieparzyste 1-15

    // Tworzy serwer nasłuchujący na porcie 5000
    public Server() throws IOException, SQLException {
        // Tworzenie katalogu images
        Files.createDirectories(Paths.get(IMAGES_DIR));
        // Tworzenie bazy jeśli nie istnieje
        createDbIfNotExists();
    }

    // Rozpoczyna nasłuchiwanie i obsługę klientów w osobnych wątkach
    public void listen() throws Exception {
        System.out.println("Serwer nasłuchuje na porcie " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Połączono z klientem: " + socket.getInetAddress());
                    handleClient(socket);
                }
            }
        }
    }

    private void handleClient(Socket socket) throws Exception {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        // Najpierw odbierz promień filtra (jako bajt)
        int filterRadius = in.read();
        if (filterRadius < 1 || filterRadius > 15 || filterRadius % 2 == 0) filterRadius = 3;
        // Odbierz plik
        long fileSize = in.readLong();
        String timestamp = sdf.format(new Date());
        String inputFile = IMAGES_DIR + "/" + timestamp + ".png";
        try (FileOutputStream fos = new FileOutputStream(inputFile)) {
            byte[] buffer = new byte[8192];
            long received = 0;
            while (received < fileSize) {
                int toRead = (int)Math.min(buffer.length, fileSize - received);
                int count = in.read(buffer, 0, toRead);
                if (count == -1) break;
                fos.write(buffer, 0, count);
                received += count;
            }
        }
        // Przetwarzanie obrazu
        BufferedImage img = ImageIO.read(new File(inputFile));
        long start = System.currentTimeMillis();
        BufferedImage blurred = boxBlurParallel(img, filterRadius);
        long delay = System.currentTimeMillis() - start;
        String outputFile = IMAGES_DIR + "/" + timestamp + "_blur.png";
        ImageIO.write(blurred, "png", new File(outputFile));
        // Zapis do bazy
        saveToDb(outputFile, filterRadius, delay);
        // Odesłanie pliku
        File outFile = new File(outputFile);
        out.writeLong(outFile.length());
        try (FileInputStream fis = new FileInputStream(outFile)) {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = fis.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
        }
        System.out.println("Zakończono obsługę klienta.");
    }

    // Box blur zrównoleglony
    private BufferedImage boxBlurParallel(BufferedImage src, int radius) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dst = new BufferedImage(w, h, src.getType());
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(cores);
        int chunk = h / cores;
        for (int t = 0; t < cores; t++) {
            final int yStart = t * chunk;
            final int yEnd = (t == cores - 1) ? h : (t + 1) * chunk;
            pool.submit(() -> {
                for (int y = yStart; y < yEnd; y++) {
                    for (int x = 0; x < w; x++) {
                        dst.setRGB(x, y, boxBlurPixel(src, x, y, radius));
                    }
                }
            });
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
            try { Thread.sleep(10); } catch (InterruptedException e) { }
        }
        return dst;
    }

    private int boxBlurPixel(BufferedImage img, int x, int y, int radius) {
        int w = img.getWidth();
        int h = img.getHeight();
        int r = 0, g = 0, b = 0, count = 0;
        int half = radius / 2;
        for (int dy = -half; dy <= half; dy++) {
            for (int dx = -half; dx <= half; dx++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < w && ny >= 0 && ny < h) {
                    int rgb = img.getRGB(nx, ny);
                    r += (rgb >> 16) & 0xFF;
                    g += (rgb >> 8) & 0xFF;
                    b += rgb & 0xFF;
                    count++;
                }
            }
        }
        r /= count;
        g /= count;
        b /= count;
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    private void createDbIfNotExists() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("DROP TABLE IF EXISTS images");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS images (id INTEGER PRIMARY KEY AUTOINCREMENT, path TEXT, size INTEGER, delay INTEGER)");
            }
        }
    }

    private void saveToDb(String path, int size, long delay) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO images (path, size, delay) VALUES (?, ?, ?);")) {
                ps.setString(1, path);
                ps.setInt(2, size);
                ps.setLong(3, delay);
                ps.executeUpdate();
            }
        }
    }

    // Główna metoda uruchamiająca serwer i obsługująca zamknięcie
    public static void main(String[] args) throws IOException, SQLException, Exception {
        Server server = new Server();
        server.listen();
    }
}
