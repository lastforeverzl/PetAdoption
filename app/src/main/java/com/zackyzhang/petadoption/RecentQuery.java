package com.zackyzhang.petadoption;

/**
 * Created by lei on 8/15/17.
 */

public class RecentQuery {

    String address;
    String zipCode;
    String animalType;
    String animalSize;
    String animalSex;
    String animalAge;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getAnimalSize() {
        return animalSize;
    }

    public void setAnimalSize(String animalSize) {
        this.animalSize = animalSize;
    }

    public String getAnimalSex() {
        return animalSex;
    }

    public void setAnimalSex(String animalSex) {
        this.animalSex = animalSex;
    }

    public String getAnimalAge() {
        return animalAge;
    }

    public void setAnimalAge(String animalAge) {
        this.animalAge = animalAge;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof RecentQuery) {
            RecentQuery t = (RecentQuery) obj;
            boolean result = this.getAddress().equals(t.getAddress()) &&
                    this.getAnimalType().equals(t.getAnimalType()) &&
                    this.getAnimalAge().equals(t.getAnimalAge()) &&
                    this.getAnimalSex().equals(t.getAnimalSex()) &&
                    this.getAnimalSize().equals(t.getAnimalSize());
            return result;
        }
        return false;
    }
}
