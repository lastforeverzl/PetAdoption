package com.zackyzhang.petadoption.ui.presenter;

import com.zackyzhang.petadoption.ui.base.PresenterFactory;

/**
 * Created by lei on 8/8/17.
 */

public class TabPresenterFactory implements PresenterFactory<TabPresenter> {

    private String animal;
    private String zipCode;

    public TabPresenterFactory(String animal, String zipCode) {
        this.animal = animal;
        this.zipCode = zipCode;
    }

    @Override
    public TabPresenter create() {
        return new TabPresenter(animal, zipCode);
    }
}
