package com.zackyzhang.petadoption.ui.presenter;

import com.zackyzhang.petadoption.ApiUtils;
import com.zackyzhang.petadoption.data.PetAdoptionPreferences;
import com.zackyzhang.petadoption.data.RecentQuery;
import com.zackyzhang.petadoption.api.DataManager;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.api.model.PetFindResponse;
import com.zackyzhang.petadoption.ui.base.SearchContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by lei on 8/13/17.
 */

public class SearchPresenter implements SearchContract.Presenter {
    private static final String TAG = "SearchPresenter";

    private PetAdoptionPreferences myPreferences;
    private SearchContract.View mView;
    private int count = 0;
    private DataManager mDataManager;
    private String mZipCode;
    private String lastOffset;
    private List<PetBean> petsData = new ArrayList<>();
    private String type, size, sex, age;

    public SearchPresenter(PetAdoptionPreferences myPreferences) {
        this.myPreferences = myPreferences;
    }

    @Override
    public void onViewAttached(SearchContract.View view) {
        this.mView = view;
        this.count++;
        Timber.tag(TAG).d("View attached " + count + " times.");
        Timber.tag(TAG).d("pets size: " + petsData.size());
        if (!petsData.isEmpty() && mZipCode != null) {
            mView.loadData(petsData);
            mView.setZipCode(mZipCode);
        }
    }

    @Override
    public void onViewDetached() {
        this.mView = null;
    }

    @Override
    public void onDestroyed() {

    }

    @Override
    public void loadPets() {
        this.type = myPreferences.getAnimalType();
        this.size = myPreferences.getAnimalSize();
        this.sex = myPreferences.getAnimalSex();
        this.age = myPreferences.getAnimalAge();
        if (!petsData.isEmpty()) {
            Timber.tag(TAG).d("petsData is not empty, " + petsData.size());
            mView.loadData(petsData);
        } else {
            runDataManager();
        }
    }

    private void runDataManager() {
        mDataManager = new DataManager<PetFindResponse>() {
            @Override
            public void onDataLoaded(PetFindResponse data) {
                lastOffset = data.getPetfinder().getLastOffset();
                petsData.addAll(data.getPetfinder().getPets());
                Timber.tag(TAG).d(" pets size after addAll: " + petsData.size());
                mView.loadData(petsData);
            }

            @Override
            public void noDataReturned() {
                mView.loadData(null);
            }
        };
        Map<String, String> options;
        if (mZipCode != null) {
            options = ApiUtils.getPetFindOptions(type, size, sex, age);
            mDataManager.loadFindPets(mZipCode, options);
        } else {
            mView.loadData(null);
        }
    }

    @Override
    public void setRecentQuery(String name, String zipCode) {
        myPreferences.saveRecentQuery(name, zipCode);
    }

    @Override
    public void fetchMoreData() {
        Map<String, String> options;
        options = ApiUtils.getPetFindOptions(type, size, sex, age);
        options.put("offset", lastOffset);
        mDataManager.loadFindPets(mZipCode, options);
    }

    @Override
    public void setZipCode(String zipCode) {
        mZipCode = zipCode;
    }

    @Override
    public void getRecentQuery() {
        List<RecentQuery> queryList = myPreferences.getRecentQuery();
        if (queryList != null) {
            mView.loadRecentQuery(queryList);
        }
    }

    @Override
    public void clearRecentSearch() {
        myPreferences.clearRecentQueryList();
    }

    @Override
    public void loadPetsFromQuery(String zipCode, String type, String size, String sex, String age) {
        this.type = type;
        this.size = size;
        this.sex = sex;
        this.age = age;
        mZipCode = zipCode;
        runDataManager();
    }
}
