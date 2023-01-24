package com.example.bookworld.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bookworld.R;
import com.example.bookworld.adapters.ReviewAdapter;
import com.example.bookworld.beans.Book;
import com.example.bookworld.beans.Review;
import com.example.bookworld.beans.User;
import com.example.bookworld.database.db.AppData;
import com.example.bookworld.databinding.ActivityReviewBinding;
import com.example.bookworld.network.NetworkHelper;
import com.example.bookworld.utils.Constant;
import com.example.bookworld.utils.Result;
import com.example.bookworld.utils.ResultListener;
import com.example.bookworld.utils.Validator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_NEW_REVIEW = 1;
    private ActivityReviewBinding binding;
    private ReviewAdapter adapter;
    private NetworkHelper networkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        networkHelper = NetworkHelper.getInstance(getApplicationContext());

        reloadReviews();
        setUpClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.review_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reload_reviews) {
            reloadReviews();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_REVIEW && resultCode == RESULT_OK) {
            Review review = (Review) data.getSerializableExtra(Constant.KET_REVIEW);
            adapter.itemInserted(review);
        }
    }

    private void reloadReviews() {
        List<Review> temp = new ArrayList<>();
        networkHelper.readReviews(new ResultListener<List<Review>>() {
            @Override
            public void onResult(Result<List<Review>> result) {
                Error error = (result != null) ? result.getError() : null;
                if ((result == null) || (error != null)) {
                    String errorMsg = (error != null) ? error.getMessage() : getString(R.string.error_loading_list);
                    Toast.makeText(ReviewActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    return;
                }
                for (Review review : result.getItem()) {
                    if (review.getBook().equals(AppData.getCurrentBook().getBookId())) {
                        temp.add(review);
                    }
                }
                setUpRecyclerView(temp);
            }
        });
    }

    private void setUpRecyclerView(List<Review> reviews) {
        adapter = new ReviewAdapter(this, reviews);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                linearLayoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
    }


    private void setUpClickListener() {
        binding.buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_save)
            onSaveClicked();
    }

    private void onSaveClicked() {
        removeErrors();
        User currentUser = AppData.getCurrentUser();
        String reviewer = currentUser.getFirstName() + " " + currentUser.getLastName();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String reviewDate = formatter.format(date);
        String content = binding.inputContent.getText().toString();

        boolean isValidInput = validateInput(content);

        if (isValidInput) {
            Review review = new Review(AppData.getCurrentBook().getBookId(), reviewer, reviewDate, content);

            networkHelper.insertReview(review, currentUser, new ResultListener<Review>() {
                @Override
                public void onResult(Result<Review> result) {
                    Error error = (result != null) ? result.getError() : null;
                    Review resultReview = (result != null) ? result.getItem() : null;
                    if ((result == null) || (resultReview == null) || (error != null)) {
                        String errorMsg = (error != null) ? error.getMessage() : getString(R.string.insert_error);
                        Toast.makeText(ReviewActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        return;
                    }
                    review.setReviewId(resultReview.getReviewId());
                    Toast.makeText(ReviewActivity.this, R.string.review_insert_success, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KET_REVIEW, review);
                    setResult(RESULT_OK, intent);
                    binding.inputContent.setText("");
                    reloadReviews();
                    //finish();
                }
            });
        }
    }

    private void removeErrors() {
        binding.inputContent.setError(null);
    }

    private boolean validateInput(String title) {
        boolean isValid = true;

        if (!Validator.validate(title, Validator.TEXT)) {
            isValid = false;
            binding.inputContent.setError(getString(R.string.error_empty_content));
        }
        return isValid;
    }

}
