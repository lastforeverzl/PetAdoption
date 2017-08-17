package com.zackyzhang.petadoption.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lei on 8/10/17.
 */

public class ShelterGetResponse extends BaseResponse{

    @SerializedName("petfinder")
    private PetfinderBean petfinder;

    public PetfinderBean getPetfinder() {
        return petfinder;
    }

    public void setPetfinder(PetfinderBean petfinder) {
        this.petfinder = petfinder;
    }

    public static class PetfinderBean {
        @SerializedName("shelter")
        private ShelterBean shelter;

        public ShelterBean getShelter() {
            return shelter;
        }

        public void setShelter(ShelterBean shelter) {
            this.shelter = shelter;
        }

    }
}
