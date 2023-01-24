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
import com.example.bookworld.adapters.BookAdapter;
import com.example.bookworld.beans.Book;
import com.example.bookworld.database.async.book.GetBooksAsyncTask;
import com.example.bookworld.database.db.AppData;
import com.example.bookworld.database.db.DbResponse;
import com.example.bookworld.databinding.ActivitySearchBinding;
import com.example.bookworld.listeners.BookActionListener;
import com.example.bookworld.utils.Action;
import com.example.bookworld.utils.Validator;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements BookActionListener, View.OnClickListener {

    private ActivitySearchBinding binding;
    private BookAdapter adapter;
    private boolean isEmpty1 = false;
    private boolean isEmpty2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);
        setUpClickListener();
    }

    private void searchOnBookName() {
        GetBooksAsyncTask task = new GetBooksAsyncTask(this, Action.GET_NAME_FILTERED_ACTION, new DbResponse<List<Book>>() {
            @Override
            public void onSuccess(List<Book> loadedBooks) {
                if (loadedBooks.isEmpty()) {
                    isEmpty1 = true;
                    binding.recyclerViewName.setVisibility(View.GONE);
                } else {
                    binding.recyclerViewName.setVisibility(View.VISIBLE);
                    isEmpty1 = false;
                    setUpRecyclerViewName(loadedBooks);
                }
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        task.execute(binding.editSearch.getText().toString());
    }

    private void searchOnBookAuthor() {
        GetBooksAsyncTask task = new GetBooksAsyncTask(this, Action.GET_AUTHOR_FILTERED_ACTION, new DbResponse<List<Book>>() {
            @Override
            public void onSuccess(List<Book> loadedBooks) {
                if (loadedBooks.isEmpty()) {
                    isEmpty2 = true;
                    binding.recyclerViewAuthor.setVisibility(View.INVISIBLE);
                } else {
                    binding.recyclerViewAuthor.setVisibility(View.VISIBLE);
                    isEmpty2 = false;
                    setUpRecyclerViewAuthor(loadedBooks);
                }
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        task.execute(binding.editSearch.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpRecyclerViewName(List<Book> books) {
        adapter = new BookAdapter(this, books, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewName.setLayoutManager(linearLayoutManager);
        binding.recyclerViewName.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                linearLayoutManager.getOrientation());
        binding.recyclerViewName.addItemDecoration(dividerItemDecoration);
    }

    private void setUpRecyclerViewAuthor(List<Book> books) {
        adapter = new BookAdapter(this, books, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerViewAuthor.setLayoutManager(linearLayoutManager);
        binding.recyclerViewAuthor.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                linearLayoutManager.getOrientation());
        binding.recyclerViewAuthor.addItemDecoration(dividerItemDecoration);
    }

    private void setUpClickListener() {
        binding.searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_btn)
            onSearchClicked();
    }

    @Override
    public void onBookClicked(Book book) {
        AppData.setCurrentBook(book);
        Intent intent = new Intent(SearchActivity.this, BookDetailsActivity.class);
        startActivity(intent);
    }

    private void onSearchClicked() {
        removeErrors();
        String input = binding.editSearch.getText().toString();
        boolean isValidInput = validateInput(input);
        if (isValidInput) {
            searchOnBookName();
            searchOnBookAuthor();
            if ((isEmpty1 == true) && (isEmpty2 == true)) {
                Toast.makeText(SearchActivity.this, R.string.no_result, Toast.LENGTH_LONG).show();
                isEmpty1 = isEmpty2 = false;
            }
        }
    }

    private void removeErrors() {
        binding.editSearch.setError(null);
    }

    private boolean validateInput(String title) {
        boolean isValid = true;
        if (!Validator.validate(title, Validator.TEXT)) {
            isValid = false;
            binding.editSearch.setError(getString(R.string.error_empty_content));
        }
        return isValid;
    }
}
