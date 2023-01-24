package com.example.bookworld.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookworld.R;
import com.example.bookworld.beans.Book;
import com.example.bookworld.databinding.ItemFavoriteWatchBinding;
import com.example.bookworld.listeners.FavoriteWatchActionListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAndWatchAdapter extends RecyclerView.Adapter<FavoriteAndWatchAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> booksList;
    private final LayoutInflater inflater;
    private final FavoriteWatchActionListener actionListener;

    public FavoriteAndWatchAdapter(Context context, List<Book> booksList,
                                   FavoriteWatchActionListener actionListener) {
        this.context = context;
        this.booksList = booksList;
        this.actionListener = actionListener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FavoriteAndWatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteWatchBinding binding = ItemFavoriteWatchBinding.inflate(inflater, parent, false);
        return new FavoriteAndWatchAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAndWatchAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public void insertItem(Book book) {
        booksList.add(book);
        notifyItemInserted(getItemCount() - 1);
    }

    public void deleteItem(int position) {
        booksList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemFavoriteWatchBinding mBinding;
        private Book book;
        private int mPosition;

        public ViewHolder(@NonNull ItemFavoriteWatchBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.layout.setOnClickListener(this);
            mBinding.buttonDelete.setOnClickListener(this);
        }

        public void setData(int position) {
            try {
                mPosition = position;
                book = booksList.get(mPosition);

                Picasso.with(context).load(book.getImage()).into(mBinding.bookImage);

                mBinding.bookName.setText(book.getName());
                mBinding.bookAuthor.setText("Author: " + book.getAuthor());
                mBinding.bookPrice.setText("Price: " + book.getPrice() + "$");
                mBinding.bookGenre.setText("Genre: " + book.getGenre());

                boolean availability = book.isAvailability();
                if (availability == true) {
                    mBinding.bookAvailability.setText("Available");
                } else {
                    mBinding.bookAvailability.setText("Not Available");
                }
            } catch (Exception ex) {
                Toast.makeText(context, R.string.error_loading_list, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout:
                    actionListener.onItemClicked(book);
                    break;
                case R.id.button_delete:
                    actionListener.onDeleteClicked(book, mPosition);
                    break;
            }
        }
    }
}
