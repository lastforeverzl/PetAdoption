package com.zackyzhang.petadoption.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lei on 8/11/17.
 */

public class ShelterFindResponse extends BaseResponse {

    @SerializedName("petfinder")
    private ShelterFindBean petfinder;

    public ShelterFindBean getPetfinder() {
        return petfinder;
    }

    public void setPetfinder(ShelterFindBean petfinder) {
        this.petfinder = petfinder;
    }

    public static class ShelterFindBean {
        @SerializedName("lastOffset")
        private LastOffsetBean lastOffset;
        @SerializedName("shelters")
        private SheltersBean shelters;

        public String getLastOffset() {
            return lastOffset.getValue();
        }

        public void setLastOffset(LastOffsetBean lastOffset) {
            this.lastOffset = lastOffset;
        }

        public List<ShelterBean> getShelters() {
            return shelters.getShelter();
        }

        public void setShelters(SheltersBean shelters) {
            this.shelters = shelters;
        }

        public static class LastOffsetBean {
            @SerializedName("value")
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public static class SheltersBean {
            @SerializedName("shelter")
            private List<ShelterBean> shelters;

            public List<ShelterBean> getShelter() {
                return shelters;
            }

            public void setShelter(List<ShelterBean> shelters) {
                this.shelters = shelters;
            }

        }

    }
}
