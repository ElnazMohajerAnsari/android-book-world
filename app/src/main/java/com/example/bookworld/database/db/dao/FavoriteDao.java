package com.example.bookworld.database.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bookworld.beans.Book;
import com.example.bookworld.beans.UserBookCrossRef;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserBookCrossRef userBookCrossRef);

    @Delete
    int delete(UserBookCrossRef userBookCrossRef);

    //@Query("SELECT * FROM book JOIN userbookcrossref ON book.bookId = userbookcrossref.bookId JOIN user ON userbookcrossref.id = user.id WHERE user.id = :id")
    @Query("SELECT * FROM book JOIN userBookCrossRef ON book.bookId = userBookCrossRef.bookId WHERE id = :id")
    List<Book> getUserWithBooks(String id);

    @Query("SELECT * FROM userbookcrossref")
    List<UserBookCrossRef> getAll();
}
