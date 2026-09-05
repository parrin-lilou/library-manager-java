package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private final Connection conn;


    public BookDAO(Connection conn) {
        this.conn = conn;
    }


    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("main_genre"),
                        rs.getInt("main_genre_id"),
                        rs.getString("sub_genre"),
                        rs.getInt("sub_genre_id"),
                        rs.getString("type"),
                        rs.getString("price_display"),
                        rs.getDouble("rating"),
                        rs.getInt("number_of_people_rated"),
                        rs.getString("url")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des livres : " + e.getMessage());
        }
        return books;
    }


    public boolean addBook(Book book) {
        String sql = "INSERT INTO book (title, author, main_genre, main_genre_id, sub_genre, sub_genre_id, type, price_display, price, rating, number_of_people_rated, url) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getMainGenre());
            ps.setInt(4, book.getMainGenreId());
            ps.setString(5, book.getSubGenre());
            ps.setInt(6, book.getSubGenreId());
            ps.setString(7, book.getType());
            ps.setString(8, book.getPriceDisplay());
            ps.setDouble(9, book.getPrice());
            ps.setDouble(10, book.getRating());
            ps.setInt(11, book.getNumberOfPeopleRated());
            ps.setString(12, book.getUrl());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du livre : " + e.getMessage());
            return false;
        }
    }




    public boolean updateBook(Book book) {
        String sql = "UPDATE book SET title=?, author=?, main_genre=?, main_genre_id=?, sub_genre=?, sub_genre_id=?, type=?,price_display=?, price=?, rating=?, number_of_people_rated=?, url=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getMainGenre());
            ps.setInt(4, book.getMainGenreId());
            ps.setString(5, book.getSubGenre());
            ps.setInt(6, book.getSubGenreId());
            ps.setString(7, book.getType());
            ps.setString(8, book.getPriceDisplay());
            ps.setDouble(9, book.getPrice());
            ps.setDouble(10, book.getRating());
            ps.setInt(11, book.getNumberOfPeopleRated());
            ps.setString(12, book.getUrl());
            ps.setInt(13, book.getId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du livre : " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBookById(int id) {
        String sql = "DELETE FROM book WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);  // suppression via l'ID
            int rows = ps.executeUpdate();
            return rows > 0;    // true si au moins une ligne supprimée
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du book : " + e.getMessage());
            return false;
        }
    }

    public List<Book> searchBooksByTitle(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE title LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("main_genre"),
                        rs.getInt("main_genre_id"),
                        rs.getString("sub_genre"),
                        rs.getInt("sub_genre_id"),
                        rs.getString("type"),
                        rs.getString("price_display"),
                        rs.getDouble("rating"),
                        rs.getInt("number_of_people_rated"),
                        rs.getString("url")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des livres par titre : " + e.getMessage());
        }
        return books;
    }


    public List<Book> searchBooksByAuthor(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE author LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("main_genre"),
                        rs.getInt("main_genre_id"),
                        rs.getString("sub_genre"),
                        rs.getInt("sub_genre_id"),
                        rs.getString("type"),
                        rs.getString("price_display"),
                        rs.getDouble("rating"),
                        rs.getInt("number_of_people_rated"),
                        rs.getString("url")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des livres par auteur : " + e.getMessage());
        }
        return books;
    }


    public boolean isTableBookEmpty(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public void updateMainGenreTitleInBooks(String oldTitle, String newTitle) {
        String sql = "UPDATE book SET main_genre = ? WHERE main_genre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newTitle);
            ps.setString(2, oldTitle);
            int rows = ps.executeUpdate();
            System.out.println(rows + " livres mis à jour avec le nouveau genre : " + newTitle);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du genre principal dans les livres : " + e.getMessage());
        }
    }

    public void updateSubGenresTitleInBooks(String oldTitle, String newTitle) {
        String sql = "UPDATE book SET sub_genre = ? WHERE sub_genre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newTitle);
            ps.setString(2, oldTitle);
            int rows = ps.executeUpdate();
            System.out.println(rows + " livres mis à jour avec le nouveau sous-genre : " + newTitle);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du sous-genre dans les livres : " + e.getMessage());
        }
    }

    public List<Book> getBooksByMainGenre(Genre mainGenre) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE main_genre = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Passer le nom du genre
            ps.setString(1,mainGenre.getTitle().trim());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("main_genre"),
                        rs.getInt("main_genre_id"),
                        rs.getString("sub_genre"),
                        rs.getInt("sub_genre_id"),
                        rs.getString("type"),
                        rs.getString("price_display"),
                        rs.getDouble("rating"),
                        rs.getInt("number_of_people_rated"),
                        rs.getString("url")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des livres par genre : " + e.getMessage());
        }
        return books;
    }

    public List<Book> getBooksByMainGenreAndSubGenre(Genre mainGenre, SubGenre subGenre) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE main_genre_id = ? AND sub_genre_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mainGenre.getId());
            ps.setInt(2, subGenre.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("main_genre"),
                        rs.getInt("main_genre_id"),
                        rs.getString("sub_genre"),
                        rs.getInt("sub_genre_id"),
                        rs.getString("type"),
                        rs.getString("price_display"),
                        rs.getDouble("rating"),
                        rs.getInt("number_of_people_rated"),
                        rs.getString("url")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des livres par genre et sous-genre : " + e.getMessage());
        }
        return books;
    }


    public double getAverageRatingForGenre(int genreId) {
        double average = 0.0;
        String sql = "SELECT AVG(rating) FROM book WHERE main_genre_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, genreId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                average = rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du calcul de la note moyenne : " + e.getMessage());
        }
        return average;
    }

    public double getAveragePriceForGenre(int genreId) {
        double average = 0.0;
        String sql = "SELECT AVG(price) FROM book WHERE main_genre_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, genreId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                average = rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du calcul du prix : " + e.getMessage());
        }
        return average;
    }

    public int getTotalNumberOfBooksForGenre(int genreId) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM book WHERE main_genre_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, genreId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du calcul du nombre total de livres : " + e.getMessage());
        }
        return total;
    }

    public double getAverageRatingForSubGenre(int subGenreId) {
        double average = 0.0;
        String sql = "SELECT AVG(rating) FROM book WHERE sub_genre_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subGenreId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                average = rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du calcul de la note moyenne : " + e.getMessage());
        }
        return average;
    }

    public double getAveragePriceForSubGenre(int subGenreId) {
        double average = 0.0;
        String sql = "SELECT AVG(price) FROM book WHERE sub_genre_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subGenreId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                average = rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du calcul du prix  moyen : " + e.getMessage());
        }
        return average;
    }

    public int getTotalNumberOfBooksForSubGenre(int subGenreId) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM book WHERE sub_genre_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subGenreId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du calcul du nombre total de livres : " + e.getMessage());
        }
        return total;
    }

    public boolean deleteAllBooksOfGenre(int genreId) {
        String sql = "DELETE FROM book WHERE main_genre_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, genreId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des books : " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAllBooksOfSubGenre(int subGenreId) {
        String sql = "DELETE FROM book WHERE sub_genre_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subGenreId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des books du subgenre : " + e.getMessage());
            return false;
        }
    }

}













