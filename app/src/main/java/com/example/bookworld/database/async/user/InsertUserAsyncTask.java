package com.example.bookworld.database.async.user;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bookworld.R;
import com.example.bookworld.beans.User;
import com.example.bookworld.database.db.DbManager;
import com.example.bookworld.utils.Result;
import com.example.bookworld.utils.ResultListener;

public class InsertUserAsyncTask extends AsyncTask<User, Void, Result<User>> {

    private Context context;
    private ResultListener<User> resultListener;

    public InsertUserAsyncTask(Context context, ResultListener<User> userResultListener) {
        this.context = context;
        this.resultListener = userResultListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Result<User> doInBackground(User... inputUsers) {
        DbManager db = DbManager.getInstance(context);
        User inputUser = (inputUsers.length > 0) ? inputUsers[0] : null;
        Error error = null;

        try {
            long iResult = db.userDao().insert(inputUser);
            error = (iResult < 1) ? new Error(context.getString(R.string.insert_error)) : null;
        } catch (Exception ex) {
            ex.printStackTrace();
            error = new Error(context.getString(R.string.db_general_error));
        }

        return new Result<User>(inputUser, null, error);
    }

    @Override
    protected void onPostExecute(Result<User> result) {
        super.onPostExecute(result);
        resultListener.onResult(result);
    }
}
