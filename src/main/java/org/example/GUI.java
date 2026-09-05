package org.example;
import javax.swing.*;

public class GUI extends JFrame {

    private final GenrePanel genrePanel;
    private final SubGenrePanel subGenrePanel;
    private final BookPanel bookPanel;

    public GUI(LibraryManager manager) {

        setTitle("Library Management");
        setSize(1800, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Créer tous les panels indépendamment
        genrePanel = new GenrePanel(manager);
        subGenrePanel = new SubGenrePanel(manager);
        bookPanel = new BookPanel(manager);

        // Injecter les références pour que chaque panel connaisse les autres
        genrePanel.setSubGenrePanel(subGenrePanel);
        genrePanel.setBookPanel(bookPanel);

        subGenrePanel.setGenrePanel(genrePanel);
        subGenrePanel.setBookPanel(bookPanel);

        bookPanel.setGenrePanel(genrePanel);
        bookPanel.setSubGenrePanel(subGenrePanel);

        // Ajouter les panels dans les onglets
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Genres", genrePanel);
        tabbedPane.addTab("SubGenres", subGenrePanel);
        tabbedPane.addTab("Books", bookPanel);

        add(tabbedPane);
        setVisible(true);
    }
}
