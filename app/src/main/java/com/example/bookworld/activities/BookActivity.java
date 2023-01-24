package com.example.bookworld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookworld.R;
import com.example.bookworld.adapters.BookAdapter;
import com.example.bookworld.beans.Book;
import com.example.bookworld.beans.Genre;
import com.example.bookworld.database.async.book.GetBooksAsyncTask;
import com.example.bookworld.database.async.book.BookCudAsyncTask;
import com.example.bookworld.database.async.genre.GenreCudAsyncTask;
import com.example.bookworld.database.async.genre.GetGenresAsyncTask;
import com.example.bookworld.database.db.AppData;
import com.example.bookworld.database.db.DbResponse;
import com.example.bookworld.databinding.ActivityBookBinding;
import com.example.bookworld.listeners.BookActionListener;
import com.example.bookworld.network.NetworkHelper;
import com.example.bookworld.utils.Action;
import com.example.bookworld.utils.Result;
import com.example.bookworld.utils.ResultListener;

import java.util.List;

public class BookActivity extends AppCompatActivity implements BookActionListener {

    private ActivityBookBinding binding;
    private BookAdapter adapter;
    private NetworkHelper networkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        networkHelper = NetworkHelper.getInstance(getApplicationContext());
        loadAndShowBooks();
    }

    private void loadAndShowBooks() {
        GetBooksAsyncTask task = new GetBooksAsyncTask(this, Action.GET_GENRE_FILTERED_ACTION,
                new DbResponse<List<Book>>() {
                    @Override
                    public void onSuccess(List<Book> books) {
                        if (books.isEmpty() == false) {
                            setUpRecyclerView(books);
                        } else {
                            reloadBooks();
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(BookActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        task.execute(AppData.getCurrentGenre().getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.book_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reload_books) {
            reloadBooks();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void emptyBook() {
        GetBooksAsyncTask task = new GetBooksAsyncTask(this, Action.GET_GENRE_FILTERED_ACTION,
                new DbResponse<List<Book>>() {
                    @Override
                    public void onSuccess(List<Book> books) {
                        if (books.isEmpty() == false) {
                            for (Book book : books) {
                                BookCudAsyncTask task2 = new BookCudAsyncTask(getApplicationContext(), Action.DELETE_ACTION, new ResultListener<Book>() {
                                    @Override
                                    public void onResult(Result<Book> response) {
                                        Error error = (response != null) ? response.getError() : null;
                                        if ((response == null) || (error != null)) {
                                            String errorMsg = (error != null) ? error.getMessage() : getString(R.string.delete_error);
                                            Toast.makeText(BookActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                });
                                task2.execute(book);
                            }
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(BookActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        task.execute(AppData.getCurrentGenre().getName());
    }

    private void reloadBooks() {
        emptyBook();
        networkHelper.readBooks(new ResultListener<List<Book>>() {
            @Override
            public void onResult(Result<List<Book>> result) {
                Error error = (result != null) ? result.getError() : null;
                if ((result == null) || (error != null)) {
                    String errorMsg = (error != null) ? error.getMessage() : getString(R.string.error_loading_list);
                    Toast.makeText(BookActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    return;
                }
                BookCudAsyncTask task = new BookCudAsyncTask(getApplicationContext(), Action.INSERT_LIST_ACTION, new ResultListener<Book>() {
                    @Override
                    public void onResult(Result<Book> response) {
                        Error error = (response != null) ? response.getError() : null;
                        if ((response == null) || (error != null)) {
                            String errorMsg = (error != null) ? error.getMessage() : getString(R.string.list_insert_error);
                            Toast.makeText(BookActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            return;
                        }
                        loadAndShowBooks();
                        Toast.makeText(BookActivity.this, getString(R.string.books_reload_success), Toast.LENGTH_LONG).show();
                    }
                });
                task.execute(result.getItem().toArray(new Book[result.getItem().size()]));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpRecyclerView(List<Book> books) {
        adapter = new BookAdapter(this, books, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerBook.setLayoutManager(linearLayoutManager);
        binding.recyclerBook.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                linearLayoutManager.getOrientation());
        binding.recyclerBook.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onBookClicked(Book book) {
        AppData.setCurrentBook(book);
        Intent intent = new Intent(BookActivity.this, BookDetailsActivity.class);
        startActivity(intent);
    }
}
