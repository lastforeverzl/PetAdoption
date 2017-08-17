package com.zackyzhang.petadoption.ui.presenter;

import com.zackyzhang.petadoption.PetAdoptionPreferences;
import com.zackyzhang.petadoption.ui.base.PresenterFactory;

/**
 * Created by lei on 8/13/17.
 */

public class SearchPresenterFactory implements PresenterFactory<SearchPresenter> {

    private PetAdoptionPreferences mPreferences;

    public SearchPresenterFactory(PetAdoptionPreferences preferences) {
        mPreferences = preferences;
    }

    @Override
    public SearchPresenter create() {
        return new SearchPresenter(mPreferences);
    }
}
