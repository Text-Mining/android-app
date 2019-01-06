package com.github.bkhezry.persianner.model;

import com.google.gson.annotations.SerializedName;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class NerStandardTagsItem {

  @Id
  private Long id;

  @SerializedName("PersianName")
  private String persianName;

  @SerializedName("Description")
  private String description;

  @SerializedName("Shortcut")
  private String shortcut;

  @SerializedName("Color")
  private String color;

  @SerializedName("Title")
  private String title;

  public String getPersianName() {
    return persianName;
  }

  public void setPersianName(String persianName) {
    this.persianName = persianName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getShortcut() {
    return shortcut;
  }

  public void setShortcut(String shortcut) {
    this.shortcut = shortcut;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}