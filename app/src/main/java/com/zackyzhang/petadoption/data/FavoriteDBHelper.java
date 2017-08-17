package com.zackyzhang.petadoption.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zackyzhang.petadoption.data.FavoriteDataContract.FavoriteEntry;

/**
 * Created by lei on 8/16/17.
 */

public class FavoriteDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favoritepet.db";

    private static final int DATABASE_VERSION = 1;

    public FavoriteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_FAVORITE_TABLE =
            "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                    FavoriteEntry._ID                           + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FavoriteEntry.COLUMN_PET_ID                 + " INTEGER NOT NULL, "                 +
                    FavoriteEntry.COLUMN_PET_NAME               + " TEXT NOT NULL,"                     +
                    FavoriteEntry.COLUMN_PET_INFO               + " TEXT NOT NULL, "                    +
                    FavoriteEntry.COLUMN_PET_PHOTO              + " TEXT NOT NULL, "                    +
                    FavoriteEntry.COLUMN_PET_DATE               + " TEXT, "                             +
                    FavoriteEntry.COLUMN_PET_STATUS             + " TEXT, "                             +
                    FavoriteEntry.COLUMN_PET_AGE                + " TEXT, "                             +
                    FavoriteEntry.COLUMN_PET_SIZE               + " TEXT, "                             +
                    FavoriteEntry.COLUMN_PET_SHELTER_PET_ID     + " TEXT, "                             +
                    FavoriteEntry.COLUMN_PET_BREEDS             + " TEXT, "                             +
                    FavoriteEntry.COLUMN_PET_MEDIA              + " TEXT, "                             +
                    FavoriteEntry.COLUMN_PET_SEX                + " TEXT, "                             +
                    FavoriteEntry.COLUMN_PET_DESCRIPTION        + " TEXT, "                             +
                    FavoriteEntry.COLUMN_PET_MIX                + " TEXT, "                             +
                    FavoriteEntry.COLUMN_PET_SHELTER_ID         + " TEXT, "                             +
                    FavoriteEntry.COLUMN_PET_ANIMAL             + " TEXT, "                             +
                    " UNIQUE (" + FavoriteEntry.COLUMN_PET_ID   + ") ON CONFLICT REPLACE);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
