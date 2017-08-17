package com.zackyzhang.petadoption.ui.presenter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.zackyzhang.petadoption.MyApplication;
import com.zackyzhang.petadoption.api.DataManager;
import com.zackyzhang.petadoption.api.GoogleApiHelper;
import com.zackyzhang.petadoption.api.LocationCallback;
import com.zackyzhang.petadoption.ui.base.MainActivityContract;

import timber.log.Timber;

/**
 * Created by lei on 8/8/17.
 */

public class MainPresenter implements MainActivityContract.Presenter, LocationCallback {
    private static final String TAG = "MainPresenter";

    private MainActivityContract.View mView;
    private GoogleApiHelper mGoogleApiHelper;
    private GoogleApiClient mGoogleApiClient;
    private double[] currentLatLng;
    private DataManager mFindPetsData;
    private int count = 0;
    private String mZipCode;

    public MainPresenter() {
        mGoogleApiHelper = MyApplication.getGoogleApiHelper();
        mGoogleApiHelper.setLocationCallback(this);
    }

    @Override
    public void onViewAttached(MainActivityContract.View view) {
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

    @Override
    public void onLocationApiConnected() {
        /* Test for Http request.
        if (petsData != null) {
            mView.loadData(petsData);
        } else {
            mGoogleApiClient = mGoogleApiHelper.getGoogleApiClient();
            currentLatLng = mView.getCurrentLocation(mGoogleApiClient);
            mFindPetsData = new DataManager() {
                @Override
                public void onDataLoaded(PetFindResponse data) {
                    Timber.tag(TAG).d("pets size: " + data.getPetfinder().getPets().size());
                    petsData = data;
                    mView.loadData(petsData);
                }
            };

            String zipCode = mView.getZipCode(currentLatLng);
            if (zipCode != null) {
                mFindPetsData.loadFindPets(zipCode, new HashMap<String, String>());
            } else {

            }
        }
        */
        Timber.tag(TAG).d("onLocationApiConnected");
        if (mZipCode == null) {
            Timber.tag(TAG).d("mZipCode is null");
            mGoogleApiClient = mGoogleApiHelper.getGoogleApiClient();
            currentLatLng = mView.getCurrentLocation(mGoogleApiClient);
            mZipCode = mView.getZipCode(currentLatLng);
        }
        mView.loadZipCode(mZipCode);
    }

}
