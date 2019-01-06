package com.github.bkhezry.persianner.util;

import android.app.Application;

import com.github.bkhezry.persianner.model.MyObjectBox;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import io.objectbox.android.BuildConfig;

public class MyApplication extends Application {
  private static BoxStore boxStore;

  @Override
  public void onCreate() {
    super.onCreate();
    createBoxStore();
  }

  private void createBoxStore() {
    boxStore = MyObjectBox.builder().androidContext(MyApplication.this).build();
    if (BuildConfig.DEBUG) {
      new AndroidObjectBrowser(boxStore).start(this);
    }
  }

  public static BoxStore getBoxStore() {
    return boxStore;
  }
}
