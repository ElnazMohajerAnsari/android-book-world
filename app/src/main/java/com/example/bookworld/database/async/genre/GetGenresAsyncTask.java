package com.example.bookworld.database.async.genre;

import static com.example.bookworld.utils.Action.GET_ALL_ACTION;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bookworld.beans.Genre;
import com.example.bookworld.database.db.dao.GenreDao;
import com.example.bookworld.R;
import com.example.bookworld.database.db.DbManager;
import com.example.bookworld.database.db.DbResponse;

import java.util.List;

public class GetGenresAsyncTask extends AsyncTask<String, Void, List<Genre>> {

  private Context context;
  private GenreDao genreDao;
  private final String action;
  private DbResponse<List<Genre>> dbResponse;

  public GetGenresAsyncTask(Context context, String action, DbResponse<List<Genre>> dbResponse) {
    this.context = context;
    this.action = action;
    this.dbResponse = dbResponse;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    DbManager dbManager = DbManager.getInstance(context);
    genreDao = dbManager.genreDao();
  }

  @Override
  protected List<Genre> doInBackground(String... strings) {
    if (action.equals(GET_ALL_ACTION)){
      return genreDao.getAll();
    }
    return null;
  }

  @Override
  protected void onPostExecute(List<Genre> genres) {
    super.onPostExecute(genres);
    if (genres != null) {
      dbResponse.onSuccess(genres);
    } else {
      Error error = new Error(context.getString(R.string.something_went_wrong));
      dbResponse.onError(error);
    }
  }
}
