package com.zackyzhang.petadoption.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import timber.log.Timber;

/**
 * Created by lei on 8/8/17.
 */

public abstract class BasePresenterFragment<P extends MvpContract.MvpPresenter<V>, V extends MvpContract.MvpView>
        extends Fragment {

    private static final String TAG = "BasePresenterFragment";
    private static final int LOADER_ID = 101;

    private P presenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.tag(TAG).d("onActivityCreated-" + tag());

        Loader<P> loader = getLoaderManager().getLoader(loaderId());
        if (loader == null) {
            initLoader();
        } else {
            this.presenter = ((PresenterLoader<P>) loader).getPresenter();
            onPresenterCreatedOrRestored(presenter);
        }
    }

    private void initLoader() {
        // LoaderCallbacks as an object, so no hint regarding loader will be leak to the subclasses.
        getLoaderManager().initLoader(loaderId(), null, new LoaderManager.LoaderCallbacks<P>() {
            @Override
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                Timber.tag(TAG).d("onCreateLoader-" + tag());
                return new PresenterLoader<>(getContext(), getPresenterFactory(), tag());
            }

            @Override
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                Timber.tag(TAG).d("onLoadFinished-" + tag());
                BasePresenterFragment.this.presenter = presenter;
                onPresenterCreatedOrRestored(presenter);
            }

            @Override
            public final void onLoaderReset(Loader<P> loader) {
                Timber.tag(TAG).d("onLoaderReset-" + tag());
                BasePresenterFragment.this.presenter = null;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.tag(TAG).d("onStart-" + tag());
        presenter.onViewAttached(getPresenterView());
    }

    @Override
    public void onStop() {
        presenter.onViewDetached();
        super.onStop();
        Timber.tag(TAG).d("onStop-" + tag());
    }

    /**
     * String tag use for log purposes.
     */
    @NonNull
    protected abstract String tag();

    /**
     * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance should
     * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();

    /**
     * Hook for subclasses that deliver the {@link com.zackyzhang.petadoption.ui.base.MvpContract.MvpPresenter} before its View is attached.
     * Can be use to initialize the Presenter or simple hold a reference to it.
     */
    protected abstract void onPresenterCreatedOrRestored(@NonNull P presenter);

    /**
     * Override in case of fragment not implementing Presenter<View> interface
     */
    @NonNull
    protected V getPresenterView() {
        return (V) this;
    }

    /**
     * Use this method in case you want to specify a spefic ID for the {@link PresenterLoader}.
     * By default its value would be {@link #LOADER_ID}.
     */
    protected int loaderId() {
        return LOADER_ID;
    }
}
