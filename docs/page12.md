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

Każdy z powyższych przykładów ilustruje kluczowe funkcje Javy i popularnych bibliotek/frameworków, które mogą być użyteczne na sprawdzianach lub w codziennej pracy programistycznej.
