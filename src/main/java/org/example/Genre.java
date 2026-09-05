package org.example;
public class Genre {

    private int id;
    private String title;
    private int numberOfSubGenres;
    private String url;

    public Genre (int id,String title, int numberOfSubGenres, String url) {
        this.id = id;
        this.title = title;
        this.numberOfSubGenres = numberOfSubGenres;
        this.url = url;
    }

    public Genre (String title, int numberOfSubGenres, String url) {
        this(0, title, numberOfSubGenres,url);
    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getNumberOfSubGenres() { return numberOfSubGenres; }
    public void setNumberOfSubGenres(int numberOfSubGenres) { this.numberOfSubGenres = numberOfSubGenres; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }


}
