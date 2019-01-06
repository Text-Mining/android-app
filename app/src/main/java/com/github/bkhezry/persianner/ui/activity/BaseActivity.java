package com.github.bkhezry.persianner.ui.activity;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.github.bkhezry.persianner.util.MyApplication;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class BaseActivity extends AppCompatActivity {

  @Override
  protected void attachBaseContext(Context base) {
    Context newContext = MyApplication.localeManager.setLocale(base);
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newContext));
  }
}
