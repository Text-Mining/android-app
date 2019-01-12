package ir.textmining.app.model;

import com.google.gson.annotations.SerializedName;

public class AuthInfo {

  @SerializedName("token")
  private String token;
  private String email;
  private Long createTimestamp;
  private Long currentTimestamp;
  private Long expireTimestamp;

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

  public Long getCreateTimestamp() {
    return createTimestamp;
  }

  public void setCreateTimestamp(Long createTimestamp) {
    this.createTimestamp = createTimestamp;
  }

  public Long getCurrentTimestamp() {
    return currentTimestamp;
  }

  public void setCurrentTimestamp(Long currentTimestamp) {
    this.currentTimestamp = currentTimestamp;
  }

  public Long getExpireTimestamp() {
    return expireTimestamp;
  }

  public void setExpireTimestamp(Long expireTimestamp) {
    this.expireTimestamp = expireTimestamp;
  }
}