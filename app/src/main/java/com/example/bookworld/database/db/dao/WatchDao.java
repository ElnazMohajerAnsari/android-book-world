package com.example.bookworld.database.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bookworld.beans.Book;
import com.example.bookworld.beans.UserBookCrossRefWatch;

import java.util.List;

@Dao
public interface WatchDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserBookCrossRefWatch userBookCrossRef);

    @Delete
    int delete(UserBookCrossRefWatch userBookCrossRef);

    @Query("SELECT * FROM book JOIN userBookCrossRefWatch ON book.bookId = userBookCrossRefWatch.bookId WHERE id = :id")
    List<Book> getUserWithBooks(String id);

    @Query("SELECT * FROM userBookCrossRefWatch")
    List<UserBookCrossRefWatch> getAll();

}
