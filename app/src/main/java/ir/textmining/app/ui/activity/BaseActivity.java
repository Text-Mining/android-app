package ir.textmining.app.ui.activity;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.textmining.app.util.MyApplication;

public class BaseActivity extends AppCompatActivity {

  @Override
  protected void attachBaseContext(Context base) {
    Context newContext = MyApplication.localeManager.setLocale(base);
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newContext));
  }
}
