package com.ashleymccallum.madimalcrossing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ashleymccallum.madimalcrossing.api.RequestSingleton;
import com.ashleymccallum.madimalcrossing.pojos.Song;
import com.ashleymccallum.madimalcrossing.pojos.Villager;
import com.ashleymccallum.madimalcrossing.pojos.VillagerList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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
    public static final String SPOTTED_COLUMN = "spotted";  //an int (0/1) if the player has seen the villager

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
    public static final String ORDERABLE_COLUMN = "orderable";  //an int (0/1) if the track can be ordered
    public static final String COLLECTED_COLUMN = "collected";  //an int (0/1) if the track has been collected
    public static final String SONG_COLUMN = "song_uri";

    //song table columns: id, title, buy_price, sell_price, orderable, collected, img_uri, song_uri
    public static final String CREATE_SONG_TABLE = "CREATE TABLE " +
            SONG_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY," +
            TITLE_COLUMN + " TEXT," + BUY_COLUMN + " TEXT," +
            SELL_COLUMN + " TEXT," + ORDERABLE_COLUMN + " INTEGER," +
            COLLECTED_COLUMN + " INTEGER," + IMG_COLUMN + " TEXT," +
            SONG_COLUMN + " TEXT)";

    //List Table
    public static final String LIST_TABLE = "lists";

    //list table columns: id, name
    public static final String CREATE_LIST_TABLE = "CREATE TABLE " +
            LIST_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY," +
            NAME_COLUMN + " TEXT)";

    //list villager table
    public static final String LIST_VILLAGER_TABLE = "list_villager_relation";
    public static final String LIST_FK_COLUMN = "list_id";
    public static final String VILLAGER_FK_COLUMN = "villager_id";

    //list villager relation table columns: list_id, villager_id
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
        sqLiteDatabase.execSQL(CREATE_LIST_VILLAGER_TABLE);
        Log.d("AppDB-------", "Tables created");
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
     * Updates a villager's information in the table
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
     * Adds all the songs from the API into the database
     * @param response a JSON Object containing all the songs in the database
     * @param context application Context
     * @author Ashley McCallum
     */
    public void addAllSongs(JSONObject response, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i = 1; i <= response.length(); i++) {
            //for each item in the original response, create a new url and get the song at that url
            String url = "https://acnhapi.com/v1/songs/" + i;
            JsonObjectRequest songRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        values.put(ID_COLUMN, response.getInt("id"));
                        JSONObject nameObject = response.getJSONObject("name");
                        values.put(TITLE_COLUMN, nameObject.getString("name-USen"));
                        String buyPrice = response.getString("buy-price");
                        if(buyPrice.equals("null")) {
                            buyPrice = "N/A";
                        }
                        values.put(BUY_COLUMN, buyPrice);
                        values.put(SELL_COLUMN, response.getString("sell-price"));
                        boolean orderable = response.getBoolean("isOrderable");
                        int orderableFlag = 0;
                        if(orderable) {
                            orderableFlag = 1;
                        }
                        values.put(ORDERABLE_COLUMN, orderableFlag);
                        values.put(COLLECTED_COLUMN, 0);
                        values.put(IMG_COLUMN, response.getString("image_uri"));
                        values.put(SONG_COLUMN, response.getString("music_uri"));
                        db.insert(SONG_TABLE, null, values);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("SONG_VOLLEY_ERROR",  error.getLocalizedMessage());
                }
            });
            RequestSingleton.getInstance(context).getRequestQueue().add(songRequest);
        }
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
                    cursor.getInt(5),
                    cursor.getString(6),
                    cursor.getString(7)));
        }
        db.close();
        return songs;
    }

    public ArrayList<Song> getCollectedSongs() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Song> songs = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SONG_TABLE + " WHERE " + COLLECTED_COLUMN + "=?", new String[]{String.valueOf(1)});
        while (cursor.moveToNext()) {
            songs.add(new Song(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getString(6),
                    cursor.getString(7)));
        }
        db.close();
        return songs;
    }

    /**
     * Updates a song's information in the table
     * Users can only update the collection status of a song
     * @param song the song being updated
     * @author Ashley McCallum
     */
    public void updateSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLLECTED_COLUMN, song.getCollected());
        db.update(SONG_TABLE, values, ID_COLUMN + "=?", new String[]{String.valueOf(song.getId())});
        db.close();
    }

    /**
     * Creates an empty list item in the table
     * @param name is the name the user wants to use for the list
     * @author Ashley McCallum
     */
    public void createList(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, name);
        db.insert(LIST_TABLE, null, values);
        db.close();
    }

    /**
     * Removes a list from the table
     * @param listID is the list being deleted
     * @author Ashley McCallum
     */
    public void deleteList(int listID) {
        SQLiteDatabase db = this.getWritableDatabase();
        emptyList(listID);  //first remove all relationships to the list being deleted
        db.delete(LIST_TABLE, ID_COLUMN + "=?", new String[]{String.valueOf(listID)});
        db.close();
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

    //TODO: verify all methods below work correctly for villager list relation table
    /**
     * Adds a single villager to a list
     * @param list the list being added to
     * @param villager the villager being added to the list
     * @author Ashley McCallum
     */
    public void createOneVillagerListRelation(VillagerList list, Villager villager) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LIST_FK_COLUMN, list.getId());
        values.put(VILLAGER_FK_COLUMN, villager.getId());
        db.insert(LIST_VILLAGER_TABLE, null, values);
        db.close();
    }

    /**
     * Adds several villagers to a list
     * @param list the list being added to
     * @param villagers the villagers being added to the list
     * @author Ashley McCallum
     */
    public void createManyVillagerListRelations(VillagerList list, ArrayList<Villager> villagers) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LIST_FK_COLUMN, list.getId());
        for (Villager villager : villagers) {
            values.put(VILLAGER_FK_COLUMN, villager.getId());
            db.insert(LIST_VILLAGER_TABLE, null, values);
        }
        db.close();
    }

    /**
     * Retrieves all villagers from the database associated with a specific list
     * @param listID the int id of the list being queried
     * @return ArrayList of Villager objects associated with that list
     * @author Ashley McCallum
     */
    public ArrayList<Villager> getAllVillagersForList(int listID) {
        ArrayList<Villager> villagers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "Select v.* FROM " + VILLAGER_TABLE + " AS v INNER JOIN "
                        + LIST_VILLAGER_TABLE + " AS lvr ON v." + ID_COLUMN + "=lvr."
                        + VILLAGER_FK_COLUMN + " INNER JOIN " + LIST_TABLE + " AS l on lvr."
                        + LIST_FK_COLUMN + "=l." + ID_COLUMN + " WHERE l." + ID_COLUMN
                        + "=" + listID, null);
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
     * Removes all relationships to a specific list in the list villager relation table
     * This effectively empties a list
     * @param listID the id of the list being emptied
     * @author Ashley McCallum
     */
    public void emptyList(int listID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LIST_VILLAGER_TABLE, LIST_FK_COLUMN + "=?", new String[]{String.valueOf(listID)});
        db.close();
    }

    /**
     * Removes the relationship between a specific list and a specific villager
     * @param villagerID the villager being removed from the list
     * @param listID the list the villager is being removed from
     * @author Ashley McCallum
     */
    public void removeVillagerFromList(int villagerID, int listID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //TODO: check placeholders in where clause function correctly
        db.delete(LIST_VILLAGER_TABLE, LIST_FK_COLUMN + "=? AND "
                + VILLAGER_FK_COLUMN + "=?", new String[]{String.valueOf(listID), String.valueOf(villagerID)});
        db.close();
    }

}
