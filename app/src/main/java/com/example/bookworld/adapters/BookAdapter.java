package com.example.bookworld.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookworld.beans.Book;
import com.example.bookworld.listeners.BookActionListener;
import com.example.bookworld.R;
import com.example.bookworld.databinding.ItemBookBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> booksList;
    private final LayoutInflater inflater;
    private final BookActionListener actionListener;

    public BookAdapter(Context context, List<Book> booksList,
                       BookActionListener actionListener) {
        this.context = context;
        this.booksList = booksList;
        this.actionListener = actionListener;
        inflater = LayoutInflater.from(context);
    }

    public void itemInserted(Book book) {
        booksList.add(book);
        notifyItemInserted(getItemCount() - 1);
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookBinding binding = ItemBookBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemBookBinding mBinding;
        private Book book;
        private int mPosition;

        public ViewHolder(@NonNull ItemBookBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.layout.setOnClickListener(this);
        }

        public void setData(int position) {
            try {
                mPosition = position;
                book = booksList.get(mPosition);

                Picasso.with(context).load(book.getImage()).into(mBinding.bookImage);

                mBinding.bookName.setText(book.getName());
                mBinding.bookAuthor.setText("Author: " + book.getAuthor());
                mBinding.bookPrice.setText("Price: " + book.getPrice() + "$");

            } catch (Exception ex) {
                Toast.makeText(context, R.string.error_loading_list, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.layout)
                actionListener.onBookClicked(book);
        }
    }
}
