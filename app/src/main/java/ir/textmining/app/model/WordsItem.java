package ir.textmining.app.model;

import com.google.gson.annotations.SerializedName;

public class WordsItem {

  @SerializedName("WordId")
  private String wordId;

  @SerializedName("Word")
  private String word;

  @SerializedName("WordOrder")
  private int wordOrder;

  @SerializedName("Tag")
  private String tag;

  @SerializedName("NerTagId")
  private String nerTagId;

  public String getWordId() {
    return wordId;
  }

  public void setWordId(String wordId) {
    this.wordId = wordId;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public int getWordOrder() {
    return wordOrder;
  }

  public void setWordOrder(int wordOrder) {
    this.wordOrder = wordOrder;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getNerTagId() {
    return nerTagId;
  }

  public void setNerTagId(String nerTagId) {
    this.nerTagId = nerTagId;
  }
}