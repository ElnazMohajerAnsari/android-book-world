package com.example.bookworld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookworld.R;
import com.example.bookworld.adapters.FavoriteAndWatchAdapter;
import com.example.bookworld.beans.Book;
import com.example.bookworld.beans.UserBookCrossRefWatch;
import com.example.bookworld.database.async.watch.GetWatchAsyncTask;
import com.example.bookworld.database.async.watch.WatchCudAsyncTask;
import com.example.bookworld.database.db.AppData;
import com.example.bookworld.database.db.DbResponse;
import com.example.bookworld.databinding.ActivityWatchBinding;
import com.example.bookworld.listeners.FavoriteWatchActionListener;
import com.example.bookworld.utils.Action;
import com.example.bookworld.utils.Result;
import com.example.bookworld.utils.ResultListener;

import java.util.List;

public class WatchListActivity extends AppCompatActivity implements FavoriteWatchActionListener {

    private ActivityWatchBinding binding;
    private FavoriteAndWatchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        loadAndShowWatchBooks();
    }

    private void loadAndShowWatchBooks() {
        GetWatchAsyncTask task = new GetWatchAsyncTask(this, Action.GET_WATCH_ACTION,
                new DbResponse<List<Book>>() {
                    @Override
                    public void onSuccess(List<Book> books) {
                        setUpRecyclerView(books);
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(WatchListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        task.execute(AppData.getCurrentUser().getId());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpRecyclerView(List<Book> books) {
        adapter = new FavoriteAndWatchAdapter(this, books, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerBook.setLayoutManager(linearLayoutManager);
        binding.recyclerBook.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                linearLayoutManager.getOrientation());
        binding.recyclerBook.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onItemClicked(Book book) {
        AppData.setCurrentBook(book);
        Intent intent = new Intent(WatchListActivity.this, BookDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(Book book, int position) {
        UserBookCrossRefWatch userBookCrossRef = new UserBookCrossRefWatch(AppData.getCurrentUser().getId(), book.getBookId());
        WatchCudAsyncTask task = new WatchCudAsyncTask(getApplicationContext(), Action.DELETE_ACTION, new ResultListener<UserBookCrossRefWatch>() {
            @Override
            public void onResult(Result<UserBookCrossRefWatch> response) {
                Error error = (response != null) ? response.getError() : null;
                if ((response == null) || (error != null)) {
                    String errorMsg = (error != null) ? error.getMessage() : getString(R.string.delete_error);
                    Toast.makeText(WatchListActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    return;
                }
                adapter.deleteItem(position);
                Toast.makeText(WatchListActivity.this, getString(R.string.message_successful_remove), Toast.LENGTH_LONG).show();
            }
        });
        task.execute(userBookCrossRef);
    }
}
