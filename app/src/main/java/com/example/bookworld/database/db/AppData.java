package com.example.bookworld.database.db;

import android.app.Application;
import android.util.Log;

import com.example.bookworld.beans.Book;
import com.example.bookworld.beans.Genre;
import com.example.bookworld.beans.User;

public class AppData extends Application {

    private static final String TAG = "AppData";
    private static User currentUser = null;
    private static Genre currentGenre = null;
    private static Book currentBook = null;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
        Log.d(TAG, "Current user set to: " + currentUser);
    }

    public static Genre getCurrentGenre() {
        return currentGenre;
    }

    public static void setCurrentGenre(Genre genre) {
        currentGenre = genre;
        Log.d(TAG, "Current genre set to: " + currentGenre);
    }

    public static Book getCurrentBook() {
        return currentBook;
    }

    public static void setCurrentBook(Book book) {
        currentBook = book;
        Log.d(TAG, "Current book set to: " + currentBook);
    }
}
