package com.example.bookworld.beans;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"id", "bookId"})
public class UserBookCrossRefWatch {
    @NonNull
    public String id;
    @NonNull
    public String bookId;

    public UserBookCrossRefWatch(@NonNull String id, @NonNull String bookId) {
        this.id = id;
        this.bookId = bookId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getBookId() {
        return bookId;
    }

    public void setBookId(@NonNull String bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "UserBookCrossRef{" +
                "id='" + id + '\'' +
                ", bookId='" + bookId + '\'' +
                '}';
    }
}
