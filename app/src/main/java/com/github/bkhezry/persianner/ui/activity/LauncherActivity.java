package com.github.bkhezry.persianner.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.bkhezry.persianner.R;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LauncherActivity extends AppCompatActivity {

  @BindView(R.id.username_edit_text)
  TextInputEditText usernameEditText;
  @BindView(R.id.password_edit_text)
  TextInputEditText passwordEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launcher);
    ButterKnife.bind(this);
  }

  @OnClick({R.id.sign_in_button, R.id.sign_up})
  public void handleButtonClick(View view) {
    switch (view.getId()) {
      case R.id.sign_in_button:
        signIn();
        break;
      case R.id.sign_up:
        signUp();
        break;
    }
  }

  private void signIn() {

  }

  private void signUp() {

  }
}