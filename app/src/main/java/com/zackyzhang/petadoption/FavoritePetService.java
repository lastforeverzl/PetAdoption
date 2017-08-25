package com.zackyzhang.petadoption;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.zackyzhang.petadoption.api.DataManager;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.api.model.PetGetResponse;
import com.zackyzhang.petadoption.data.FavoriteDataContract;
import com.zackyzhang.petadoption.widget.FavoritePetWidgetProvider;

import timber.log.Timber;

/**
 * Created by lei on 8/17/17.
 */

public class FavoritePetService extends IntentService {

    public static final String ACTION_ADD_FAVORITE = "com.zackyzhang.petadoption.action.add_favorite";
    public static final String ACTION_REMOVE_FAVORITE = "com.zackyzhang.petadoption.action.remove_favorite";
    public static final String ACTION_UPDATE_FAVORITE_PET_WIDGETS = "com.zackyzhang.petadoption.action.update_favorite_widgets";
    public static final String ACTION_UPDATE_PET_STATUS = "com.zackyzhang.petadoption.action.update_pet_status";
    public static final String EXTRA_ADD_PET = "com.zackyzhang.petadoption.extra.add_pet";
    public static final String EXTRA_REMOVE_PET_ID = "com.zackyzhang.petadoption.extra.remove_pet_id";

    public FavoritePetService() {
        super("FavoritePetService");
    }

    public static void startActionAddFavorite(Context context, PetBean pet) {
        Intent intent = new Intent(context, FavoritePetService.class);
        intent.setAction(ACTION_ADD_FAVORITE);
        intent.putExtra(EXTRA_ADD_PET, pet);
        context.startService(intent);
    }

    public static void startActionRemoveFavorite(Context context, String id) {
        Intent intent = new Intent(context, FavoritePetService.class);
        intent.setAction(ACTION_REMOVE_FAVORITE);
        intent.putExtra(EXTRA_REMOVE_PET_ID, id);
        context.startService(intent);
    }

    public static void startActionFavoritePetWidgets(Context context) {
        Intent intent = new Intent(context, FavoritePetService.class);
        intent.setAction(ACTION_UPDATE_FAVORITE_PET_WIDGETS);
        context.startService(intent);
    }

    public static void startActionUpdatePetStatus(Context context) {
        Intent intent = new Intent(context, FavoritePetService.class);
        intent.setAction(ACTION_UPDATE_PET_STATUS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ADD_FAVORITE.equals(action)) {
                PetBean pet = intent.getParcelableExtra(EXTRA_ADD_PET);
                handleActionAddFavorite(pet);
            } else if (ACTION_REMOVE_FAVORITE.equals(action)) {
                String id = intent.getStringExtra(EXTRA_REMOVE_PET_ID);
                handleActionRemoveFavorite(id);
            } else if (ACTION_UPDATE_FAVORITE_PET_WIDGETS.equals(action)) {
                handleActionUpdateFavoriteWidget();
            } else if (ACTION_UPDATE_PET_STATUS.equals(action)) {
                handleActionUpdatePetStatus();
            }
        }
    }

    private void handleActionAddFavorite(PetBean pet) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_ID, Integer.valueOf(pet.getId()));
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_NAME, pet.getName());
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_DATE, pet.getLastUpdate());
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_STATUS, pet.getStatus());
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_AGE, pet.getAge());
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_SIZE, pet.getSize());
//        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_SHELTER_PET_ID, pet.getShelterPetId());
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_SEX, pet.getSex());
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_DESCRIPTION, pet.getDescription());
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_MIX, pet.getMix());
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_SHELTER_ID, pet.getShelterId());
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_ANIMAL, pet.getAnimal());
        String info = ApiUtils.getPetInfo(pet);
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_INFO, info);
        String url = ApiUtils.getFirstPhotoUrl(pet);
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_PHOTO, url);
        String breeds = ApiUtils.getBreedsWithSemiColon(pet);
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_BREEDS, breeds);
        String urls = ApiUtils.getPhotoUrlsString(pet);
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_MEDIA, urls);
        getContentResolver().insert(FavoriteDataContract.FavoriteEntry.CONTENT_URI, contentValues);

        startActionFavoritePetWidgets(this);
    }

    private void handleActionRemoveFavorite(String id) {
        Uri uri = FavoriteDataContract.FavoriteEntry.buildPetUri(Long.valueOf(id));
        getContentResolver().delete(uri, null, null);

        startActionFavoritePetWidgets(this);
    }

    private void handleActionUpdateFavoriteWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, FavoritePetWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        FavoritePetWidgetProvider.updateFavoritePetWidgets(this, appWidgetManager, appWidgetIds);
    }

    private void handleActionUpdatePetStatus() {
        int petId;
        String petStatus;
        String petLastUpdate;
        DataManager dataManager = new DataManager<PetGetResponse>() {
            @Override
            public void onDataLoaded(PetGetResponse data) {}

            @Override
            public void noDataReturned() {

            }
        };
        Uri petsQueryUri = FavoriteDataContract.FavoriteEntry.CONTENT_URI;
        Cursor cursor = getContentResolver().query(
                petsQueryUri,
                Constants.PETS_QUERY_PROJECTION,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            petId = cursor.getInt(Constants.INDEX_PET_ID);
            petStatus = cursor.getString(Constants.INDEX_PET_STATUS);
            petLastUpdate = cursor.getString(Constants.INDEX_PET_DATE);
            Timber.tag("FirebaseJob").d("DB: " + petId + ", " + petStatus + ", " + petLastUpdate);
            PetGetResponse response = dataManager.getSinglePet(String.valueOf(petId));
            if (response != null) {
                Timber.tag("FirebaseJob").d("HTTP: " + response.getPetGet().getPet().getStatus()
                                + ", " + response.getPetGet().getPet().getLastUpdate());
                String newStatus = response.getPetGet().getPet().getStatus();
                String newLastUpdate = response.getPetGet().getPet().getLastUpdate();
                if (!petStatus.equals(newStatus) || !petLastUpdate.equals(newLastUpdate)) {
                    Uri uri = FavoriteDataContract.FavoriteEntry.buildPetUri(Long.valueOf(petId));
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_STATUS, newStatus);
                    contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_DATE, newLastUpdate);
                    getContentResolver().update(uri, contentValues, null, null);
                } else {
                    Timber.tag("FirebaseJob").d("status or update is not change");
                }
            }
            cursor.moveToNext();
        }
        startActionFavoritePetWidgets(this);
        Intent updateFinishedIntent = new Intent(WidgetUpdateJobService.ACTION_UPDATE_FINISHED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(updateFinishedIntent);
    }
}
