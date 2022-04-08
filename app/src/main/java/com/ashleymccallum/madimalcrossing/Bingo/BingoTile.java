package com.ashleymccallum.madimalcrossing.Bingo;

/**
 * BingoTile class
 * Represents one tile on the bingo board
 * @author Ashley McCallum
 */
public class BingoTile {

    private final int id;
    private final String name;
    private final String iconURL;
    private final int value;
    private int available = 1;

    public BingoTile(int id, String name, String iconURL, int value, int available) {
        this.id = id;
        this.name = name;
        this.iconURL = iconURL;
        this.value = value;
        this.available = available;
    }

    //constructor to generate tiles on new game
    public BingoTile(int id, String name, String iconURL, int value) {
        this.id = id;
        this.name = name;
        this.iconURL = iconURL;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconURL() {
        return iconURL;
    }

    public int getValue() {
        return value;
    }

    public int getAvailable() {
        return available;
    }

    protected void setUnavailable() {
        this.available = 0;
    }
}
