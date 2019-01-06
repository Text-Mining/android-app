package com.github.bkhezry.persianner.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.github.bkhezry.persianner.R;
import com.github.bkhezry.persianner.model.NerStandardTagsItem;
import com.github.bkhezry.persianner.model.WordsItem;
import com.github.bkhezry.persianner.util.MyApplication;

import java.util.List;

import butterknife.ButterKnife;
import io.objectbox.Box;
import io.objectbox.BoxStore;

public class SelectTagFragment extends DialogFragment {
  private WordsItem item;
  private Box<NerStandardTagsItem> tagsItemBox;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_select_tag,
        container, false);
    ButterKnife.bind(this, view);
    BoxStore boxStore = MyApplication.getBoxStore();
    tagsItemBox = boxStore.boxFor(NerStandardTagsItem.class);
    List<NerStandardTagsItem> tagsItems = tagsItemBox.getAll();
    handleTagItems(tagsItems);
    return view;
  }

  private void handleTagItems(List<NerStandardTagsItem> tagsItems) {

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


  public void setWordItem(WordsItem wordsItem) {
    this.item = wordsItem;
  }
}
