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
import com.ashleymccallum.madimalcrossing.pojos.Villager;

import java.util.ArrayList;

public class BingoRecyclerViewAdapter extends RecyclerView.Adapter<BingoViewHolder>{
    private ArrayList<Villager> villagers;
    private Context context;

    public BingoRecyclerViewAdapter(Context context, ArrayList<Villager> villagers) {
        this.villagers = villagers;
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
        Villager villager = villagers.get(position);
//        holder.bingoImg.setImageResource();
        holder.bingoText.setText(villager.getName());
    }

    @Override
    public int getItemCount() {
        return villagers.size();
    }
}
 class BingoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected ImageView bingoImg;
    protected TextView bingoText;

     public BingoViewHolder(@NonNull View itemView) {
         super(itemView);
         this.bingoImg = itemView.findViewById(R.id.bingoImage);
         this.bingoText = itemView.findViewById(R.id.bingoName);
         itemView.setOnClickListener(this);
     }

     @Override
     public void onClick(View view) {
         //TODO: on click, update game
     }
 }