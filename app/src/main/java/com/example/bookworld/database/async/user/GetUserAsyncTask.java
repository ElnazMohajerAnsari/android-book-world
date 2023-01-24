package com.example.bookworld.database.async.user;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bookworld.R;
import com.example.bookworld.beans.User;
import com.example.bookworld.database.db.DbManager;
import com.example.bookworld.database.db.DbResponse;
import com.example.bookworld.database.db.dao.UserDao;
import com.example.bookworld.utils.Action;

public class GetUserAsyncTask extends AsyncTask<String, Void, User> {

  private Context context;
  private UserDao userDao;
  private final String action;
  private final DbResponse<User> dbResponse;

  public GetUserAsyncTask(Context context, String action, DbResponse<User> dbResponse) {
    this.context = context;
    this.action = action;
    this.dbResponse = dbResponse;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    DbManager dbManager = DbManager.getInstance(context);
    userDao = dbManager.userDao();
  }

  @Override
  protected User doInBackground(String... strings) {
    if (action.equals(Action.BASED_ON_USER_PASS_ACTION)) {
      String username = strings[0];
      String password = strings[1];

      return userDao.getUser(username, password);
    }

    String userId = strings[0];
    return userDao.getUser(userId);
  }

  @Override
  protected void onPostExecute(User user) {
    super.onPostExecute(user);
    if (user != null) {
      dbResponse.onSuccess(user);
    } else {
      Error error = new Error(context.getString(R.string.user_not_exist));
      dbResponse.onError(error);
    }
  }
}
