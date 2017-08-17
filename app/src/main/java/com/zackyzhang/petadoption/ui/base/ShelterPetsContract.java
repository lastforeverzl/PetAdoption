package com.zackyzhang.petadoption.ui.base;

import com.zackyzhang.petadoption.api.model.PetBean;

import java.util.List;

/**
 * Created by lei on 8/13/17.
 */

public interface ShelterPetsContract {
    interface View extends MvpContract.MvpView {
        void loadData(List<PetBean> pets);
    }

    interface Presenter extends MvpContract.MvpPresenter<View> {
        void setShelterId(String id);
        void fetchMoreData();
    }

}
