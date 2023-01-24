package com.example.bookworld.database.async.genre;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bookworld.R;
import com.example.bookworld.beans.Genre;
import com.example.bookworld.database.db.DbManager;
import com.example.bookworld.database.db.dao.GenreDao;
import com.example.bookworld.utils.Action;
import com.example.bookworld.utils.Result;
import com.example.bookworld.utils.ResultListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenreCudAsyncTask extends AsyncTask<Genre, Void, Result<Genre>> {

    private Context context;
    private final String action;
    private GenreDao genreDao;
    private ResultListener<Genre> resultListener;

    public GenreCudAsyncTask(Context context, String action, ResultListener<Genre> resultListener) {
        this.context = context;
        this.action = action;
        this.resultListener = resultListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance(context);
        genreDao = dbManager.genreDao();
    }

    @Override
    protected Result<Genre> doInBackground(Genre... genres) {
        DbManager db = DbManager.getInstance(context);
        Genre inputGenre = (genres.length > 0) ? genres[0] : null;
        List<Genre> genreList = null;
        Error error = null;

        try {
            switch (action) {
                case Action.GET_ALL_ACTION:
                    genreList = db.genreDao().getAll();
                    error = (genreList == null) ? new Error(context.getString(R.string.get_all_error)) : null;
                    break;
                case Action.INSERT_ACTION:
                    long iResult = db.genreDao().insert(inputGenre);
                    error = (iResult < 1) ? new Error(context.getString(R.string.insert_error)) : null;
                    break;
                case Action.DELETE_ACTION:
                    int dResult = db.genreDao().delete(inputGenre);
                    error = (dResult < 1) ? new Error(context.getString(R.string.delete_error)) : null;
                    break;
                case Action.INSERT_LIST_ACTION:
                    genreList = new ArrayList<>(Arrays.asList(genres));
                    List<Long> lResult = genreDao.insertList(genreList);
                    error = (lResult == null) ? new Error(context.getString(R.string.list_insert_error)) : null;
                    break;
                default:
                    error = new Error(context.getString(R.string.db_general_error));
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            error = new Error(context.getString(R.string.db_general_error));
        }

        return new Result<Genre>(inputGenre, genreList, error);
    }

    @Override
    protected void onPostExecute(Result<Genre> result) {
        super.onPostExecute(result);
        resultListener.onResult(result);
    }
}