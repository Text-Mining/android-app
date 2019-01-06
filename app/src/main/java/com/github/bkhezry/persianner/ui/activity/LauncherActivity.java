package com.github.bkhezry.persianner.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.bkhezry.persianner.R;
import com.github.bkhezry.persianner.model.AuthInfo;
import com.github.bkhezry.persianner.model.NerStandardTagsItem;
import com.github.bkhezry.persianner.model.TagInfo;
import com.github.bkhezry.persianner.service.APIService;
import com.github.bkhezry.persianner.util.AppUtil;
import com.github.bkhezry.persianner.util.Constant;
import com.github.bkhezry.persianner.util.MyApplication;
import com.github.bkhezry.persianner.util.RetrofitUtil;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LauncherActivity extends AppCompatActivity {

  @BindView(R.id.username_edit_text)
  TextInputEditText usernameEditText;
  @BindView(R.id.password_edit_text)
  TextInputEditText passwordEditText;
  private Prefser prefser;
  private Dialog loadingDialog;
  private APIService apiService;
  private Box<NerStandardTagsItem> tagsItemBox;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launcher);
    ButterKnife.bind(this);
    prefser = new Prefser(this);
    loadingDialog = AppUtil.getLoadingDialog(this);
    apiService = RetrofitUtil.getRetrofit("").create(APIService.class);
    BoxStore boxStore = MyApplication.getBoxStore();
    tagsItemBox = boxStore.boxFor(NerStandardTagsItem.class);
    checkAuthInfo();
    getTags();
  }

  private void getTags() {
    Call<TagInfo> call = apiService.tags();
    call.enqueue(new Callback<TagInfo>() {
      @Override
      public void onResponse(@NonNull Call<TagInfo> call, @NonNull Response<TagInfo> response) {
        if (response.isSuccessful()) {
          storeTags(response.body());
        }
      }

      @Override
      public void onFailure(@NonNull Call<TagInfo> call, @NonNull Throwable t) {
        t.printStackTrace();

      }
    });
  }

  private void storeTags(TagInfo tagInfo) {
    tagsItemBox.removeAll();
    tagsItemBox.put(tagInfo.getNerStandardTags());
  }

  private void checkAuthInfo() {
    if (prefser.contains(Constant.AUTH_INFO)) {
      AuthInfo authInfo = prefser.get(Constant.AUTH_INFO, AuthInfo.class, null);
      if (!AppUtil.isTokenExpire(authInfo.getStoreTimestamp())) {
        startMainActivity();
      }
    }
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
      loadingDialog.show();
      Call<AuthInfo> call = apiService.signIn(email, password);
      call.enqueue(new Callback<AuthInfo>() {
        @Override
        public void onResponse(@NonNull Call<AuthInfo> call, @NonNull Response<AuthInfo> response) {
          loadingDialog.dismiss();
          if (response.isSuccessful()) {
            AuthInfo authInfo = response.body();
            authInfo.setEmail(email);
            authInfo.setStoreTimestamp(System.currentTimeMillis());
            storeAuthInfo(authInfo);
          } else {
            //TODO show user & password is invalid message
          }
        }

        @Override
        public void onFailure(@NonNull Call<AuthInfo> call, @NonNull Throwable t) {
          loadingDialog.dismiss();
          t.printStackTrace();
        }
      });
    }
  }

  private void storeAuthInfo(AuthInfo authInfo) {
    prefser.put(Constant.AUTH_INFO, authInfo);
    startMainActivity();
  }

  private void startMainActivity() {
    startActivity(new Intent(this, MainActivity.class));
    finish();
  }

  private void signUp() {
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(Constant.SIGN_UP_URL));
    startActivity(i);
  }
}