package com.ashleymccallum.madimalcrossing.SongRecycler;

import static com.ashleymccallum.madimalcrossing.MainActivity.currentSong;
import static com.ashleymccallum.madimalcrossing.MainActivity.mediaPlayer;
import static com.ashleymccallum.madimalcrossing.pojos.Song.COLLECTED;
import static com.ashleymccallum.madimalcrossing.pojos.Song.UNCOLLECTED;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashleymccallum.madimalcrossing.AppDatabase;
import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.pojos.Song;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongViewHolder> {
    private final ArrayList<Song> songs;
    private final Context context;

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
     * Updates the checkbox image of a song based on its collection status
     * @param imageView imageview being updated
     * @param collectionStatus the collection status of the item
     * @author Ashley McCallum
     */
    private void setImage(ImageView imageView, int collectionStatus) {
        if(collectionStatus == UNCOLLECTED) {
            //if item uncollected, set image to blank checkbox
            imageView.setImageResource(R.drawable.ic_outline_check_box_outline_blank_24);
        } else if(collectionStatus == COLLECTED) {
            //if item collected, set image to checked checkbox
            imageView.setImageResource(R.drawable.ic_outline_check_box_24);
        }
    }

    /**
     * Resets the media player and starts a new song
     * @param song the Song to be played
     * @author Ashley McCallum
     */
    private void startNewSong(Song song) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(song.getSongURI());
            currentSong = song.getId();
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songTitle.setText(song.getTitle());
        Picasso.get().load(song.getImgURI()).into(holder.songImg);
        if(currentSong == song.getId() && mediaPlayer.isPlaying()) {
            holder.musicControl.setImageResource(R.drawable.ic_baseline_pause_24);
        } else {
            holder.musicControl.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }

        holder.musicControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if the media player is playing
                if(mediaPlayer.isPlaying()) {
                    //if the current song id != the selected song id, start the new song
                    if(currentSong != song.getId()) {
                        //set image to pause
                        holder.musicControl.setImageResource(R.drawable.ic_baseline_pause_24);
                        //notify the adapter to change the previous song image back to play
                        notifyItemChanged(currentSong - 1);
                        startNewSong(song);
                    } else {
                        //otherwise user is selecting same song, pause the song
                        mediaPlayer.pause();
                        //set image to play
                        holder.musicControl.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    }
                } else {
                    //otherwise the player is not playing anything, start the new song
                    //set image to pause
                    holder.musicControl.setImageResource(R.drawable.ic_baseline_pause_24);
                    startNewSong(song);
                }

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });
            }
        });

        holder.songMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder songDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = songDialog.create().getLayoutInflater();
                View alertView = inflater.inflate(R.layout.song_detail, null);
                songDialog.setView(alertView);

                ImageView songImg = alertView.findViewById(R.id.songDetailImg);
                TextView title = alertView.findViewById(R.id.songDetailTitle);
                TextView buyPrice = alertView.findViewById(R.id.songBuyText);
                TextView sellPrice = alertView.findViewById(R.id.songSellText);
                TextView orderableText = alertView.findViewById(R.id.songOrderText);
                ImageView collectedImg = alertView.findViewById(R.id.songCollectionImg);

                Picasso.get().load(song.getImgURI()).into(songImg);
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

                songDialog.setNeutralButton("Close", null);

                //when the dialog box is dismissed from the close or by clicking off it, update the song
                songDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        AppDatabase db = new AppDatabase(context);
                        db.updateSong(song);
                        db.close();
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
    ImageView musicControl;

    public SongViewHolder(@NonNull View itemView) {
        super(itemView);
        this.songImg = itemView.findViewById(R.id.songImg);
        this.songTitle = itemView.findViewById(R.id.songTitle);
        this.songMoreBtn = itemView.findViewById(R.id.songMoreBtn);
        this.musicControl = itemView.findViewById(R.id.musicControlBtn);
    }
}
