package com.example.bookworld.database.async.user;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bookworld.R;
import com.example.bookworld.beans.User;
import com.example.bookworld.database.db.DbManager;
import com.example.bookworld.database.db.DbResponse;
import com.example.bookworld.database.db.dao.UserDao;

import java.util.List;

public class GetAllUserAsyncTask extends AsyncTask<Void, Void, List<User>> {

    private Context context;
    private UserDao userDao;
    private DbResponse<List<User>> userDbResponse;

    public GetAllUserAsyncTask(Context context, DbResponse<List<User>> userDbResponse) {
        this.context = context;
        this.userDbResponse = userDbResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance(context);
        userDao = dbManager.userDao();
    }

    @Override
    protected List<User> doInBackground(Void... voids) {
        return userDao.getAll();
    }

    @Override
    protected void onPostExecute(List<User> users) {
        super.onPostExecute(users);
        if (users != null) {
            userDbResponse.onSuccess(users);
        } else {
            Error error = new Error(context.getString(R.string.something_went_wrong));
            userDbResponse.onError(error);
        }
    }
}
