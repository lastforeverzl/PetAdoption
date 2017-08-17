package com.zackyzhang.petadoption.ui.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.zackyzhang.petadoption.ApiUtils;
import com.zackyzhang.petadoption.Constants;
import com.zackyzhang.petadoption.api.DataManager;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.api.model.ShelterGetResponse;
import com.zackyzhang.petadoption.data.FavoriteDataContract;
import com.zackyzhang.petadoption.ui.base.PetDetailContract;

import timber.log.Timber;

/**
 * Created by lei on 8/10/17.
 */

public class PetDetailPresenter implements PetDetailContract.Presenter {
    private static final String TAG = "PetDetailPresenter";

    private Context mContext;
    private PetDetailContract.View mView;
    private DataManager mDataManager;
    private int count = 0;
    private PetBean pet;

    @Override
    public void onViewAttached(PetDetailContract.View view) {
        this.mView = view;
        this.count++;
        Timber.tag(TAG).d("View attached " + count + " times.");
        mContext = mView.getActivityContext();
    }

    @Override
    public void onViewDetached() {
        this.mView = null;
    }

    @Override
    public void onDestroyed() {

    }

    @Override
    public void setShelterId(String id) {
        mDataManager = new DataManager<ShelterGetResponse>() {

            @Override
            public void onDataLoaded(ShelterGetResponse data) {
                Timber.tag(TAG).d(data.getPetfinder().getShelter().getName());
                mView.loadData(data.getPetfinder().getShelter());
            }
        };
        mDataManager.getShelterById(id);
    }

    @Override
    public void addFavorite() {
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
        String breeds = ApiUtils.getBreedsString(pet);
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_BREEDS, breeds);
        String urls = ApiUtils.getPhotoUrlsString(pet);
        contentValues.put(FavoriteDataContract.FavoriteEntry.COLUMN_PET_MEDIA, urls);
        Uri uri = mContext.getContentResolver().insert(FavoriteDataContract.FavoriteEntry.CONTENT_URI, contentValues);
        Timber.tag(TAG).d("uri: " + uri);
        mView.inFavorite();
    }

    @Override
    public void removeFavorite() {
        Uri uri = FavoriteDataContract.FavoriteEntry.buildPetUri(Long.valueOf(pet.getId()));
        mContext.getContentResolver().delete(uri, null, null);
        mView.notInFavorite();
    }

    @Override
    public void checkFavorite(final PetBean pet) {
        this.pet = pet;
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = FavoriteDataContract.FavoriteEntry.buildPetUri(Long.valueOf(pet.getId()));
                Cursor cursor = mContext.getContentResolver().query(uri,
                        Constants.CHECK_FAVORITE_PROJECTION,
                        null,
                        null,
                        null);
                if (cursor == null || cursor.getCount() == 0) {
                    mView.notInFavorite();
                } else {
                    mView.inFavorite();
                }
                cursor.close();
            }
        });
        checkForEmpty.start();
    }
}
