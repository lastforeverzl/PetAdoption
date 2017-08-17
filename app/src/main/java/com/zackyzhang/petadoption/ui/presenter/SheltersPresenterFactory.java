package com.zackyzhang.petadoption.ui.presenter;

import com.zackyzhang.petadoption.ui.base.PresenterFactory;

/**
 * Created by lei on 8/11/17.
 */

public class SheltersPresenterFactory implements PresenterFactory<SheltersPresenter> {

    private String mZipCode;

    public SheltersPresenterFactory(String zipCode) {
        mZipCode = zipCode;
    }

    @Override
    public SheltersPresenter create() {
        return new SheltersPresenter(mZipCode);
    }
}
