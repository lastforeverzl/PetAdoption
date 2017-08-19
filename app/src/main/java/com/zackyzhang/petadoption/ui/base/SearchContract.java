package com.zackyzhang.petadoption.ui.base;

import com.zackyzhang.petadoption.data.RecentQuery;
import com.zackyzhang.petadoption.api.model.PetBean;

import java.util.List;

/**
 * Created by lei on 8/13/17.
 */

public interface SearchContract {

    interface View extends MvpContract.MvpView {
        void loadData(List<PetBean> pets);

        void setZipCode(String zipCode);

        void loadRecentQuery(List<RecentQuery> recentQueryList);
    }

    interface Presenter extends MvpContract.MvpPresenter<View> {
        void loadPets();

        void setRecentQuery(String name, String zipCode);

        void fetchMoreData();

        void setZipCode(String zipCode);

        void getRecentQuery();

        void clearRecentSearch();

        void loadPetsFromQuery(String zipCode, String type, String size, String sex, String age);
    }
}
