package com.github.bkhezry.persianner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Sentence {

  @SerializedName("words")
  private List<WordsItem> words;

  @SerializedName("sentenceId")
  private String sentenceId;

  public List<WordsItem> getWords() {
    return words;
  }

  public void setWords(List<WordsItem> words) {
    this.words = words;
  }

  public String getSentenceId() {
    return sentenceId;
  }

  public void setSentenceId(String sentenceId) {
    this.sentenceId = sentenceId;
  }

}