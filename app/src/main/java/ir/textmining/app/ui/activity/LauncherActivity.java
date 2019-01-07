package ir.textmining.app.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.pwittchen.prefser.library.rx2.Prefser;
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
    initVariables();
    checkAuthInfo();
    getTags();
  }

  private void initVariables() {
    prefser = new Prefser(this);
    loadingDialog = AppUtil.getLoadingDialog(this);
    apiService = RetrofitUtil.getRetrofit("").create(APIService.class);
    BoxStore boxStore = MyApplication.getBoxStore();
    tagsItemBox = boxStore.boxFor(NerStandardTagsItem.class);
  }

  private void getTags() {
    Call<TagInfo> call = apiService.tags();
    call.enqueue(new Callback<TagInfo>() {
      @Override
      public void onResponse(@NonNull Call<TagInfo> call, @NonNull Response<TagInfo> response) {
        if (response.isSuccessful()) {
          TagInfo tagInfo = response.body();
          if (tagInfo != null) {
            storeTags(tagInfo);
          }
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
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(Constant.SIGN_UP_URL));
    startActivity(intent);
  }
}