package org.example;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GenrePanel extends JPanel{
    private final LibraryManager manager;
    private final JTable genreTable;
    private List<Genre> currentGenres;
    private SubGenrePanel subGenrePanel;
    private BookPanel bookPanel;
    private final JTextField txtSearchGenre;

    public void setSubGenrePanel(SubGenrePanel subGenrePanel) {
        this.subGenrePanel = subGenrePanel;
    }

    public void setBookPanel(BookPanel bookPanel) {
        this.bookPanel = bookPanel;
    }

    public GenrePanel(LibraryManager manager) {
        this.manager = manager;

        // Définir le layout du panel
        setLayout(new BorderLayout());

        // Champ de recherche
        txtSearchGenre = new JTextField();
        txtSearchGenre.setMaximumSize(new Dimension(200, 25));
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel SearchLabel = new JLabel("Search: ");
        searchPanel.add(SearchLabel, BorderLayout.WEST);
        searchPanel.add(txtSearchGenre, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);// Ajouter en haut

        //Tableau
        genreTable = new JTable();
        add(new JScrollPane(genreTable), BorderLayout.CENTER);

        //Panel des boutons
        JPanel buttonPanelGenre = new JPanel();
        buttonPanelGenre.setLayout(new BoxLayout(buttonPanelGenre, BoxLayout.Y_AXIS));

        JButton buttonLoadGenres = new JButton("Load Genres");
        JButton buttonAddGenre = new JButton("Add Genre");
        JButton buttonUpdateGenre = new JButton("Update Genre");
        JButton buttonDeleteGenre = new JButton("Delete Genre");
        JButton buttonRechercheGenre = new JButton("Search");
        JButton buttonAverageRating = new JButton("Average Rating");
        JButton buttonAveragePrice = new JButton("Average Price");
        JButton buttonTotalNumberOfBooks = new JButton("Total Number Of Books");
        JButton buttonReadGenre = new JButton("Read Genre");
        JButton buttonExportRapport = new JButton("Export Rapport");


        buttonLoadGenres.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonAddGenre.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonUpdateGenre.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonDeleteGenre.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonRechercheGenre.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonAverageRating.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonAveragePrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonTotalNumberOfBooks.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonTotalNumberOfBooks.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonReadGenre.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonExportRapport.setAlignmentX(Component.CENTER_ALIGNMENT);


        buttonLoadGenres.addActionListener(e -> refreshGenreTable());
        buttonAddGenre.addActionListener(e -> addGenre());
        buttonUpdateGenre.addActionListener(e -> updateGenre());
        buttonDeleteGenre.addActionListener(e -> deleteGenre());
        buttonRechercheGenre.addActionListener(e -> {
            String keyword = txtSearchGenre.getText();
            searchGenre(keyword);
        });
        buttonAverageRating.addActionListener(e -> averageRating());
        buttonAveragePrice.addActionListener(e -> averagePrice());
        buttonTotalNumberOfBooks.addActionListener(e -> totalNumberOfBooks());
        buttonReadGenre.addActionListener(e -> readGenre());
        /*buttonExportRapport.addActionListener(e -> exportRapport());*/


        buttonPanelGenre.add(buttonLoadGenres);
        buttonPanelGenre.add(buttonReadGenre);
        buttonPanelGenre.add(buttonAddGenre);
        buttonPanelGenre.add(buttonUpdateGenre);
        buttonPanelGenre.add(buttonDeleteGenre);
        buttonPanelGenre.add(buttonRechercheGenre);
        buttonPanelGenre.add(buttonAverageRating);
        buttonPanelGenre.add(buttonAveragePrice);
        buttonPanelGenre.add(buttonTotalNumberOfBooks);
        buttonPanelGenre.add(buttonExportRapport);


        add(buttonPanelGenre, BorderLayout.WEST);
        refreshGenreTable();
    }

    public void refreshGenreTable() {
        currentGenres = manager.getAllGenres();
        List<Genre> genres = manager.getAllGenres();
        String[] columns = {"ID", "Title", "Number of SubGenres", "URL"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 2;
            }
        };

        for (Genre g : genres) {
            model.addRow(new Object[]{
                    g.getId(),
                    g.getTitle(),
                    g.getNumberOfSubGenres(),
                    g.getUrl()
            });
        }
        genreTable.setModel(model);
    }

    private void addGenre() {
        // Demander le nom
        String title = JOptionPane.showInputDialog(this, "Enter the genre name:");
        if (title == null || title.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid name!");
            return;
        }
        int nOfSubGenre = 0;

        String url = JOptionPane.showInputDialog(this, "Enter the url:");

        // Créer le nouvel objet Genre avec toutes les infos
        Genre newGenre = new Genre(title, nOfSubGenre, url);


        boolean added = manager.addGenre(newGenre);
        if (added) {
            refreshGenreTable();
            JOptionPane.showMessageDialog(this,"The genre '" + title + "' has been added!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: Unable to add the genre!");
        }
    }

    private void updateGenre() {
        int row = genreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a genre to update.");
            return;
        }

        // Récupérer le genre correspondant (ancien nom)
        Genre selected = currentGenres.get(row);
        String oldTitle = selected.getTitle();

        // Récupérer les nouvelles valeurs directement depuis la table
        String newTitle = (String) genreTable.getValueAt(row, 1);
        String newUrl = (String) genreTable.getValueAt(row, 3);

        // Modifier l'objet
        selected.setTitle(newTitle);
        selected.setUrl(newUrl);

        boolean success = manager.updateGenre(selected);
        if (success) {
            // Mettre à jour dans le manager (base de données)
            manager.updateMainGenreTitleInSubGenre(oldTitle, newTitle);
            manager.updateMainGenreTitleInBooks(oldTitle, newTitle);
            refreshGenreTable();
            subGenrePanel.refreshSubGenreTable();
            bookPanel.refreshBookTable();
            JOptionPane.showMessageDialog(this, "Genre updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: could not update the genre.");
        }
    }

    private void deleteGenre() {
        int row = genreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a genre to delete.");
            return;
        }

        // Récupérer le genre sélectionné
        Genre selected = currentGenres.get(row);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deleting '" + selected.getTitle() + "' will also delete all its subgenres and related books.\n"
                        + "Are you sure you want to continue?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean ok = true;

        // Supprimer les subgenres liés
        if (!manager.deleteAllSubgenresOfGenre(selected.getId())) {
            ok = false;
        }

        // Supprimer les livres liés
        if (!manager.deleteAllBooksOfGenre(selected.getId())) {
            ok = false;
        }

        // Supprimer le genre lui-même
        if (!manager.deleteGenreById(selected.getId())) {
            ok = false;
        }


        if (ok) {
            refreshGenreTable();
            bookPanel.refreshBookTable();
            subGenrePanel.refreshSubGenreTable();
            JOptionPane.showMessageDialog(this, "Genre and all related subgenres/books deleted successfully!");
        } else {

            JOptionPane.showMessageDialog(this, "Error: some elements could not be deleted.");
        }
    }


    private void searchGenre(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // Si la recherche est vide, on recharge tous les genres
            refreshGenreTable();
            return;
        }

        // Appeler directement la recherche SQL via le manager
        List<Genre> filteredGenres = manager.searchGenres(keyword);

        // Mettre à jour la JTable
        String[] columns = {"ID", "Title", "Number of SubGenres", "URL"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 2;
            }
        };

        for (Genre g : filteredGenres) {
            model.addRow(new Object[]{
                    g.getId(),
                    g.getTitle(),
                    g.getNumberOfSubGenres(),
                    g.getUrl()
            });
        }
        genreTable.setModel(model);
    }

    private void averageRating() {
        int row = genreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a genre to have average rating.");
            return;
        }

        // Récupérer l'objet Genre correspondant à la ligne sélectionnée
        Genre selected = currentGenres.get(row);


        double avgRating = manager.getAverageRatingForGenre(selected.getId());

        JOptionPane.showMessageDialog(this,
                "Average rating for '" + selected.getTitle() + "' is: " + String.format("%.2f", avgRating));
    }

    private void averagePrice() {
        int row = genreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a genre to have average price.");
            return;
        }

        // Récupérer l'objet Genre correspondant à la ligne sélectionnée
        Genre selected = currentGenres.get(row);

        double avgPrice = manager.getAveragePriceForGenre(selected.getId());

        JOptionPane.showMessageDialog(this,
                "Average price for '" + selected.getTitle() + "' is: " + String.format("%.2f", avgPrice));
    }


    private void totalNumberOfBooks() {
        int row = genreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a genre to have average price.");
            return;
        }

        // Récupérer l'objet Genre correspondant à la ligne sélectionnée
        Genre selected = currentGenres.get(row);

        int totalNuberOfBooks = manager.getTotalNumberOfBooksForGenre(selected.getId());

        JOptionPane.showMessageDialog(this,
                "Total Number Of Books In The Genre '" + selected.getTitle() + "' is: " +  totalNuberOfBooks);
    }



    private void readGenre() {
        int row = genreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a genre to view its sub-genres.");
            return;
        }

        // Récupérer le genre sélectionné
        Genre selectedGenre = currentGenres.get(row);

        // Récupérer la liste des sous-genres associés
        List<SubGenre> subGenres = manager.getSubGenresByMainGenre(selectedGenre);

        // Construction message
        StringBuilder message = new StringBuilder();
        if (subGenres.isEmpty()) {
            message.append("No sub-genres found for genre: "+ selectedGenre.getTitle());
        } else {
            message.append("Title: " +selectedGenre.getTitle()+"\nNumber Of Sub Genres: "+
                    selectedGenre.getNumberOfSubGenres()+"\nUrl: "+selectedGenre.getUrl()+
                    "\n"+"\n\n"+"List Sub Genres of Genre "+selectedGenre.getTitle()+":\n");
            for (SubGenre sg : subGenres) {
                message.append("- "+sg.getTitle()+":\n"+"    - "+"Number Of Books: "+
                        sg.getNumberOfBooks()+"\n"+"    - "+"Url: "+sg.getUrl()+"\n\n");
            }
        }

        // Afficher la liste dans un JOptionPane
        JTextArea textArea = new JTextArea(message.toString());
        textArea.setEditable(false);// ne pas modifier le texte
        textArea.setCaretPosition(0);// remonter en haut

        textArea.setFont(new Font("San Francisco", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400)); // taille de la fenêtre

        JOptionPane.showMessageDialog(this,
                scrollPane,
                "Genre Details",
                JOptionPane.INFORMATION_MESSAGE);
    }


}
