package com.example.bookworld.listeners;

import com.example.bookworld.beans.Book;

public interface WatchActionListener {

    void onWatchClicked(Book book);

    void onDeleteClicked(Book book, int position);
}
