package com.zackyzhang.petadoption.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zackyzhang.petadoption.data.FavoriteDataContract.FavoriteEntry;

import java.util.ArrayList;
import java.util.Arrays;

import timber.log.Timber;

/**
 * Created by lei on 8/16/17.
 */

public class FavoriteProvider extends ContentProvider {
    private static final String TAG = "FavoriteProvider";

    public static final int CODE_FAVORITE = 100;
    public static final int CODE_FAVORITE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteDBHelper mFavoriteDBHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteDataContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoriteDataContract.PATH_FAVORITE, CODE_FAVORITE);
        matcher.addURI(authority, FavoriteDataContract.PATH_FAVORITE + "/#", CODE_FAVORITE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mFavoriteDBHelper = new FavoriteDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        final SQLiteDatabase db = mFavoriteDBHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE:
                cursor = db.query(FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE_FAVORITE_WITH_ID:
                String petId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{ petId };
                cursor = db.query(FavoriteEntry.TABLE_NAME,
                        projection,
                        FavoriteEntry.COLUMN_PET_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mFavoriteDBHelper.getWritableDatabase();
        long _id;
        Uri returnId;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE:
                _id = db.insert(FavoriteEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnId = FavoriteEntry.buildPetUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new android.database.SQLException("Unknown uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnId;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mFavoriteDBHelper.getWritableDatabase();
        int rowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE_WITH_ID:
                String id = uri.getLastPathSegment();
                Timber.tag(TAG).d("delete id: " + id);
                rowsDeleted = db.delete(FavoriteEntry.TABLE_NAME,
                        FavoriteEntry.COLUMN_PET_ID + " = ? ",
                        new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavoriteDBHelper.getWritableDatabase();
        int petsUpdated;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE:
                petsUpdated = db.update(FavoriteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_FAVORITE_WITH_ID:
                if (selection == null) {
                    selection = FavoriteEntry.COLUMN_PET_ID + "=?";
                } else {
                    selection += " AND " + FavoriteEntry.COLUMN_PET_ID + "=?";
                }
                String id = uri.getLastPathSegment();
                if (selectionArgs == null) {
                    selectionArgs = new String[]{id};
                } else {
                    ArrayList<String> selectionArgsList = new ArrayList<String>();
                    selectionArgsList.addAll(Arrays.asList(selectionArgs));
                    selectionArgsList.add(id);
                    selectionArgs = selectionArgsList.toArray(new String[selectionArgsList.size()]);
                }
                petsUpdated = db.update(FavoriteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (petsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return petsUpdated;
    }
}
