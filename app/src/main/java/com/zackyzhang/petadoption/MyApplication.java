package com.zackyzhang.petadoption;

import android.app.Application;

import com.zackyzhang.petadoption.api.GoogleApiHelper;

import timber.log.Timber;

/**
 * Created by lei on 8/4/17.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private GoogleApiHelper mGoogleApiHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        mInstance = this;
        mGoogleApiHelper = GoogleApiHelper.instance(mInstance);
    }

    private static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public static GoogleApiHelper getGoogleApiHelper() {
        return getInstance().mGoogleApiHelper;
    }
}
