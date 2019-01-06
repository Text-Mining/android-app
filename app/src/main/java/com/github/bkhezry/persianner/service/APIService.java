package com.github.bkhezry.persianner.service;

import com.github.bkhezry.persianner.model.AuthInfo;
import com.github.bkhezry.persianner.model.ResponseMessage;
import com.github.bkhezry.persianner.model.Sentence;
import com.github.bkhezry.persianner.model.TagInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {

  @POST("auth/login")
  @FormUrlEncoded
  Call<AuthInfo> signIn(@Field("Email") String email,
                        @Field("Password") String password);

  @GET("ner/GetRandomSentence")
  Call<Sentence> randomSentence();


  @GET("ner/standardtags")
  Call<TagInfo> tags();


  @POST("/ner/tagword")
  @FormUrlEncoded
  Call<ResponseMessage> tagWord(@Field("SentenceId") String sentenceId,
                                @Field("WordId") String WordId,
                                @Field("UserTag") String userTag);
}
