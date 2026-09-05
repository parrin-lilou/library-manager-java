package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO {
    private final Connection conn;

    public GenreDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM genre";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                genres.add(new Genre(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("number_of_subgenres"),
                        rs.getString("url")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des genres : " + e.getMessage());
        }
        return genres;
    }

    public boolean addGenre(Genre genre) {
        String sql = "INSERT INTO genre (title, number_of_subgenres, url) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genre.getTitle());
            ps.setInt(2, genre.getNumberOfSubGenres());
            ps.setString(3, genre.getUrl());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du genre : " + e.getMessage());
            return false;
        }
    }


    public boolean updateGenre(Genre genre) {
        String sql = "UPDATE genre SET title = ?, number_of_subgenres = ?, url = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genre.getTitle());
            ps.setInt(2, genre.getNumberOfSubGenres());
            ps.setString(3, genre.getUrl());
            ps.setInt(4, genre.getId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du genre : " + e.getMessage());
            return false;
        }
    }


    public boolean deleteGenreById(int id) {
        String sql = "DELETE FROM genre WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;    // true si au moins une ligne supprimée
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du genre : " + e.getMessage());
            return false;
        }
    }


    public List<Genre> searchGenres(String keyword) {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM genre WHERE title LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                genres.add(new Genre(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("number_of_subgenres"),
                        rs.getString("url")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de genres : " + e.getMessage());
        }
        return genres;
    }


    public void incrementSubGenreCount(int genreId) {
        String sql = "UPDATE genre SET number_of_subgenres = number_of_subgenres + 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, genreId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'incrémentation du nombre de subgenres : " + e.getMessage());
        }
    }


    public void decrementSubGenreCount(int genreId) {
        String sql = "UPDATE genre SET number_of_subgenres = number_of_subgenres - 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, genreId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'incrémentation du nombre de subgenres : " + e.getMessage());
        }
    }


    public boolean isTableGenreEmpty(String tableName) {
        String sql = "SELECT COUNT(*) FROM "+tableName;
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // en cas d'erreur, on considère que ce n'est pas vide
    }

}
