package com.zackyzhang.petadoption.ui.presenter;

import com.zackyzhang.petadoption.ui.base.PresenterFactory;

/**
 * Created by lei on 8/16/17.
 */

public class FavoritePresenterFactory implements PresenterFactory<FavoritePresenter> {

    @Override
    public FavoritePresenter create() {
        return new FavoritePresenter();
    }
}
