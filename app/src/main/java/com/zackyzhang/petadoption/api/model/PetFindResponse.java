package com.zackyzhang.petadoption.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lei on 8/3/17.
 */

public class PetFindResponse extends BaseResponse {

    @SerializedName("petfinder")
    private PetfinderBean petfinder;

    public PetfinderBean getPetfinder() {
        return petfinder;
    }

    public void setPetfinder(PetfinderBean petfinder) {
        this.petfinder = petfinder;
    }

    public static class PetfinderBean {
        @SerializedName("lastOffset")
        private LastOffsetBean lastOffset;
        @SerializedName("pets")
        private PetsBean pets;
        @SerializedName("header")
        private HeaderBean header;

        public HeaderBean getHeader() {
            return header;
        }

        public String getResponseCode() {
            return getHeader().getStatus();
        }

        public void setHeader(HeaderBean header) {
            this.header = header;
        }

        public String getLastOffset() {
            return lastOffset.getValue();
        }

        public void setLastOffset(LastOffsetBean lastOffset) {
            this.lastOffset = lastOffset;
        }

        public List<PetBean> getPets() {
            return pets.getPet();
        }

        public void setPets(PetsBean pets) {
            this.pets = pets;
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

        public static class PetsBean {
            @SerializedName("pet")
            private List<PetBean> pets;

            public List<PetBean> getPet() {
                return pets;
            }

            public void setPet(List<PetBean> pets) {
                this.pets = pets;
            }

        }

    }
}
