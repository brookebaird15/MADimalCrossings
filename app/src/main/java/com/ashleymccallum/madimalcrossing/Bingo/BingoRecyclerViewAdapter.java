package com.ashleymccallum.madimalcrossing.Bingo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashleymccallum.madimalcrossing.R;

public class BingoRecyclerViewAdapter extends RecyclerView.Adapter<BingoRecyclerViewAdapter.BingoViewHolder>{
    private BingoGame game;
    private Context context;

    public BingoRecyclerViewAdapter(Context context, BingoGame game) {
        this.game = game;
        this.context = context;
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
//        holder.bingoImg.setImageResource();     SET IMAGE TO TILE IMG
        //TODO: check if itemview having onClickListener works as intended
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(game.selectTile(holder.getAbsoluteAdapterPosition())) {
                    //TODO: stamp tile to indicate used
                }
                if(game.isWon()) {
                    //TODO: flag as won -> need to somehow pass this info to bingo fragment to prompt new game or mode change
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

        public BingoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.bingoImg = itemView.findViewById(R.id.bingoImage);
            this.bingoText = itemView.findViewById(R.id.bingoName);
        }
    }
}
