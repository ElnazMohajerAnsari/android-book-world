package com.example.bookworld.database.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookworld.beans.Book;
import com.example.bookworld.beans.Genre;

import java.util.List;

@Dao
public interface BookDao {

    @Insert
    long insert(Book book);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertList(List<Book> books);

    @Delete
    int delete(Book book);

    @Query("SELECT * FROM book")
    List<Book> getAll();

    @Query("SELECT * FROM book WHERE genre = :genre")
    List<Book> getBooksFilteredByGenre(String genre);

    @Query("SELECT * FROM book WHERE name = :name")
    List<Book> getBooksFilteredByName(String name);

    @Query("SELECT * FROM book WHERE author = :author")
    List<Book> getBooksFilteredByAuthor(String author);
}
