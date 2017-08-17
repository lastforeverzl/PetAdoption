package com.zackyzhang.petadoption.ui.base;

/**
 * Created by lei on 8/8/17.
 */

public interface MvpContract {
    interface MvpView {

    }

    interface MvpPresenter<V extends MvpView> {
        void onViewAttached(V view);
        void onViewDetached();
        void onDestroyed();
    }

}
