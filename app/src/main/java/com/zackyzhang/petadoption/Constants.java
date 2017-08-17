package com.zackyzhang.petadoption;

import com.zackyzhang.petadoption.data.FavoriteDataContract;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lei on 8/15/17.
 */

public class Constants {

    public static final Map<String, String> petSizeMap;
    public static final Map<String, String> petStatusMap;
    public static final Map<String, String> petSexMap;
    public static final Map<String, String> petTypeMap;

    static {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("any", "All size");
        tempMap.put("S", "Small");
        tempMap.put("M", "Medium");
        tempMap.put("L", "Large");
        tempMap.put("XL", "Extra-large");
        petSizeMap = Collections.unmodifiableMap(tempMap);
    }

    static {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("A", "adoptable");
        tempMap.put("H", "hold");
        tempMap.put("P", "pending");
        tempMap.put("Z", "adopted/removed");
        petStatusMap = Collections.unmodifiableMap(tempMap);
    }

    static {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("any", "All sex");
        tempMap.put("M", "Male");
        tempMap.put("F", "Female");
        petSexMap = Collections.unmodifiableMap(tempMap);
    }

    static {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("any", "All type");
        tempMap.put("dog", "Dog");
        tempMap.put("cat", "Cat");
        tempMap.put("bird", "Bird");
        tempMap.put("horse", "Horse");
        tempMap.put("reptile", "Reptile");
        tempMap.put("smallfurry", "Small Furry");
        tempMap.put("barnyard", "Barnyard");
        petTypeMap = Collections.unmodifiableMap(tempMap);
    }

    public static final String[] CHECK_FAVORITE_PROJECTION = {
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_ID,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_NAME
    };

    public static final String[] PETS_QUERY_PROJECTION = {
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_ID,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_NAME,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_INFO,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_PHOTO,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_DATE,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_STATUS,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_AGE,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_SIZE,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_BREEDS,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_MEDIA,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_SEX,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_DESCRIPTION,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_MIX,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_SHELTER_ID,
            FavoriteDataContract.FavoriteEntry.COLUMN_PET_ANIMAL
    };

    public static final int INDEX_PET_ID = 0;
    public static final int INDEX_PET_NAME = 1;
    public static final int INDEX_PET_INFO = 2;
    public static final int INDEX_PET_PHOTO = 3;
    public static final int INDEX_PET_DATE = 4;
    public static final int INDEX_PET_STATUS = 5;
    public static final int INDEX_PET_AGE = 6;
    public static final int INDEX_PET_SIZE = 7;
    public static final int INDEX_PET_BREEDS = 8;
    public static final int INDEX_PET_MEDIA = 9;
    public static final int INDEX_PET_SEX = 10;
    public static final int INDEX_PET_DESCRIPTION = 11;
    public static final int INDEX_PET_MIX = 12;
    public static final int INDEX_PET_SHELTER_ID = 13;
    public static final int INDEX_PET_ANIMAL = 14;

}
