Aby zrealizować Twoje wymagania, podzielmy projekt na kroki, zgodnie z podanymi punktami. Poniżej znajdziesz szczegółowy opis implementacji każdego kroku:

### 1. Utworzenie projektu z paczkami server, client, databasecreator

#### Struktura projektu
- Projekt 1: Aplikacja Java
  - Paczka: `pl.umcs.oop.databasecreator`
    - Plik: `Creator.java`
  - Paczka: `pl.umcs.oop.server`
    - Plik: `Server.java`
  - Paczka: `pl.umcs.oop.client`
    - Plik: `Client.java`
- Projekt 2: Aplikacja Spring Boot
  - Kontroler: `EEGController.java`
  - Aplikacja: `EEGApplication.java`

#### Plik `Creator.java`
```java
package pl.umcs.oop.databasecreator;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is responsible for creating and deleting a SQLite database.
 */
public class Creator {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:/tmp/usereeg.db";
        Creator creator = new Creator();
        creator.create(url);
    }

    public void create(String url) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS user_eeg ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL,"
                + "electrode_number INTEGER NOT NULL,"
                + "image TEXT NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Database created successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(String url) {
        String filepath = url.substring(url.indexOf("/"));
        File dbFile = new File(filepath);
        if (dbFile.exists()) {
            if (!dbFile.delete()) {
                System.out.println("Error during delete database");
            }
        } else {
            System.out.println("Error database doesn't exist");
        }
    }
}
```

### 2. Aplikacja kliencka

#### Plik `Client.java`
```java
package pl.umcs.oop.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter CSV file path: ");
        String filepath = scanner.nextLine();
        sendData(username, filepath);
    }

    public static void sendData(String username, String filepath) {
        try (Socket socket = new Socket("localhost", 12345);
             OutputStream out = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(new FileReader(filepath))) {

            out.write((username + "\n").getBytes());

            String line;
            int electrodeNumber = 0;
            while ((line = reader.readLine()) != null) {
                out.write((electrodeNumber + "," + line + "\n").getBytes());
                Thread.sleep(2000);
                electrodeNumber++;
            }
            out.write("bye\n".getBytes());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

### 3. Aplikacja serwerowa

#### Plik `Server.java`
```java
package pl.umcs.oop.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.ByteArrayOutputStream;

public class Server {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345");

            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private String username;
        private Connection conn;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                username = in.readLine();

                String url = "jdbc:sqlite:/tmp/usereeg.db";
                conn = DriverManager.getConnection(url);

                String line;
                while ((line = in.readLine()) != null && !line.equals("bye")) {
                    String[] parts = line.split(",", 2);
                    int electrodeNumber = Integer.parseInt(parts[0]);
                    String data = parts[1];

                    String base64Image = createGraphImage(data);
                    saveToDatabase(username, electrodeNumber, base64Image);
                }

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }

        private String createGraphImage(String data) {
            String[] values = data.split(",");
            int[] intValues = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                intValues[i] = (int) (Double.parseDouble(values[i]) * 10 + 50);
            }

            BufferedImage image = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 200, 100);
            g2d.setColor(Color.RED);
            for (int i = 0; i < intValues.length; i++) {
                g2d.fillRect(i, 50 - intValues[i], 1, 1);
            }
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", baos);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }

        private void saveToDatabase(String username, int electrodeNumber, String base64Image) {
            String sql = "INSERT INTO user_eeg(username, electrode_number, image) VALUES(?,?,?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setInt(2, electrodeNumber);
                pstmt.setString(3, base64Image);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
```

### 4. Testowanie metody `sendData`

#### Plik `ClientTest.java`
```java
package pl.umcs.oop.client;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/test.csv", numLinesToSkip = 1)
    public void testSendData(String username, String filepath, String expectedImagePath) {
        Client.sendData(username, filepath);

        String expectedBase64Image = getBase64Image(expectedImagePath);
        String actualBase64Image = getBase64ImageFromDB(username, 0); // Assuming single electrode

        assertEquals(expectedBase64Image, actualBase64Image);
    }

    private String getBase64Image(String imagePath) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(imagePath));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getBase64ImageFromDB(String username, int electrodeNumber) {
        // Implement this method to retrieve the base64 image from the database
        // based on the username and electrodeNumber.
        return "";
    }
}
```

### 5. Kontroler aplikacji Spring Boot

#### Plik `EEGController.java`
```java
package com.example.eeg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

@RestController
public class EEGController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/user/{username}/electrode/{electrodeNumber}")
    public String getEEGImage(@PathVariable String username, @PathVariable int electrodeNumber) {
        String sql = "SELECT image FROM user_eeg WHERE username = ?

 AND electrode_number = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, username, electrodeNumber);

        String base64Image = (String) result.get("image");
        return "<html><body><h1>User: " + username + "</h1><h2>Electrode: " + electrodeNumber + "</h2>"
                + "<img src='data:image/png;base64, " + base64Image + "'/></body></html>";
    }
}
```

#### Plik `EEGApplication.java`
```java
package com.example.eeg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EEGApplication {

    public static void main(String[] args) {
        SpringApplication.run(EEGApplication.class, args);
    }
}
```

### Uwagi końcowe
- **Baza danych**: Utwórz bazę danych przy użyciu klasy `Creator` przed uruchomieniem serwera.
- **Spring Boot**: Upewnij się, że masz skonfigurowaną aplikację Spring Boot z odpowiednimi zależnościami w `pom.xml`.
- **Testowanie**: Upewnij się, że dane testowe są prawidłowo skonfigurowane w pliku `test.csv` oraz że baza danych jest dostępna podczas testowania.

Przestrzeganie tych kroków powinno pozwolić na poprawne zrealizowanie zadania. Jeśli masz dodatkowe pytania lub potrzebujesz dalszej pomocy, daj znać!
