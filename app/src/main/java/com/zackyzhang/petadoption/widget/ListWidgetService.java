package com.zackyzhang.petadoption.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squareup.picasso.Picasso;
import com.zackyzhang.petadoption.ApiUtils;
import com.zackyzhang.petadoption.Constants;
import com.zackyzhang.petadoption.R;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.data.FavoriteDataContract;
import com.zackyzhang.petadoption.ui.activity.PetDetailActivity;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

/**
 * Created by lei on 8/17/17.
 */

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "ListRemoteViewsFactory";

    Context mContext;
    Cursor mCursor;
    private int appWidgetId;

    public ListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Timber.tag(TAG).d("onDataSetChanged");
        Uri petsQueryUri = FavoriteDataContract.FavoriteEntry.CONTENT_URI;
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(
                petsQueryUri,
                Constants.PETS_QUERY_PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) {
            Timber.tag(TAG).d("mCursor is null");
            return null;
        }
        mCursor.moveToPosition(position);
        final PetBean pet = new PetBean();
        pet.setId(new PetBean.IdBean(String.valueOf(mCursor.getInt(Constants.INDEX_PET_ID))));
        pet.setName(new PetBean.NameBean(mCursor.getString(Constants.INDEX_PET_NAME)));
        pet.setLastUpdate(new PetBean.LastUpdateBean(mCursor.getString(Constants.INDEX_PET_DATE)));
        pet.setStatus(new PetBean.StatusBean(mCursor.getString(Constants.INDEX_PET_STATUS)));
        pet.setAge(new PetBean.AgeBean(mCursor.getString(Constants.INDEX_PET_AGE)));
        pet.setSize(new PetBean.SizeBean(mCursor.getString(Constants.INDEX_PET_SIZE)));
//            pet.setShelterPetId(new PetBean.ShelterPetIdBean(data.getString(Constants.INDEX_PET_SHELTER_PET_ID)));
        pet.setSex(new PetBean.SexBean(mCursor.getString(Constants.INDEX_PET_SEX)));
        pet.setDescription(new PetBean.DescriptionBean(mCursor.getString(Constants.INDEX_PET_DESCRIPTION)));
        pet.setMix(new PetBean.MixBean(mCursor.getString(Constants.INDEX_PET_MIX)));
        pet.setShelterId(new PetBean.ShelterIdBean(mCursor.getString(Constants.INDEX_PET_SHELTER_ID)));
        pet.setAnimal(new PetBean.AnimalBean(mCursor.getString(Constants.INDEX_PET_ANIMAL)));

        PetBean.BreedsBean breeds = new PetBean.BreedsBean();
        List<PetBean.BreedsBean.BreedBean> breedList =
                ApiUtils.getBreedsList(mCursor.getString(Constants.INDEX_PET_BREEDS));
        breeds.setBreed(breedList);
        pet.setBreeds(breeds);

        PetBean.MediaBean medias = new PetBean.MediaBean();
        PetBean.MediaBean.PhotosBean photosBean = new PetBean.MediaBean.PhotosBean();
        List<PetBean.MediaBean.PhotosBean.PhotoBean> photoList =
                ApiUtils.getPhotoList(mCursor.getString(Constants.INDEX_PET_MEDIA));
        photosBean.setPhoto(photoList);
        medias.setPhotos(photosBean);
        pet.setMedia(medias);

        final RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.favorite_pet_widget);

        try {
            Bitmap b = Picasso.with(mContext).load(ApiUtils.getFirstPhotoUrl(pet)).get();
            views.setImageViewBitmap(R.id.iv_photo, b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: 8/18/17 display location info in widget
        views.setTextViewText(R.id.tv_pet_name, pet.getName());
        views.setTextViewText(R.id.tv_info, ApiUtils.getBreedString(pet));
        views.setTextViewText(R.id.tv_status, ApiUtils.getAdoptionStatus(pet));
        views.setTextViewText(R.id.tv_location, "Dublin");
        views.setTextViewText(R.id.tv_updated_date, ApiUtils.getLastUpdate(pet));

        Bundle extras = new Bundle();
        extras.putParcelable(PetDetailActivity.EXTRA_PET_DETAIL, pet);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.pet_container, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
