package com.example.bookworld.database.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bookworld.beans.Book;
import com.example.bookworld.beans.Genre;
import com.example.bookworld.beans.User;
import com.example.bookworld.beans.UserBookCrossRef;
import com.example.bookworld.beans.UserBookCrossRefWatch;
import com.example.bookworld.database.db.dao.BookDao;
import com.example.bookworld.database.db.dao.FavoriteDao;
import com.example.bookworld.database.db.dao.GenreDao;
import com.example.bookworld.database.db.dao.UserDao;
import com.example.bookworld.database.db.dao.WatchDao;

@Database(entities = {User.class, Genre.class, Book.class, UserBookCrossRef.class, UserBookCrossRefWatch.class}, version = 1)
public abstract class DbManager extends RoomDatabase {

  private static final String DB_NAME = "BookWorld";
  private static DbManager dbManager;

  public abstract UserDao userDao();

  public abstract BookDao bookDao();

  public abstract GenreDao genreDao();

  public abstract FavoriteDao favoriteDao();

  public abstract WatchDao watchDao();

  public static DbManager getInstance(Context context) {
    if (dbManager == null) {
      dbManager = Room.databaseBuilder(context, DbManager.class, DB_NAME)
          .fallbackToDestructiveMigration()
          .build();
    }
    return dbManager;
  }
}
