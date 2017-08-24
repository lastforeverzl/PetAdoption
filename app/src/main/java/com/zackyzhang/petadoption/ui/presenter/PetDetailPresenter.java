package com.zackyzhang.petadoption.ui.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.zackyzhang.petadoption.Constants;
import com.zackyzhang.petadoption.FavoritePetService;
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
        FavoritePetService.startActionAddFavorite(mContext, pet);
        mView.inFavorite();
    }

    @Override
    public void removeFavorite() {
        FavoritePetService.startActionRemoveFavorite(mContext, pet.getId());
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
