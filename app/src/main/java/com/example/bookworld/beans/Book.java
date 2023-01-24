package com.example.bookworld.beans;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Book implements Serializable {

    @PrimaryKey
    @NonNull
    @SerializedName("objectId")
    private String bookId;
    private String name;
    private String genre;
    private String author;
    private String price;
    private boolean availability;
    private String image;
    private String rating;
    private String description;

    public Book(@NonNull String bookId, String name, String genre, String author, String price, boolean availability, String image, String rating, String description) {
        this.bookId = bookId;
        this.name = name;
        this.genre = genre;
        this.author = author;
        this.price = price;
        this.availability = availability;
        this.image = image;
        this.rating = rating;
        this.description = description;
    }

    public void changeStatus() {
        this.availability = !availability;
    }

    @NonNull
    public String getBookId() {
        return bookId;
    }

    public void setBookId(@NonNull String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", availability=" + availability +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                '}';
    }
}
