package com.github.bkhezry.persianner.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.bkhezry.persianner.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.chip_group)
  ChipGroup chipGroup;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
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
