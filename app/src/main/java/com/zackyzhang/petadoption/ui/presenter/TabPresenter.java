package com.zackyzhang.petadoption.ui.presenter;

import com.zackyzhang.petadoption.api.DataManager;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.api.model.PetFindResponse;
import com.zackyzhang.petadoption.ui.base.TabContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by lei on 8/8/17.
 */

public class TabPresenter implements TabContract.Presenter {
    private static final String TAG = "TabPresenter";

    private final String mAnimal;
    private String mZipCode;
    private TabContract.View mView;
    private int count = 0;
    private DataManager mDataManager;
    private String lastOffset;
    private List<PetBean> petsData = new ArrayList<>();

    public TabPresenter(String animal, String zipCode) {
        mAnimal = animal;
        mZipCode = zipCode;
    }

    @Override
    public void onViewAttached(TabContract.View view) {
        this.mView = view;
        this.count++;
        fetchData();
    }

    @Override
    public void onViewDetached() {
        Timber.tag(TAG).d("onViewDetached.");
        this.mView = null;
    }

    @Override
    public void onDestroyed() {

    }

    private void fetchData() {
//        if (petsData != null) {
        if (!petsData.isEmpty() && mView != null) {
            Timber.tag(TAG).d("petsData is not empty, " + petsData.size());
            mView.loadData(petsData);
        } else {
            mDataManager = new DataManager<PetFindResponse>() {
                @Override
                public void onDataLoaded(PetFindResponse data) {
                    lastOffset = data.getPetfinder().getLastOffset();
                    petsData.addAll(data.getPetfinder().getPets());
                    Timber.tag(TAG).d(mAnimal + " pets size after addAll: " + petsData.size());
                    if (mView != null)
                        mView.loadData(petsData);
                }
            };
            Map<String, String> options = new HashMap<>();
            if (mZipCode != null) {
                options.put("animal", mAnimal);
                mDataManager.loadFindPets(mZipCode, options);
            } else {
                mView.loadData(null);
            }
        }
    }

    @Override
    public void fetchMoreData() {
        Map<String, String> options = new HashMap<>();
        options.put("animal", mAnimal);
        options.put("offset", lastOffset);
        mDataManager.loadFindPets(mZipCode, options);
    }
}
