package com.ashleymccallum.madimalcrossing.pojos;

/**
 * Song Class
 * @author Ashley McCallum
 */
public class Song {

    private int id;
    private String title;
    private String buyPrice;
    private String sellPrice;
    private int orderable;
    private int collected = 0;
    //TODO: add property to hold Spotify search URL? could just be a search query + title...

    public Song(int id, String title, String buyPrice, String sellPrice, int orderable, int collected) {
        this.id = id;
        this.title = title;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.orderable = orderable;
        this.collected = collected;
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

    public void changeCollectionStatus() {
        if(collected == 0) {
            collected = 1;
        } else {
            collected = 0;
        }
    }
}
