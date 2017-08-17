package com.zackyzhang.petadoption.api;

import com.zackyzhang.petadoption.BuildConfig;
import com.zackyzhang.petadoption.api.model.BaseResponse;
import com.zackyzhang.petadoption.api.model.PetFindResponse;
import com.zackyzhang.petadoption.api.model.ShelterFindResponse;
import com.zackyzhang.petadoption.api.model.ShelterGetResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by lei on 8/4/17.
 */

public abstract class DataManager<T extends BaseResponse> {
    private static final String TAG = "DataManager";

    private PetFinderApi mPetFinderApi;

    public DataManager() {
        mPetFinderApi = PetFinderApi.instance();
    }

    public abstract void onDataLoaded(T data);

    public void loadFindPets(String location, Map<String, String> options) {
        Call<PetFindResponse> findPets = mPetFinderApi.getApi().petFind(BuildConfig.PETFINDER_API_KEY, location, options);
        findPets.enqueue(new Callback<PetFindResponse>() {
            @Override
            public void onResponse(Call<PetFindResponse> call, Response<PetFindResponse> response) {
                if (response.isSuccessful()) {
                    onDataLoaded((T) response.body());
                }
            }

            @Override
            public void onFailure(Call<PetFindResponse> call, Throwable t) {
                Timber.tag(TAG).d(t.getMessage());
            }
        });
    }

    public void getShelterById(String id) {
        Call<ShelterGetResponse> getShelter = mPetFinderApi.getApi().getShelterById(BuildConfig.PETFINDER_API_KEY, id);
        getShelter.enqueue(new Callback<ShelterGetResponse>() {
            @Override
            public void onResponse(Call<ShelterGetResponse> call, Response<ShelterGetResponse> response) {
                if (response.isSuccessful()) {
                    onDataLoaded((T)response.body());
                }
            }

            @Override
            public void onFailure(Call<ShelterGetResponse> call, Throwable t) {
                Timber.tag(TAG).d(t.getMessage());
            }
        });
    }

    public void getFindShelters(String location, Map<String, String> options) {
        Call<ShelterFindResponse> findShelters = mPetFinderApi.getApi().getShelters(BuildConfig.PETFINDER_API_KEY, location, options);
        findShelters.enqueue(new Callback<ShelterFindResponse>() {
            @Override
            public void onResponse(Call<ShelterFindResponse> call, Response<ShelterFindResponse> response) {
                if (response.isSuccessful()) {
                    onDataLoaded((T) response.body());
                }
            }

            @Override
            public void onFailure(Call<ShelterFindResponse> call, Throwable t) {
                Timber.tag(TAG).d(t.getMessage());
            }
        });
    }

    public void getShelterPets(String id, Map<String, String> options) {
        Call<PetFindResponse> shelterPets = mPetFinderApi.getApi().getShelterPets(BuildConfig.PETFINDER_API_KEY, id, options);
        shelterPets.enqueue(new Callback<PetFindResponse>() {
            @Override
            public void onResponse(Call<PetFindResponse> call, Response<PetFindResponse> response) {
                if (response.isSuccessful()) {
                    onDataLoaded((T) response.body());
                }
            }

            @Override
            public void onFailure(Call<PetFindResponse> call, Throwable t) {
                Timber.tag(TAG).d(t.getMessage());
                onDataLoaded(null);
            }
        });
    }
}
