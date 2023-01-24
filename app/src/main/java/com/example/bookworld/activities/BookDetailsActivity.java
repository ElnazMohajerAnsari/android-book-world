package com.example.bookworld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookworld.R;
import com.example.bookworld.beans.Book;
import com.example.bookworld.beans.UserBookCrossRef;
import com.example.bookworld.beans.UserBookCrossRefWatch;
import com.example.bookworld.database.async.favorite.GetUserBookCrossRefAsyncTask;
import com.example.bookworld.database.async.favorite.FavoriteCudAsyncTask;
import com.example.bookworld.database.async.watch.GetUserBookCrossRefWatchAsyncTask;
import com.example.bookworld.database.async.watch.WatchCudAsyncTask;
import com.example.bookworld.database.db.AppData;
import com.example.bookworld.database.db.DbResponse;
import com.example.bookworld.databinding.ActivityBookDetailsBinding;
import com.example.bookworld.utils.Action;
import com.example.bookworld.utils.Result;
import com.example.bookworld.utils.ResultListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityBookDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initialize();
    }

    private void initialize() {
        setData();
        setListeners();
        checkFavorite();
        checkWatch();
    }

    private void setListeners() {
        binding.buttonAddToWatchList.setOnClickListener(this);
        binding.buttonAddToFavoriteList.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_to_favorite_list:
                if (binding.buttonAddToFavoriteList.getText().equals(getString(R.string.add_to_favorites))) {
                    addToFavorites();
                    checkFavorite();
                } else if (binding.buttonAddToFavoriteList.getText().equals(getString(R.string.remove_from_favorites))) {
                    removeFavorite();
                    checkFavorite();
                }
                break;
            case R.id.button_add_to_watch_list:
                if (binding.buttonAddToWatchList.getText().equals(getString(R.string.add_to_watch_list))) {
                    addToWatches();
                    checkWatch();
                } else if (binding.buttonAddToWatchList.getText().equals(getString(R.string.remove_from_watch_list))) {
                    removeWatch();
                    checkWatch();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.book_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_view_reviews) {
            onViewReviewsClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onViewReviewsClicked() {
        Intent intent = new Intent(BookDetailsActivity.this, ReviewActivity.class);
        startActivity(intent);
    }

    private void checkFavorite() {
        GetUserBookCrossRefAsyncTask task = new GetUserBookCrossRefAsyncTask(this, Action.GET_ALL_ACTION,
                new DbResponse<List<UserBookCrossRef>>() {
                    @Override
                    public void onSuccess(List<UserBookCrossRef> userBookCrossRefList) {
                        if (!userBookCrossRefList.isEmpty()) {
                            for (UserBookCrossRef userBookCrossRef : userBookCrossRefList) {
                                if ((userBookCrossRef.getId().equals(AppData.getCurrentUser().getId())) && (userBookCrossRef.getBookId().equals(AppData.getCurrentBook().getBookId()))) {
                                    binding.buttonAddToFavoriteList.setText(getString(R.string.remove_from_favorites));
                                } else {
                                    binding.buttonAddToFavoriteList.setText(getString(R.string.add_to_favorites));
                                }
                            }
                        } else {
                            binding.buttonAddToFavoriteList.setText(getString(R.string.add_to_favorites));
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(BookDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        task.execute();
    }

    private void checkWatch() {
        GetUserBookCrossRefWatchAsyncTask task = new GetUserBookCrossRefWatchAsyncTask(this, Action.GET_ALL_ACTION,
                new DbResponse<List<UserBookCrossRefWatch>>() {
                    @Override
                    public void onSuccess(List<UserBookCrossRefWatch> userBookCrossRefList) {
                        if (!userBookCrossRefList.isEmpty()) {
                            for (UserBookCrossRefWatch userBookCrossRef : userBookCrossRefList) {
                                if ((userBookCrossRef.getId().equals(AppData.getCurrentUser().getId())) && (userBookCrossRef.getBookId().equals(AppData.getCurrentBook().getBookId()))) {
                                    binding.buttonAddToWatchList.setText(getString(R.string.remove_from_watch_list));
                                } else {
                                    binding.buttonAddToWatchList.setText(getString(R.string.add_to_watch_list));
                                }
                            }
                        } else {
                            binding.buttonAddToWatchList.setText(getString(R.string.add_to_watch_list));
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(BookDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        task.execute();
    }

    public void removeFavorite() {
        UserBookCrossRef userBookCrossRef = new UserBookCrossRef(AppData.getCurrentUser().getId(), AppData.getCurrentBook().getBookId());
        FavoriteCudAsyncTask task = new FavoriteCudAsyncTask(getApplicationContext(), Action.DELETE_ACTION, new ResultListener<UserBookCrossRef>() {
            @Override
            public void onResult(Result<UserBookCrossRef> response) {
                Error error = (response != null) ? response.getError() : null;
                if ((response == null) || (error != null)) {
                    String errorMsg = (error != null) ? error.getMessage() : getString(R.string.delete_error);
                    Toast.makeText(BookDetailsActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(BookDetailsActivity.this, getString(R.string.message_successful_remove), Toast.LENGTH_LONG).show();
            }
        });
        task.execute(userBookCrossRef);
    }

    private void addToFavorites() {
        UserBookCrossRef userBookCrossRef = new UserBookCrossRef(AppData.getCurrentUser().getId(), AppData.getCurrentBook().getBookId());
        FavoriteCudAsyncTask task = new FavoriteCudAsyncTask(getApplicationContext(), Action.INSERT_ACTION, new ResultListener<UserBookCrossRef>() {
            @Override
            public void onResult(Result<UserBookCrossRef> response) {
                Error error = (response != null) ? response.getError() : null;
                if ((response == null) || (error != null)) {
                    String errorMsg = (error != null) ? error.getMessage() : getString(R.string.item_insert_error);
                    Toast.makeText(BookDetailsActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(BookDetailsActivity.this, getString(R.string.item_insert_success), Toast.LENGTH_LONG).show();
            }
        });
        task.execute(userBookCrossRef);
    }

    private void addToWatches() {
        UserBookCrossRefWatch userBookCrossRef = new UserBookCrossRefWatch(AppData.getCurrentUser().getId(), AppData.getCurrentBook().getBookId());
        WatchCudAsyncTask task = new WatchCudAsyncTask(getApplicationContext(), Action.INSERT_ACTION, new ResultListener<UserBookCrossRefWatch>() {
            @Override
            public void onResult(Result<UserBookCrossRefWatch> response) {
                Error error = (response != null) ? response.getError() : null;
                if ((response == null) || (error != null)) {
                    String errorMsg = (error != null) ? error.getMessage() : getString(R.string.item_insert_error);
                    Toast.makeText(BookDetailsActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(BookDetailsActivity.this, getString(R.string.item_insert_success), Toast.LENGTH_LONG).show();
            }
        });
        task.execute(userBookCrossRef);
    }

    public void removeWatch() {
        UserBookCrossRefWatch userBookCrossRef = new UserBookCrossRefWatch(AppData.getCurrentUser().getId(), AppData.getCurrentBook().getBookId());
        WatchCudAsyncTask task = new WatchCudAsyncTask(getApplicationContext(), Action.DELETE_ACTION, new ResultListener<UserBookCrossRefWatch>() {
            @Override
            public void onResult(Result<UserBookCrossRefWatch> response) {
                Error error = (response != null) ? response.getError() : null;
                if ((response == null) || (error != null)) {
                    String errorMsg = (error != null) ? error.getMessage() : getString(R.string.delete_error);
                    Toast.makeText(BookDetailsActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(BookDetailsActivity.this, getString(R.string.message_successful_remove), Toast.LENGTH_LONG).show();
            }
        });
        task.execute(userBookCrossRef);
    }

    private void setData() {
        Book book = AppData.getCurrentBook();
        Picasso.with(BookDetailsActivity.this).load(book.getImage()).into(binding.bookImage);
        binding.bookName.setText(book.getName());
        binding.bookAuthor.setText(book.getAuthor());
        binding.bookGenre.setText(book.getGenre());
        binding.bookPrice.setText(book.getPrice() + "$");
        binding.bookRating.setText(book.getRating());
        binding.bookDescription.setText(book.getDescription());
        if (book.isAvailability() == true) {
            binding.bookAvailability.setText(getString(R.string.book_available));
            binding.buttonAddToWatchList.setVisibility(View.INVISIBLE);
        } else {
            binding.bookAvailability.setText(getString(R.string.book_not_available));
            binding.buttonAddToWatchList.setVisibility(View.VISIBLE);
        }
    }
}
