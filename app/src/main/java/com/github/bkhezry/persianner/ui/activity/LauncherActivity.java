package com.github.bkhezry.persianner.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.bkhezry.persianner.R;
import com.github.bkhezry.persianner.model.AuthInfo;
import com.github.bkhezry.persianner.service.APIService;
import com.github.bkhezry.persianner.util.RetrofitUtil;
import com.google.android.material.textfield.TextInputEditText;

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

      APIService apiService = RetrofitUtil.getRetrofit("").create(APIService.class);
      Call<AuthInfo> call = apiService.signIn(email, password);
      call.enqueue(new Callback<AuthInfo>() {
        @Override
        public void onResponse(@NonNull Call<AuthInfo> call, @NonNull Response<AuthInfo> response) {
          if (response.isSuccessful()) {
            AuthInfo authInfo = response.body();

          } else {

          }
        }

        @Override
        public void onFailure(@NonNull Call<AuthInfo> call, @NonNull Throwable t) {
          t.printStackTrace();
        }
      });
    }
  }

  private void signUp() {

  }
}