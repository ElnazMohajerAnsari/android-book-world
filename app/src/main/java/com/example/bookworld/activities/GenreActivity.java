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
import com.example.bookworld.adapters.GenreAdapter;
import com.example.bookworld.beans.Genre;
import com.example.bookworld.beans.User;
import com.example.bookworld.database.async.genre.GetGenresAsyncTask;
import com.example.bookworld.database.async.genre.GenreCudAsyncTask;
import com.example.bookworld.database.db.AppData;
import com.example.bookworld.database.db.DbResponse;
import com.example.bookworld.databinding.ActivityGenreBinding;
import com.example.bookworld.listeners.GenreActionListener;
import com.example.bookworld.network.NetworkHelper;
import com.example.bookworld.prefrences.PreferencesManager;
import com.example.bookworld.utils.Action;
import com.example.bookworld.utils.Result;
import com.example.bookworld.utils.ResultListener;

import java.util.List;

public class GenreActivity extends AppCompatActivity implements GenreActionListener, View.OnClickListener {

    private ActivityGenreBinding binding;
    private GenreAdapter adapter;
    private NetworkHelper networkHelper;
    private PreferencesManager preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGenreBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        networkHelper = NetworkHelper.getInstance(getApplicationContext());
        preferences = PreferencesManager.getInstance(this);
        loadAndShowGenres();
        setListeners();
    }

    private void loadAndShowGenres() {
        GetGenresAsyncTask task = new GetGenresAsyncTask(this, Action.GET_ALL_ACTION,
                new DbResponse<List<Genre>>() {
                    @Override
                    public void onSuccess(List<Genre> genres) {
                        if (genres.isEmpty() == false) {
                            setUpRecyclerView(genres);
                        } else {
                            reloadGenres();
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(GenreActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.genre_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reload_genres) {
            reloadGenres();
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            search();
        } else if (item.getItemId() == R.id.action_logout) {
            logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }

    private void search() {
        Intent intent = new Intent(GenreActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    private void logoutUser() {
        User currentUser = AppData.getCurrentUser();
        preferences.put(PreferencesManager.PREF_KEY_LOGIN_STATUS, false);
        Intent intent = new Intent(GenreActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void emptyGenre() {
        GetGenresAsyncTask task = new GetGenresAsyncTask(this, Action.GET_ALL_ACTION,
                new DbResponse<List<Genre>>() {
                    @Override
                    public void onSuccess(List<Genre> genres) {
                        if (genres.isEmpty() == false) {
                            for (Genre genre : genres) {
                                GenreCudAsyncTask task2 = new GenreCudAsyncTask(getApplicationContext(), Action.DELETE_ACTION, new ResultListener<Genre>() {
                                    @Override
                                    public void onResult(Result<Genre> response) {
                                        Error error = (response != null) ? response.getError() : null;
                                        if ((response == null) || (error != null)) {
                                            String errorMsg = (error != null) ? error.getMessage() : getString(R.string.delete_error);
                                            Toast.makeText(GenreActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                });
                                task2.execute(genre);
                            }
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(GenreActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        task.execute();
    }

    private void reloadGenres() {
        emptyGenre();
        networkHelper.readGenres(new ResultListener<List<Genre>>() {
            @Override
            public void onResult(Result<List<Genre>> result) {
                Error error = (result != null) ? result.getError() : null;
                if ((result == null) || (error != null)) {
                    String errorMsg = (error != null) ? error.getMessage() : getString(R.string.error_loading_list);
                    Toast.makeText(GenreActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    return;
                }

                GenreCudAsyncTask task = new GenreCudAsyncTask(getApplicationContext(), Action.INSERT_LIST_ACTION, new ResultListener<Genre>() {
                    @Override
                    public void onResult(Result<Genre> response) {
                        Error error = (response != null) ? response.getError() : null;
                        if ((response == null) || (error != null)) {
                            String errorMsg = (error != null) ? error.getMessage() : getString(R.string.list_insert_error);
                            Toast.makeText(GenreActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(GenreActivity.this, getString(R.string.genres_reload_success), Toast.LENGTH_LONG).show();
                        loadAndShowGenres();
                    }
                });
                task.execute(result.getItem().toArray(new Genre[result.getItem().size()]));
            }
        });
    }

    @Override
    public void onGenreClicked(Genre genre) {
        AppData.setCurrentGenre(genre);
        Intent intent = new Intent(GenreActivity.this, BookActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpRecyclerView(List<Genre> genres) {
        adapter = new GenreAdapter(this, genres, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerGenre.setLayoutManager(linearLayoutManager);
        binding.recyclerGenre.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                linearLayoutManager.getOrientation());
        binding.recyclerGenre.addItemDecoration(dividerItemDecoration);
    }

    private void setListeners() {
        binding.favoriteListBtn.setOnClickListener(this);
        binding.watchListBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.favorite_list_btn:
                Intent intent = new Intent(GenreActivity.this, FavoriteListActivity.class);
                startActivity(intent);
                break;
            case R.id.watch_list_btn:
                Intent intent2 = new Intent(GenreActivity.this, WatchListActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
