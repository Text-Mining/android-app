package ir.textmining.app.ui.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import ir.textmining.app.R;
import ir.textmining.app.listener.SelectTagEventListener;
import ir.textmining.app.listener.SettingsEventListener;
import ir.textmining.app.model.AuthInfo;
import ir.textmining.app.model.NerStandardTagsItem;
import ir.textmining.app.model.Sentence;
import ir.textmining.app.model.WordsItem;
import ir.textmining.app.service.APIService;
import ir.textmining.app.ui.fragment.AboutFragment;
import ir.textmining.app.ui.fragment.SelectTagFragment;
import ir.textmining.app.ui.fragment.SettingsFragment;
import ir.textmining.app.ui.fragment.TagLegendFragment;
import ir.textmining.app.util.AppUtil;
import ir.textmining.app.util.Constant;
import ir.textmining.app.util.MyApplication;
import ir.textmining.app.util.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements SettingsEventListener {

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
    private SkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        subscribeFirebaseTopic();
        setSupportActionBar(bar);
        initBottomNavigationView();
        initVariables();
        getSentence();
    }

    private void subscribeFirebaseTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("update_app")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.about_item) {
                    showAboutFragment();
                } else if (menuItem.getItemId() == R.id.settings_item) {
                    showSettingsFragment();
                } else if (menuItem.getItemId() == R.id.app_intro_item) {
                    startActivity(new Intent(MainActivity.this, AppIntroActivity.class));
                }
                hideBottomDrawer();
                return false;
            }
        });

    }

    private void showAboutFragment() {
        AppUtil.showFragment(new AboutFragment(), getSupportFragmentManager());
    }

    private void showSettingsFragment() {
        SettingsFragment settingsFragment = new SettingsFragment();
        AppUtil.showFragment(settingsFragment, getSupportFragmentManager());
    }

    private void hideBottomDrawer() {
        bottomDrawerBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void initVariables() {
        Prefser prefser = new Prefser(this);
        authInfo = prefser.get(Constant.AUTH_INFO, AuthInfo.class, null);
        BoxStore boxStore = MyApplication.getBoxStore();
        tagsItemBox = boxStore.boxFor(NerStandardTagsItem.class);
    }

    private void getSentence() {
        if (NetworkUtils.isConnected()) {
            requestGetSentence();
        } else {
            AppUtil.showSnackbar(coordinatorLayout, getString(R.string.no_internet_label), this, SnackbarUtils.LENGTH_LONG);
        }
    }

    private void requestGetSentence() {
        showSkeleton();
        APIService apiService = RetrofitUtil.getRetrofit(authInfo.getToken()).create(APIService.class);
        Call<Sentence> call = apiService.randomSentence();
        call.enqueue(new Callback<Sentence>() {
            @Override
            public void onResponse(@NonNull Call<Sentence> call, @NonNull Response<Sentence> response) {
                skeletonScreen.hide();
                if (response.isSuccessful()) {
                    Sentence sentence = response.body();
                    if (sentence != null) {
                        if (sentence.getWords().size() == 0) {
                            getSentence();
                        } else {
                            handleSentence(sentence);
                        }
                    }
                } else if (response.code() == 401) {
                    tokenInvalid();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Sentence> call, @NonNull Throwable t) {
                skeletonScreen.hide();
                t.printStackTrace();
                AppUtil.showSnackbar(chipGroup, getString(R.string.retry_request_message), MainActivity.this, SnackbarUtils.LENGTH_LONG);
            }
        });
    }

    private void tokenInvalid() {
        Intent intent = new Intent(this, LauncherActivity.class);
        intent.putExtra(Constant.TOKEN_INVALID, true);
        startActivity(intent);
        finish();
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
            public void tagSuccessEvent(String tagTitle) {
                AppUtil.showSnackbar(chipGroup, getString(R.string.tag_success_message), MainActivity.this, SnackbarUtils.LENGTH_LONG);
                wordsItem.setTag(tagTitle);
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(AppUtil.colorHexString(tagTitle, tagsItemBox))));
                if (!tagTitle.equals("O")) {
                    chip.setTextColor(Color.WHITE);
                } else {
                    chip.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void tokenInvalidEvent() {
                tokenInvalid();
            }
        });
        AppUtil.showFragment(fragment, getSupportFragmentManager());
    }

    @OnClick(R.id.next_sentence_button)
    public void nextSentence() {
        getSentence();
    }

    @OnClick(R.id.legend_button)
    public void showLegend() {
        AppUtil.showFragment(new TagLegendFragment(), getSupportFragmentManager());
    }

    private void showSkeleton() {
        skeletonScreen = Skeleton.bind(chipGroup)
                .shimmer(true)
                .angle(0)
                .color(R.color.shimmer_color)
                .load(R.layout.item_skeleton)
                .show();
    }

    @Override
    public void signOut() {
        tokenInvalid();
    }
}
