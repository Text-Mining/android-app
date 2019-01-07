package ir.textmining.app.model;

import com.google.gson.annotations.SerializedName;

public class AuthInfo {

  @SerializedName("token")
  private String token;

  private String email;

  private Long storeTimestamp;

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

  public Long getStoreTimestamp() {
    return storeTimestamp;
  }

  public void setStoreTimestamp(Long storeTimestamp) {
    this.storeTimestamp = storeTimestamp;
  }
}