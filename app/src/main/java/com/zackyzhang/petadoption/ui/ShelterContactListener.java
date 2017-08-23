package com.zackyzhang.petadoption.ui;

/**
 * Created by lei on 8/22/17.
 */

public interface ShelterContactListener {
    void clickShelter(String id, String name, String phone, String email, String lat, String lng, String address);

    void callShelter(String number);

    void directToShelter(String lat, String lng, String address);

    void emailShelter(String email);
}
