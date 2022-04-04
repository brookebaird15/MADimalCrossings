package com.ashleymccallum.madimalcrossing.NewsRecycler;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
