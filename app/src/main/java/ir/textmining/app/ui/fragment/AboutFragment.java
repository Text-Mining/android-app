package ir.textmining.app.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.SnackbarUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.textmining.app.R;
import ir.textmining.app.util.AppUtil;
import ir.textmining.app.util.Constant;

public class AboutFragment extends DialogFragment {
  @BindView(R.id.title)
  AppCompatTextView title;
  @BindView(R.id.version_app_text_view)
  AppCompatTextView versionAppTextView;
  private Activity activity;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_about,
        container, false);
    ButterKnife.bind(this, view);
    activity = getActivity();
    setAppVersion();
    return view;
  }

  private void setAppVersion() {
    String versionName = "";
    try {
      versionName = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    versionAppTextView.setText(versionName);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(true);
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
    dialog.getWindow().setAttributes(lp);
    return dialog;
  }

  @OnClick(R.id.close_button)
  void close() {
    dismiss();
    if (getFragmentManager() != null) {
      getFragmentManager().popBackStack();
    }
  }

  @OnClick({R.id.source_code_layout, R.id.website_layout, R.id.developer_layout, R.id.designer_layout})
  void handleLayoutClick(View view) {
    switch (view.getId()) {
      case R.id.source_code_layout:
        startBrowser(Constant.GITHUB_URL, view);
        break;
      case R.id.website_layout:
        startBrowser(Constant.WEB_SITE_URL, view);
        break;
      case R.id.developer_layout:
        startBrowser(Constant.DEVELOPER_URL, view);
      case R.id.designer_layout:
        startBrowser(Constant.DESIGNER_URL, view);
        break;
    }
  }

  private void startBrowser(String url, View view) {
    try {
      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      startActivity(browserIntent);
    } catch (ActivityNotFoundException e) {
      AppUtil.showSnackbar(view, getString(R.string.browser_not_found_label), activity, SnackbarUtils.LENGTH_INDEFINITE);
      e.printStackTrace();
    }

  }
}
