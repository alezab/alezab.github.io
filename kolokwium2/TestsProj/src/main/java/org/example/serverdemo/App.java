package org.example.serverdemo;

import java.sql.*;

public class App {
    public static void main(String[] args) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
                Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("DROP TABLE IF EXISTS person");
            statement.executeUpdate("CREATE TABLE person (id INTEGER, name TEXT)");
            statement.executeUpdate("INSERT INTO person VALUES(1, 'leo')");
            statement.executeUpdate("INSERT INTO person VALUES(2, 'yui')");
            ResultSet rs = statement.executeQuery("SELECT * FROM person");
            while (rs.next()) {
                System.out.print("name = " + rs.getString("name") + ", ");
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }
}
