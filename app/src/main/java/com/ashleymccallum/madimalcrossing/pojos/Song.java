package com.ashleymccallum.madimalcrossing.pojos;

public class Song {

    private int id;
    private String title;
    private String buyPrice;
    private String sellPrice;
    private int orderable;
    private int collected = 0;

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

    public void setCollected(int collected) {
        this.collected = collected;
    }
}
