package org.example;
public class Book {

    private int id;
    private String title;
    private String author;
    private String mainGenre;
    private int mainGenreId;
    private String subGenre;
    private int subGenreId;
    private String type;
    private String priceDisplay;
    private double price;
    private double rating;
    private int numberOfPeopleRated;
    private String url;

    public Book(int id,String title, String author, String mainGenre,int mainGenreId, String subGenre,int subGenreId, String type, String priceDisplay, double rating, int numberOfPeopleRated, String url) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.mainGenre = mainGenre;
        this.mainGenreId = mainGenreId;
        this.subGenre = subGenre;
        this.subGenreId = subGenreId;
        this.type = type;
        this.priceDisplay = priceDisplay;
        this.price = modifPriceDisplayInDouble(priceDisplay);
        this.rating = rating;
        this.numberOfPeopleRated = numberOfPeopleRated;
        this.url = url;
    }

    public Book(String title, String author, String mainGenre,int mainGenreId, String subGenre,int subGenreId, String type, String priceDisplay, double rating, int numberOfPeopleRated, String url) {
        this(0, title, author, mainGenre,mainGenreId, subGenre,subGenreId, type, priceDisplay, rating, numberOfPeopleRated, url);
    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getMainGenre() { return mainGenre; }
    public void setMainGenre(String mainGenre) { this.mainGenre = mainGenre;}

    public int getMainGenreId() { return mainGenreId; }
    public void setMainGenreId(int mainGenreId) { this.mainGenreId = mainGenreId; }

    public String getSubGenre() { return subGenre; }
    public void setSubGenre(String subGenre) { this.subGenre = subGenre; }

    public int getSubGenreId() { return subGenreId; }
    public void setSubGenreId(int subGenreId) { this.subGenreId = subGenreId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPriceDisplay() { return priceDisplay; }
    public void setPriceDisplay(String priceDisplay) { this.priceDisplay = priceDisplay; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getNumberOfPeopleRated() { return numberOfPeopleRated; }
    public void setNumberOfPeopleRated(int numberOfPeopleRated) { this.numberOfPeopleRated = numberOfPeopleRated; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public static double modifPriceDisplayInDouble(String priceDisplay) {
        if (priceDisplay == null || priceDisplay.isEmpty()) return 0.0;

        // Supprime le symbole ₹ et les espaces
        priceDisplay = priceDisplay.replace("₹", "").trim();

        // Supprime les virgules séparatrices de milliers
        priceDisplay = priceDisplay.replace(",", "");

        try {
            return Double.parseDouble(priceDisplay);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

}

