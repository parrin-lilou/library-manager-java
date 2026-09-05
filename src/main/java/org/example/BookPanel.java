package org.example;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class BookPanel extends JPanel {
    private final LibraryManager manager;
    private final JTable bookTable;
    private List<Book> currentBooks;
    private SubGenrePanel subGenrePanel;
    private GenrePanel genrePanel;
    private final JTextField txtSearchBook;
    private boolean ascendingRating = true;
    private boolean ascendingPrice = true;




    public void setGenrePanel(GenrePanel genrePanel) {
        this.genrePanel = genrePanel;
    }

    public void setSubGenrePanel(SubGenrePanel subGenrePanel) {
        this.subGenrePanel = subGenrePanel;
    }


    public BookPanel(LibraryManager manager) {
        this.manager = manager;


        // Layout du panel
        setLayout(new BorderLayout());

        // === Champ de recherche ===
        txtSearchBook = new JTextField();
        txtSearchBook.setMaximumSize(new Dimension(200, 25));
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel lblSearch = new JLabel("Search: ");
        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearchBook, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);// Ajouter en haut

        // Tableau des livres
        bookTable = new JTable();
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // Panel des boutons
        JPanel buttonPanelBook = new JPanel();
        buttonPanelBook.setLayout(new BoxLayout(buttonPanelBook, BoxLayout.Y_AXIS));

        JButton buttonLoadBooks = new JButton("Load Books");
        JButton buttonAddBook = new JButton("Add Book");
        JButton buttonUpdateBook = new JButton("Update Book");
        JButton buttonDeleteBook = new JButton("Delete Book");
        JButton buttonRechercheBookByTitle = new JButton("Search By Title");
        JButton buttonRechercheBookByAuthor = new JButton("Search By Author");
        JButton buttonFilterBookByMainGenre = new JButton("Filter Book By Main Genre And Sub Genre");
        JButton buttonSortingRating = new JButton("Sorting by Rating");
        JButton buttonSortingPrice = new JButton("Sorting by Price");
        JButton buttonReadBook = new JButton("Read Book");
        JButton buttonExportRapport = new JButton("Export Rapport");




        buttonLoadBooks.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonAddBook.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonUpdateBook.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonDeleteBook.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonRechercheBookByTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonRechercheBookByAuthor.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonFilterBookByMainGenre.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonSortingRating.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonSortingPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonReadBook.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonExportRapport.setAlignmentX(Component.CENTER_ALIGNMENT);



        buttonLoadBooks.addActionListener(e -> refreshBookTable());
        buttonAddBook.addActionListener(e -> addBook());
        buttonUpdateBook.addActionListener(e -> updateBook());
        buttonDeleteBook.addActionListener(e -> deleteBook());
        buttonRechercheBookByTitle.addActionListener(e -> {
            String keyword = txtSearchBook.getText();
            searchBookByTitle(keyword);
        });
        buttonRechercheBookByAuthor.addActionListener(e -> {
            String keyword = txtSearchBook.getText();
            searchBookByAuthor(keyword);
        });
        buttonFilterBookByMainGenre.addActionListener(e -> filterBookByMainGenreAndSubGenre());
        buttonSortingRating.addActionListener(e -> sortBooksByRating());
        buttonSortingPrice.addActionListener(e -> sortBooksByPrice());
        buttonReadBook.addActionListener(e -> readBook());
        /*buttonExportRapport.addActionListener(e -> exportRapport());*/



        buttonPanelBook.add(buttonLoadBooks);
        buttonPanelBook.add(buttonReadBook);
        buttonPanelBook.add(buttonAddBook);
        buttonPanelBook.add(buttonUpdateBook);
        buttonPanelBook.add(buttonDeleteBook);
        buttonPanelBook.add(buttonRechercheBookByTitle);
        buttonPanelBook.add(buttonRechercheBookByAuthor);
        buttonPanelBook.add(buttonFilterBookByMainGenre);
        buttonPanelBook.add(buttonSortingRating);
        buttonPanelBook.add(buttonSortingPrice);
        buttonPanelBook.add(buttonExportRapport);


        add(buttonPanelBook, BorderLayout.WEST);

        // Charger les données initiales
        refreshBookTable();
    }

    public void refreshBookTable() {
        List<Book> books = manager.getAllBooks();
        String[] columns = {"ID", "Title", "Author","Main Genre","Main Genre ID","Sub Genre","Sub Genre ID", "Type","Price", "Rating", "Number of People Rated", "URL"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 4 && column != 6;
            }
        };
        for (Book b : books) {
            model.addRow(new Object[]{
                    b.getId(), b.getTitle(), b.getAuthor(),b.getMainGenre(),
                    b.getMainGenreId(), b.getSubGenre(),b.getSubGenreId(), b.getType(),
                    b.getPriceDisplay(),b.getRating(), b.getNumberOfPeopleRated(), b.getUrl()
            });
        }
        bookTable.setModel(model);
    }

    private void addBook() {
        // Demander le nom
        String title = JOptionPane.showInputDialog(this, "Enter the book name :");
        if (title == null || title.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid name!");
            return;
        }

        String author = JOptionPane.showInputDialog(this, "Enter the author name :");
        if (author == null || author.trim().isEmpty()) {
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

        if (mainGenre == null) {
            // L'utilisateur a annulé
            return;
        }

        // Récupérer l'ID du genre choisi
        int mainGenreId = genres.stream()
                .filter(g -> g.getTitle().equals(mainGenre))
                .findFirst()
                .map(Genre::getId)
                .orElse(0);


// 3️⃣ Charger les sous-genres depuis le manager
        List<SubGenre> filteredSubgenres = manager.getSubGenresByMainGenreId(mainGenreId);


// 5️⃣ Extraire les titres des sous-genres filtrés
        String[] subGenreTitles = filteredSubgenres.stream()
                .map(SubGenre::getTitle)
                .toArray(String[]::new);

// 6️⃣ Si aucun sous-genre trouvé, informer l'utilisateur
        if (subGenreTitles.length == 0) {
            JOptionPane.showMessageDialog(this, "No sub-genres found for " + mainGenre + ".");
            return;
        }

// 7️⃣ Afficher la liste filtrée des sous-genres
        String selectedSubGenre = (String) JOptionPane.showInputDialog(
                this,
                "Select a sub-genre of " + mainGenre + ":",
                "SubGenre",
                JOptionPane.PLAIN_MESSAGE,
                null,
                subGenreTitles,
                subGenreTitles[0]
        );


        // Récupérer l'ID du subgenre choisi
        int subGenreId = filteredSubgenres.stream()
                .filter(sg -> sg.getTitle().equals(selectedSubGenre))
                .findFirst()
                .map(SubGenre::getId)
                .orElse(0);


        String type = JOptionPane.showInputDialog(this, "Enter the type of book :");
        String pricedisplay = JOptionPane.showInputDialog(this, "Enter the price :");
        double rating = 0.0;
        String ratingStr = JOptionPane.showInputDialog(this, "Enter the rating:");
        if (ratingStr != null) {
            try {
                rating = Double.parseDouble(ratingStr.replace(',', '.'));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid value for the rating. Use a number (e.g., 3.5).");
                return;
            }
        }
        int numberOfPeopleRated = 0;
        String numberOfPeopleRatedStr = JOptionPane.showInputDialog(this, "Enter the number of people rated:");
        if (numberOfPeopleRatedStr != null) {
            try {
                numberOfPeopleRated = Integer.parseInt(numberOfPeopleRatedStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid value for the number of people raed!");
                return;
            }
        }

        String url = JOptionPane.showInputDialog(this, "Enter the url:");

        // Créer le nouvel objet Genre avec toutes les infos
        Book newBook = new Book(title, author, mainGenre,mainGenreId,selectedSubGenre,subGenreId,type,pricedisplay,rating,numberOfPeopleRated, url);

        boolean added = manager.addBook(newBook);
        if (added) {
            // Mettre à jour le nombre de book pour le sub genre choisis
            manager.incrementNrOfBooksCount(subGenreId);

            // Rafraîchir les tableaux
            subGenrePanel.refreshSubGenreTable();
            genrePanel.refreshGenreTable(); // <-- ici pour voir le nouveau compteur
            refreshBookTable();
            JOptionPane.showMessageDialog(this, "The book '" + title + "' has been added!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: Unable to add the book!");
        }

    }

    private void updateBook() {
        int row = bookTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to update.");
            return;
        }

        List<Book> currentBooks = manager.getAllBooks();
        Book selected = currentBooks.get(row);

        // Lire les valeurs directement depuis la JTable
        String newTitle = (String) bookTable.getValueAt(row, 1);
        String newAuthor = (String) bookTable.getValueAt(row, 2);
        String newMainGenre = (String) bookTable.getValueAt(row, 3);
        String newSubGenre = (String) bookTable.getValueAt(row, 5);
        String newType = (String) bookTable.getValueAt(row, 7);
        String newPriceStr = (String) bookTable.getValueAt(row, 8);
        String newRatingStr = bookTable.getValueAt(row, 9).toString();
        String newNombreOfRatingStr = bookTable.getValueAt(row, 10).toString();
        String newUrl = (String) bookTable.getValueAt(row, 11);




        double newRating;
        try {
            newRating = Double.parseDouble(newRatingStr);
        } catch (NumberFormatException e) {
            refreshBookTable();
            JOptionPane.showMessageDialog(this, "Invalid rating value. Please enter a valid number.");
            return;
        }

        int newNumberOfRatings;
        try {
            newNumberOfRatings = Integer.parseInt(newNombreOfRatingStr);
        } catch (NumberFormatException e) {
            refreshBookTable();
            JOptionPane.showMessageDialog(this, "Invalid number of ratings. Please enter a valid integer.");
            return;
        }


        // Chercher l'ID correspondant au Sub Genre choisi
        List<SubGenre> subGenres = manager.getAllSubGenres();
        SubGenre matchingSubGenre = subGenres.stream()
                .filter(sg -> sg.getTitle().equalsIgnoreCase(newSubGenre))
                .findFirst()
                .orElse(null);

        if (matchingSubGenre == null) {
            refreshBookTable();

            JOptionPane.showMessageDialog(this, "Subgenre not found!");
            return;
        }

        int newSubGenreId = matchingSubGenre.getId();


        // --- Récupération du Genre principal ---
        List<Genre> genres = manager.getAllGenres();
        Genre matchingGenre = genres.stream()
                .filter(g -> g.getTitle().equalsIgnoreCase(newMainGenre))
                .findFirst()
                .orElse(null);

        if (matchingGenre == null) {
            refreshBookTable();
            JOptionPane.showMessageDialog(this, "Main genre not found!");
            return;
        }

        int newMainGenreId = matchingGenre.getId();

        // --- Vérifier que le SubGenre correspond bien au MainGenre ---
        List<SubGenre> validSubGenresForMainGenre = manager.getSubGenresByMainGenreId(newMainGenreId);
        boolean subGenreBelongsToMain = validSubGenresForMainGenre.stream()
                .anyMatch(sg -> sg.getId() == newSubGenreId);

        if (!subGenreBelongsToMain) {
            refreshBookTable();
            JOptionPane.showMessageDialog(this,
                    "Invalid subgenre for the selected main genre. Please choose a subgenre that belongs to this main genre.");
            return;
        }


        // --- Mettre à jour les compteurs si le subgenre a changé ---
        if (selected.getSubGenreId() != newSubGenreId) {
            manager.decrementNrOfBooksCount(selected.getSubGenreId());
            manager.incrementNrOfBooksCount(newSubGenreId);
        }

        // --- Mettre à jour l'objet Book ---
        selected.setTitle(newTitle);
        selected.setAuthor(newAuthor);
        selected.setMainGenre(newMainGenre);
        selected.setMainGenreId(newMainGenreId);
        selected.setSubGenre(newSubGenre);
        selected.setSubGenreId(newSubGenreId);
        selected.setType(newType);
        selected.setPriceDisplay(newPriceStr);
        selected.setRating(newRating);
        selected.setNumberOfPeopleRated(newNumberOfRatings);
        selected.setUrl(newUrl);





// --- Mise à jour via le manager ---
        boolean success = manager.updateBook(selected);
        if (success) {
            genrePanel.refreshGenreTable();
            subGenrePanel.refreshSubGenreTable();
            refreshBookTable();
            JOptionPane.showMessageDialog(this, "Book updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: could not update the book.");
        }
    }

    private void deleteBook() {
        int row = bookTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }

        // Récupérer l'objet Genre correspondant à la ligne sélectionnée
        currentBooks = manager.getAllBooks();
        Book selected = currentBooks.get(row);

        // Confirmation
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete '" + selected.getTitle() + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;



        // Supprimer via l'ID
        boolean success = manager.deleteBookById(selected.getId());

        if (success) {
            manager.decrementNrOfBooksCount(selected.getSubGenreId());
            refreshBookTable();
            subGenrePanel.refreshSubGenreTable();
            JOptionPane.showMessageDialog(this, "Book deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error: could not delete genre.");
        }
    }


    private void searchBookByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // Si la recherche est vide, on recharge tous les genres
            refreshBookTable();
            return;
        }

        // Appeler directement la recherche SQL via le manager
        List<Book> filteredBooks = manager.searchBooksByTitle(keyword);

        updateBookTable(filteredBooks);
    }

    private void searchBookByAuthor(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // Si la recherche est vide, on recharge tous les genres
            refreshBookTable();
            return;
        }

        // Appeler directement la recherche SQL via le manager
        List<Book> filteredBooks = manager.searchBooksByAuthor(keyword);

        updateBookTable(filteredBooks);
    }

    private void filterBookByMainGenreAndSubGenre() {
        // 1️⃣ Récupérer tous les genres principaux
        List<Genre> genres = manager.getAllGenres();
        if (genres == null || genres.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No genre available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2️⃣ Créer un tableau des titres pour affichage
        String[] genreTitles = genres.stream()
                .map(Genre::getTitle)
                .toArray(String[]::new);

        // 3️⃣ Sélection du genre principal
        String selectedMainGenreTitle = (String) JOptionPane.showInputDialog(
                this,
                "Select a main genre:",
                "Filter by genre",
                JOptionPane.PLAIN_MESSAGE,
                null,
                genreTitles,
                genreTitles[0]
        );

        if (selectedMainGenreTitle == null) return; // annulation

        // 4️⃣ Trouver l'objet Genre correspondant
        Genre selectedGenre = genres.stream()
                .filter(g -> g.getTitle().equals(selectedMainGenreTitle))
                .findFirst()
                .orElse(null);

        if (selectedGenre == null) return;

        // 5️⃣ Récupérer les sous-genres liés
        List<SubGenre> subGenres = manager.getSubGenresByMainGenre(selectedGenre);

        if (subGenres == null || subGenres.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No subgenres found for this genre.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return ;
        }

        // 6️⃣ Liste des sous-genres
        String[] subGenreTitles = subGenres.stream()
                .map(SubGenre::getTitle)
                .toArray(String[]::new);

        // 7️⃣ Sélection d’un seul sous-genre
        String selectedSubGenreTitle = (String) JOptionPane.showInputDialog(
                this,
                "Select a sub genre:",
                "Filter by sub genre",
                JOptionPane.PLAIN_MESSAGE,
                null,
                subGenreTitles,
                subGenreTitles[0]
        );


        if (selectedSubGenreTitle == null) return; // annulation

        // 8️⃣ Trouver l'objet SubGenre correspondant
        SubGenre selectedSubGenre = subGenres.stream()
                .filter(sg -> sg.getTitle().equals(selectedSubGenreTitle))
                .findFirst()
                .orElse(null);

        if (selectedSubGenre == null) return;

        // 9️⃣ Filtrer les livres par genre principal et sous-genre
        List<Book> filteredBooks = manager.getBooksByMainGenreAndSubGenre(selectedGenre, selectedSubGenre);

        // 🔟 Mettre à jour la JTable
        // Mettre à jour la JTable
        updateBookTable(filteredBooks);

    }

    public void sortBooksByRating() {
        // 1️⃣ Récupérer les livres actuellement affichés dans la JTable
        List<Book> books = manager.getAllBooks(); // ou utiliser la liste filtrée si tu en as une
        if (books == null || books.isEmpty()) return;

        // 2️⃣ Trier selon la note
        books.sort((b1, b2) -> {
            if (ascendingRating) {
                return Double.compare(b1.getRating(), b2.getRating()); // croissant
            } else {
                return Double.compare(b2.getRating(), b1.getRating()); // décroissant
            }
        });

        // 3️⃣ Mettre à jour l’affichage
        updateBookTable(books);

        // 4️⃣ Inverser le sens pour le prochain clic
        ascendingRating = !ascendingRating;
    }

    public void sortBooksByPrice() {
        // 1️⃣ Récupérer les livres actuellement affichés dans la JTable
        List<Book> books = manager.getAllBooks(); // ou utiliser la liste filtrée si tu en as une
        if (books == null || books.isEmpty()) return;

        // 2️⃣ Trier selon la note
        books.sort((b1, b2) -> {
            if (ascendingPrice) {
                return Double.compare(b1.getPrice(), b2.getPrice()); // croissant
            } else {
                return Double.compare(b2.getPrice(), b1.getPrice()); // décroissant
            }
        });

        // 3️⃣ Mettre à jour l’affichage
        updateBookTable(books);

        // 4️⃣ Inverser le sens pour le prochain clic
        ascendingPrice = !ascendingPrice;
    }

    private void readBook() {
        int row = bookTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to view details.");
            return;
        }

        // Récupère l'objet Genre correspondant à la ligne sélectionnée
        currentBooks = manager.getAllBooks();
        Book selected = currentBooks.get(row);

        // Construit un message détaillé
        String message = String.format(
                "Title: %s\n" +"Author: %s\n" +"Main Genre: %s\n" +"Sub Genre: %s\n" +"Type: %s\n"+
                        "Price: %s\n" +"Rating: %s\n" + "Number of Rating: %d\n" + "URL: %s",
                selected.getTitle(),
                selected.getAuthor(),
                selected.getMainGenre(),
                selected.getSubGenre(),
                selected.getType(),
                selected.getPriceDisplay(),
                selected.getRating(),
                selected.getNumberOfPeopleRated(),
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










    private void updateBookTable(List<Book> filteredBooks) {
        if (filteredBooks == null) return;

        // Colonnes de la JTable
        String[] columns = {
                "ID", "Title", "Author", "Main Genre", "Main Genre ID",
                "Sub Genre", "Sub Genre ID", "Type", "Price",
                "Rating", "Number of Rating", "URL"
        };

        // Création du modèle
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 4 && column != 6;
            }
        };

        // Remplissage du modèle avec les livres filtrés
        for (Book b : filteredBooks) {
            model.addRow(new Object[]{
                    b.getId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getMainGenre(),
                    b.getMainGenreId(),
                    b.getSubGenre(),
                    b.getSubGenreId(),
                    b.getType(),
                    b.getPriceDisplay(),
                    b.getRating(),
                    b.getNumberOfPeopleRated(),
                    b.getUrl()
            });
        }

        // Mise à jour de la JTable
        bookTable.setModel(model);
    }







}

