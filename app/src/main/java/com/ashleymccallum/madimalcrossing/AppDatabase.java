package com.ashleymccallum.madimalcrossing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ashleymccallum.madimalcrossing.pojos.Song;
import com.ashleymccallum.madimalcrossing.pojos.Villager;
import com.ashleymccallum.madimalcrossing.pojos.VillagerList;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * AppDatabase Class
 * Contains all table create statements and CRUD operations
 * @author Ashley McCallum
 */
public class AppDatabase extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "villagerapp";

    //Main Villager Table
    public static final String VILLAGER_TABLE = "villagers";
    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";
    public static final String PERSONALITY_COLUMN = "personality";
    public static final String BIRTHDAY_COLUMN = "birthday";
    public static final String SPECIES_COLUMN = "species";
    public static final String GENDER_COLUMN = "gender";
    public static final String HOBBY_COLUMN = "hobby";
    public static final String CATCHPHRASE_COLUMN = "catchphrase";
    public static final String ICON_COLUMN = "icon_uri";
    public static final String IMG_COLUMN = "img_uri";
    public static final String SPOTTED_COLUMN = "spotted";  //a boolean if the player has seen the villager

    //villager table columns: id, name, personality, birthday, species, gender, hobby, catchphrase, icon_uri, img_uri, spotted
    public static final String CREATE_VILLAGER_TABLE = "CREATE TABLE " +
            VILLAGER_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY," +
            NAME_COLUMN + " TEXT," + PERSONALITY_COLUMN + " TEXT," +
            BIRTHDAY_COLUMN + " TEXT," + SPECIES_COLUMN + " TEXT," +
            GENDER_COLUMN + " TEXT," + HOBBY_COLUMN + " TEXT," +
            CATCHPHRASE_COLUMN + " TEXT," + ICON_COLUMN + " TEXT," +
            IMG_COLUMN + " TEXT," + SPOTTED_COLUMN + " INTEGER)";

    //KK Slider Song Table
    public static final String SONG_TABLE = "kk_songs";
    public static final String TITLE_COLUMN = "title";
    public static final String BUY_COLUMN = "buy_price";
    public static final String SELL_COLUMN = "sell_price";
    public static final String ORDERABLE_COLUMN = "orderable";  //a boolean if the track can be ordered
    public static final String COLLECTED_COLUMN = "collected";  //a boolean if the track has been collected

    //song table columns: id, title, buy_price, sell_price, orderable, collected
    public static final String CREATE_SONG_TABLE = "CREATE TABLE " +
            SONG_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY," +
            TITLE_COLUMN + " TEXT," + BUY_COLUMN + " TEXT," +
            SELL_COLUMN + " TEXT," + ORDERABLE_COLUMN + " INTEGER," +
            COLLECTED_COLUMN + " INTEGER)";

    //List Table
    public static final String LIST_TABLE = "lists";

    //list table columns: id, name, villager_id
    public static final String CREATE_LIST_TABLE = "CREATE TABLE " +
            LIST_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY," +
            NAME_COLUMN + " TEXT)";

    //list villager table
    public static final String LIST_VILLAGER_TABLE = "list_villager_relation";
    public static final String LIST_FK_COLUMN = "list_id";
    public static final String VILLAGER_FK_COLUMN = "villager_id";

    public static final String CREATE_LIST_VILLAGER_TABLE = "CREATE TABLE " +
            LIST_VILLAGER_TABLE + "(" + LIST_FK_COLUMN + " INTEGER," +
            VILLAGER_FK_COLUMN + " INTEGER," +
            "FOREIGN KEY (" + LIST_FK_COLUMN + ") REFERENCES " + LIST_TABLE + "(" + ID_COLUMN + ")," +
            "FOREIGN KEY (" + VILLAGER_FK_COLUMN + ") REFERENCES " + VILLAGER_TABLE + "(" + ID_COLUMN + ")," +
            "PRIMARY KEY (" + LIST_FK_COLUMN + ", " + VILLAGER_FK_COLUMN + "))";

    public AppDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_VILLAGER_TABLE);
        sqLiteDatabase.execSQL(CREATE_SONG_TABLE);
        sqLiteDatabase.execSQL(CREATE_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * TODO: update comment
     * to be used in adding all villagers to the db
     * @param villagers
     */
    public void addAllVillagers(JSONArray villagers) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: parse data from jsonArray
        db.close();
    }

    /**
     * Retrieves all villagers in the database
     * @return ArrayList of Villager objects
     * @author Ashley McCallum
     */
    public ArrayList<Villager> getAllVillagers() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Villager> villagers = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + VILLAGER_TABLE, null);
        while (cursor.moveToNext()) {
            villagers.add(new Villager(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getInt(10)));
        }
        db.close();
        return villagers;
    }

    /**
     * Updates a villagers information in the table
     * Users can only update a villager's catchphrase and spotted columns
     * @param villager the villager being updated
     * @author Ashley McCallum
     */
    public void updateVillager(Villager villager) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATCHPHRASE_COLUMN, villager.getCatchphrase());
        values.put(SPOTTED_COLUMN, villager.getSpotted());
        db.update(VILLAGER_TABLE, values, ID_COLUMN + "=?", new String[]{String.valueOf(villager.getId())});
        db.close();
    }

    /**
     * TODO: update comment
     * to be used in adding all songs to the db
     * @param songs
     */
    public void addAllSongs(JSONArray songs) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: parse data from array
        db.close();
    }

    /**
     * Retrieves all songs in the songs table
     * @return ArrayList of Song objects
     * @author Ashley McCallum
     */
    public ArrayList<Song> getAllSongs() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Song> songs = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SONG_TABLE, null);
        while (cursor.moveToNext()) {
            songs.add(new Song(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getInt(5)));
        }
        db.close();
        return songs;
    }

    /**
     * Retrieves all lists in the list table
     * @return ArrayList of VillagerList objects
     */
    public ArrayList<VillagerList> getAllLists() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<VillagerList> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + LIST_TABLE, null);
        while (cursor.moveToNext()) {
            list.add(new VillagerList(
                    cursor.getInt(0),
                    cursor.getString(1)));
        }
        db.close();
        return list;
    }

    public void createList(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, name);
    }

    public void deleteList(int list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LIST_TABLE, ID_COLUMN + "=?", new String[]{String.valueOf(list)});
        db.close();
    }

}
