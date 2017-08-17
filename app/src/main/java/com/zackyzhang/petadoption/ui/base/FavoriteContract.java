package com.zackyzhang.petadoption.ui.base;

import android.app.Activity;

import com.zackyzhang.petadoption.api.model.PetBean;

import java.util.List;

/**
 * Created by lei on 8/16/17.
 */

public interface FavoriteContract  {
    interface View extends MvpContract.MvpView {
        Activity getActivityContext();

        void loadData(List<PetBean> pets);
    }

    interface Presenter extends MvpContract.MvpPresenter<View> {

    }

}
