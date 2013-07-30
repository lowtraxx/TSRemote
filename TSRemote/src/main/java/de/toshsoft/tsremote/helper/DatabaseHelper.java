package de.toshsoft.tsremote.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import de.toshsoft.tsremote.R;

/**
 *
 * Copyright (c) 20013 Oliver Pahl
 * Distributed under the GNU GPL v2. For full terms see the file LICENSE.
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;
    private final String TABLE_NAME = "remotes_list";
    private static final String DB_NAME = "remotes";

    // The keys
    public static final String DB_COLUMN_NAME = "_name";
    public static final String DB_COLUMN_VENDOR = "vendor";
    public static final String DB_COLUMN_TYPE = "type";

    public static final String DB_COLUMN_KEY_ON_OFF = "key_on_off";

    public static final String DB_COLUMN_KEY_1 = "key_1";
    public static final String DB_COLUMN_KEY_2 = "key_2";
    public static final String DB_COLUMN_KEY_3 = "key_3";
    public static final String DB_COLUMN_KEY_4 = "key_4";
    public static final String DB_COLUMN_KEY_5 = "key_5";
    public static final String DB_COLUMN_KEY_6 = "key_6";
    public static final String DB_COLUMN_KEY_7 = "key_7";
    public static final String DB_COLUMN_KEY_8 = "key_8";
    public static final String DB_COLUMN_KEY_9 = "key_9";
    public static final String DB_COLUMN_KEY_0 = "key_0";

    public static final String DB_COLUMN_KEY_RED = "key_red";
    public static final String DB_COLUMN_KEY_GREEN = "key_green";
    public static final String DB_COLUMN_KEY_YELLOW = "key_yellow";
    public static final String DB_COLUMN_KEY_BLUE = "key_blue";

    public static final String DB_COLUMN_KEY_VOL_UP = "key_vol_up";
    public static final String DB_COLUMN_KEY_VOL_DOWN = "key_vol_down";
    public static final String DB_COLUMN_KEY_CHANNEL_UP = "key_channel_up";
    public static final String DB_COLUMN_KEY_CHANNEL_DOWN = "key_channel_down";

    public static final String DB_COLUMN_KEY_UP = "key_up";
    public static final String DB_COLUMN_KEY_DOWN = "key_down";
    public static final String DB_COLUMN_KEY_RIGHT = "key_right";
    public static final String DB_COLUMN_KEY_LEFT = "key_left";
    public static final String DB_COLUMN_KEY_ENTER = "key_enter";

    public static final String DB_COLUMN_KEY_REWIND = "key_rewind";
    public static final String DB_COLUMN_KEY_FAST_FORWARD = "key_fast_forward";
    public static final String DB_COLUMN_KEY_PLAY = "key_play";
    public static final String DB_COLUMN_KEY_PAUSE = "key_pause";

    public static final String DB_COLUMN_KEY_NEXT_CHAPTER = "key_next_chapter";
    public static final String DB_COLUMN_KEY_PREV_CHAPTER = "key_pref_chapter";

    public static final String DB_COLUMN_KEY_CUSTOM_1 = "key_custom_1";
    public static final String DB_COLUMN_KEY_CUSTOM_2 = "key_custom_2";
    public static final String DB_COLUMN_KEY_CUSTOM_3 = "key_custom_3";
    public static final String DB_COLUMN_KEY_CUSTOM_4 = "key_custom_4";
    public static final String DB_COLUMN_KEY_CUSTOM_5 = "key_custom_5";
    public static final String DB_COLUMN_KEY_CUSTOM_6 = "key_custom_6";
    public static final String DB_COLUMN_KEY_CUSTOM_7 = "key_custom_7";
    public static final String DB_COLUMN_KEY_CUSTOM_8 = "key_custom_8";
    public static final String DB_COLUMN_KEY_CUSTOM_9 = "key_custom_9";

    public static final Map<String, Number> UI_TO_DB_MAP = generateMap();

    // Generate a map that adds corresponding keys to UI elements
    private static Map<String, Number> generateMap() {
        Map tmp = new HashMap<String, Number>();

        tmp.put(DB_COLUMN_KEY_ON_OFF, R.id.button_onoff);

        tmp.put(DB_COLUMN_KEY_0, R.id.button_number_0);
        tmp.put(DB_COLUMN_KEY_1, R.id.button_number_1);
        tmp.put(DB_COLUMN_KEY_2, R.id.button_number_2);
        tmp.put(DB_COLUMN_KEY_3, R.id.button_number_3);
        tmp.put(DB_COLUMN_KEY_4, R.id.button_number_4);
        tmp.put(DB_COLUMN_KEY_5, R.id.button_number_5);
        tmp.put(DB_COLUMN_KEY_6, R.id.button_number_6);
        tmp.put(DB_COLUMN_KEY_7, R.id.button_number_7);
        tmp.put(DB_COLUMN_KEY_8, R.id.button_number_8);
        tmp.put(DB_COLUMN_KEY_9, R.id.button_number_9);

        tmp.put(DB_COLUMN_KEY_RED, R.id.button_function_red);
        tmp.put(DB_COLUMN_KEY_GREEN, R.id.button_function_green);
        tmp.put(DB_COLUMN_KEY_YELLOW, R.id.button_function_yellow);
        tmp.put(DB_COLUMN_KEY_BLUE, R.id.button_function_blue);

        tmp.put(DB_COLUMN_KEY_VOL_UP, R.id.button_volume_up);
        tmp.put(DB_COLUMN_KEY_VOL_DOWN, R.id.button_volume_down);
        tmp.put(DB_COLUMN_KEY_CHANNEL_UP, R.id.button_channel_up);
        tmp.put(DB_COLUMN_KEY_CHANNEL_DOWN, R.id.button_channel_down);

        tmp.put(DB_COLUMN_KEY_UP, R.id.button_up);
        tmp.put(DB_COLUMN_KEY_DOWN, R.id.button_down);
        tmp.put(DB_COLUMN_KEY_RIGHT, R.id.button_right);
        tmp.put(DB_COLUMN_KEY_LEFT, R.id.button_left);
        tmp.put(DB_COLUMN_KEY_ENTER, R.id.button_enter);

        tmp.put(DB_COLUMN_KEY_REWIND, R.id.button_fast_rewind);
        tmp.put(DB_COLUMN_KEY_FAST_FORWARD, R.id.button_fast_forward);
        tmp.put(DB_COLUMN_KEY_PLAY, R.id.button_play);
        tmp.put(DB_COLUMN_KEY_PAUSE, R.id.button_pause);

        tmp.put(DB_COLUMN_KEY_NEXT_CHAPTER, R.id.button_chapter_forward);
        tmp.put(DB_COLUMN_KEY_PREV_CHAPTER, R.id.button_chapter_back);

        tmp.put(DB_COLUMN_KEY_CUSTOM_1, R.id.button_custom_1);
        tmp.put(DB_COLUMN_KEY_CUSTOM_2, R.id.button_custom_2);

        return tmp;
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void open() throws SQLException {
        //open database in reading/writing mode
        database = getWritableDatabase();
    }

    public void close() {
        if (database != null)
            database.close();
    }

    public void insertRemote(String name, String vendor, String type, String key_on_off, String key_1, String key_2, String key_3, String key_4, String key_5, String key_6, String key_7, String key_8,
                             String key_9, String key_0, String key_red, String key_green, String key_yellow, String key_blue, String key_vol_up, String key_vol_down, String key_channel_up, String key_channel_down,
                             String key_up, String key_down, String key_right, String key_left, String key_enter, String key_rewind, String key_fast_forward, String key_play, String key_pause, String key_next_chapter,
                             String key_pref_chapter, String key_custom_1, String key_custom_2, String key_custom_3, String key_custom_4, String key_custom_5, String key_custom_6, String key_custom_7, String key_custom_8, String key_custom_9) {
        ContentValues newCon = new ContentValues();
        newCon.put("_name", name);
        newCon.put("vendor", vendor.isEmpty() ? "none" : vendor);
        newCon.put("type", type.isEmpty() ? "none" : type);
        newCon.put("key_on_off", key_on_off.isEmpty() ? "none" : key_on_off);
        newCon.put("key_1", key_1.isEmpty() ? "none" : key_1);
        newCon.put("key_2", key_2.isEmpty() ? "none" : key_2);
        newCon.put("key_3", key_3.isEmpty() ? "none" : key_3);
        newCon.put("key_4", key_4.isEmpty() ? "none" : key_4);
        newCon.put("key_5", key_5.isEmpty() ? "none" : key_5);
        newCon.put("key_6", key_6.isEmpty() ? "none" : key_6);
        newCon.put("key_7", key_7.isEmpty() ? "none" : key_7);
        newCon.put("key_8", key_8.isEmpty() ? "none" : key_8);
        newCon.put("key_9", key_9.isEmpty() ? "none" : key_9);
        newCon.put("key_0", key_0.isEmpty() ? "none" : key_0);
        newCon.put("key_red", key_red.isEmpty() ? "none" : key_red);
        newCon.put("key_green", key_green.isEmpty() ? "none" : key_green);
        newCon.put("key_yellow", key_yellow.isEmpty() ? "none" : key_yellow);
        newCon.put("key_blue", key_blue.isEmpty() ? "none" : key_blue);
        newCon.put("key_vol_up", key_vol_up.isEmpty() ? "none" : key_vol_up);
        newCon.put("key_vol_down", key_vol_down.isEmpty() ? "none" : key_vol_down);
        newCon.put("key_channel_up", key_channel_up.isEmpty() ? "none" : key_channel_up);
        newCon.put("key_channel_down", key_channel_down.isEmpty() ? "none" : key_channel_down);
        newCon.put("key_up", key_up.isEmpty() ? "none" : key_up);
        newCon.put("key_down", key_down.isEmpty() ? "none" : key_down);
        newCon.put("key_right", key_right.isEmpty() ? "none" : key_right);
        newCon.put("key_left", key_left.isEmpty() ? "none" : key_left);
        newCon.put("key_enter", key_enter.isEmpty() ? "none" : key_enter);
        newCon.put("key_rewind", key_rewind.isEmpty() ? "none" : key_rewind);
        newCon.put("key_fast_forward", key_fast_forward.isEmpty() ? "none" : key_fast_forward);
        newCon.put("key_play", key_play.isEmpty() ? "none" : key_play);
        newCon.put("key_pause", key_pause.isEmpty() ? "none" : key_pause);
        newCon.put("key_next_chapter", key_next_chapter.isEmpty() ? "none" : key_next_chapter);
        newCon.put("key_pref_chapter", key_pref_chapter.isEmpty() ? "none" : key_pref_chapter);
        newCon.put("key_custom_1", key_custom_1.isEmpty() ? "none" : key_custom_1);
        newCon.put("key_custom_2", key_custom_2.isEmpty() ? "none" : key_custom_2);
        newCon.put("key_custom_3", key_custom_3.isEmpty() ? "none" : key_custom_3);
        newCon.put("key_custom_4", key_custom_4.isEmpty() ? "none" : key_custom_4);
        newCon.put("key_custom_5", key_custom_5.isEmpty() ? "none" : key_custom_5);
        newCon.put("key_custom_6", key_custom_6.isEmpty() ? "none" : key_custom_6);
        newCon.put("key_custom_7", key_custom_7.isEmpty() ? "none" : key_custom_7);
        newCon.put("key_custom_8", key_custom_8.isEmpty() ? "none" : key_custom_8);
        newCon.put("key_custom_9", key_custom_9.isEmpty() ? "none" : key_custom_9);

        try {
            open();
            database.insert(TABLE_NAME, null, newCon);
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cursor getAllRemotes() {
        return database.rawQuery( "SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getRemote(String name) {
        return database.rawQuery( "SELECT * FROM " + TABLE_NAME + " WHERE " + DB_COLUMN_NAME + "=\"" + name + "\"", null);
    }

    public Cursor getKeyFromRemote(String name, String key) {
        return database.rawQuery( "SELECT " + key + " FROM " + TABLE_NAME + " WHERE " + DB_COLUMN_NAME + "=\"" + name + "\"", null);
    }

    public void updateKeyFromRemote(String name, String key, String newValue) {
        try {
            open();

            // Build a Update Query
            ContentValues cv=new ContentValues();
            cv.put(key, newValue);
            String[] whereArgs = {name};
            database.update(TABLE_NAME, cv, DB_COLUMN_NAME + "=?",whereArgs);

            // database.rawQuery("UPDATE " + TABLE_NAME + " SET " + key + "=\"" + newValue + "\"" + " WHERE " + DB_COLUMN_NAME + "=\"" + name + "\"", null);
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRemote(String name, String vendor, String type, String key_on_off, String key_1, String key_2, String key_3, String key_4, String key_5, String key_6, String key_7, String key_8,
                             String key_9, String key_0, String key_red, String key_green, String key_yellow, String key_blue, String key_vol_up, String key_vol_down, String key_channel_up, String key_channel_down,
                             String key_up, String key_down, String key_right, String key_left, String key_enter, String key_rewind, String key_fast_forward, String key_play, String key_pause, String key_next_chapter,
                             String key_pref_chapter, String key_custom_1, String key_custom_2, String key_custom_3, String key_custom_4, String key_custom_5, String key_custom_6, String key_custom_7, String key_custom_8, String key_custom_9) {
        try {
            open();

            Cursor cur = getRemote(name);
            if(cur.getCount() != 0)
            {
                ContentValues newCon = new ContentValues();
                newCon.put("vendor", vendor.isEmpty() ? cur.getString(cur.getColumnIndex("vendor")) : vendor);
                newCon.put("type", type.isEmpty() ? cur.getString(cur.getColumnIndex("type")) : type);
                newCon.put("key_on_off", key_on_off.isEmpty() ? cur.getString(cur.getColumnIndex("key_on_off")) : key_on_off);
                newCon.put("key_1", key_1.isEmpty() ? cur.getString(cur.getColumnIndex("key_1")) : key_1);
                newCon.put("key_2", key_2.isEmpty() ? cur.getString(cur.getColumnIndex("key_2")) : key_2);
                newCon.put("key_3", key_3.isEmpty() ? cur.getString(cur.getColumnIndex("key_3")) : key_3);
                newCon.put("key_4", key_4.isEmpty() ? cur.getString(cur.getColumnIndex("key_4")) : key_4);
                newCon.put("key_5", key_5.isEmpty() ? cur.getString(cur.getColumnIndex("key_5")) : key_5);
                newCon.put("key_6", key_6.isEmpty() ? cur.getString(cur.getColumnIndex("key_6")) : key_6);
                newCon.put("key_7", key_7.isEmpty() ? cur.getString(cur.getColumnIndex("key_7")) : key_7);
                newCon.put("key_8", key_8.isEmpty() ? cur.getString(cur.getColumnIndex("key_8")) : key_8);
                newCon.put("key_9", key_9.isEmpty() ? cur.getString(cur.getColumnIndex("key_9")) : key_9);
                newCon.put("key_0", key_0.isEmpty() ? cur.getString(cur.getColumnIndex("key_0")) : key_0);
                newCon.put("key_red", key_red.isEmpty() ? cur.getString(cur.getColumnIndex("key_red")) : key_red);
                newCon.put("key_green", key_green.isEmpty() ? cur.getString(cur.getColumnIndex("key_green")) : key_green);
                newCon.put("key_yellow", key_yellow.isEmpty() ? cur.getString(cur.getColumnIndex("key_yellow")) : key_yellow);
                newCon.put("key_blue", key_blue.isEmpty() ? cur.getString(cur.getColumnIndex("key_blue")) : key_blue);
                newCon.put("key_vol_up", key_vol_up.isEmpty() ? cur.getString(cur.getColumnIndex("key_vol_up")) : key_vol_up);
                newCon.put("key_vol_down", key_vol_down.isEmpty() ? cur.getString(cur.getColumnIndex("key_vol_down")) : key_vol_down);
                newCon.put("key_channel_up", key_channel_up.isEmpty() ? cur.getString(cur.getColumnIndex("key_channel_up")) : key_channel_up);
                newCon.put("key_channel_down", key_channel_down.isEmpty() ? cur.getString(cur.getColumnIndex("key_channel_down")) : key_channel_down);
                newCon.put("key_up", key_up.isEmpty() ? cur.getString(cur.getColumnIndex("key_up")) : key_up);
                newCon.put("key_down", key_down.isEmpty() ? cur.getString(cur.getColumnIndex("key_down")) : key_down);
                newCon.put("key_right", key_right.isEmpty() ? cur.getString(cur.getColumnIndex("key_right")) : key_right);
                newCon.put("key_left", key_left.isEmpty() ? cur.getString(cur.getColumnIndex("key_left")) : key_left);
                newCon.put("key_enter", key_enter.isEmpty() ? cur.getString(cur.getColumnIndex("key_enter")) : key_enter);
                newCon.put("key_rewind", key_rewind.isEmpty() ? cur.getString(cur.getColumnIndex("key_rewind")) : key_rewind);
                newCon.put("key_fast_forward", key_fast_forward.isEmpty() ? cur.getString(cur.getColumnIndex("key_fast_forward")) : key_fast_forward);
                newCon.put("key_play", key_play.isEmpty() ? cur.getString(cur.getColumnIndex("key_play")) : key_play);
                newCon.put("key_pause", key_pause.isEmpty() ? cur.getString(cur.getColumnIndex("key_pause")) : key_pause);
                newCon.put("key_next_chapter", key_next_chapter.isEmpty() ? cur.getString(cur.getColumnIndex("key_next_chapter")) : key_next_chapter);
                newCon.put("key_pref_chapter", key_pref_chapter.isEmpty() ? cur.getString(cur.getColumnIndex("key_pref_chapter")) : key_pref_chapter);
                newCon.put("key_custom_1", key_custom_1.isEmpty() ? cur.getString(cur.getColumnIndex("key_custom_1")) : key_custom_1);
                newCon.put("key_custom_2", key_custom_2.isEmpty() ? cur.getString(cur.getColumnIndex("key_custom_2")) : key_custom_2);
                newCon.put("key_custom_3", key_custom_3.isEmpty() ? cur.getString(cur.getColumnIndex("key_custom_3")) : key_custom_3);
                newCon.put("key_custom_4", key_custom_4.isEmpty() ? cur.getString(cur.getColumnIndex("key_custom_4")) : key_custom_4);
                newCon.put("key_custom_5", key_custom_5.isEmpty() ? cur.getString(cur.getColumnIndex("key_custom_5")) : key_custom_5);
                newCon.put("key_custom_6", key_custom_6.isEmpty() ? cur.getString(cur.getColumnIndex("key_custom_6")) : key_custom_6);
                newCon.put("key_custom_7", key_custom_7.isEmpty() ? cur.getString(cur.getColumnIndex("key_custom_7")) : key_custom_7);
                newCon.put("key_custom_8", key_custom_8.isEmpty() ? cur.getString(cur.getColumnIndex("key_custom_8")) : key_custom_8);
                newCon.put("key_custom_9", key_custom_9.isEmpty() ? cur.getString(cur.getColumnIndex("key_custom_9")) : key_custom_9);

                database.update(TABLE_NAME, newCon, DB_COLUMN_NAME + name, null);
            }
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRemote(String name) {
        try {
            open();
            database.delete(TABLE_NAME, DB_COLUMN_NAME + "=\"" + name + "\"", null);
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FileHelper.deleteDirectoryWithName(name);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + DB_COLUMN_NAME + " text primary key, vendor text not null, type text not null, key_on_off text not null," +
                " key_1 text not null, key_2 text not null, key_3 text not null, key_4 text not null, key_5 text not null, key_6 text not null, key_7 text not null, key_8 text not null," +
                " key_9 text not null, key_0 text not null, key_red text not null, key_green text not null, key_yellow text not null, key_blue text not null, key_vol_up text not null," +
                " key_vol_down text not null, key_channel_up text not null, key_channel_down text not null, key_up text not null, key_down text not null, key_right text not null," +
                " key_left text not null, key_enter text not null, key_rewind text not null, key_fast_forward text not null, key_play text not null, key_pause text not null," +
                " key_next_chapter text not null, key_pref_chapter text not null, key_custom_1 text not null, key_custom_2 text not null, key_custom_3 text not null," +
                " key_custom_4 text not null, key_custom_5 text not null, key_custom_6 text not null, key_custom_7 text not null, key_custom_8 text not null, key_custom_9 text not null);";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
