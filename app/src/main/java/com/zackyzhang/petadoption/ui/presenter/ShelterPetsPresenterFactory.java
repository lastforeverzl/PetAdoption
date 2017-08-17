package com.zackyzhang.petadoption.ui.presenter;

import com.zackyzhang.petadoption.ui.base.PresenterFactory;

/**
 * Created by lei on 8/13/17.
 */

public class ShelterPetsPresenterFactory implements PresenterFactory<ShelterPetsPresenter> {

    @Override
    public ShelterPetsPresenter create() {
        return new ShelterPetsPresenter();
    }
}
