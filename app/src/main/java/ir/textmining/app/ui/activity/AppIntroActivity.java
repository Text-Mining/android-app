package ir.textmining.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.github.pwittchen.prefser.library.rx2.Prefser;

import androidx.fragment.app.Fragment;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.textmining.app.R;
import ir.textmining.app.util.Constant;
import ir.textmining.app.util.MyApplication;

public class AppIntroActivity extends AppIntro2 {
  private Prefser prefser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    prefser = new Prefser(this);
    setIndicatorColor(Color.BLACK, getResources().getColor(R.color.grey_90));
    SliderPage sliderPage1 = new SliderPage();
    sliderPage1.setTitle(getString(R.string.title_1_label));
    sliderPage1.setDescription(getString(R.string.desc_1_label));
    sliderPage1.setImageDrawable(R.drawable.page_1);
    sliderPage1.setTitleColor(Color.BLACK);
    sliderPage1.setDescColor(Color.BLACK);
    sliderPage1.setBgColor(getResources().getColor(R.color.grey_10));
    addSlide(AppIntroFragment.newInstance(sliderPage1));
    SliderPage sliderPage2 = new SliderPage();
    sliderPage2.setTitle(getString(R.string.title_2_label));
    sliderPage2.setDescription(getString(R.string.desc_2_label));
    sliderPage2.setImageDrawable(R.drawable.page_2);
    sliderPage2.setTitleColor(Color.BLACK);
    sliderPage2.setDescColor(Color.BLACK);
    sliderPage2.setBgColor(getResources().getColor(R.color.grey_10));
    addSlide(AppIntroFragment.newInstance(sliderPage2));
    SliderPage sliderPage3 = new SliderPage();
    sliderPage3.setTitle(getString(R.string.title_3_label));
    sliderPage3.setDescription(getString(R.string.desc_3_label));
    sliderPage3.setImageDrawable(R.drawable.page_3);
    sliderPage3.setTitleColor(Color.BLACK);
    sliderPage3.setDescColor(Color.BLACK);
    sliderPage3.setBgColor(getResources().getColor(R.color.grey_10));
    addSlide(AppIntroFragment.newInstance(sliderPage3));
    SliderPage sliderPage4 = new SliderPage();
    sliderPage4.setTitle(getString(R.string.title_4_label));
    sliderPage4.setDescription(getString(R.string.desc_4_label));
    sliderPage4.setImageDrawable(R.drawable.page_4);
    sliderPage4.setTitleColor(Color.BLACK);
    sliderPage4.setDescColor(Color.BLACK);
    sliderPage4.setBgColor(getResources().getColor(R.color.grey_10));
    addSlide(AppIntroFragment.newInstance(sliderPage4));
    SliderPage sliderPage5 = new SliderPage();
    sliderPage5.setTitle(getString(R.string.title_5_label));
    sliderPage5.setDescription(getString(R.string.desc_5_label));
    sliderPage5.setImageDrawable(R.drawable.page_5);
    sliderPage5.setTitleColor(Color.BLACK);
    sliderPage5.setDescColor(Color.BLACK);
    sliderPage5.setBgColor(getResources().getColor(R.color.grey_10));
    addSlide(AppIntroFragment.newInstance(sliderPage5));
    showSkipButton(false);
  }

  @Override
  public void onDonePressed(Fragment currentFragment) {
    super.onDonePressed(currentFragment);
    if (!prefser.get(Constant.APP_INTRO, Boolean.class, false)) {
      prefser.put(Constant.APP_INTRO, true);
      startActivity(new Intent(this, MainActivity.class));
      finish();
    } else {
      finish();
    }
  }

  @Override
  protected void attachBaseContext(Context base) {
    Context newContext = MyApplication.localeManager.setLocale(base);
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newContext));
  }
}
