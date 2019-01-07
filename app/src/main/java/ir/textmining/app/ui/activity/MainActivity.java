package ir.textmining.app.ui.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import ir.textmining.app.R;
import ir.textmining.app.listener.SelectTagEventListener;
import ir.textmining.app.model.AuthInfo;
import ir.textmining.app.model.NerStandardTagsItem;
import ir.textmining.app.model.Sentence;
import ir.textmining.app.model.WordsItem;
import ir.textmining.app.service.APIService;
import ir.textmining.app.ui.fragment.SelectTagFragment;
import ir.textmining.app.util.AppUtil;
import ir.textmining.app.util.Constant;
import ir.textmining.app.util.MyApplication;
import ir.textmining.app.util.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

  @BindView(R.id.chip_group)
  ChipGroup chipGroup;
  @BindView(R.id.coordinatorLayout)
  CoordinatorLayout coordinatorLayout;
  @BindView(R.id.bar)
  BottomAppBar bar;
  @BindView(R.id.navigation_view)
  NavigationView navigationView;
  private BottomSheetBehavior bottomDrawerBehavior;
  private AuthInfo authInfo;
  private Box<NerStandardTagsItem> tagsItemBox;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(bar);
    initBottomNavigationView();
    initVariables();
    getSentence();
  }

  private void initBottomNavigationView() {
    View bottomDrawer = coordinatorLayout.findViewById(R.id.bottom_drawer);
    bottomDrawerBehavior = BottomSheetBehavior.from(bottomDrawer);
    bottomDrawerBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    bar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            bottomDrawerBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
          }
        });
    bar.setNavigationIcon(R.drawable.ic_menu_black_24dp);

  }

  private void initVariables() {
    Prefser prefser = new Prefser(this);
    authInfo = prefser.get(Constant.AUTH_INFO, AuthInfo.class, null);
    BoxStore boxStore = MyApplication.getBoxStore();
    tagsItemBox = boxStore.boxFor(NerStandardTagsItem.class);
  }

  private void getSentence() {
    APIService apiService = RetrofitUtil.getRetrofit(authInfo.getToken()).create(APIService.class);
    Call<Sentence> call = apiService.randomSentence();
    call.enqueue(new Callback<Sentence>() {
      @Override
      public void onResponse(@NonNull Call<Sentence> call, @NonNull Response<Sentence> response) {
        if (response.isSuccessful()) {
          Sentence sentence = response.body();
          if (sentence != null) {
            if (sentence.getWords().size() == 0) {
              getSentence();
            } else {
              handleSentence(sentence);
            }
          }
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
      Chip chip = generateClip(wordsItem, sentence.getSentenceId());
      chipGroup.addView(chip);
    }
  }

  private Chip generateClip(WordsItem wordsItem, String sentenceId) {
    Chip chip =
        (Chip) getLayoutInflater().inflate(R.layout.item_chip, chipGroup, false);
    chip.setText(wordsItem.getWord());
    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(AppUtil.colorHexString(wordsItem.getTag(), tagsItemBox))));
    if (!wordsItem.getTag().equals("O")) {
      chip.setTextColor(Color.WHITE);
    }
    chip.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        selectTagFragment(chip, wordsItem, sentenceId);
      }
    });
    return chip;
  }

  private void selectTagFragment(Chip chip, WordsItem wordsItem, String sentenceId) {
    SelectTagFragment fragment = new SelectTagFragment();
    fragment.setWordData(wordsItem, sentenceId);
    fragment.setListener(new SelectTagEventListener() {
      @Override
      public void tagSuccess(String tagTitle) {
        wordsItem.setTag(tagTitle);
        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(AppUtil.colorHexString(tagTitle, tagsItemBox))));
      }
    });
    AppUtil.showFragment(fragment, getSupportFragmentManager());
  }

  @OnClick(R.id.next_sentence_button)
  public void nextSentence() {
    getSentence();
  }
}
