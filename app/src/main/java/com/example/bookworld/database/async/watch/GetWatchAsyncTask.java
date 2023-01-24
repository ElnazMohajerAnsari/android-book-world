package com.example.bookworld.database.async.watch;

import static com.example.bookworld.utils.Action.GET_WATCH_ACTION;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bookworld.R;
import com.example.bookworld.beans.Book;
import com.example.bookworld.database.db.DbManager;
import com.example.bookworld.database.db.DbResponse;
import com.example.bookworld.database.db.dao.WatchDao;

import java.util.List;

public class GetWatchAsyncTask extends AsyncTask<String, Void, List<Book>> {

    private Context context;
    private WatchDao watchDao;
    private final String action;
    private DbResponse<List<Book>> dbResponse;

    public GetWatchAsyncTask(Context context, String action, DbResponse<List<Book>> dbResponse) {
        this.context = context;
        this.action = action;
        this.dbResponse = dbResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance(context);
        watchDao = dbManager.watchDao();
    }

    @Override
    protected List<Book> doInBackground(String... strings) {
        String input = strings[0];
        if (action.equals(GET_WATCH_ACTION)){
            return watchDao.getUserWithBooks(input);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Book> favoriteLists) {
        super.onPostExecute(favoriteLists);
        if (favoriteLists != null) {
            dbResponse.onSuccess(favoriteLists);
        } else {
            Error error = new Error(context.getString(R.string.something_went_wrong));
            dbResponse.onError(error);
        }
    }
}
