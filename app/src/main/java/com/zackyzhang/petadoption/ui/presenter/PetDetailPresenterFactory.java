package com.zackyzhang.petadoption.ui.presenter;

import com.zackyzhang.petadoption.ui.base.PresenterFactory;

/**
 * Created by lei on 8/10/17.
 */

public class PetDetailPresenterFactory implements PresenterFactory<PetDetailPresenter> {

    @Override
    public PetDetailPresenter create() {
        return new PetDetailPresenter();
    }
}
