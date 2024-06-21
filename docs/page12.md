Oczywiście! Oto przydatne snippety kodu w Javie, które mogą pomóc w różnych sytuacjach programistycznych.

### 1. JUnit Test (JUnit 5 - Jupiter)

JUnit jest powszechnie używanym frameworkiem do testowania jednostkowego w Javie. Oto przykład prostego testu.

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleTest {
    
    @Test
    public void testAddition() {
        assertEquals(5, add(2, 3), "2 + 3 should equal 5");
    }
    
    private int add(int a, int b) {
        return a + b;
    }
}
```
- `@Test` oznacza metodę testową.
- `assertEquals` sprawdza, czy wynik działania metody jest zgodny z oczekiwanym wynikiem.

### 2. SQLite z org.xerial.jdbc

SQLite jest lekki, wbudowany w bazę danych. `org.xerial.sqlite-jdbc` to biblioteka JDBC do obsługi SQLite w Javie.

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLiteExample {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS sample_table (id INTEGER, name TEXT)");
            statement.executeUpdate("INSERT INTO sample_table VALUES (1, 'John Doe')");
            
            ResultSet resultSet = statement.executeQuery("SELECT * FROM sample_table");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("name"));
            }
            
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
- `DriverManager.getConnection` otwiera połączenie z bazą danych SQLite.
- `Statement` umożliwia wykonanie zapytań SQL.

### 3. Manipulacja jasnością obrazka

Manipulacja obrazem, np. zmiana jasności, może być wykonana za pomocą `BufferedImage`.

```java
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageBrightness {
    public static void main(String[] args) throws Exception {
        BufferedImage image = ImageIO.read(new File("path/to/image.jpg"));
        BufferedImage brightened = changeBrightness(image, 50);
        ImageIO.write(brightened, "jpg", new File("path/to/brightened_image.jpg"));
    }

    public static BufferedImage changeBrightness(BufferedImage img, int value) {
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                int r = clamp((rgb >> 16) & 0xff + value);
                int g = clamp((rgb >> 8) & 0xff + value);
                int b = clamp((rgb & 0xff) + value);
                int newRgb = (r << 16) | (g << 8) | b;
                result.setRGB(x, y, newRgb);
            }
        }
        return result;
    }

    private static int clamp(int value) {
        return Math.min(255, Math.max(0, value));
    }
}
```
- `BufferedImage` pozwala na manipulację pikselami obrazu.
- Funkcja `clamp` ogranicza wartości kolorów do zakresu 0-255.

### 4. Wątki: implementacja Runnable i dziedziczenie po Thread

```java
// Implementacja Runnable
public class RunnableExample implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable is running");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new RunnableExample());
        thread.start();
    }
}

// Dziedziczenie po Thread
public class ThreadExample extends Thread {
    @Override
    public void run() {
        System.out.println("Thread is running");
    }

    public static void main(String[] args) {
        ThreadExample thread = new ThreadExample();
        thread.start();
    }
}
```
- `Runnable` umożliwia definiowanie zadań, które mogą być uruchamiane w wątkach.
- `Thread` umożliwia tworzenie nowych wątków poprzez dziedziczenie.

### 5. ArrayList: iteracja

```java
import java.util.ArrayList;

public class ArrayListExample {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");

        // Iteracja przy użyciu pętli for-each
        for (String fruit : list) {
            System.out.println(fruit);
        }

        // Iteracja przy użyciu iteratora
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
```
- `ArrayList` to dynamicznie rozrastająca się lista w Javie.
- Można iterować przez elementy listy za pomocą pętli `for-each` lub `Iterator`.

### 6. Manipulacja plikami CSV bez dodatkowych bibliotek

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVExample {
    public static void main(String[] args) {
        String csvFile = "path/to/csvfile.csv";
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                System.out.println("Data [column1= " + data[0] + " , column2=" + data[1] + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("path/to/output.csv")) {
            writer.append("Column1,Column2\n");
            writer.append("Data1,Data2\n");
            writer.append("Data3,Data4\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
- `BufferedReader` do odczytu pliku CSV linia po linii.
- `FileWriter` do zapisu danych do pliku CSV.

### 7. Spring Boot: podstawowy kontroler

Spring Boot upraszcza tworzenie aplikacji Spring, umożliwiając szybkie tworzenie kontrolerów HTTP.

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringBootExample {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootExample.class, args);
    }
}

@RestController
class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}
```
- `@SpringBootApplication` oznacza główną klasę aplikacji Spring Boot.
- `@RestController` tworzy kontroler REST.
- `@GetMapping` mapuje żądanie GET do metody.

Każdy z powyższych przykładów ilustruje kluczowe funkcje Javy i popularnych bibliotek/frameworków, które mogą być użyteczne na sprawdzianach lub w 
codziennej pracy programistycznej.

Oczywiście, oto bardziej rozbudowany przykład wykorzystania biblioteki `org.sqlite.JDBC` do wykonywania różnych operacji SQL, takich jak zapis, odczyt, aktualizacja i usuwanie danych z gotowej bazy danych SQLite.

### Przykład operacji SQL w SQLite

Najpierw dodajemy zależność do `org.xerial:sqlite-jdbc` w pliku `pom.xml` (jeśli używamy Maven):

```xml
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.36.0.3</version>
</dependency>
```

Następnie tworzymy klasę `SQLiteExample`, która zawiera wszystkie operacje na bazie danych.

### SQLiteExample.java

```java
import java.sql.*;

public class SQLiteExample {

    private static final String URL = "jdbc:sqlite:sample.db";

    public static void main(String[] args) {
        createTable();
        insertData(1, "John Doe", 30);
        insertData(2, "Jane Smith", 25);
        readData();
        updateData(1, "John Doe Updated", 31);
        deleteData(2);
        readData();
    }

    // Tworzenie tabeli
    private static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     "id INTEGER PRIMARY KEY," +
                     "name TEXT NOT NULL," +
                     "age INTEGER NOT NULL)";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Wstawianie danych
    private static void insertData(int id, String name, int age) {
        String sql = "INSERT INTO users(id, name, age) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.executeUpdate();
            System.out.println("Data inserted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Odczyt danych
    private static void readData() {
        String sql = "SELECT id, name, age FROM users";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Age: " + rs.getInt("age"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Aktualizacja danych
    private static void updateData(int id, String name, int age) {
        String sql = "UPDATE users SET name = ?, age = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            System.out.println("Data updated.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Usuwanie danych
    private static void deleteData(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Data deleted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
```

### Wyjaśnienie:

1. **`createTable`**: Tworzy tabelę `users`, jeśli jeszcze nie istnieje.
   - `id`: Klucz główny, liczba całkowita.
   - `name`: Nazwa użytkownika, tekst.
   - `age`: Wiek użytkownika, liczba całkowita.

2. **`insertData`**: Wstawia dane do tabeli `users`.
   - `PreparedStatement` służy do zabezpieczenia przed SQL injection.

3. **`readData`**: Odczytuje wszystkie wiersze z tabeli `users` i wyświetla je na konsoli.
   - `ResultSet` jest używany do iteracji przez wyniki zapytania SQL.

4. **`updateData`**: Aktualizuje dane użytkownika na podstawie `id`.
   - `PreparedStatement` służy do wykonywania aktualizacji danych.

5. **`deleteData`**: Usuwa użytkownika na podstawie `id`.
   - `PreparedStatement` służy do wykonywania usuwania danych.

### Jak używać:

1. Upewnij się, że masz bibliotekę `sqlite-jdbc` w swoim projekcie (np. dodając ją do `pom.xml` w Maven).
2. Skompiluj i uruchom powyższą klasę `SQLiteExample`.
3. Program stworzy bazę danych `sample.db`, utworzy tabelę, wstawi dane, odczyta je, zaktualizuje i usunie wybrane wiersze, a następnie ponownie odczyta dane, aby pokazać wyniki operacji.

Ten kod pokazuje, jak wykonywać podstawowe operacje CRUD (Create, Read, Update, Delete) w bazie danych SQLite przy użyciu JDBC. Jest to przydatne w wielu aplikacjach, które wymagają lokalnego przechowywania danych.

Oczywiście, oto pełna aplikacja chatu typu klient-serwer wykorzystująca gniazda (sockets) w Javie. Serwer umożliwia użytkownikom logowanie się za pomocą nazwy użytkownika i hasła, które są przechowywane w bazie danych SQLite. Dodatkowo, aplikacja obsługuje komendy `/broadcast` do wysyłania wiadomości do wszystkich użytkowników oraz `/whisper` do wysyłania prywatnych wiadomości do konkretnego użytkownika.

### 1. Konfiguracja bazy danych SQLite

Najpierw utwórz bazę danych SQLite i tabelę użytkowników.

```sql
-- create_users_table.sql
CREATE TABLE IF NOT EXISTS users (
    username TEXT PRIMARY KEY,
    password TEXT NOT NULL
);

-- Insert example users
INSERT INTO users (username, password) VALUES ('user1', 'pass1');
INSERT INTO users (username, password) VALUES ('user2', 'pass2');
```

### 2. Serwer (Server.java)

```java
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private static Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
    private static final String DB_URL = "jdbc:sqlite:chat.db";

    public static void main(String[] args) {
        createTableIfNotExists();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     "username TEXT PRIMARY KEY," +
                     "password TEXT NOT NULL)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static boolean authenticate(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return password.equals(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    static void broadcast(String message, String sender) {
        for (ClientHandler client : clients.values()) {
            if (!client.getUsername().equals(sender)) {
                client.sendMessage(message);
            }
        }
    }

    static void whisper(String message, String recipient, String sender) {
        ClientHandler client = clients.get(recipient);
        if (client != null) {
            client.sendMessage("[Whisper from " + sender + "]: " + message);
        }
    }

    static void addClient(String username, ClientHandler client) {
        clients.put(username, client);
    }

    static void removeClient(String username) {
        clients.remove(username);
    }

    static boolean isUsernameTaken(String username) {
        return clients.containsKey(username);
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("Enter username:");
            username = in.readLine();
            out.println("Enter password:");
            String password = in.readLine();

            if (!Server.authenticate(username, password)) {
                out.println("Authentication failed. Closing connection.");
                socket.close();
                return;
            }

            if (Server.isUsernameTaken(username)) {
                out.println("Username already taken. Closing connection.");
                socket.close();
                return;
            }

            Server.addClient(username, this);
            out.println("Welcome to the chat, " + username + "!");

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.startsWith("/broadcast")) {
                    String message = clientMessage.substring(11);
                    Server.broadcast("[Broadcast from " + username + "]: " + message, username);
                } else if (clientMessage.startsWith("/whisper")) {
                    String[] parts = clientMessage.split(" ", 3);
                    if (parts.length == 3) {
                        String recipient = parts[1];
                        String message = parts[2];
                        Server.whisper(message, recipient, username);
                    } else {
                        out.println("Invalid whisper command format.");
                    }
                } else {
                    out.println("Unknown command.");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            Server.removeClient(username);
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
```

### 3. Klient (Client.java)

```java
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // Odbieranie wiadomości od serwera w osobnym wątku
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String serverMessage;
                        while ((serverMessage = in.readLine()) != null) {
                            System.out.println(serverMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // Wysyłanie wiadomości do serwera
            while (scanner.hasNextLine()) {
                String clientMessage = scanner.nextLine();
                out.println(clientMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### Jak uruchomić:

1. **Baza danych SQLite:**
   - Utwórz plik bazy danych `chat.db` i uruchom skrypt `create_users_table.sql`, aby utworzyć tabelę użytkowników i dodać przykładowe dane.

2. **Serwer:**
   - Skompiluj i uruchom `Server.java`. Serwer będzie nasłuchiwał na porcie 12345.

3. **Klient:**
   - Skompiluj i uruchom `Client.java`. Po uruchomieniu klienta, wprowadź nazwę użytkownika i hasło.

### Komendy:
- **`/broadcast <message>`**: Wysyła wiadomość do wszystkich użytkowników.
- **`/whisper <username> <message>`**: Wysyła prywatną wiadomość do konkretnego użytkownika.

### Wyjaśnienie:

- **Serwer:**
  - Przechowuje aktualnie zalogowanych użytkowników w `ConcurrentHashMap`.
  - Obsługuje logowanie użytkowników, sprawdzając dane w bazie danych SQLite.
  - Umożliwia wysyłanie wiadomości publicznych i prywatnych.

- **Klient:**
  - Łączy się z serwerem, wysyła dane logowania i odbiera wiadomości od serwera w osobnym wątku.
  - Wysyła komendy do serwera na podstawie wpisów użytkownika.

Ta aplikacja zapewnia podstawową funkcjonalność czatu z logowaniem, autoryzacją i obsługą prostych komend. Można ją rozszerzyć o dodatkowe funkcje, takie jak rejestracja nowych użytkowników, lepsze zarządzanie sesjami czy bardziej zaawansowane komendy.


Aby uruchomić plik SQL na istniejącej bazie danych SQLite za pomocą JDBC, możesz użyć Java do odczytu pliku SQL i wykonania zawartych w nim poleceń. Oto przykład, jak to zrobić:

### 1. Tworzenie pliku SQL

Upewnij się, że masz plik SQL, który zawiera polecenia SQL do wykonania. Przykładowy plik `script.sql`:

```sql
-- script.sql
CREATE TABLE IF NOT EXISTS users (
    username TEXT PRIMARY KEY,
    password TEXT NOT NULL
);

INSERT INTO users (username, password) VALUES ('user1', 'pass1');
INSERT INTO users (username, password) VALUES ('user2', 'pass2');
```

### 2. Kod Javy do uruchomienia pliku SQL

Oto przykład kodu Javy, który odczytuje plik SQL i wykonuje zawarte w nim polecenia na istniejącej bazie danych SQLite:

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteSQLScript {
    private static final String DB_URL = "jdbc:sqlite:chat.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            executeScript(conn, "path/to/script.sql");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeScript(Connection conn, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             Statement stmt = conn.createStatement()) {

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sql.append(line);
                // If the line ends with a semicolon, it means the end of a SQL command
                if (line.trim().endsWith(";")) {
                    stmt.execute(sql.toString());
                    sql.setLength(0);  // Clear the StringBuilder for the next command
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
```

### Wyjaśnienie:

1. **Łączenie z bazą danych:**
   - `DriverManager.getConnection(DB_URL)`: Otwiera połączenie z bazą danych SQLite znajdującą się w pliku `chat.db`.

2. **Odczyt i wykonanie pliku SQL:**
   - `BufferedReader` odczytuje plik SQL linia po linii.
   - `Statement` wykonuje każdą komendę SQL, gdy zostanie wykryty koniec komendy (oznaczony przez średnik `;`).

3. **Główna funkcja:**
   - `executeScript(conn, "path/to/script.sql")`: Wywołuje metodę do wykonania skryptu SQL, podając połączenie do bazy danych oraz ścieżkę do pliku SQL.

### Jak uruchomić:

1. Upewnij się, że plik bazy danych `chat.db` istnieje w odpowiedniej lokalizacji.
2. Utwórz plik `script.sql` z odpowiednimi komendami SQL.
3. Zaktualizuj ścieżkę do pliku SQL w metodzie `executeScript`.
4. Skompiluj i uruchom program `ExecuteSQLScript`.

```bash
javac ExecuteSQLScript.java
java ExecuteSQLScript
```

Powyższy kod odczyta i wykona wszystkie komendy zawarte w pliku SQL na wskazanej bazie danych SQLite. Możesz go dostosować do własnych potrzeb, np. dodać obsługę bardziej złożonych skryptów SQL czy logowanie błędów.
