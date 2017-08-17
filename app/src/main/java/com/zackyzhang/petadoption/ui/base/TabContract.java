package com.zackyzhang.petadoption.ui.base;

import com.zackyzhang.petadoption.api.model.PetBean;

import java.util.List;

/**
 * Created by lei on 8/8/17.
 */

public interface TabContract {

    interface View extends MvpContract.MvpView {
        void loadData(List<PetBean> pets);
    }

    interface Presenter extends MvpContract.MvpPresenter<View> {
        void fetchMoreData();
    }

}
