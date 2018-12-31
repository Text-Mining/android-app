package com.github.bkhezry.persianner.service;

import com.github.bkhezry.persianner.model.AuthInfo;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {

  @POST("auth/login")
  Call<AuthInfo> signIn(@Body JsonObject signInData);

}
