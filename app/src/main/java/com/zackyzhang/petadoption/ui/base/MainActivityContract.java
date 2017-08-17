package com.zackyzhang.petadoption.ui.base;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by lei on 8/8/17.
 */

public interface MainActivityContract {
    interface View extends MvpContract.MvpView {
        void loadZipCode(String zipCode);

        double[] getCurrentLocation(GoogleApiClient googleApiClient);

        String getZipCode(double[] latLng);
    }

    interface Presenter extends MvpContract.MvpPresenter<View> {

    }
}
