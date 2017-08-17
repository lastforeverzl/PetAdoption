package com.zackyzhang.petadoption.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lei on 8/10/17.
 */

public class ShelterBean {
    @SerializedName("country")
    private CountryBean country;
    @SerializedName("longitude")
    private LongitudeBean longitude;
    @SerializedName("name")
    private NameBean name;
    @SerializedName("phone")
    private PhoneBean phone;
    @SerializedName("state")
    private StateBean state;
    @SerializedName("address2")
    private Address2Bean address2;
    @SerializedName("email")
    private EmailBean email;
    @SerializedName("city")
    private CityBean city;
    @SerializedName("zip")
    private ZipBean zip;
    @SerializedName("fax")
    private FaxBean fax;
    @SerializedName("latitude")
    private LatitudeBean latitude;
    @SerializedName("id")
    private IdBean id;
    @SerializedName("address1")
    private Address1Bean address1;

    public String getCountry() {
        return country.getValue();
    }

    public void setCountry(CountryBean country) {
        this.country = country;
    }

    public String getLongitude() {
        return longitude.getValue();
    }

    public void setLongitude(LongitudeBean longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(NameBean name) {
        this.name = name;
    }

    public String getPhone() {
        return phone.getValue();
    }

    public void setPhone(PhoneBean phone) {
        this.phone = phone;
    }

    public String getState() {
        return state.getValue();
    }

    public void setState(StateBean state) {
        this.state = state;
    }

    public String getAddress2() {
        return address2.getValue();
    }

    public void setAddress2(Address2Bean address2) {
        this.address2 = address2;
    }

    public String getEmail() {
        return email.getValue();
    }

    public void setEmail(EmailBean email) {
        this.email = email;
    }

    public String getCity() {
        return city.getValue();
    }

    public void setCity(CityBean city) {
        this.city = city;
    }

    public String getZip() {
        return zip.getValue();
    }

    public void setZip(ZipBean zip) {
        this.zip = zip;
    }

    public String getFax() {
        return fax.getValue();
    }

    public void setFax(FaxBean fax) {
        this.fax = fax;
    }

    public String getLatitude() {
        return latitude.getValue();
    }

    public void setLatitude(LatitudeBean latitude) {
        this.latitude = latitude;
    }

    public String getId() {
        return id.getValue();
    }

    public void setId(IdBean id) {
        this.id = id;
    }

    public String getAddress1() {
        return address1.getValue();
    }

    public void setAddress1(Address1Bean address1) {
        this.address1 = address1;
    }

    public static class CountryBean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class LongitudeBean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class NameBean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class PhoneBean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class StateBean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Address2Bean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class EmailBean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class CityBean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class ZipBean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class FaxBean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class LatitudeBean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class IdBean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Address1Bean {
        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}