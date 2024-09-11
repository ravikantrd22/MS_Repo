package com.mission_shakti.missionshaktiassets.api;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserClient {

    @GET("Master/statemaster")
    Call<JsonObject> getState();

    @GET("Master/districtMaster/")
    Call<JsonObject> getDistrict(@Query("stateid") int stateid);

    @GET("Master/BlockMaster/")
    Call<JsonObject> getBlock(@Query("stateid") int stateid, @Query("districtid") int districtId);

    @GET("Master/VillageMaster/")
    Call<JsonObject> getVillage(@Query("stateid") int stateid, @Query("districtid") int districtId, @Query("blockid") int blockId);

    @GET("Master/SubDistrictMaster/")
    Call<JsonObject> getSubDistrict(@Query("stateid") int stateid, @Query("districtid") int districtId);

    @GET("Master/WardMaster/")
    Call<JsonObject> getWard(@Query("stateid") int stateid, @Query("districtid") int districtId, @Query("subdistrictid") int subDistId);

    @GET("Master/SelfBeneficiaryMaster/")
    Call<JsonObject> getUserDetail(@Query("mobilenumber") String mobileNo);

    @POST("Master/SelfBeneficiaryMaster/")
    Call<JsonObject> regSelfUser(@Body Map<String, Object> map);
}
