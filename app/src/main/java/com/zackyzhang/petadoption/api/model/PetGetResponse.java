package com.zackyzhang.petadoption.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lei on 8/19/17.
 */

public class PetGetResponse extends BaseResponse{

    @SerializedName("petfinder")
    private PetGetBean petGet;

    public PetGetBean getPetGet() {
        return petGet;
    }

    public void setPetGet(PetGetBean petfinder) {
        this.petGet = petfinder;
    }

    public static class PetGetBean {
        @SerializedName("pet")
        private PetBean pet;

        public PetBean getPet() {
            return pet;
        }

        public void setPet(PetBean pet) {
            this.pet = pet;
        }

    }
}
