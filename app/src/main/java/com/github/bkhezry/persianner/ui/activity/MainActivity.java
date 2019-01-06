package com.github.bkhezry.persianner.ui.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.bkhezry.persianner.R;
import com.github.bkhezry.persianner.model.AuthInfo;
import com.github.bkhezry.persianner.model.NerStandardTagsItem;
import com.github.bkhezry.persianner.model.Sentence;
import com.github.bkhezry.persianner.model.WordsItem;
import com.github.bkhezry.persianner.service.APIService;
import com.github.bkhezry.persianner.util.AppUtil;
import com.github.bkhezry.persianner.util.Constant;
import com.github.bkhezry.persianner.util.MyApplication;
import com.github.bkhezry.persianner.util.RetrofitUtil;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

  @BindView(R.id.chip_group)
  ChipGroup chipGroup;
  private Prefser prefser;
  private AuthInfo authInfo;
  private Box<NerStandardTagsItem> tagsItemBox;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    prefser = new Prefser(this);
    authInfo = prefser.get(Constant.AUTH_INFO, AuthInfo.class, null);
    BoxStore boxStore = MyApplication.getBoxStore();
    tagsItemBox = boxStore.boxFor(NerStandardTagsItem.class);
    getSentence();
  }

  private void getSentence() {
    APIService apiService = RetrofitUtil.getRetrofit(authInfo.getToken()).create(APIService.class);
    Call<Sentence> call = apiService.randomSentence();
    call.enqueue(new Callback<Sentence>() {
      @Override
      public void onResponse(@NonNull Call<Sentence> call, @NonNull Response<Sentence> response) {
        if (response.isSuccessful()) {
          Sentence sentence = response.body();
          handleSentence(sentence);
        }
      }

      @Override
      public void onFailure(@NonNull Call<Sentence> call, @NonNull Throwable t) {
        t.printStackTrace();
      }
    });
  }

  private void handleSentence(Sentence sentence) {
    chipGroup.removeAllViews();
    for (WordsItem wordsItem : sentence.getWords()) {
      Chip chip = generateClip(wordsItem);
      chipGroup.addView(chip);
    }
  }

  private Chip generateClip(WordsItem wordsItem) {
    Chip chip =
        (Chip) getLayoutInflater().inflate(R.layout.item_chip, chipGroup, false);
    chip.setText(wordsItem.getWord());
    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(AppUtil.colorHexString(wordsItem.getTag(), tagsItemBox))));
    chip.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });
    if (!wordsItem.getTag().equals("O")) {
      chip.setTextColor(Color.WHITE);
    }
    return chip;
  }

  @OnClick(R.id.next_sentence_button)
  public void nextSentence() {
    getSentence();
  }
}
