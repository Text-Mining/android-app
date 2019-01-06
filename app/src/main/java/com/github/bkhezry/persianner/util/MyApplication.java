package com.github.bkhezry.persianner.util;

import android.app.Application;

import com.github.bkhezry.persianner.R;
import com.github.bkhezry.persianner.model.MyObjectBox;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import io.objectbox.android.BuildConfig;

public class MyApplication extends Application {
  private static BoxStore boxStore;

  @Override
  public void onCreate() {
    super.onCreate();
    ViewPump.init(ViewPump.builder()
        .addInterceptor(new CalligraphyInterceptor(
            new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Vazir.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()))
        .build());
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
