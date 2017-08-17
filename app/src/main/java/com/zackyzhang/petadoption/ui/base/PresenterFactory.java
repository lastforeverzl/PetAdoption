package com.zackyzhang.petadoption.ui.base;

/**
 * Created by lei on 8/5/17.
 */

public interface PresenterFactory<T extends MvpContract.MvpPresenter> {
    T create();
}