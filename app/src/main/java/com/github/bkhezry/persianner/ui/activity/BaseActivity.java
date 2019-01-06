package com.github.bkhezry.persianner.ui.activity;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class BaseActivity extends AppCompatActivity {

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(ViewPumpContextWrapper.wrap(base));
  }
}
