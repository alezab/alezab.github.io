Here's a snippet using the SQLite JDBC driver (org.xerial.sqlite-jdbc) to read from an existing database using an SQL `SELECT` statement and to insert values into a table.

### Maven Dependency
First, if you're using Maven, add the SQLite JDBC dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.36.0.3</version>
</dependency>
```

### Java Code Snippet
Create a scratch file in IntelliJ IDEA with the following code to connect to an SQLite database, execute a `SELECT` statement, and insert values into a table:

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Scratch {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:path/to/your/database.db";
        Connection conn = null;
        
        try {
            // Establish the connection
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                System.out.println("Connection to SQLite has been established.");
                
                // SELECT statement
                String selectSQL = "SELECT * FROM your_table";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(selectSQL);
                
                // Process the results
                while (rs.next()) {
                    // Assuming columns: id (int), name (String), value (float)
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    float value = rs.getFloat("value");
                    System.out.println("ID: " + id + ", Name: " + name + ", Value: " + value);
                }
                
                // INSERT statement
                String insertSQL = "INSERT INTO your_table(name, value) VALUES(?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertSQL);
                pstmt.setString(1, "New Name");
                pstmt.setFloat(2, 123.45f);
                pstmt.executeUpdate();
                System.out.println("A new row has been inserted.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
```

### Explanation:

1. **Establish Connection**:
   - `DriverManager.getConnection(url)` establishes a connection to the SQLite database specified by the URL. Replace `"path/to/your/database.db"` with the path to your SQLite database file.

2. **SELECT Statement**:
   - `String selectSQL = "SELECT * FROM your_table";` defines the SQL `SELECT` statement.
   - `Statement stmt = conn.createStatement();` creates a `Statement` object.
   - `ResultSet rs = stmt.executeQuery(selectSQL);` executes the `SELECT` statement and returns a `ResultSet` containing the results.
   - The results are processed in a while loop using `rs.next()`.

3. **INSERT Statement**:
   - `String insertSQL = "INSERT INTO your_table(name, value) VALUES(?, ?)";` defines the SQL `INSERT` statement with placeholders.
   - `PreparedStatement pstmt = conn.prepareStatement(insertSQL);` creates a `PreparedStatement` object.
   - `pstmt.setString(1, "New Name");` and `pstmt.setFloat(2, 123.45f);` set the values for the placeholders.
   - `pstmt.executeUpdate();` executes the `INSERT` statement.

4. **Exception Handling**:
   - `try-catch` blocks handle any `SQLException` that may occur during the database operations.
   - The connection is closed in a `finally` block to ensure it is properly released.

Replace `"your_table"` with the actual name of your table and adjust the column names and types (`id`, `name`, `value`) to match your database schema. This snippet provides a basic template for performing SQL operations with SQLite using JDBC in a Java scratch file.

Sure, here’s an example that demonstrates one thread using the `extends Thread` approach and another thread using the `implements Runnable` approach for reading from a file and writing to a file byte by byte.

### Java Code Snippet

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Scratch {
    public static void main(String[] args) {
        String inputFilePath = "path/to/your/input/file.txt";
        String outputFilePath = "path/to/your/output/file.txt";

        SharedBuffer sharedBuffer = new SharedBuffer();

        FileReaderThread readerThread = new FileReaderThread(inputFilePath, sharedBuffer);
        FileWriterRunnable writerRunnable = new FileWriterRunnable(outputFilePath, sharedBuffer);
        Thread writerThread = new Thread(writerRunnable);

        readerThread.start();
        writerThread.start();

        try {
            readerThread.join();
            writerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class SharedBuffer {
    private byte[] buffer = new byte[1];
    private boolean isReadComplete = false;
    private final Object lock = new Object();

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public boolean isReadComplete() {
        return isReadComplete;
    }

    public void setReadComplete(boolean readComplete) {
        isReadComplete = readComplete;
    }

    public Object getLock() {
        return lock;
    }
}

class FileReaderThread extends Thread {
    private final String inputFilePath;
    private final SharedBuffer sharedBuffer;

    public FileReaderThread(String inputFilePath, SharedBuffer sharedBuffer) {
        this.inputFilePath = inputFilePath;
        this.sharedBuffer = sharedBuffer;
    }

    @Override
    public void run() {
        try (FileInputStream fis = new FileInputStream(inputFilePath)) {
            int bytesRead;
            while ((bytesRead = fis.read(sharedBuffer.getBuffer())) != -1) {
                synchronized (sharedBuffer.getLock()) {
                    sharedBuffer.getLock().notify();
                    sharedBuffer.getLock().wait();
                }
            }
            synchronized (sharedBuffer.getLock()) {
                sharedBuffer.setReadComplete(true);
                sharedBuffer.getLock().notify();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class FileWriterRunnable implements Runnable {
    private final String outputFilePath;
    private final SharedBuffer sharedBuffer;

    public FileWriterRunnable(String outputFilePath, SharedBuffer sharedBuffer) {
        this.outputFilePath = outputFilePath;
        this.sharedBuffer = sharedBuffer;
    }

    @Override
    public void run() {
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            while (true) {
                synchronized (sharedBuffer.getLock()) {
                    sharedBuffer.getLock().notify();
                    if (sharedBuffer.isReadComplete() && sharedBuffer.getBuffer()[0] == 0) {
                        break;
                    }
                    fos.write(sharedBuffer.getBuffer());
                    sharedBuffer.getLock().wait();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

### Explanation

1. **SharedBuffer Class**:
   - This class contains a buffer to hold the byte being read and written, a flag `isReadComplete` to indicate when reading is complete, and a lock object for synchronization.

2. **FileReaderThread Class (extends Thread)**:
   - This thread reads bytes from the input file using a `FileInputStream`.
   - It reads each byte and notifies the writer thread, then waits for the writer to process the byte.
   - When the end of the file is reached, it sets the `isReadComplete` flag and notifies the writer thread.

3. **FileWriterRunnable Class (implements Runnable)**:
   - This runnable writes bytes to the output file using a `FileOutputStream`.
   - It waits for the reader thread to provide each byte, writes it to the file, and then notifies the reader thread.
   - The loop exits when reading is complete and the buffer is empty.

4. **Main Method**:
   - Creates instances of `SharedBuffer`, `FileReaderThread`, and `FileWriterRunnable`.
   - Starts the reader and writer threads and waits for them to finish using `join()`.

5. **Thread Synchronization**:
   - The reader and writer threads synchronize on the `lock` object in `SharedBuffer` to coordinate reading and writing.

### Usage
- Replace `"path/to/your/input/file.txt"` and `"path/to/your/output/file.txt"` with the actual file paths.
- This example demonstrates how to use both `extends Thread` and `implements Runnable` to perform synchronized file reading and writing in Java.

- 
Implementacja algorytmu wykrywania brzegów w Javie może być zrealizowana na kilka sposobów, jednym z popularniejszych jest wykorzystanie filtra Sobela, który jest podstawą do bardziej złożonych metod wykrywania krawędzi. Poniżej znajduje się przykład kodu w Javie implementującego algorytm Sobela do wykrywania krawędzi w obrazie.

```java
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class SobelEdgeDetection {

    public static void main(String[] args) throws Exception {
        BufferedImage image = ImageIO.read(new File("path/to/your/image.jpg"));
        BufferedImage result = applySobel(image);
        ImageIO.write(result, "jpg", new File("path/to/save/edge_detected_image.jpg"));
    }

    public static BufferedImage applySobel(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        BufferedImage gray = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
                int grayLevel = (r + g + b) / 3;
                int grayRgb = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                gray.setRGB(x, y, grayRgb);
            }
        }

        int[][] sobelX = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
        };

        int[][] sobelY = {
            {-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1}
        };

        BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int gx = 0;
                int gy = 0;
                
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int grayLevel = (gray.getRGB(x + j, y + i) & 0xff);
                        gx += sobelX[i + 1][j + 1] * grayLevel;
                        gy += sobelY[i + 1][j + 1] * grayLevel;
                    }
                }
                
                int g = (int) Math.sqrt(gx * gx + gy * gy);
                g = g > 255 ? 255 : g;
                int edgeRgb = (g << 16) + (g << 8) + g;
                edgeImage.setRGB(x, y, edgeRgb);
            }
        }
        
        return edgeImage;
    }
}
```

Powyższy kod wykonuje następujące kroki:
1. Wczytuje obraz z pliku.
2. Konwertuje obraz na skalę szarości.
3. Stosuje filtr Sobela w kierunkach X i Y.
4. Kombinuje wyniki z filtrów X i Y, aby uzyskać ostateczny wynik krawędziowy.
5. Zapisuje wynikowy obraz do pliku.

Pamiętaj, aby podmienić `"path/to/your/image.jpg"` i `"path/to/save/edge_detected_image.jpg"` na odpowiednie ścieżki do pliku wejściowego i wyjściowego.

Box blur to jeden z najprostszych filtrów rozmywających, który średnia wartości pikseli w określonym sąsiedztwie każdego piksela. Implementacja takiego filtra w Javie może wyglądać następująco:

```java
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class BoxBlur {

    public static void main(String[] args) throws Exception {
        BufferedImage image = ImageIO.read(new File("path/to/your/image.jpg"));
        int radius = 3; // Adjust the radius as needed
        BufferedImage result = applyBoxBlur(image, radius);
        ImageIO.write(result, "jpg", new File("path/to/save/blurred_image.jpg"));
    }

    public static BufferedImage applyBoxBlur(BufferedImage image, int radius) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage blurred = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        int[] newPixels = new int[width * height];

        int kernelSize = (2 * radius + 1) * (2 * radius + 1);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rSum = 0, gSum = 0, bSum = 0;
                int count = 0;

                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int newX = x + dx;
                        int newY = y + dy;

                        if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                            int rgb = pixels[newX + newY * width];
                            int r = (rgb >> 16) & 0xff;
                            int g = (rgb >> 8) & 0xff;
                            int b = rgb & 0xff;

                            rSum += r;
                            gSum += g;
                            bSum += b;
                            count++;
                        }
                    }
                }

                int newR = rSum / count;
                int newG = gSum / count;
                int newB = bSum / count;

                newPixels[x + y * width] = (newR << 16) | (newG << 8) | newB;
            }
        }

        blurred.setRGB(0, 0, width, height, newPixels, 0, width);
        return blurred;
    }
}
```

Powyższy kod wykonuje następujące kroki:
1. Wczytuje obraz z pliku.
2. Inicjalizuje nowy obraz do przechowywania rozmytego wyniku.
3. Iteruje przez każdy piksel obrazu wejściowego.
4. Dla każdego piksela oblicza średnią wartość kolorów (R, G, B) w zadanym promieniu (`radius`).
5. Przypisuje obliczoną średnią wartość do odpowiedniego piksela w nowym obrazie.
6. Zapisuje wynikowy obraz do pliku.

Pamiętaj, aby dostosować `"path/to/your/image.jpg"` i `"path/to/save/blurred_image.jpg"` do odpowiednich ścieżek do pliku wejściowego i wyjściowego oraz dostosować wartość `radius` według potrzeb.


Aby zaimplementować klienta i serwer w Javie, które wysyłają obrazek z tekstem "wysłano" od klienta do serwera, a serwer odbiera ten obrazek i zapisuje go na dysku, możemy skorzystać z biblioteki `Socket`. Poniżej znajduje się przykładowa implementacja.

Najpierw zaimplementujemy serwer, który będzie nasłuchiwał na określonym porcie i odbierał obrazki.

### Server.java

```java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 12345; // Wybierz odpowiedni port
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                InputStream inputStream = socket.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream("received_image.jpg");

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                fileOutputStream.close();
                socket.close();
                System.out.println("Image received and saved as 'received_image.jpg'");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
```

### Client.java

```java
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import javax.imageio.ImageIO;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Adres serwera
        int port = 12345; // Port serwera

        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Connected to the server");

            // Tworzenie obrazka z tekstem "wysłano"
            BufferedImage image = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            g.setColor(Color.BLACK);
            g.drawString("wysłano", 50, 50);
            g.dispose();

            // Zapisywanie obrazka do pliku (opcjonalne)
            ImageIO.write(image, "jpg", new File("sent_image.jpg"));

            // Wysyłanie obrazka do serwera
            OutputStream outputStream = socket.getOutputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            byteArrayOutputStream.writeTo(outputStream);

            socket.close();
            System.out.println("Image sent to the server");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
```

### Instrukcje:

1. **Uruchom serwer:**
   - Skompiluj `Server.java` i uruchom go.
   - Serwer będzie nasłuchiwał na porcie 12345.

2. **Uruchom klienta:**
   - Skompiluj `Client.java` i uruchom go.
   - Klient połączy się z serwerem, utworzy obrazek z tekstem "wysłano" i wyśle go do serwera.

3. **Serwer odbierze obrazek:**
   - Po otrzymaniu obrazka, serwer zapisze go jako `received_image.jpg`.

Powyższy kod demonstruje podstawową komunikację między klientem a serwerem, gdzie klient wysyła obrazek, a serwer go odbiera i zapisuje. Możesz dostosować port i adres serwera według własnych potrzeb.

Oczywiście, możemy zrealizować przesyłanie obrazka linia po linii zamiast bajt po bajcie. Aby to zrobić, musimy przekonwertować obraz na odpowiedni format (np. zakodowany jako base64) i przesyłać te dane linia po linii. Poniżej znajdują się zmodyfikowane wersje `Server.java` i `Client.java`.

### Server.java

```java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class Server {
    public static void main(String[] args) {
        int port = 12345; // Wybierz odpowiedni port
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                StringBuilder imageData = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    imageData.append(line);
                }

                byte[] imageBytes = Base64.getDecoder().decode(imageData.toString());
                FileOutputStream fos = new FileOutputStream("received_image.jpg");
                fos.write(imageBytes);
                fos.close();

                socket.close();
                System.out.println("Image received and saved as 'received_image.jpg'");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
```

### Client.java

```java
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Base64;
import javax.imageio.ImageIO;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Adres serwera
        int port = 12345; // Port serwera

        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Connected to the server");

            // Tworzenie obrazka z tekstem "wysłano"
            BufferedImage image = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            g.setColor(Color.BLACK);
            g.drawString("wysłano", 50, 50);
            g.dispose();

            // Zapisywanie obrazka do pliku (opcjonalne)
            ImageIO.write(image, "jpg", new File("sent_image.jpg"));

            // Konwertowanie obrazka na Base64
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            String encodedImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

            // Wysyłanie obrazka linia po linii do serwera
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(encodedImage);

            socket.close();
            System.out.println("Image sent to the server");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
```

### Instrukcje:

1. **Uruchom serwer:**
   - Skompiluj `Server.java` i uruchom go.
   - Serwer będzie nasłuchiwał na porcie 12345.

2. **Uruchom klienta:**
   - Skompiluj `Client.java` i uruchom go.
   - Klient połączy się z serwerem, utworzy obrazek z tekstem "wysłano", zakoduje go w base64 i wyśle do serwera linia po linii.

3. **Serwer odbierze obrazek:**
   - Po otrzymaniu obrazka, serwer zdekoduje dane base64 i zapisze obrazek jako `received_image.jpg`.

Ta wersja kodu przesyła obrazek jako ciąg znaków zakodowany w base64 linia po linii, co może być bardziej czytelne dla niektórych zastosowań, chociaż w praktyce zazwyczaj nie jest to optymalne rozwiązanie dla dużych plików.

