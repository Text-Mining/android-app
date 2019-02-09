package ir.textmining.app.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.github.pwittchen.prefser.library.rx2.Prefser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.textmining.app.R;
import ir.textmining.app.listener.SettingsEventListener;
import ir.textmining.app.model.AuthInfo;
import ir.textmining.app.util.Constant;
import ir.textmining.app.util.SettingsManager;

public class SettingsFragment extends DialogFragment {
  @BindView(R.id.email_text_view)
  AppCompatTextView emailTextView;

  @BindView(R.id.dark_theme_switch)
  SwitchCompat darkThemeSwitch;

  private Activity activity;
  private SettingsEventListener listener;
  private SharedPreferences prefs;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_settings,
        container, false);
    ButterKnife.bind(this, view);
    initVariables();
    setSwitchButtonListener();

    return view;
  }

  private void setSwitchButtonListener() {
    darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

        SettingsManager.getInstance(activity).setDarkThemeEnabled(checked);
        if (checked)
          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        activity.recreate();

      }
    });
  }

  private void initVariables() {
    activity = getActivity();
    Prefser prefser = new Prefser(activity);
    AuthInfo authInfo = prefser.get(Constant.AUTH_INFO, AuthInfo.class, null);
    if (authInfo != null) {
      emailTextView.setText(authInfo.getEmail());
    }
    darkThemeSwitch.setChecked(SettingsManager.getInstance(activity).isDarkThemeEnabled());
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

  @OnClick(R.id.sign_out_button)
  void signOut() {
    if (listener != null) {
      listener.signOut();
      close();
    }
  }

  public void setListener(SettingsEventListener settingsEventListener) {
    this.listener = settingsEventListener;
  }
}
