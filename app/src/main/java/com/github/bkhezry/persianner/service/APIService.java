package com.github.bkhezry.persianner.service;

import com.github.bkhezry.persianner.model.AuthInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

  @POST("auth/login")
  @FormUrlEncoded
  Call<AuthInfo> signIn(@Field("Email") String email,
                        @Field("Password") String password);
}
