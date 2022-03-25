package com.ashleymccallum.madimalcrossing;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BingoRecyclerViewAdapter extends RecyclerView.Adapter<BingoViewHolder>{
//    private ArrayList<Villager> Villager

    @NonNull
    @Override
    public BingoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BingoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
 class BingoViewHolder extends RecyclerView.ViewHolder {
    protected ImageView bingoImg;
    protected TextView bingoText;

     public BingoViewHolder(@NonNull View itemView) {
         super(itemView);
         this.bingoImg = itemView.findViewById(R.id.bingoImage);
         this.bingoText = itemView.findViewById(R.id.bingoName);
     }
 }