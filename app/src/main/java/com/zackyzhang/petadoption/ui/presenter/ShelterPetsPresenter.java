package com.zackyzhang.petadoption.ui.presenter;

import com.zackyzhang.petadoption.api.DataManager;
import com.zackyzhang.petadoption.api.model.PetBean;
import com.zackyzhang.petadoption.api.model.PetFindResponse;
import com.zackyzhang.petadoption.ui.base.ShelterPetsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by lei on 8/13/17.
 */

public class ShelterPetsPresenter implements ShelterPetsContract.Presenter {
    private static final String TAG = "ShelterPetsPresenter";

    private String mId;
    private ShelterPetsContract.View mView;
    private int count = 0;
    private List<PetBean> petsData = new ArrayList<>();
    private DataManager mDataManager;
    private String lastOffset;

    @Override
    public void onViewAttached(ShelterPetsContract.View view) {
        this.mView = view;
        this.count++;
        Timber.tag(TAG).d("View attached " + count + " times.");
    }

    @Override
    public void onViewDetached() {
        this.mView = null;
    }

    @Override
    public void onDestroyed() {

    }

    private void fetchData() {
        if (!petsData.isEmpty()) {
            Timber.tag(TAG).d("petsData is not empty, " + petsData.size());
            mView.loadData(petsData);
        } else {
            mDataManager = new DataManager<PetFindResponse>() {
                @Override
                public void onDataLoaded(PetFindResponse data) {
                    if (data != null && data.getPetfinder().getPets() != null) {
                        lastOffset = data.getPetfinder().getLastOffset();
                        petsData.addAll(data.getPetfinder().getPets());
                        Timber.tag(TAG).d(" pets size after addAll: " + petsData.size());
                        mView.loadData(petsData);
                    } else {
                        Timber.tag(TAG).d("data is null");
                        mView.loadData(null);
                    }
                }

                @Override
                public void noDataReturned() {
                    mView.loadData(null);
                }
            };
            Map<String, String> options = new HashMap<>();
            Timber.tag(TAG).d(mId);
            mDataManager.getShelterPets(mId, options);
        }
    }

    @Override
    public void setShelterId(String id) {
        mId = id;
        fetchData();
    }

    @Override
    public void fetchMoreData() {
        Map<String, String> options = new HashMap<>();
        options.put("offset", lastOffset);
        mDataManager.getShelterPets(mId, options);
    }
}
