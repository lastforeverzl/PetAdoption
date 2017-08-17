package com.zackyzhang.petadoption.ui.base;

import com.zackyzhang.petadoption.api.model.ShelterBean;

import java.util.List;

/**
 * Created by lei on 8/11/17.
 */

public interface SheltersContract {
    interface View extends MvpContract.MvpView {
        void loadData(List<ShelterBean> shelters);
    }

    interface Presenter extends MvpContract.MvpPresenter<View> {
        void fetchMoreData();
    }

}
