package com.zackyzhang.petadoption.ui.presenter;

import com.zackyzhang.petadoption.ui.base.PresenterFactory;

/**
 * Created by lei on 8/8/17.
 */

public class MainPresenterFactory implements PresenterFactory<MainPresenter> {

    @Override
    public MainPresenter create() {
        return new MainPresenter();
    }
}
