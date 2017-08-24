package com.zackyzhang.petadoption.ui;

import android.view.View;

import com.zackyzhang.petadoption.api.model.PetBean;

/**
 * Created by lei on 8/16/17.
 */

public interface PetOnClickHandler {
    void onItemClick(PetBean pet, View transitionView);
}
