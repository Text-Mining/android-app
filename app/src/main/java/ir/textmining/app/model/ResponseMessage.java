package ir.textmining.app.model;

import com.google.gson.annotations.SerializedName;

public class ResponseMessage {
  @SerializedName("message")
  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
