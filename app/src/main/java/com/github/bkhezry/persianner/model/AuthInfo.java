package com.github.bkhezry.persianner.model;

import com.google.gson.annotations.SerializedName;

public class AuthInfo {

  @SerializedName("token")
  private String token;

  private String email;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}