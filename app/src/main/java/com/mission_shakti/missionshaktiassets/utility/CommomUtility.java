package com.mission_shakti.missionshaktiassets.utility;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mission_shakti.missionshaktiassets.api.UserClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CommomUtility {
    String date_common;
    OkHttpClient okHttpClient = new OkHttpClient()
            .newBuilder().connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .build();
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://training10.pmmvy-cas.nic.in/adafsadfsdg/yurtures4tyewyey/pmmvyapi/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient);
    Retrofit retrofit = builder.build();
    UserClient userClient = retrofit.create(UserClient.class);
    boolean fileCheck = false;
    private static String jwt;
    private static String Token;
    ArrayList pullFileList = new ArrayList();
    ArrayList pushFileList = new ArrayList();
    //
    boolean compressedFileDeleted = false;
    Map<String, String> params = new HashMap<>();
    boolean isPushSuccess = false;
    boolean isPullSuccess = false;
    Context context = null;
    public ArrayList<String> relationCode, documentCode, categoryCode, immunCenterId;
    private ArrayList<String> stateCode, districtCode, blockCode, subDistrictCode, villageCode, wardCode;
    public JsonObject jsonObject=new JsonObject();
    public String ref_id;
    public String setPhotoReferenceNumber;
    public byte[] decodedString;
    private String form6a;

    androidx.appcompat.app.AlertDialog alertDialog;

    ArraylistReturn arraylistReturn;
    StringReturn stringReturn;
    MyCallback myCallback;
    public void getState(ArraylistReturn arraylistReturn) {
        System.out.println("In state fetch..............................");
        ArrayList<String> state = new ArrayList<>();
        stateCode = new ArrayList<>();

        this.arraylistReturn = arraylistReturn;
        Call<JsonObject> feedModelCall = userClient.getState();
        feedModelCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code()==200) {
                    /*Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();*/
                    JsonObject stateDetail = response.body();
                    JsonArray stateDetail1 = stateDetail.getAsJsonArray("StateList");
                    System.out.println("State detail : "+stateDetail1);
                    state.add("Select State");
                    stateCode.add("0");
                    for (int i = 0; i < stateDetail1.size(); i++) {
                        JsonObject jsonObject = null;
                        jsonObject = gson.toJsonTree(stateDetail1.get(i)).getAsJsonObject();
                        state.add(String.valueOf(jsonObject.get("state")).substring(1, String.valueOf(jsonObject.get("state")).length() - 1));
                        stateCode.add(String.valueOf(jsonObject.get("id")));
                    }
                    arraylistReturn.onCallback(state, stateCode);
                    System.out.println("State payload: " + state);
                    System.out.println("State payload:1 " + stateCode.toString());
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String errorResponse = jsonObject.optString("message");
                    System.out.println(errorResponse);
                    /*Toast.makeText(context.getApplicationContext(), errorResponse, Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Coming in onFailure State " + t.getMessage());
                /*Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();*/
            }
        });
    }

    public void getDistrict(int stateId, ArraylistReturn arraylistReturn) {
        System.out.println("In District fetch..............................");
        ArrayList<String> district = new ArrayList<>();
        districtCode = new ArrayList<>();

        this.arraylistReturn = arraylistReturn;
        Call<JsonObject> feedModelCall = userClient.getDistrict(stateId);
        feedModelCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code()==200) {
                    /*Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();*/
                    JsonObject districtDetail = response.body();
                    JsonArray districtDetail1 = districtDetail.getAsJsonArray("DistrictList");
                    System.out.println("District detail : "+districtDetail1);
                    district.add("Select District");
                    districtCode.add("0");
                    if (districtDetail1.size()>0){
                        for (int i = 0; i < districtDetail1.size(); i++) {
                            JsonObject jsonObject = null;
                            jsonObject = gson.toJsonTree(districtDetail1.get(i)).getAsJsonObject();
                            district.add(String.valueOf(jsonObject.get("district")).substring(1, String.valueOf(jsonObject.get("district")).length() - 1));
                            districtCode.add(String.valueOf(jsonObject.get("id")));
                        }
                        arraylistReturn.onCallback(district, districtCode);
                        System.out.println("District payload: " + district);
                        System.out.println("District payload:1 " + districtCode.toString());
                    }else {
                        arraylistReturn.onCallback(district, districtCode);
                    }

                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String errorResponse = jsonObject.optString("message");
                    System.out.println(errorResponse);
                    /*Toast.makeText(context.getApplicationContext(), errorResponse, Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Coming in onFailure District " + t.getMessage());
                /*Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();*/
            }
        });
    }

    public void getBlock(int stateId, int districtId, ArraylistReturn arraylistReturn) {
        System.out.println("In Block fetch..............................");
        ArrayList<String> block = new ArrayList<>();
        blockCode = new ArrayList<>();

        this.arraylistReturn = arraylistReturn;
        Call<JsonObject> feedModelCall = userClient.getBlock(stateId,districtId);
        feedModelCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code()==200) {
                    /*Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();*/
                    JsonObject blockDetail = response.body();
                    JsonArray blockDetail1 = blockDetail.getAsJsonArray("BlockList");
                    System.out.println("Block detail : "+blockDetail1);
                    block.add("Select Block");
                    blockCode.add("0");
                    if (blockDetail1 != null){
                        for (int i = 0; i < blockDetail1.size(); i++) {
                            JsonObject jsonObject = null;
                            jsonObject = gson.toJsonTree(blockDetail1.get(i)).getAsJsonObject();
                            block.add(String.valueOf(jsonObject.get("block")).substring(1, String.valueOf(jsonObject.get("block")).length() - 1));
                            blockCode.add(String.valueOf(jsonObject.get("id")));
                        }
                        arraylistReturn.onCallback(block, blockCode);
                        System.out.println("Block payload: " + block);
                        System.out.println("Block payload:1 " + blockCode.toString());
                    }else {
                        arraylistReturn.onCallback(block, blockCode);
                    }
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String errorResponse = jsonObject.optString("message");
                    System.out.println(errorResponse);
                    /*Toast.makeText(context.getApplicationContext(), errorResponse, Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Coming in onFailure Block " + t.getMessage());
                /*Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();*/
            }
        });
    }

    public void getVillage(int stateId, int districtId, int blockId, ArraylistReturn arraylistReturn) {
        System.out.println("In Village fetch..............................");
        ArrayList<String> village = new ArrayList<>();
        villageCode = new ArrayList<>();

        this.arraylistReturn = arraylistReturn;
        Call<JsonObject> feedModelCall = userClient.getVillage(stateId,districtId,blockId);
        feedModelCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code()==200) {
                    if (response.body()!= null){
                        JsonObject villageDetail = response.body();
                        JsonArray villageDetail1 = villageDetail.getAsJsonArray("VillageList");
                        System.out.println("Village detail : "+villageDetail1);
                        village.add("Select Village");
                        villageCode.add("0");
                        if (villageDetail1!=null) {
                            for (int i = 0; i < villageDetail1.size(); i++) {
                                JsonObject jsonObject = null;
                                jsonObject = gson.toJsonTree(villageDetail1.get(i)).getAsJsonObject();
                                village.add(String.valueOf(jsonObject.get("village")).substring(1, String.valueOf(jsonObject.get("village")).length() - 1));
                                villageCode.add(String.valueOf(jsonObject.get("id")));
                            }
                            arraylistReturn.onCallback(village, villageCode);
                            System.out.println("Village payload: " + village);
                            System.out.println("Village payload:1 " + villageCode.toString());
                        }else {
                            arraylistReturn.onCallback(village, villageCode);
                        }
                    }
                    /*Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();*/
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String errorResponse = jsonObject.optString("message");
                    System.out.println(errorResponse);
                    /*Toast.makeText(context.getApplicationContext(), errorResponse, Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Coming in onFailure Village " + t.getMessage());
                /*Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();*/
            }
        });
    }

    public void getSubDistrict(int stateId, int districtId, ArraylistReturn arraylistReturn) {
        System.out.println("In Sub-District fetch..............................");
        ArrayList<String> subDistrict = new ArrayList<>();
        subDistrictCode = new ArrayList<>();

        this.arraylistReturn = arraylistReturn;
        Call<JsonObject> feedModelCall = userClient.getSubDistrict(stateId,districtId);
        feedModelCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code()==200) {
                    /*Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();*/
                    JsonObject subDistrictDetail = response.body();
                    JsonArray subDistrictDetail1 = subDistrictDetail.getAsJsonArray("SubDistrictList");
                    System.out.println("Sub-District detail : "+subDistrictDetail1);
                    subDistrict.add("Select Sub-District");
                    subDistrictCode.add("0");
                    if (subDistrictDetail1 != null){
                        for (int i = 0; i < subDistrictDetail1.size(); i++) {
                            JsonObject jsonObject = null;
                            jsonObject = gson.toJsonTree(subDistrictDetail1.get(i)).getAsJsonObject();
                            subDistrict.add(String.valueOf(jsonObject.get("subdistrictname")).substring(1, String.valueOf(jsonObject.get("subdistrictname")).length() - 1));
                            subDistrictCode.add(String.valueOf(jsonObject.get("subdistrictid")));
                        }
                        arraylistReturn.onCallback(subDistrict, subDistrictCode);
                        System.out.println("Sub-District payload: " + subDistrict);
                        System.out.println("Sub-District payload:1 " + subDistrictCode.toString());
                    }else {
                        arraylistReturn.onCallback(subDistrict, subDistrictCode);
                    }
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String errorResponse = jsonObject.optString("message");
                    System.out.println(errorResponse);
                    /*Toast.makeText(context.getApplicationContext(), errorResponse, Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Coming in onFailure Sub-District " + t.getMessage());
                /*Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();*/
            }
        });
    }

    public void getWard(int stateId, int districtId, int subDistId, ArraylistReturn arraylistReturn) {
        System.out.println("In Ward fetch..............................");
        ArrayList<String> ward = new ArrayList<>();
        wardCode = new ArrayList<>();

        this.arraylistReturn = arraylistReturn;
        Call<JsonObject> feedModelCall = userClient.getWard(stateId,districtId,subDistId);
        feedModelCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code()==200) {
                    if (response.body()!= null){
                        JsonObject wardDetail = response.body();
                        JsonArray wardDetail1 = wardDetail.getAsJsonArray("WardList");
                        System.out.println("Ward detail : "+wardDetail1);
                        ward.add("Select Ward");
                        wardCode.add("0");
                        if (wardDetail1!=null) {
                            for (int i = 0; i < wardDetail1.size(); i++) {
                                JsonObject jsonObject = null;
                                jsonObject = gson.toJsonTree(wardDetail1.get(i)).getAsJsonObject();
                                ward.add(String.valueOf(jsonObject.get("wardname")).substring(1, String.valueOf(jsonObject.get("wardname")).length() - 1));
                                wardCode.add(String.valueOf(jsonObject.get("wardid")));
                            }
                            arraylistReturn.onCallback(ward, wardCode);
                            System.out.println("Ward payload: " + ward);
                            System.out.println("Ward payload:1 " + wardCode.toString());
                        }else {
                            arraylistReturn.onCallback(ward, wardCode);
                        }
                    }
                    /*Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();*/
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String errorResponse = jsonObject.optString("message");
                    System.out.println(errorResponse);
                    /*Toast.makeText(context.getApplicationContext(), errorResponse, Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Coming in onFailure Ward " + t.getMessage());
                /*Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();*/
            }
        });
    }

    public void getUserDetail(String mobileNo, StringReturn stringReturn) {
        System.out.println("In user fetch..............................");
        final String[] id = new String[1];
        final String[] name = new String[1];
        final String[] mobileNum = new String[1];

        this.stringReturn = stringReturn;
        Call<JsonObject> feedModelCall = userClient.getUserDetail(mobileNo);
        feedModelCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code()==200) {
                    /*Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();*/
                    JsonObject userDetail = response.body();
                    JsonArray userDetail1 = userDetail.getAsJsonArray("Self Beneficiary");
                    System.out.println("User detail : "+userDetail1);
                    if (userDetail1.size()>0){
                        for (int i = 0; i < userDetail1.size(); i++) {
                            JsonObject jsonObject = null;
                            jsonObject = gson.toJsonTree(userDetail1.get(i)).getAsJsonObject();
                            id[0] = String.valueOf(jsonObject.get("id"));
                            name[0] = String.valueOf(jsonObject.get("name")).substring(1, String.valueOf(jsonObject.get("name")).length() - 1);
                            mobileNum[0] = String.valueOf(jsonObject.get("mobilenumber")).substring(1, String.valueOf(jsonObject.get("mobilenumber")).length() - 1);
                        }
                        stringReturn.onCallback(id, name, mobileNum);
                        System.out.println("User payload: " + name);
                        System.out.println("User payload:1 " + id);
                    }else {
                        stringReturn.onCallback(id, name, mobileNum);
                    }

                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String errorResponse = jsonObject.optString("message");
                    System.out.println(errorResponse);
                    /*Toast.makeText(context.getApplicationContext(), errorResponse, Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Coming in onFailure User Detail " + t.getMessage());
                /*Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_LONG).show();*/
            }
        });
    }

    public String message;
    public JsonObject submitResponse = null;
    public void regSelfUser(Map<String, Object> map, MyCallback myCallback){
        System.out.println("Self user Registration..............................");
        this.myCallback=myCallback;
        Call<JsonObject> callSubmit = userClient.regSelfUser(map);
        callSubmit.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {

                    if (response.code() == 200) {

                        submitResponse = response.body();
//                    msg = String.valueOf(submitResponse.get("message"));
                        System.out.println("User registered successfully ........" + submitResponse.get("status"));
                        myCallback.onCallback(String.valueOf(submitResponse.get("status")).substring(1, String.valueOf(submitResponse.get("status")).length() - 1));
                    }
                }else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String errorResponse = jsonObject.optString("message");
                    System.out.println("error msg..." + errorResponse);
                    message = errorResponse;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("coming in onFailure on Self user Reg " +  t.getMessage());
            }
        });
    }

}
