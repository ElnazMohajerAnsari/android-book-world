package com.example.bookworld.database.async.favorite;

import static com.example.bookworld.utils.Action.GET_ALL_ACTION;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bookworld.R;
import com.example.bookworld.beans.UserBookCrossRef;
import com.example.bookworld.database.db.DbManager;
import com.example.bookworld.database.db.DbResponse;
import com.example.bookworld.database.db.dao.FavoriteDao;

import java.util.List;

public class GetUserBookCrossRefAsyncTask extends AsyncTask<String, Void, List<UserBookCrossRef>> {

    private Context context;
    private FavoriteDao favoriteDao;
    private final String action;
    private DbResponse<List<UserBookCrossRef>> dbResponse;

    public GetUserBookCrossRefAsyncTask(Context context, String action, DbResponse<List<UserBookCrossRef>> dbResponse) {
        this.context = context;
        this.action = action;
        this.dbResponse = dbResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance(context);
        favoriteDao = dbManager.favoriteDao();
    }

    @Override
    protected List<UserBookCrossRef> doInBackground(String... strings) {
        switch (action){
            case GET_ALL_ACTION:
                return favoriteDao.getAll();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<UserBookCrossRef> userBookCrossRefs) {
        super.onPostExecute(userBookCrossRefs);
        if (userBookCrossRefs != null) {
            dbResponse.onSuccess(userBookCrossRefs);
        } else {
            Error error = new Error(context.getString(R.string.something_went_wrong));
            dbResponse.onError(error);
        }
    }
}
