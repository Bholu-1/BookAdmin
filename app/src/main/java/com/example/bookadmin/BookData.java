package com.example.bookadmin;

public class BookData {

    private String title;
    private String author;
    private String price;
    private String publisher;
    private String hates;
    private String likes;
    private String rating;
    private String no_of_pages;
    private String isbn;
    private String image;

    public BookData() {
    }

    public BookData(String title, String author, String price, String publisher, String hates, String likes, String rating, String no_of_pages, String isbn, String image) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.publisher = publisher;
        this.hates = hates;
        this.likes = likes;
        this.rating = rating;
        this.no_of_pages = no_of_pages;
        this.isbn = isbn;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPrice() {
        return price;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getHates() {
        return hates;
    }

    public String getLikes() {
        return likes;
    }

    public String getRating() {
        return rating;
    }

    public String getNo_of_pages() {
        return no_of_pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getImage() {
        return image;
    }
}
