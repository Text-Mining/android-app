package com.github.bkhezry.persianner.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.bkhezry.persianner.R;
import com.github.bkhezry.persianner.model.NerStandardTagsItem;
import com.github.bkhezry.persianner.model.WordsItem;
import com.github.bkhezry.persianner.util.MyApplication;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.Box;
import io.objectbox.BoxStore;

public class SelectTagFragment extends DialogFragment {
  @BindView(R.id.title)
  AppCompatTextView title;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  private WordsItem item;
  private String sentenceId;
  private Box<NerStandardTagsItem> tagsItemBox;
  private FastAdapter<NerStandardTagsItem> mFastAdapter;
  private ItemAdapter<NerStandardTagsItem> mItemAdapter;


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_select_tag,
        container, false);
    ButterKnife.bind(this, view);
    BoxStore boxStore = MyApplication.getBoxStore();
    tagsItemBox = boxStore.boxFor(NerStandardTagsItem.class);
    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    mItemAdapter = new ItemAdapter<>();
    mFastAdapter = FastAdapter.with(mItemAdapter);
    recyclerView.setAdapter(mFastAdapter);
    mFastAdapter.withOnClickListener(new OnClickListener<NerStandardTagsItem>() {
      @Override
      public boolean onClick(@Nullable View v, @NonNull IAdapter<NerStandardTagsItem> adapter, @NonNull NerStandardTagsItem item, int position) {
        submitTag(item);
        return true;
      }
    });
    handleTagItems();
    return view;
  }

  private void submitTag(NerStandardTagsItem item) {

  }

  private void handleTagItems() {
    List<NerStandardTagsItem> tagsItems = tagsItemBox.getAll();
    mItemAdapter.clear();
    mItemAdapter.add(tagsItems);
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


  public void setWordData(WordsItem wordsItem, String sentenceId) {
    this.item = wordsItem;
    this.sentenceId = sentenceId;
  }
}
