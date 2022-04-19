package com.ashleymccallum.madimalcrossing.Bingo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashleymccallum.madimalcrossing.AppDatabase;
import com.ashleymccallum.madimalcrossing.R;
import com.squareup.picasso.Picasso;

public class BingoRecyclerViewAdapter extends RecyclerView.Adapter<BingoRecyclerViewAdapter.BingoViewHolder>{
    private final BingoGame game;
    private final OnGameWinListener listener;
    private final AppDatabase db;

    public BingoRecyclerViewAdapter(BingoGame game, OnGameWinListener listener, AppDatabase db) {
        this.game = game;
        this.listener = listener;
        this.db = db;
    }

    @NonNull
    @Override
    public BingoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bingo_tile, parent, false);
        return new BingoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BingoViewHolder holder, int position) {
        BingoTile tile = game.tiles[position];
        holder.bingoText.setText(tile.getName());
        Picasso.get().load(tile.getIconURL()).into(holder.bingoImg);
        if(tile.getAvailable() == 0) {
            holder.bingoStamp.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(game.canSelectTile(holder.getAbsoluteAdapterPosition())) {
                    holder.bingoStamp.setVisibility(View.VISIBLE);
                    db.updateTile(tile);
                    db.close();
                }
                if(game.isWon()) {
                    listener.onGameWin(game);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return game.tiles.length;
    }

    static class BingoViewHolder extends RecyclerView.ViewHolder {
        protected ImageView bingoImg;
        protected TextView bingoText;
        protected ImageView bingoStamp;

        public BingoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.bingoImg = itemView.findViewById(R.id.bingoImage);
            this.bingoText = itemView.findViewById(R.id.bingoName);
            this.bingoStamp = itemView.findViewById(R.id.bingoStamp);
            bingoStamp.setVisibility(View.INVISIBLE);
        }
    }
}
