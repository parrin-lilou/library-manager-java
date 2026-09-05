package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        // Connexion à la base
        String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/library");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "");

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connecté à la base de données");
            DatabaseInitializer.createTables(conn);


            // Création des DAO
            BookDAO bookDAO = new BookDAO(conn);
            GenreDAO genreDAO = new GenreDAO(conn);
            SubGenreDAO subGenreDAO = new SubGenreDAO(conn);


            // Création du LibraryManager
            LibraryManager libraryManager = new LibraryManager(bookDAO, genreDAO, subGenreDAO);

            if (libraryManager.isTableGenreEmpty("genre")) {
                CsvImporter.importGenres(conn, "/Users/lilou/ProjetJava/Genre_df.csv");
            }

            if (libraryManager.isTableSubGenreEmpty("sub_genre")) {
                CsvImporter.importSubGenres(conn, "/Users/lilou/ProjetJava/Sub_Genre_df.csv");
            }

            if (libraryManager.isTableBookEmpty("book")) {
                CsvImporter.importBooks(conn, "/Users/lilou/ProjetJava/Books_df.csv");
            }

            CsvImporter.updateForeignKeys(conn);
            CsvImporter.updateNumberOfBooks(conn);


            // Lancer la GUI
            SwingUtilities.invokeLater(() -> {
                GUI gui = new GUI(libraryManager);
                gui.setVisible(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


