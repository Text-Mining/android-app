package ir.textmining.app.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import ir.textmining.app.R;
import ir.textmining.app.model.AuthInfo;
import ir.textmining.app.model.NerStandardTagsItem;
import ir.textmining.app.model.TagInfo;
import ir.textmining.app.service.APIService;
import ir.textmining.app.util.AppUtil;
import ir.textmining.app.util.Constant;
import ir.textmining.app.util.MyApplication;
import ir.textmining.app.util.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LauncherActivity extends BaseActivity {

  @BindView(R.id.email_edit_text)
  TextInputEditText emailEditText;
  @BindView(R.id.password_edit_text)
  TextInputEditText passwordEditText;
  @BindView(R.id.sign_in_layout)
  MaterialCardView signInLayout;
  @BindView(R.id.no_internet_layout)
  MaterialCardView noInternetLayout;
  private Prefser prefser;
  private Dialog loadingDialog;
  private APIService apiService;
  private Box<NerStandardTagsItem> tagsItemBox;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launcher);
    ButterKnife.bind(this);
    initVariables();
    checkInternetConnection();
  }

  private void initVariables() {
    prefser = new Prefser(this);
    loadingDialog = AppUtil.getLoadingDialog(this);
    apiService = RetrofitUtil.getRetrofit("").create(APIService.class);
    BoxStore boxStore = MyApplication.getBoxStore();
    tagsItemBox = boxStore.boxFor(NerStandardTagsItem.class);
  }

  @OnClick(R.id.retry_button)
  void checkInternetConnection() {
    if (NetworkUtils.isConnected()) {
      showSignInLayout();
      checkAuthInfo();
    } else {
      signInLayout.setVisibility(View.GONE);
      showNoInternetLayout();
    }
  }

  private void showSignInLayout() {
    signInLayout.setVisibility(View.VISIBLE);
    noInternetLayout.setVisibility(View.GONE);
  }

  private void requestGetTags() {
    loadingDialog.show();
    Call<TagInfo> call = apiService.tags();
    call.enqueue(new Callback<TagInfo>() {
      @Override
      public void onResponse(@NonNull Call<TagInfo> call, @NonNull Response<TagInfo> response) {
        loadingDialog.dismiss();
        if (response.isSuccessful()) {
          TagInfo tagInfo = response.body();
          if (tagInfo != null) {
            storeTags(tagInfo);
            startMainActivity();
          }
        } else if (response.code() == 401) {
          tokenInvalid();
        }
      }

      @Override
      public void onFailure(@NonNull Call<TagInfo> call, @NonNull Throwable t) {
        showNoInternetLayout();
        loadingDialog.dismiss();
        t.printStackTrace();

      }
    });
  }

  private void tokenInvalid() {
    prefser.remove(Constant.AUTH_INFO);
    showNoInternetLayout();
    AppUtil.showSnackbar(signInLayout, getString(R.string.sign_in_again_message), LauncherActivity.this, SnackbarUtils.LENGTH_INDEFINITE);
  }

  private void storeTags(TagInfo tagInfo) {
    tagsItemBox.removeAll();
    tagsItemBox.put(tagInfo.getNerStandardTags());
  }

  private void checkAuthInfo() {
    if (prefser.contains(Constant.AUTH_INFO)) {
      AuthInfo authInfo = prefser.get(Constant.AUTH_INFO, AuthInfo.class, null);
      if (!AppUtil.isTokenExpire(authInfo.getStoreTimestamp())) {
        hideLayouts();
        if (NetworkUtils.isConnected()) {
          requestGetTags();
        } else {
          showNoInternetLayout();
        }
      }
    }
  }

  private void showNoInternetLayout() {
    noInternetLayout.setVisibility(View.VISIBLE);
    AppUtil.showSnackbar(noInternetLayout, getString(R.string.no_internet_label), LauncherActivity.this, SnackbarUtils.LENGTH_LONG);
  }

  private void hideLayouts() {
    noInternetLayout.setVisibility(View.GONE);
    signInLayout.setVisibility(View.GONE);
  }

  @OnClick({R.id.sign_in_button, R.id.sign_up})
  public void handleButtonClick(View view) {
    switch (view.getId()) {
      case R.id.sign_in_button:
        signIn(view);
        break;
      case R.id.sign_up:
        signUp();
        break;
    }
  }

  private void signIn(View view) {
    if (NetworkUtils.isConnected()) {
      requestSignIn(view);
    } else {
      AppUtil.showSnackbar(view, getString(R.string.no_internet_label), LauncherActivity.this, SnackbarUtils.LENGTH_LONG);
    }
  }

  private void requestSignIn(View view) {
    String email = emailEditText.getText().toString();
    String password = passwordEditText.getText().toString();
    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
      if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        loadingDialog.show();
        Call<AuthInfo> call = apiService.signIn(email, password);
        call.enqueue(new Callback<AuthInfo>() {
          @Override
          public void onResponse(@NonNull Call<AuthInfo> call, @NonNull Response<AuthInfo> response) {
            loadingDialog.dismiss();
            if (response.isSuccessful()) {
              AuthInfo authInfo = response.body();
              handleAuthInfo(authInfo, email);
            } else {
              AppUtil.showSnackbar(view, getString(R.string.email_password_incorrect_label), LauncherActivity.this, SnackbarUtils.LENGTH_LONG);
            }
          }

          @Override
          public void onFailure(@NonNull Call<AuthInfo> call, @NonNull Throwable t) {
            AppUtil.showSnackbar(view, getString(R.string.retry_request_message), LauncherActivity.this, SnackbarUtils.LENGTH_LONG);
            loadingDialog.dismiss();
            t.printStackTrace();
          }
        });
      } else {
        AppUtil.showSnackbar(view, getString(R.string.email_incorrect_label), LauncherActivity.this, SnackbarUtils.LENGTH_LONG);
      }
    } else {
      AppUtil.showSnackbar(view, getString(R.string.no_email_password_label), LauncherActivity.this, SnackbarUtils.LENGTH_LONG);
    }
  }

  private void handleAuthInfo(AuthInfo authInfo, String email) {
    authInfo.setEmail(email);
    authInfo.setStoreTimestamp(System.currentTimeMillis());
    storeAuthInfo(authInfo);
    hideLayouts();
    requestGetTags();
  }

  private void storeAuthInfo(AuthInfo authInfo) {
    prefser.put(Constant.AUTH_INFO, authInfo);
  }

  private void startMainActivity() {
    startActivity(new Intent(this, MainActivity.class));
    finish();
  }

  private void signUp() {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(Constant.SIGN_UP_URL));
    startActivity(intent);
  }
}