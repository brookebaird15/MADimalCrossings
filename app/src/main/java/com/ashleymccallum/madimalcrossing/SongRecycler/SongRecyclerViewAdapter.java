package com.ashleymccallum.madimalcrossing.SongRecycler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    /**
     * Updates the image of a checklist item based on its completion
     * @param imageView imageview being updated
     * @param collectionStatus the collection status of the item
     * @author Ashley McCallum
     */
    public void setImage(ImageView imageView, int collectionStatus) {
        if(collectionStatus == 0) {
            //if item uncollected, set image to blank checkbox
            imageView.setImageResource(R.drawable.ic_outline_check_box_outline_blank_24);
        } else if(collectionStatus == 1) {
            //if item collected, set image to checked checkbox
            imageView.setImageResource(R.drawable.ic_outline_check_box_24);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songTitle.setText(song.getTitle());
//        holder.songImg.setImageResource();    SET IMAGE
        holder.songMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder songDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = songDialog.create().getLayoutInflater();
                View alertView = inflater.inflate(R.layout.song_detail, null);
                songDialog.setView(alertView);

                ImageView songImg = alertView.findViewById(R.id.songDetailImg);
                TextView title = alertView.findViewById(R.id.songTitle);
                TextView buyPrice = alertView.findViewById(R.id.songBuyText);
                TextView sellPrice = alertView.findViewById(R.id.songSellText);
                TextView orderableText = alertView.findViewById(R.id.songOrderText);
                ImageView collectedImg = alertView.findViewById(R.id.songCollectionImg);

//                songImg.setImageResource();   TODO: SET IMAGE FOR COVER ART
                title.setText(song.getTitle());
                buyPrice.setText(song.getBuyPrice());
                sellPrice.setText(song.getSellPrice());
                setImage(collectedImg, song.getCollected());
                if(song.getOrderable() == 0) {
                    orderableText.setText(R.string.no);
                } else if (song.getOrderable() == 1) {
                    orderableText.setText(R.string.yes);
                }

                //change the collection status and image of the song on click
                collectedImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        song.changeCollectionStatus();
                        setImage(collectedImg, song.getCollected());
                    }
                });

                songDialog.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO: save song changes on close
                    }
                });
                songDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}

class SongViewHolder extends RecyclerView.ViewHolder {
    TextView songTitle;
    ImageView songImg;
    ImageView songMoreBtn;

    public SongViewHolder(@NonNull View itemView) {
        super(itemView);
        this.songImg = itemView.findViewById(R.id.songImg);
        this.songTitle = itemView.findViewById(R.id.songTitle);
        this.songMoreBtn = itemView.findViewById(R.id.songMoreBtn);
    }
}
