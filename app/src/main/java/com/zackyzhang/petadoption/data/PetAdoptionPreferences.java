package com.zackyzhang.petadoption.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zackyzhang.petadoption.R;

import java.lang.reflect.Type;
import java.util.LinkedList;

import timber.log.Timber;

/**
 * Created by lei on 8/15/17.
 */

public class PetAdoptionPreferences {
    private static final String TAG = "PetAdoptionPreferences";

    private static final String RECENT_QUERY_PREFERENCES = "recent_query_preferences";
    private static final String RESENT_QUERY = "recent_query";

    private static Context mContext;
    private SharedPreferences defaultSharedPrefs;
    private SharedPreferences sharedPrefs;
    private static PetAdoptionPreferences instance;
    private LinkedList<RecentQuery> mRecentQueryList;

    private PetAdoptionPreferences(Context context) {
        this.mContext = context.getApplicationContext();
        defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefs = mContext.getSharedPreferences(RECENT_QUERY_PREFERENCES, context.MODE_PRIVATE);
        if (sharedPrefs.contains(RESENT_QUERY)) {
            mRecentQueryList = getRecentQuery();
        } else {
            mRecentQueryList = new LinkedList<>();
        }
    }

    public static synchronized PetAdoptionPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new PetAdoptionPreferences(context);
        }
        return instance;
    }

    public String getAnimalType() {
        String keyForAnimalType = mContext.getString(R.string.pref_animals_key);
        String defaultAnimalType = mContext.getString(R.string.pref_animal_any);
        return defaultSharedPrefs.getString(keyForAnimalType, defaultAnimalType);
    }

    public String getAnimalSize() {
        String keyForAnimalSize = mContext.getString(R.string.pref_size_key);
        String defaultAnimalSize = mContext.getString(R.string.pref_size_any);
        return defaultSharedPrefs.getString(keyForAnimalSize, defaultAnimalSize);
    }

    public String getAnimalSex() {
        String keyForAnimalSex = mContext.getString(R.string.pref_sex_key);
        String defaultAnimalSex = mContext.getString(R.string.pref_sex_any);
        return defaultSharedPrefs.getString(keyForAnimalSex, defaultAnimalSex);
    }

    public String getAnimalAge() {
        String keyForAnimalAge = mContext.getString(R.string.pref_age_key);
        String defaultAnimalAge = mContext.getString(R.string.pref_age_any);
        return defaultSharedPrefs.getString(keyForAnimalAge, defaultAnimalAge);
    }

    public void saveRecentQuery(String name, String zipCode) {
        String type = getAnimalType();
        String size = getAnimalSize();
        String sex = getAnimalSex();
        String age = getAnimalAge();

        if (name != null && zipCode != null) {
            RecentQuery rq = new RecentQuery();
            rq.setAddress(name);
            rq.setZipCode(zipCode);
            rq.setAnimalType(type);
            rq.setAnimalAge(age);
            rq.setAnimalSize(size);
            rq.setAnimalSex(sex);
            if (mRecentQueryList.contains(rq)) {
                Timber.tag(TAG).d("contain one: " + rq.getAddress());
                mRecentQueryList.remove(rq);
            }
            mRecentQueryList.addFirst(rq);
        }

        Gson gson = new Gson();
        String json = gson.toJson(mRecentQueryList);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(RESENT_QUERY, json);
        editor.commit();
    }

    public LinkedList<RecentQuery> getRecentQuery() {
        String json = sharedPrefs.getString(RESENT_QUERY, "");
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedList<RecentQuery>>(){}.getType();
        LinkedList<RecentQuery> queryList = gson.fromJson(json, type);
        return queryList;
    }

    public void clearRecentQueryList() {
        mRecentQueryList.clear();
        saveRecentQuery(null, null);
    }
}
