package com.example.bookworld.database.async.book;

import static com.example.bookworld.utils.Action.GET_ALL_ACTION;
import static com.example.bookworld.utils.Action.GET_AUTHOR_FILTERED_ACTION;
import static com.example.bookworld.utils.Action.GET_GENRE_FILTERED_ACTION;
import static com.example.bookworld.utils.Action.GET_NAME_FILTERED_ACTION;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bookworld.R;
import com.example.bookworld.beans.Book;
import com.example.bookworld.database.db.DbManager;
import com.example.bookworld.database.db.DbResponse;
import com.example.bookworld.database.db.dao.BookDao;

import java.util.List;

public class GetBooksAsyncTask extends AsyncTask<String, Void, List<Book>> {

    private Context context;
    private BookDao bookDao;
    private final String action;
    private DbResponse<List<Book>> dbResponse;

    public GetBooksAsyncTask(Context context, String action, DbResponse<List<Book>> dbResponse) {
        this.context = context;
        this.action = action;
        this.dbResponse = dbResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance(context);
        bookDao = dbManager.bookDao();
    }

    @Override
    protected List<Book> doInBackground(String... strings) {
        String input = strings[0];
        switch (action){
            case GET_ALL_ACTION:
                return bookDao.getAll();
            case GET_GENRE_FILTERED_ACTION:
                return bookDao.getBooksFilteredByGenre(input);
            case GET_NAME_FILTERED_ACTION:
                return bookDao.getBooksFilteredByName(input);
            case GET_AUTHOR_FILTERED_ACTION:
                return bookDao.getBooksFilteredByAuthor(input);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Book> books) {
        super.onPostExecute(books);
        if (books != null) {
            dbResponse.onSuccess(books);
        } else {
            Error error = new Error(context.getString(R.string.something_went_wrong));
            dbResponse.onError(error);
        }
    }
}
