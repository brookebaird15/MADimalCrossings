package com.ashleymccallum.madimalcrossing.SongRecycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.pojos.Song;

import java.util.ArrayList;

public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongViewHolder> {
    private ArrayList<Song> songs;
    private Context context;

    public SongRecyclerViewAdapter(Context context, ArrayList<Song> songs) {
        this.songs = songs;
        this.context = context;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        //TODO: set items
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}

//TODO: complete view holder
class SongViewHolder extends RecyclerView.ViewHolder {

    public SongViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
