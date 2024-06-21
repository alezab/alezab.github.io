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
