package com.example.bookworld.database.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bookworld.beans.User;

import java.util.List;

@Dao
public interface UserDao {

  @Insert (onConflict = OnConflictStrategy.REPLACE)
  long insert(User user);

  @Query("SELECT * FROM user WHERE username = :username AND password = :password")
  User getUser(String username, String password);

  @Query("SELECT * FROM user WHERE id = :userId")
  User getUser(String userId);

  @Query("SELECT * FROM user")
  List<User> getAll();
}
