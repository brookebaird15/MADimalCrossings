package com.ashleymccallum.madimalcrossing.Bingo;

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
    public static final String BINGO_ROW_KEY = "5 in a row";
    public static final String BINGO_X_KEY = "x combo";
    public static final String BINGO_CORNERS_KEY = "4 corners";
    public static final String BINGO_BLACKOUT_KEY = "blackout";
    public static final String BINGO_RING_KEY = "ring combo";
    //TODO: set strings to be hidden values of buttons user uses to select game mode

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
    private final int[] xCombo = new int[] {18162001};
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
     * @param villagers ArrayList of Villager objects to be used in generating tiles
     * @param gameMode String representation of the game mode the user selected
     * @author Ashley McCallum
     */
    //TODO: user must select game mode before anything is loaded, prompt user on screen load and also when game is over
    public void startNew(ArrayList<Villager> villagers, String gameMode) {
        generateTiles(villagers);
        selectMode(gameMode);
    }

    /**
     * Generates the BingoTiles for the game
     * @param villagers the ArrayList of Villager objects being used to generate the board
     * @author Ashley McCallum
     */
    private void generateTiles(ArrayList<Villager> villagers) {
        for (int i = 0; i < villagers.size(); i++) {
            Villager villager = villagers.get(i);
            tiles[i] = new BingoTile(villager.getName(), villager.getIconURI(), (int) Math.pow(2, i));
        }
    }

    /**
     * Selects the game mode the user wants to use
     * @param gameMode the String key representing the game mode
     * @author Ashley McCallum
     */
    private void selectMode(String gameMode) {
        winCombos = allWinningCombos.get(gameMode);
    }

    //TODO: select tile to be called when user clicks a tile
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
        if(tile.isAvailable()) {
            //set the tile as no longer available, increase the board score
            tile.setUnavailable();
            boardScore += tile.getValue();
        }
        return false;
    }

    //TODO: to be called after user selects a tile to check game state, if the game is over call changeGameMode() with a prompt
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

    //TODO: to be called once a game is over, use a prompt to allow user to select
    /**
     * Changes the mode of the game so the user can continue to play with the same board
     * @param mode the mode the user wants to select
     * @return false if the user was using the blackout mode (a new board must be generated), true if the mode was changed successfully
     * @author Ashley McCallum
     */
    public boolean canChangeMode(String mode) {
        if(Arrays.equals(winCombos, allWinningCombos.get(BINGO_BLACKOUT_KEY))) {
            return false;
        }
        selectMode(mode);
        return true;
    }
}

/**
 * BingoTile class
 * Represents one tile on the bingo board
 * @author Ashley McCallum
 */
class BingoTile {

    private final String name;
    private final String iconURL;
    private final int value;
    private boolean available;

    protected BingoTile(String name, String iconURL, int value) {
        this.name = name;
        this.iconURL = iconURL;
        this.value = value;
        this.available = true;
    }

    public String getName() {
        return name;
    }

    public String getIconURL() {
        return iconURL;
    }

    protected int getValue() {
        return value;
    }

    protected boolean isAvailable() {
        return available;
    }

    protected void setUnavailable() {
        this.available = false;
    }
}
