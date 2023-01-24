package com.example.bookworld.listeners;

import com.example.bookworld.beans.Book;

public interface FavoriteWatchActionListener {

    void onItemClicked(Book book);

    void onDeleteClicked(Book book, int position);
}
