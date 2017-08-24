package com.zackyzhang.petadoption.ui.presenter;

import com.zackyzhang.petadoption.api.DataManager;
import com.zackyzhang.petadoption.api.model.ShelterBean;
import com.zackyzhang.petadoption.api.model.ShelterFindResponse;
import com.zackyzhang.petadoption.ui.base.SheltersContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by lei on 8/11/17.
 */

public class SheltersPresenter implements SheltersContract.Presenter {
    private static final String TAG = "SheltersPresenter";

    private SheltersContract.View mView;
    private String mZipCode;
    private int count = 0;
    private DataManager mDataManager;
    private String lastOffset;
    private List<ShelterBean> sheltersData = new ArrayList<>();

    public SheltersPresenter() {
    }

    @Override
    public void onViewAttached(SheltersContract.View view) {
        this.mView = view;
        this.count++;
        Timber.tag(TAG).d("View attached " + count + " times.");
        if (mZipCode == null && sheltersData.isEmpty()) {
            mView.checkPermission();
        } else {
            mView.loadData(sheltersData);
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
    public void setZipCode(String zipCode) {
        this.mZipCode = zipCode;
        fetchData();
    }

    private void fetchData() {
        if (!sheltersData.isEmpty() && mView != null) {
            Timber.tag(TAG).d("sheltersData is not empty, " + sheltersData.size());
            mView.loadData(sheltersData);
        } else {
            mDataManager = new DataManager<ShelterFindResponse>() {
                @Override
                public void onDataLoaded(ShelterFindResponse data) {
                    lastOffset = data.getPetfinder().getLastOffset();
                    sheltersData.addAll(data.getPetfinder().getShelters());
                    Timber.tag(TAG).d("shelters size after addAll: " + sheltersData.size());
                    if (mView != null)
                        mView.loadData(sheltersData);
                }
            };
            Map<String, String> options = new HashMap<>();
            if (mZipCode != null) {
                mDataManager.getFindShelters(mZipCode, options);
            } else {
                mView.loadData(null);
            }
        }
    }

    @Override
    public void fetchMoreData() {
        Map<String, String> options = new HashMap<>();
        options.put("offset", lastOffset);
        mDataManager.getFindShelters(mZipCode, options);
    }
}
