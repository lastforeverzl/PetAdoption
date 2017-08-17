package com.zackyzhang.petadoption.ui.presenter;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.zackyzhang.petadoption.ApiUtils;
import com.zackyzhang.petadoption.Constants;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.data.FavoriteDataContract;
import com.zackyzhang.petadoption.ui.base.FavoriteContract;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


/**
 * Created by lei on 8/16/17.
 */

public class FavoritePresenter implements FavoriteContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "FavoritePresenter";

    private static final int ID_PETS_LOADER = 41;

    private FavoriteContract.View mView;
    private Activity mContext;
    private int count = 0;
    private List<PetBean> mPetList;

    public FavoritePresenter() {
        mPetList = new ArrayList<>();
    }

    @Override
    public void onViewAttached(FavoriteContract.View view) {
        this.mView = view;
        this.count++;
        Timber.tag(TAG).d("View attached " + count + " times.");
        mContext = mView.getActivityContext();
        mContext.getLoaderManager().initLoader(ID_PETS_LOADER, null, this);
    }

    @Override
    public void onViewDetached() {
        this.mView = null;
        Timber.tag(TAG).d("View detached!");
    }

    @Override
    public void onDestroyed() {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_PETS_LOADER:
                Uri petsQueryUri = FavoriteDataContract.FavoriteEntry.CONTENT_URI;
                return new CursorLoader(mContext,
                        petsQueryUri,
                        Constants.PETS_QUERY_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPetList.clear();
        data.moveToFirst();
        while (!data.isAfterLast()) {
//            Timber.tag(TAG).d("info: " + data.getString(Constants.INDEX_PET_INFO));
//            Timber.tag(TAG).d("photo: " + data.getString(Constants.INDEX_PET_PHOTO));
//            Timber.tag(TAG).d("breeds: " + data.getString(Constants.INDEX_PET_BREEDS));
//            Timber.tag(TAG).d("media: " + data.getString(Constants.INDEX_PET_MEDIA));
//
//            Timber.tag(TAG).d("id: " + data.getInt(Constants.INDEX_PET_ID));
//            Timber.tag(TAG).d("name: " + data.getString(Constants.INDEX_PET_NAME));
//            Timber.tag(TAG).d("date: " + data.getString(Constants.INDEX_PET_DATE));
//            Timber.tag(TAG).d("status: " + data.getString(Constants.INDEX_PET_STATUS));
//            Timber.tag(TAG).d("age: " + data.getString(Constants.INDEX_PET_AGE));
//            Timber.tag(TAG).d("size: " + data.getString(Constants.INDEX_PET_SIZE));
////            Timber.tag(TAG).d("shelter_pet_id: " + data.getString(Constants.INDEX_PET_SHELTER_PET_ID));
//            Timber.tag(TAG).d("sex: " + data.getString(Constants.INDEX_PET_SEX));
//            Timber.tag(TAG).d("description: " + data.getString(Constants.INDEX_PET_DESCRIPTION));
//            Timber.tag(TAG).d("mix: " + data.getString(Constants.INDEX_PET_MIX));
//            Timber.tag(TAG).d("shelter_id: " + data.getString(Constants.INDEX_PET_SHELTER_ID));
//            Timber.tag(TAG).d("animal: " + data.getString(Constants.INDEX_PET_ANIMAL));

            PetBean pet = new PetBean();
            pet.setId(new PetBean.IdBean(String.valueOf(data.getInt(Constants.INDEX_PET_ID))));
            pet.setName(new PetBean.NameBean(data.getString(Constants.INDEX_PET_NAME)));
            pet.setLastUpdate(new PetBean.LastUpdateBean(data.getString(Constants.INDEX_PET_DATE)));
            pet.setStatus(new PetBean.StatusBean(data.getString(Constants.INDEX_PET_STATUS)));
            pet.setAge(new PetBean.AgeBean(data.getString(Constants.INDEX_PET_AGE)));
            pet.setSize(new PetBean.SizeBean(data.getString(Constants.INDEX_PET_SIZE)));
//            pet.setShelterPetId(new PetBean.ShelterPetIdBean(data.getString(Constants.INDEX_PET_SHELTER_PET_ID)));
            pet.setSex(new PetBean.SexBean(data.getString(Constants.INDEX_PET_SEX)));
            pet.setDescription(new PetBean.DescriptionBean(data.getString(Constants.INDEX_PET_DESCRIPTION)));
            pet.setMix(new PetBean.MixBean(data.getString(Constants.INDEX_PET_MIX)));
            pet.setShelterId(new PetBean.ShelterIdBean(data.getString(Constants.INDEX_PET_SHELTER_ID)));
            pet.setAnimal(new PetBean.AnimalBean(data.getString(Constants.INDEX_PET_ANIMAL)));

            PetBean.BreedsBean breeds = new PetBean.BreedsBean();
            List<PetBean.BreedsBean.BreedBean> breedList =
                    ApiUtils.getBreedsList(data.getString(Constants.INDEX_PET_BREEDS));
            breeds.setBreed(breedList);
            pet.setBreeds(breeds);

            PetBean.MediaBean medias = new PetBean.MediaBean();
            PetBean.MediaBean.PhotosBean photosBean = new PetBean.MediaBean.PhotosBean();
            List<PetBean.MediaBean.PhotosBean.PhotoBean> photoList =
                    ApiUtils.getPhotoList(data.getString(Constants.INDEX_PET_MEDIA));
            photosBean.setPhoto(photoList);
            medias.setPhotos(photosBean);
            pet.setMedia(medias);
            mPetList.add(pet);
            data.moveToNext();
        }
        Log.d(TAG, "cursor finished");
        if (mView != null) {
            mView.loadData(mPetList);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "in onLoaderReset");
    }

}
