package com.example.bookworld.database.async.watch;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bookworld.R;
import com.example.bookworld.beans.UserBookCrossRefWatch;
import com.example.bookworld.database.db.DbManager;
import com.example.bookworld.database.db.dao.BookDao;
import com.example.bookworld.utils.Action;
import com.example.bookworld.utils.Result;
import com.example.bookworld.utils.ResultListener;

public class WatchCudAsyncTask extends AsyncTask<UserBookCrossRefWatch, Void, Result<UserBookCrossRefWatch>> {

    private Context context;
    private final String action;
    private BookDao bookDao;
    private ResultListener<UserBookCrossRefWatch> resultListener;

    public WatchCudAsyncTask(Context context, String action, ResultListener<UserBookCrossRefWatch> resultListener) {
        this.context = context;
        this.action = action;
        this.resultListener = resultListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance(context);
        bookDao = dbManager.bookDao();
    }

    @Override
    protected Result<UserBookCrossRefWatch> doInBackground(UserBookCrossRefWatch... userBookCrossRefs) {
        DbManager db = DbManager.getInstance(context);
        UserBookCrossRefWatch inputUserBookCrossRefs = (userBookCrossRefs.length > 0) ? userBookCrossRefs[0] : null;
        Error error = null;

        try {
            switch (action) {
                case Action.INSERT_ACTION:
                    long iResult = db.watchDao().insert(inputUserBookCrossRefs);
                    error = (iResult < 1) ? new Error(context.getString(R.string.insert_error)) : null;
                    break;
                case Action.DELETE_ACTION:
                    int dResult = db.watchDao().delete(inputUserBookCrossRefs);
                    error = (dResult < 1) ? new Error(context.getString(R.string.delete_error)) : null;
                    break;
                default:
                    error = new Error(context.getString(R.string.db_general_error));
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            error = new Error(context.getString(R.string.db_general_error));
        }
        return new Result<UserBookCrossRefWatch>(inputUserBookCrossRefs, null, error);
    }

    @Override
    protected void onPostExecute(Result<UserBookCrossRefWatch> result) {
        super.onPostExecute(result);
        resultListener.onResult(result);
    }
}
