package com.ashleymccallum.madimalcrossing.Bingo;

import android.util.Log;

import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.pojos.Villager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Bingo Game class
 * Contains all bingo game functionality
 * The bingo game uses bitwise functionality to check for marked spots and calculate winning combinations
 * @author Ashley McCallum
 */
public class BingoGame {

    //String keys for the possible winning combinations
    public static final String BINGO_ROW_KEY = "5-in-a-Row";
    public static final String BINGO_X_KEY = "X Combo";
    public static final String BINGO_CORNERS_KEY = "4 Corners";
    public static final String BINGO_BLACKOUT_KEY = "Blackout";
    public static final String BINGO_RING_KEY = "Ring";

    /**
     * BingoTile[] tiles -> an array of 25 BingoTile objects representing the board positions
     * int[] rowCombo -> an int array of the possible winning tile values for the 5-in-a-row combo
     * int[] xCombo -> an int array of the possible winning tile values for the X combo
     * int[] cornerCombo -> an int array of the possible winning tile values for the 4-corners combo
     * int[] blackoutCombo -> an int array of the possible winning tile values for the blackout combo
     * int[] ringCombo -> an int array of the possible winning tile values for the outer-ring combo
     * allWinningCombos -> a hashmap of all winning combos where the String key is the name of the combo the player is using and the int[] value is the array possible scores
     * int boardScore -> the score of all marked pieces currently on the board
     * int[] winCombos -> an int array of the winning combos for the specific game mode the user has selected
     */
    public final BingoTile[] tiles = new BingoTile[25];
    private final int[] rowCombo = new int[] {31, 992, 31744, 1015808, 32505856, 1082401, 2164802, 4329604, 8659208, 17318416, 1118480, 17043521};
    private final int[] xCombo = new int[] {18157905};
    private final int[] cornerCombo = new int[] {17825809};
    private final int[] blackoutCombo = new int[] {33554431};
    private final int[] ringCombo = new int[] {33080895};

    HashMap<String, int[]> allWinningCombos = new HashMap<>();
    {
        allWinningCombos.put(BINGO_ROW_KEY, rowCombo);
        allWinningCombos.put(BINGO_X_KEY, xCombo);
        allWinningCombos.put(BINGO_CORNERS_KEY, cornerCombo);
        allWinningCombos.put(BINGO_BLACKOUT_KEY, blackoutCombo);
        allWinningCombos.put(BINGO_RING_KEY, ringCombo);
    }

    private int boardScore = 0;
    private int[] winCombos;
    public String currentMode;
    private static BingoGame instance = null;

    public static BingoGame getInstance() {
        if (instance == null) {
            instance = new BingoGame();
        }
        return instance;
    }

    private BingoGame (){
    }

    /**
     * Generates a new set of tiles and sets the game mode
     * Default starting game mode is 5-in-a-row
     * @param villagers ArrayList of Villager objects to be used in generating tiles
     * @author Ashley McCallum
     */
    public void startNew(ArrayList<Villager> villagers) {
        generateTiles(villagers);
        if(currentMode == null) {
            selectMode(BINGO_ROW_KEY);
        }
        boardScore = 0;
    }

    /**
     * Retrieves the tiles from the bingo table in the db
     * @param tiles ArrayList of BingoTiles from the database
     * @author Ashley McCallum
     */
    public void continueGame(ArrayList<BingoTile> tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            BingoTile tile = tiles.get(i);
            this.tiles[i] = new BingoTile(tile.getId(), tile.getName(), tile.getIconURL(), tile.getValue(), tile.getAvailable());
        }
        if(winCombos == null) {
            selectMode(BINGO_ROW_KEY);
        }
    }

    /**
     * Generates the BingoTiles for the game
     * @param villagers the ArrayList of Villager objects being used to generate the board
     * @author Ashley McCallum
     */
    private void generateTiles(ArrayList<Villager> villagers) {
        villagers.add(12, new Villager("Free Space", "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/Animal_Crossing_Leaf.svg/150px-Animal_Crossing_Leaf.svg.png"));
        for (int i = 0; i < villagers.size(); i++) {
            Villager villager = villagers.get(i);
            tiles[i] = new BingoTile(i + 1, villager.getName(), villager.getIconURI(), (int) Math.pow(2, i));
        }
    }

    /**
     * Selects the game mode the user wants to use
     * @param gameMode the String key representing the game mode
     * @author Ashley McCallum
     */
    private void selectMode(String gameMode) {
        currentMode = gameMode;
        winCombos = allWinningCombos.get(gameMode);
    }

    /**
     * Selects the tile the user chooses
     * @param position the position of the selected tile
     * @return true if the tile was valid, false if not
     * @author Ashley McCallum
     */
    public boolean canSelectTile(int position) {
        //get the tile at the position
        BingoTile tile = tiles[position];
        //if the tile is available
        if(tile.getAvailable() == 1) {
            //set the tile as no longer available, increase the board score
            tile.setUnavailable();
            boardScore += tile.getValue();
            return true;
        }
        return false;
    }

    /**
     * Checks if the game has been won
     * @return true if there is a winning combo, false if not
     * @author Ashley McCallum
     */
    public boolean isWon() {
        //for each score in the winCombo array
        for(int score : winCombos) {
            //if the value of the bitwise comparison of the board value and the score in the array is equal to the array score
            if((boardScore & score) == score) {
                //the user has a winning combination
                return true;
            }
        }
        return false;
    }

    /**
     * Changes the mode of the game so the user can continue to play with the same board
     * @param mode the mode the user wants to select
     * @return false if the user was using the blackout mode and won (a new board must be generated), true if the mode was changed successfully
     * @author Ashley McCallum
     */
    public boolean canChangeMode(String mode) {
        if(currentMode.equals(BINGO_BLACKOUT_KEY) && boardScore == 33554431) {
            return false;
        }
        selectMode(mode);
        return true;
    }
}

