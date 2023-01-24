package com.example.bookworld.database.async.book;

import android.content.Context;
import android.os.AsyncTask;

import com.example.bookworld.R;
import com.example.bookworld.beans.Book;
import com.example.bookworld.database.db.DbManager;
import com.example.bookworld.database.db.dao.BookDao;
import com.example.bookworld.utils.Action;
import com.example.bookworld.utils.Result;
import com.example.bookworld.utils.ResultListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookCudAsyncTask extends AsyncTask<Book, Void, Result<Book>> {

    private Context context;
    private final String action;
    private BookDao bookDao;
    private ResultListener<Book> resultListener;

    public BookCudAsyncTask(Context context, String action, ResultListener<Book> resultListener) {
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
    protected Result<Book> doInBackground(Book... books) {
        DbManager db = DbManager.getInstance(context);
        Book inputBook = (books.length > 0) ? books[0] : null;
        List<Book> bookList = null;
        Error error = null;

        try {
            switch (action) {
                case Action.GET_ALL_ACTION:
                    bookList = db.bookDao().getAll();
                    error = (bookList == null) ? new Error(context.getString(R.string.get_all_error)) : null;
                    break;
                case Action.INSERT_ACTION:
                    long iResult = db.bookDao().insert(inputBook);
                    error = (iResult < 1) ? new Error(context.getString(R.string.insert_error)) : null;
                    break;
                case Action.DELETE_ACTION:
                    int dResult = db.bookDao().delete(inputBook);
                    error = (dResult < 1) ? new Error(context.getString(R.string.delete_error)) : null;
                    break;
                case Action.INSERT_LIST_ACTION:
                    bookList = new ArrayList<>(Arrays.asList(books));
                    List<Long> lResult = bookDao.insertList(bookList);
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

        return new Result<Book>(inputBook, bookList, error);
    }

    @Override
    protected void onPostExecute(Result<Book> result) {
        super.onPostExecute(result);
        resultListener.onResult(result);
    }
}
