package com.example.bookworld.database.async.watch;

import static com.example.bookworld.utils.Action.GET_ALL_ACTION;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bookworld.R;
import com.example.bookworld.beans.UserBookCrossRef;
import com.example.bookworld.beans.UserBookCrossRefWatch;
import com.example.bookworld.database.db.DbManager;
import com.example.bookworld.database.db.DbResponse;
import com.example.bookworld.database.db.dao.FavoriteDao;
import com.example.bookworld.database.db.dao.WatchDao;

import java.util.List;

public class GetUserBookCrossRefWatchAsyncTask extends AsyncTask<String, Void, List<UserBookCrossRefWatch>> {

    private Context context;
    private WatchDao watchDao;
    private final String action;
    private DbResponse<List<UserBookCrossRefWatch>> dbResponse;

    public GetUserBookCrossRefWatchAsyncTask(Context context, String action, DbResponse<List<UserBookCrossRefWatch>> dbResponse) {
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
    protected List<UserBookCrossRefWatch> doInBackground(String... strings) {
        switch (action){
            case GET_ALL_ACTION:
                return watchDao.getAll();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<UserBookCrossRefWatch> userBookCrossRefs) {
        super.onPostExecute(userBookCrossRefs);
        if (userBookCrossRefs != null) {
            dbResponse.onSuccess(userBookCrossRefs);
        } else {
            Error error = new Error(context.getString(R.string.something_went_wrong));
            dbResponse.onError(error);
        }
    }
}
