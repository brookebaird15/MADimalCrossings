package com.ashleymccallum.madimalcrossing;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
    public static final String VILLAGER_FK_COLUMN = "villager_id";

    //list table columns: id, name, villager_id
    public static final String CREATE_LIST_TABLE = "CREATE TABLE " +
            LIST_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY," +
            NAME_COLUMN + " TEXT," + VILLAGER_FK_COLUMN + " INTEGER," +
            "FOREIGN KEY(" + VILLAGER_FK_COLUMN + ") REFERENCES " + VILLAGER_TABLE + "(" + ID_COLUMN + "))";

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

   

}
