package com.example.bookworld.database.db;

public interface DbResponse<T> {

  void onSuccess(T t);

  void onError(Error error);
}
