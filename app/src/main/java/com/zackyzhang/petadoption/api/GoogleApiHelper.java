package com.zackyzhang.petadoption.api;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;

import timber.log.Timber;

/**
 * Created by lei on 8/5/17.
 */

public class GoogleApiHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static GoogleApiHelper sInstance;

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LocationCallback mLocationCallback;

    private GoogleApiHelper(Context context) {
        this.mContext = context;
        buildGoogleApiClient();
    }

    public static GoogleApiHelper instance(Context context) {
        if (sInstance == null) {
            sInstance = new GoogleApiHelper(context);
            return sInstance;
        }
        return sInstance;
    }

    public GoogleApiClient getGoogleApiClient() {
        return this.mGoogleApiClient;
    }

    public void connect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    public boolean isConnected() {
        if (mGoogleApiClient != null) {
            return mGoogleApiClient.isConnected();
        } else {
            return false;
        }
    }

    public void setLocationCallback(LocationCallback locationCallback) {
        this.mLocationCallback = locationCallback;
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.tag("OkHttp").d("google api client connected");
//        mGoogleApiListener.googleApiConnected();
        mLocationCallback.onLocationApiConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // TODO: 8/5/17
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // TODO: 8/5/17 handle connection failed
    }

}
