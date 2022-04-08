package com.ashleymccallum.madimalcrossing.pojos;

/**
 * Song Class
 * @author Ashley McCallum
 */
public class Song {

    public static final int UNCOLLECTED = 0;
    public static final int COLLECTED = 1;

    private int id;
    private String title;
    private String buyPrice;
    private String sellPrice;
    private int orderable;
    private int collected;
    private String imgURI;
    private String songURI;

    public Song(int id, String title, String buyPrice, String sellPrice, int orderable, int collected, String imgURI, String songURI) {
        this.id = id;
        this.title = title;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.orderable = orderable;
        this.collected = collected;
        this.imgURI = imgURI;
        this.songURI = songURI;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public int getOrderable() {
        return orderable;
    }

    public int getCollected() {
        return collected;
    }

    public String getImgURI() {
        return imgURI;
    }

    public String getSongURI() {
        return songURI;
    }

    public void changeCollectionStatus() {
        if(collected == UNCOLLECTED) {
            collected = COLLECTED;
        } else {
            collected = UNCOLLECTED;
        }
    }
}
