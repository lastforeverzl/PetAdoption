package com.zackyzhang.petadoption.api;

import com.zackyzhang.petadoption.api.model.PetFindResponse;
import com.zackyzhang.petadoption.api.model.PetGetResponse;
import com.zackyzhang.petadoption.api.model.ShelterFindResponse;
import com.zackyzhang.petadoption.api.model.ShelterGetResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by lei on 8/4/17.
 */

public interface PetFinderService {

    String ENDPOINT = "http://api.petfinder.com/";

    @GET("pet.find?format=json")
    Call<PetFindResponse> petFind(@Query("key") String key,
                                  @Query("location") String location,
                                  @QueryMap Map<String, String> options);

    @GET("shelter.get?format=json")
    Call<ShelterGetResponse> getShelterById(@Query("key") String key,
                                            @Query("id") String id);

    @GET("shelter.find?format=json")
    Call<ShelterFindResponse> getShelters(@Query("key") String key,
                                          @Query("location") String location,
                                          @QueryMap Map<String, String> options);

    @GET("shelter.getPets?format=json")
    Call<PetFindResponse> getShelterPets(@Query("key") String key,
                                         @Query("id") String id,
                                         @QueryMap Map<String, String> options);

    @GET("pet.get?format=json")
    Call<PetGetResponse> getPetById(@Query("key") String key,
                                    @Query("id") String id);
}
