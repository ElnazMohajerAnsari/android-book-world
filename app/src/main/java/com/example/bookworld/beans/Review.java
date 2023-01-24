package com.example.bookworld.beans;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Review implements Serializable {

    @PrimaryKey
    @NonNull
    @SerializedName("objectId")
    private String reviewId;
    private String book;
    private String reviewerName;
    private String date;
    private String content;

    @Ignore
    public Review() {
    }

    public Review(@NonNull String reviewId, String book, String reviewerName, String date, String content) {
        this.reviewId = reviewId;
        this.book = book;
        this.reviewerName = reviewerName;
        this.date = date;
        this.content = content;
    }
    @Ignore
    public Review(String book, String reviewerName, String date, String content) {
        this.book = book;
        this.reviewerName = reviewerName;
        this.date = date;
        this.content = content;
    }

    @NonNull
    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(@NonNull String reviewId) {
        this.reviewId = reviewId;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + reviewId + '\'' +
                ", book='" + book + '\'' +
                ", reviewerName='" + reviewerName + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
