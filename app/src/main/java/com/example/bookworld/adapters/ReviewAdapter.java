package com.example.bookworld.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookworld.R;
import com.example.bookworld.beans.Review;
import com.example.bookworld.databinding.ItemReviewBinding;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final Context context;
    private final List<Review> reviewList;
    private final LayoutInflater inflater;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
        inflater = LayoutInflater.from(context);
    }

    public void itemInserted(Review review) {
        reviewList.add(review);
        notifyItemInserted(getItemCount() - 1);
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReviewBinding binding = ItemReviewBinding.inflate(inflater, parent, false);
        return new ReviewAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemReviewBinding mBinding;
        private Review review;
        private int mPosition;

        public ViewHolder(@NonNull ItemReviewBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            //mBinding.layout.setOnClickListener(this);
        }

        public void setData(int position) {
            try {
                mPosition = position;
                review = reviewList.get(mPosition);

                mBinding.reviewerName.setText("Reviewer: " + review.getReviewerName());
                mBinding.reviewDate.setText("Date: " + review.getDate());
                mBinding.reviewContent.setText(review.getContent());

            } catch (Exception ex) {
                Toast.makeText(context, R.string.error_loading_list, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.layout) {

            }
        }
    }
}
