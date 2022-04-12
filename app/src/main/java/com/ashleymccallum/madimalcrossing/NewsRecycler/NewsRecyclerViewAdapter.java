package com.ashleymccallum.madimalcrossing.NewsRecycler;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.pojos.NewsItem;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {
    ArrayList<NewsItem> items;
    Context context;

    public NewsRecyclerViewAdapter(ArrayList<NewsItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.author.setText(item.getAuthorName());
        holder.publisher.setText(item.getPublisherName());
        //todo: parse timestamp
//        Date date = Date.from(Instant.parse(item.getTimestamp()));
        holder.date.setText(item.getTimestamp());
        holder.description.setText(item.getDescription());
        Picasso.get().load(item.getImgURL()).into(holder.image);
        holder.readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(item.getArticleURL()));
                try {
                    context.startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(holder.itemView, context.getString(R.string.ext_app_error), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView publisher;
        TextView author;
        TextView date;
        TextView description;
        ImageView readButton;
        ImageView image;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.newsTitle);
            this.publisher = itemView.findViewById(R.id.newsPublisher);
            this.author = itemView.findViewById(R.id.newsAuthor);
            this.date = itemView.findViewById(R.id.newsDate);
            this.description = itemView.findViewById(R.id.newsDescription);
            this.readButton = itemView.findViewById(R.id.newsBtn);
            this.image = itemView.findViewById(R.id.newsImg);
        }
    }
}
