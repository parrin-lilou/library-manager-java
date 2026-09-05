package org.example;
public class SubGenre{

    private int id;
    private String title;
    private String mainGenre;
    private int mainGenreId;
    private int numberOfBooks;
    private String url;

    public SubGenre (int id,String title, String mainGenre,int mainGenreId, int numberOfBooks,String url) {
        this.id = id;
        this.title = title;
        this.mainGenre = mainGenre;
        this.mainGenreId = mainGenreId;
        this.numberOfBooks = numberOfBooks;
        this.url = url;
    }

    public SubGenre (String title, String mainGenre,int mainGenreId,int numberOfBooks,String url) {
        this(0, title, mainGenre,mainGenreId,numberOfBooks, url);
    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMainGenre() { return mainGenre; }
    public void setMainGenre(String mainGenre) { this.mainGenre = mainGenre; }

    public int getMainGenreId() { return mainGenreId; }
    public void setMainGenreId(int mainGenreId) { this.mainGenreId = mainGenreId; }


    public int getNumberOfBooks() { return numberOfBooks; }
    public void setNumberOfBooks(int numberOfBooks) { this.numberOfBooks = numberOfBooks; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }


}
