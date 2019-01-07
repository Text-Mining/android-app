package ir.textmining.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TagInfo {

  @SerializedName("NerStandardTags")
  private List<NerStandardTagsItem> nerStandardTags;

  public List<NerStandardTagsItem> getNerStandardTags() {
    return nerStandardTags;
  }

  public void setNerStandardTags(List<NerStandardTagsItem> nerStandardTags) {
    this.nerStandardTags = nerStandardTags;
  }
}