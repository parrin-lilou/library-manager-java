package org.example;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.sql.*;

public class CsvImporter {

    // === GENRES ===============================================================
    public static void importGenres(Connection connection, String csvPath) throws Exception {
        // Requête SQL d'insertion
        String sql = "INSERT INTO genre (title, number_of_subgenres, url) VALUES (?,?,?) ";

        // Ouverture du fichier CSV et préparation de la requête
        try (CSVReader reader = new CSVReader(new FileReader(csvPath));
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Ignorer l’en-tête (1re ligne du CSV)
            reader.readNext();

            String[] row;
            int count = 0;

            while ((row = reader.readNext()) != null) {
                ps.setString(1, row[0]);
                ps.setInt(2, Integer.parseInt(row[1]));
                ps.setString(3, row[2]);
                ps.addBatch(); // empile la requête
                count++;
            }

            ps.executeBatch(); // envoie tout d’un coup à MySQL
            System.out.println("✅ Import terminé ! Genres insérés : " + count);

        }
    }
    // === SUBGENRES ===============================================================
    public static void importSubGenres(Connection connection, String csvPath) throws Exception {
        // Requête SQL d'insertion
        String sql = "INSERT INTO sub_genre (title, main_genre, number_of_books,url) VALUES (?,?,?,?) ";

        // Ouverture du fichier CSV et préparation de la requête
        try (CSVReader reader = new CSVReader(new FileReader(csvPath));
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Ignorer l’en-tête (1re ligne du CSV)
            reader.readNext();

            String[] row;
            int count = 0;

            while ((row = reader.readNext()) != null) {

                ps.setString(1, row[0]);
                ps.setString(2, row[1]);
                ps.setInt(3, (int) Double.parseDouble(row[2]));
                ps.setString(4,row[3]);

                ps.addBatch(); // empile la requête
                count++;
            }

            ps.executeBatch(); // envoie tout d’un coup à MySQL
            System.out.println("✅ Import terminé ! SubGenres insérés : " + count);
        }
    }

    // === BOOKS ===============================================================
    public static void importBooks(Connection connection, String csvPath) throws Exception {
        // Requête SQL d'insertion
        String sql = "INSERT INTO book (title, author,main_genre,sub_genre, type,price_display,price, rating, number_of_people_rated,url) VALUES (?,?,?,?,?,?,?,?,?,?) ";

        // Ouverture du fichier CSV et préparation de la requête
        try (CSVReader reader = new CSVReader(new FileReader(csvPath));
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Ignorer l’en-tête (1re ligne du CSV)
            reader.readNext();
            String[] row;
            int count = 0;

            while ((row = reader.readNext()) != null) {

                ps.setString(1, row[1]);
                ps.setString(2, row[2]);
                ps.setString(3, row[3]);
                ps.setString(4, row[4]);
                ps.setString(5, row[5]);
                ps.setString(6, row[6]);
                ps.setDouble(7, Book.modifPriceDisplayInDouble(row[6]));
                ps.setDouble(8, Double.parseDouble(row[7]));
                ps.setInt(9, (int) Double.parseDouble(row[8]));
                ps.setString(10, row[9]);


                ps.addBatch(); // empile la requête
                count++;
            }

            ps.executeBatch(); // envoie tout d’un coup à MySQL
            System.out.println("✅ Import terminé ! Books insérés : " + count);
        }
    }


    public static void updateForeignKeys(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        System.out.println("⏳ Création des index (si inexistants)...");

        // Créer des index pour accélérer les jointures
        createIndex(statement, "CREATE INDEX idx_genre_title ON genre(title)");
        createIndex(statement, "CREATE INDEX idx_sub_genre_title ON sub_genre(title)");
        createIndex(statement, "CREATE INDEX idx_sub_genre_main ON sub_genre(main_genre)");
        createIndex(statement, "CREATE INDEX idx_book_main_genre ON book(main_genre)");
        createIndex(statement, "CREATE INDEX idx_book_sub_genre ON book(sub_genre)");

        System.out.println("✅ Index créés ou déjà existants.");

        // 1️⃣ Update sub_genre
        String sqlSubGenre = "UPDATE sub_genre " +
                "JOIN genre ON sub_genre.main_genre = genre.title " +
                "SET sub_genre.main_genre_id = genre.id";
        int rowsSubGenre = statement.executeUpdate(sqlSubGenre);
        System.out.println("✅ Sub_genre main_genre_id mis à jour : " + rowsSubGenre + " lignes.");

        // 2️⃣ Update book
        String sqlBook = "UPDATE book " +
                "JOIN genre ON book.main_genre = genre.title " +
                "JOIN sub_genre ON book.sub_genre = sub_genre.title AND book.main_genre = sub_genre.main_genre " +
                "SET book.main_genre_id = genre.id, book.sub_genre_id = sub_genre.id";


        int rowsBook = statement.executeUpdate(sqlBook);
        System.out.println("✅ Book main_genre_id et sub_genre_id mis à jour : " + rowsBook + " lignes.");


        statement.close();
    }

    //Fonction pour créer un index sans planter si il existe déjà
    private static void createIndex(Statement stmt, String sql) {
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            // Code erreur MySQL 1061 = index déjà existant
            if (!e.getMessage().contains("1061")) {
                e.printStackTrace();
            }
        }


    }

    public static void updateNumberOfBooks(Connection connection) throws SQLException {
        System.out.println("⏳ Mise à jour automatique du nombre de livres par sous-genre...");

        String sql = "UPDATE sub_genre sg SET sg.number_of_books = (SELECT COUNT(*) FROM book b WHERE b.sub_genre_id = sg.id)";

        try (Statement stmt = connection.createStatement()) {
            int rows = stmt.executeUpdate(sql);
            System.out.println("✅ Nombre de sous-genres mis à jour : " + rows);
        }
    }



}
