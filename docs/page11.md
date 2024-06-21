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
