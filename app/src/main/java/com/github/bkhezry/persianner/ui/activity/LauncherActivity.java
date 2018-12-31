package com.github.bkhezry.persianner.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.bkhezry.persianner.R;
import com.github.bkhezry.persianner.model.AuthInfo;
import com.github.bkhezry.persianner.service.APIService;
import com.github.bkhezry.persianner.util.RetrofitUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    String email = usernameEditText.getText().toString();
    String password = passwordEditText.getText().toString();
    if (!email.equals("") && !password.equals("")) {
      JsonObject signInData = new JsonObject();
      signInData.addProperty("Email", email);
      signInData.addProperty("Password", password);
      APIService apiService = RetrofitUtil.getRetrofit("").create(APIService.class);
      Call<AuthInfo> call = apiService.signIn(signInData);
      call.enqueue(new Callback<AuthInfo>() {
        @Override
        public void onResponse(@NonNull Call<AuthInfo> call, @NonNull Response<AuthInfo> response) {
          if (response.isSuccessful()) {
            AuthInfo authInfo = response.body();
            Toast.makeText(LauncherActivity.this, "Success", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(LauncherActivity.this, "Failed", Toast.LENGTH_SHORT).show();
          }
        }

        @Override
        public void onFailure(@NonNull Call<AuthInfo> call, @NonNull Throwable t) {
          Toast.makeText(LauncherActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
          t.printStackTrace();
        }
      });
    }
  }

  private void signUp() {

  }
}