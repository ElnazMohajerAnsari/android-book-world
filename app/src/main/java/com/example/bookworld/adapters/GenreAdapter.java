package com.example.bookworld.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookworld.beans.Genre;
import com.example.bookworld.R;
import com.example.bookworld.listeners.GenreActionListener;
import com.example.bookworld.databinding.ItemGenreBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    private final Context context;
    private final List<Genre> genresList;
    private final LayoutInflater mInflater;
    private final GenreActionListener actionListener;

    public GenreAdapter(Context context, List<Genre> genresList,
                        GenreActionListener actionListener) {
        this.context = context;
        this.genresList = genresList;
        this.actionListener = actionListener;
        mInflater = LayoutInflater.from(context);
    }

    public void itemInserted(Genre genre) {
        genresList.add(genre);
        notifyItemInserted(getItemCount() - 1);
    }

    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGenreBinding binding = ItemGenreBinding.inflate(mInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return genresList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemGenreBinding mBinding;
        private Genre genre;
        private int mPosition;

        public ViewHolder(@NonNull ItemGenreBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.layout.setOnClickListener(this);
        }

        public void setData(int position) {
            try {
                mPosition = position;
                genre = genresList.get(mPosition);

                Picasso.with(context).load(genre.getImage()).into(mBinding.genreImage);
                mBinding.genreName.setText(genre.getName());

            } catch (Exception ex) {
                Toast.makeText(context, R.string.error_loading_list, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.layout)
                actionListener.onGenreClicked(genre);
        }
    }
}
