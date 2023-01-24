package com.example.bookworld.database.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bookworld.beans.Genre;

import java.util.List;

@Dao
public interface GenreDao {

    @Insert
    long insert(Genre genre);

    @Delete
    int delete(Genre genre);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertList(List<Genre> genres);

    @Query("SELECT * FROM genre")
    List<Genre> getAll();
}
