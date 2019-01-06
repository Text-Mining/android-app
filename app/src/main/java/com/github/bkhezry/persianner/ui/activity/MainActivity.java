package com.github.bkhezry.persianner.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.bkhezry.persianner.R;
import com.github.bkhezry.persianner.model.AuthInfo;
import com.github.bkhezry.persianner.util.Constant;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.chip_group)
  ChipGroup chipGroup;
  private Prefser prefser;
  private AuthInfo authInfo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    prefser = new Prefser(this);
    authInfo = prefser.get(Constant.AUTH_INFO, AuthInfo.class, null);
    getSentence();
  }

  private void getSentence() {

  }

  private void generateChips() {
    Chip chip =
        (Chip) getLayoutInflater().inflate(R.layout.item_chip, chipGroup, false);
    chipGroup.addView(chip);
  }
}
