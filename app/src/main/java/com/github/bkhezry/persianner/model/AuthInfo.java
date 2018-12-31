package com.github.bkhezry.persianner.model;

import com.google.gson.annotations.SerializedName;

public class AuthInfo {

  @SerializedName("token")
  private String token;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}