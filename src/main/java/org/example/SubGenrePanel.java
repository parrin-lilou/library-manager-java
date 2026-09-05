package org.example;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SubGenrePanel extends JPanel {
    private final LibraryManager manager;
    private final JTable subGenreTable;
    private List<SubGenre> currentSubGenres;
    private GenrePanel genrePanel;
    private BookPanel bookPanel;
    private final JTextField txtSearchSubGenre;

    public void setGenrePanel(GenrePanel genrePanel) {
        this.genrePanel = genrePanel;
    }
    public void setBookPanel(BookPanel bookPanel) {
        this.bookPanel = bookPanel;
    }

    public SubGenrePanel(LibraryManager manager) {
        this.manager = manager;

        // Définir le layout du panel
        setLayout(new BorderLayout());

        // === Champ de recherche ===
        txtSearchSubGenre = new JTextField();
        txtSearchSubGenre.setMaximumSize(new Dimension(200, 25));
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel lblSearch = new JLabel("Search: ");
        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearchSubGenre, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);// Ajouter en haut

        // Créer le tableau et ajouter avec scroll
        subGenreTable = new JTable();
        add(new JScrollPane(subGenreTable), BorderLayout.CENTER);

        // Créer le panel pour les boutons
        JPanel buttonPanelSubGenre = new JPanel();
        buttonPanelSubGenre.setLayout(new BoxLayout(buttonPanelSubGenre, BoxLayout.Y_AXIS));

        JButton buttonLoadSubGenres = new JButton("Load Sub Genres");
        JButton buttonAddSubGenre = new JButton("Add Sub Genre");
        JButton buttonUpdateSubGenre = new JButton("Update Sub Genre");
        JButton buttonDeleteSubGenre = new JButton("Delete Sub Genre");
        JButton buttonRechercheSubGenre = new JButton("Search");
        JButton buttonAverageRating = new JButton("Average Rating");
        JButton buttonAveragePrice = new JButton("Average Price");
        JButton buttonTotalNumberOfBooks = new JButton("Total Number Of Books");
        JButton buttonReadSubGenre = new JButton("Read Sub Genre");
        JButton buttonExportRapport = new JButton("Export Rapport");



        buttonLoadSubGenres.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonAddSubGenre.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonUpdateSubGenre.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonDeleteSubGenre.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonRechercheSubGenre.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonAverageRating.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonAveragePrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonTotalNumberOfBooks.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonReadSubGenre.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonExportRapport.setAlignmentX(Component.CENTER_ALIGNMENT);


        buttonLoadSubGenres.addActionListener(e -> refreshSubGenreTable());
        buttonAddSubGenre.addActionListener(e -> addSubGenre());
        buttonUpdateSubGenre.addActionListener(e -> updateSubGenre());
        buttonDeleteSubGenre.addActionListener(e -> deleteSubGenre());
        buttonRechercheSubGenre.addActionListener(e -> {
            String keyword = txtSearchSubGenre.getText();
            searchSubGenre(keyword);
        });
        buttonAverageRating.addActionListener(e -> averageRating());
        buttonAveragePrice.addActionListener(e -> averagePrice());
        buttonTotalNumberOfBooks.addActionListener(e -> totalNumberOfBooks());
        buttonReadSubGenre.addActionListener(e -> readSubGenre());
        /*buttonExportRapport.addActionListener(e -> exportRapport());*/

        buttonPanelSubGenre.add(buttonLoadSubGenres);
        buttonPanelSubGenre.add(buttonReadSubGenre);
        buttonPanelSubGenre.add(buttonAddSubGenre);
        buttonPanelSubGenre.add(buttonUpdateSubGenre);
        buttonPanelSubGenre.add(buttonDeleteSubGenre);
        buttonPanelSubGenre.add(buttonRechercheSubGenre);
        buttonPanelSubGenre.add(buttonAverageRating);
        buttonPanelSubGenre.add(buttonAveragePrice);
        buttonPanelSubGenre.add(buttonTotalNumberOfBooks);
        buttonPanelSubGenre.add(buttonExportRapport);


        add(buttonPanelSubGenre, BorderLayout.WEST);

        // Charger les données initiales
        refreshSubGenreTable();
    }

    public void refreshSubGenreTable() {
        currentSubGenres = manager.getAllSubGenres();
        List<SubGenre> subGenres = manager.getAllSubGenres();
        String[] columns = {"ID", "Title", "Main Genre", "Main Genre ID", "Number of Books", "URL"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 3 && column != 4;
            }
        };
        for (SubGenre sg : subGenres) {
            model.addRow(new Object[]{
                    sg.getId(), sg.getTitle(), sg.getMainGenre(),
                    sg.getMainGenreId(), sg.getNumberOfBooks(), sg.getUrl()
            });
        }
        subGenreTable.setModel(model);
    }

    private void addSubGenre() {
        // Demander le nom
        String title = JOptionPane.showInputDialog(this, "Enter the sub genre name :");
        if (title == null || title.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid name!");
            return;
        }
        // Sélectionner le genre parent
        List<Genre> genres = manager.getAllGenres();
        String[] genreTitles = genres.stream().map(Genre::getTitle).toArray(String[]::new);
        String mainGenre = (String) JOptionPane.showInputDialog(
                this,
                "Select the main genre:",
                "Main Genre",
                JOptionPane.PLAIN_MESSAGE,
                null,
                genreTitles,
                genreTitles[0]
        );


        // Récupérer l'ID du genre choisi
        int mainGenreId = genres.stream()
                .filter(g -> g.getTitle().equals(mainGenre))
                .findFirst()
                .map(Genre::getId)
                .orElse(0);  // <- si genre introuvable, mais ne devrait pas arriver

        int nOfBooks = 0;

        String url = JOptionPane.showInputDialog(this, "Enter the url :");

        // Créer le nouvel objet Genre avec toutes les infos
        SubGenre newSubGenre = new SubGenre(title, mainGenre,mainGenreId,nOfBooks, url); // selon ton constructeur

        boolean added = manager.addSubGenre(newSubGenre);
        if (added) {
            // Mettre à jour le nombre de subgenres pour le genre parent
            manager.incrementSubGenreCount(mainGenreId);

            // Rafraîchir les tableaux
            refreshSubGenreTable();
            genrePanel.refreshGenreTable(); // <-- ici pour voir le nouveau compteur
            JOptionPane.showMessageDialog(this, "The sub-genre '" + title + "' has been added!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: Unable to add subgenre!");
        }

    }

    private void updateSubGenre() {
        int row = subGenreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subgenre to update.");
            return;
        }

        List<SubGenre> currentSubGenres = manager.getAllSubGenres();
        SubGenre selected = currentSubGenres.get(row);
        String oldTitle = selected.getTitle();

        // Lire les valeurs directement depuis la JTable
        String newTitle = (String) subGenreTable.getValueAt(row, 1);
        String newMainGenre = (String) subGenreTable.getValueAt(row, 2);
        String newUrl = (String) subGenreTable.getValueAt(row, 5);


        // Chercher l'ID correspondant au Main Genre choisi
        List<Genre> genres = manager.getAllGenres();
        Genre matchingGenre = genres.stream()
                .filter(g -> g.getTitle().equals(newMainGenre))
                .findFirst()
                .orElse(null);

        if (matchingGenre == null) {
            JOptionPane.showMessageDialog(this, "Main genre not found!");
            refreshSubGenreTable();
            return;
        }

        int newMainGenreId = matchingGenre.getId();

        // Mettre à jour les compteurs si le Main Genre a changé
        if (selected.getMainGenreId() != newMainGenreId) {
            manager.decrementSubGenreCount(selected.getMainGenreId()); // Ancien genre
            manager.incrementSubGenreCount(newMainGenreId);            // Nouveau genre
        }

        // Mettre à jour le subgenre
        selected.setTitle(newTitle);
        selected.setMainGenre(newMainGenre);
        selected.setMainGenreId(newMainGenreId);
        selected.setUrl(newUrl);


        boolean success = manager.updateSubGenre(selected);
        if (success) {
            // Mettre à jour dans le manager (et base de données)
            manager.updateSubGenre(selected);
            manager.updateSubGenresTitleInBooks(oldTitle, newTitle);
            refreshSubGenreTable();
            bookPanel.refreshBookTable();
            JOptionPane.showMessageDialog(this, "Subgenre updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: could not update subgenre.");
        }
    }


    private void deleteSubGenre() {
        int row = subGenreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a sub genre to delete.");
            return;
        }

        // Récupérer l'objet SubGenre correspondant à la ligne sélectionnée
        currentSubGenres = manager.getAllSubGenres();
        SubGenre selected = currentSubGenres.get(row);

        // Confirmation
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deleting '" + selected.getTitle() + "' will also delete all books belonging to this subgenre.\n"
                        + "Are you sure you want to continue?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        // 1️⃣ Supprimer tous les books liés à ce subgenre
        boolean booksDeleted = manager.deleteAllBooksOfSubGenre(selected.getId());
        if (!booksDeleted) {
            bookPanel.refreshBookTable();
            JOptionPane.showMessageDialog(this, "Error: could not delete books of this subgenre.");
            return;
        }

        // 2️⃣ Supprimer le subgenre lui-même
        boolean subGenreDeleted = manager.deleteSubGenreById(selected.getId());
        if (!subGenreDeleted) {
            JOptionPane.showMessageDialog(this, "Error: could not delete subgenre.");
            return;
        }

        // 3️⃣ Décrémenter le compteur du genre parent
        manager.decrementSubGenreCount(selected.getMainGenreId());

        // 4️⃣ Rafraîchir les tables
        genrePanel.refreshGenreTable();
        bookPanel.refreshBookTable();
        refreshSubGenreTable();

        JOptionPane.showMessageDialog(this, "Subgenre and all related books deleted successfully!");
    }


    private void searchSubGenre(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // Si la recherche est vide, on recharge tous les genres
            refreshSubGenreTable();
            return;
        }

        // Appeler directement la recherche SQL via le manager
        List<SubGenre> filteredSubGenres = manager.searchSubGenres(keyword);

        // Mettre à jour la JTable
        String[] columns = {"ID", "Title", "Main Genre", "Main Genre ID", "Number of Books", "URL"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 3 && column != 4;
            }
        };
        for (SubGenre sg : filteredSubGenres) {
            model.addRow(new Object[]{
                    sg.getId(),
                    sg.getTitle(),
                    sg.getMainGenre(),
                    sg.getMainGenreId(),
                    sg.getNumberOfBooks(),
                    sg.getUrl()
            });
        }

        subGenreTable.setModel(model);
    }

    private void averageRating() {

        int row = subGenreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a sub genre to have average rating.");
            return;
        }

        // Récupérer l'objet Genre correspondant à la ligne sélectionnée
        SubGenre selected = currentSubGenres.get(row);

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Subgenre not found.");
            return;
        }

        // Appeler la méthode manager pour récupérer la moyenne des notes
        double avgRating = manager.getAverageRatingForSubGenre(selected.getId());

        // Afficher le résultat
        JOptionPane.showMessageDialog(this,
                "Average rating for '" + selected.getTitle() + "' is: " + String.format("%.2f", avgRating));
    }

    private void averagePrice() {

        int row = subGenreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a sub genre to have average price.");
            return;
        }

        // Récupérer l'objet Genre correspondant à la ligne sélectionnée
        SubGenre selected = currentSubGenres.get(row);

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Subgenre not found.");
            return;
        }

        // Appeler la méthode manager pour récupérer la moyenne des notes
        double avgPrice = manager.getAveragePriceForSubGenre(selected.getId());

        // Afficher le résultat
        JOptionPane.showMessageDialog(this,
                "Average price for '" + selected.getTitle() + "' is: " + String.format("%.2f", avgPrice));
    }

    private void totalNumberOfBooks() {

        int row = subGenreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a sub genre to have total number of books.");
            return;
        }

        // Récupérer l'objet Genre correspondant à la ligne sélectionnée
        SubGenre selected = currentSubGenres.get(row);


        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Sub genre not found.");
            return;
        }

        // Appeler la méthode manager pour récupérer la moyenne des notes
        int totalNuberOfBooks = manager.getTotalNumberOfBooksForSubGenre(selected.getId());

        // Afficher le résultat
        JOptionPane.showMessageDialog(this,
                "Total Number Of Books In The Sub Genre '" + selected.getTitle() + "' is: " +  totalNuberOfBooks);
    }

    private void readSubGenre() {
        int row = subGenreTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a sub genre to view details.");
            return;
        }

        // Récupère l'objet Genre correspondant à la ligne sélectionnée
        currentSubGenres = manager.getAllSubGenres();
        SubGenre selected = currentSubGenres.get(row);

        // Construit un message détaillé
        String message = String.format(
                "Title: %s\n" + "Main Genre: %s\n" + "Number of Books: %d\n" + "URL: %s",
                selected.getTitle(),
                selected.getMainGenre(),
               selected.getNumberOfBooks(),
                selected.getUrl()
        );

        // Affiche la fenêtre de lecture
        JOptionPane.showMessageDialog(
                this,
                message,
                "Genre Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


}

