package org.example;
import java.sql.*;

public class DatabaseInitializer {

    public static void createTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        // ---------------------------------------------------------
        // 1️⃣ INSERT INTO – Add new records
        // ---------------------------------------------------------
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS genre (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  title VARCHAR(200)," +
                "  number_of_subgenres INT," +
                "  url VARCHAR(255)" +
                ")"
        );

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS sub_genre (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  title VARCHAR(200)," +
                "  main_genre VARCHAR(200)," +
                "  main_genre_id INT," +
                "  number_of_books INT," +
                "  url VARCHAR(255)" +
                ")"
        );

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS book (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  title VARCHAR(500)," +
                "  author VARCHAR(150)," +
                "  main_genre VARCHAR(150)," +
                "  main_genre_id INT," +
                "  sub_genre VARCHAR(150)," +
                "  sub_genre_id INT," +
                "  type VARCHAR(50)," +
                "  price_display VARCHAR(50)," +
                "  price DECIMAL(65,2)," +
                "  rating DECIMAL(3,2)," +
                "  number_of_people_rated INT," +
                "  url VARCHAR(500)" +
                ")"
        );
        statement.close();
    }
}


