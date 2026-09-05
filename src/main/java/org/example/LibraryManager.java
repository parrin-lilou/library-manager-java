package org.example;
import java.util.List;

public class LibraryManager {

    private final BookDAO bookDAO;
    private final GenreDAO genreDAO;
    private final SubGenreDAO subGenreDAO;


    public LibraryManager(BookDAO bookDAO, GenreDAO genreDAO, SubGenreDAO subGenreDAO) {
        this.bookDAO = bookDAO;
        this.genreDAO = genreDAO;
        this.subGenreDAO = subGenreDAO;
    }

    // ====================== BOOKS ======================
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public boolean addBook(Book book) {
        return bookDAO.addBook(book);
    }

    public boolean updateBook(Book book) {
        return bookDAO.updateBook(book);
    }

    public boolean deleteBookById(int id) {
        return bookDAO.deleteBookById(id);
    }

    public boolean isTableBookEmpty(String tableName) {
        return bookDAO.isTableBookEmpty(tableName);
    }

    public List<Book> searchBooksByTitle(String keyword){

        return bookDAO.searchBooksByTitle(keyword);
    }

    public List<Book> searchBooksByAuthor(String keyword) {
        return bookDAO.searchBooksByAuthor(keyword);
    }

    public void updateSubGenresTitleInBooks(String oldTitle, String newTitle) {
        bookDAO.updateSubGenresTitleInBooks(oldTitle, newTitle);
    }

    public void updateMainGenreTitleInBooks(String oldTitle, String newTitle) {
        bookDAO.updateMainGenreTitleInBooks(oldTitle, newTitle);
    }

    public List<Book> getBooksByMainGenre(Genre mainGenre) {

        return bookDAO.getBooksByMainGenre(mainGenre);
    }

    public List<Book> getBooksByMainGenreAndSubGenre(Genre mainGenre, SubGenre subGenre){
        return bookDAO.getBooksByMainGenreAndSubGenre(mainGenre, subGenre);
    }

    public double getAverageRatingForGenre(int genreId) {
        return bookDAO.getAverageRatingForGenre(genreId);
    }
    public double getAveragePriceForGenre(int genreId) {
        return bookDAO.getAveragePriceForGenre(genreId);
    }

    public int getTotalNumberOfBooksForGenre(int GenreId) {
        return bookDAO.getTotalNumberOfBooksForGenre(GenreId);
    }

    public double getAverageRatingForSubGenre(int subGenreId) {
        return bookDAO.getAverageRatingForSubGenre(subGenreId);
    }

    public double getAveragePriceForSubGenre(int subGenreId) {
        return bookDAO.getAveragePriceForSubGenre(subGenreId);
    }

    public int getTotalNumberOfBooksForSubGenre(int subGenreId) {
        return bookDAO.getTotalNumberOfBooksForSubGenre(subGenreId);
    }

    public boolean deleteAllBooksOfGenre(int genreId) {
        return bookDAO.deleteAllBooksOfGenre(genreId);
    }

    public boolean deleteAllBooksOfSubGenre(int subGenreId) {
        return bookDAO.deleteAllBooksOfSubGenre(subGenreId);
    }




    // ====================== GENRES ======================
    public List<Genre> getAllGenres() {
        return genreDAO.getAllGenres();
    }

    public boolean addGenre(Genre genre) {
        return genreDAO.addGenre(genre);
    }

    public boolean updateGenre(Genre genre) {
        return genreDAO.updateGenre(genre);
    }

    public boolean deleteGenreById(int id) {
        return genreDAO.deleteGenreById(id);
    }

    public void incrementSubGenreCount(int mainGenreId) {
        genreDAO.incrementSubGenreCount(mainGenreId);
    }

    public void decrementSubGenreCount(int mainGenreId) {
        genreDAO.decrementSubGenreCount(mainGenreId);
    }

    public boolean isTableGenreEmpty(String tableName) {
        return genreDAO.isTableGenreEmpty(tableName);
    }
    public List<Genre> searchGenres(String keyword){
        return genreDAO.searchGenres(keyword);
    }




    // ====================== SUBGENRES ======================
    public List<SubGenre> getAllSubGenres() {
        return subGenreDAO.getAllSubGenres();
    }

    public boolean addSubGenre(SubGenre subGenre) {
        return subGenreDAO.addSubGenre(subGenre);
    }

    public boolean updateSubGenre(SubGenre subGenre) {
        return subGenreDAO.updateSubGenre(subGenre);
    }

    public List<SubGenre> searchSubGenres(String keyword){
        return subGenreDAO.searchSubGenres(keyword);
    }

    public boolean isTableSubGenreEmpty(String tableName) {
        return subGenreDAO.isTableSubGenreEmpty(tableName);
    }

    public boolean deleteSubGenreById(int id) {
        return subGenreDAO.deleteSubGenreById(id);
    }

    public void updateMainGenreTitleInSubGenre(String oldTitle, String newTitle) {
        subGenreDAO.updateMainGenreTitleInSubGenre(oldTitle, newTitle);

    }

    public void     incrementNrOfBooksCount(int subGenreId) {
        subGenreDAO.incrementNrOfBooksCount(subGenreId);
    }

    public void decrementNrOfBooksCount(int subGenreId) {
        subGenreDAO.decrementNrOfBooksCount(subGenreId);
    }


    public List<SubGenre> getSubGenresByMainGenre(Genre mainGenre){
        return subGenreDAO.getSubGenresByMainGenre(mainGenre);
    }


    public List<SubGenre> getSubGenresByMainGenreId(int mainGenreId){
        return subGenreDAO.getSubGenresByMainGenreId(mainGenreId);
    }

    public boolean deleteAllSubgenresOfGenre(int genreId) {
        return subGenreDAO.deleteAllSubgenresOfGenre(genreId);
    }


}





