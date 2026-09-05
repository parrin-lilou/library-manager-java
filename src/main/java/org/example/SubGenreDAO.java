package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubGenreDAO {
    private final Connection conn;

    public SubGenreDAO(Connection conn) {
        this.conn = conn;
    }

    public List<SubGenre> getAllSubGenres() {
        List<SubGenre> subGenres = new ArrayList<>();
        String sql = "SELECT * FROM sub_genre";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                subGenres.add(new SubGenre(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("main_genre"),
                        rs.getInt("main_genre_id"),
                        rs.getInt("number_of_books"),
                        rs.getString("url")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des subgenres : " + e.getMessage());
        }
        return subGenres;
    }

    public boolean addSubGenre(SubGenre subGenre) {
        String sql = "INSERT INTO sub_genre (title, main_genre, main_genre_id, number_of_books, url) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subGenre.getTitle());
            ps.setString(2, subGenre.getMainGenre());
            ps.setInt(3, subGenre.getMainGenreId());
            ps.setInt(4, subGenre.getNumberOfBooks());
            ps.setString(5, subGenre.getUrl());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du subgenre : " + e.getMessage());
            return false;
        }
    }




    public boolean updateSubGenre(SubGenre subGenre) {
        String sql = "UPDATE sub_genre SET title = ?, main_genre = ?, main_genre_id = ?, number_of_books = ?, url = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subGenre.getTitle());
            ps.setString(2, subGenre.getMainGenre());
            ps.setInt(3, subGenre.getMainGenreId());
            ps.setInt(4, subGenre.getNumberOfBooks());
            ps.setString(5, subGenre.getUrl());
            ps.setInt(6, subGenre.getId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du subgenre : " + e.getMessage());
            return false;
        }
    }


    public boolean deleteSubGenreById(int id) {
        String sql = "DELETE FROM sub_genre WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);  // suppression via l'ID
            int rows = ps.executeUpdate();
            return rows > 0;    // true si au moins une ligne supprimée
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du genre : " + e.getMessage());
            return false;
        }
    }


    public List<SubGenre> searchSubGenres(String keyword) {
        List<SubGenre> subGenres = new ArrayList<>();
        String sql = "SELECT * FROM sub_genre WHERE title LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                subGenres.add(new SubGenre(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("main_genre"),
                        rs.getInt("main_genre_id"),
                        rs.getInt("number_of_books"),
                        rs.getString("url")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des subgenres : " + e.getMessage());
        }
        return subGenres;
    }

    public boolean isTableSubGenreEmpty(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
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



    public void incrementNrOfBooksCount(int subGenreId) {
        String sql = "UPDATE sub_genre SET number_of_books = number_of_books + 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subGenreId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'incrémentation du nombre de books : " + e.getMessage());
        }
    }

    public void decrementNrOfBooksCount(int subGenreId) {
        String sql = "UPDATE sub_genre SET number_of_books = number_of_books - 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subGenreId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'decrémentation du nombre de books : " + e.getMessage());
        }
    }


    public List<SubGenre> getSubGenresByMainGenre(Genre mainGenre) {
        List<SubGenre> subGenres = new ArrayList<>();
        String sql = "SELECT * FROM sub_genre WHERE main_genre_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mainGenre.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                subGenres.add(new SubGenre(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("main_genre"),
                        rs.getInt("main_genre_id"),
                        rs.getInt("number_of_books"),
                        rs.getString("url")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des sous-genres : " + e.getMessage());
        }
        return subGenres;
    }

    public List<SubGenre> getSubGenresByMainGenreId(int mainGenreId) {
        List<SubGenre> result = new ArrayList<>();
        String sql = "SELECT * FROM sub_genre WHERE main_genre_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mainGenreId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new SubGenre(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("main_genre"),
                            rs.getInt("main_genre_id"),
                            rs.getInt("number_of_books"),
                            rs.getString("url")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateMainGenreTitleInSubGenre(String oldTitle, String newTitle) {
        String sql = "UPDATE sub_genre SET main_genre = ? WHERE main_genre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newTitle);
            ps.setString(2, oldTitle);
            int rows = ps.executeUpdate();
            System.out.println(rows + " sub genre mis à jour avec le nouveau titre : " + newTitle);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du genre principal dans sub genre : " + e.getMessage());
        }
    }

    public boolean deleteAllSubgenresOfGenre(int genreId) {
        String sql = "DELETE FROM sub_genre WHERE main_genre_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, genreId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des sous-genres : " + e.getMessage());
            return false;
        }
    }

}








