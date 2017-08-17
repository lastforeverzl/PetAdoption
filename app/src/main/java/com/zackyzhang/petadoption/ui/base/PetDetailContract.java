package com.zackyzhang.petadoption.ui.base;

import android.content.Context;

import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.api.model.ShelterBean;

/**
 * Created by lei on 8/10/17.
 */

public interface PetDetailContract {
    interface View extends MvpContract.MvpView {
        void loadData(ShelterBean shelter);

        Context getActivityContext();

        void notInFavorite();

        void inFavorite();

    }

    interface Presenter extends MvpContract.MvpPresenter<View> {
        void setShelterId(String id);

        void addFavorite();

        void removeFavorite();

        void checkFavorite(PetBean pet);
    }


}
