package ir.textmining.app.service;

import ir.textmining.app.model.AuthInfo;
import ir.textmining.app.model.ResponseMessage;
import ir.textmining.app.model.Sentence;
import ir.textmining.app.model.TagInfo;
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


  @POST("ner/TagWord")
  @FormUrlEncoded
  Call<ResponseMessage> tagWord(@Field("SentenceId") String sentenceId,
                                @Field("WordId") String WordId,
                                @Field("UserTag") String userTag);
}
