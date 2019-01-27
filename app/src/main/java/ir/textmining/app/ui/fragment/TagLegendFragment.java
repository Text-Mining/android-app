package ir.textmining.app.ui.fragment;

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

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import ir.textmining.app.R;
import ir.textmining.app.model.NerStandardTagsItem;
import ir.textmining.app.model.TagsLegend;
import ir.textmining.app.util.AppUtil;
import ir.textmining.app.util.MyApplication;

public class TagLegendFragment extends DialogFragment {
  @BindView(R.id.title)
  AppCompatTextView title;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  private Box<NerStandardTagsItem> tagsItemBox;
  private FastAdapter<TagsLegend> mFastAdapter;
  private ItemAdapter<TagsLegend> mItemAdapter;


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tag_legend,
        container, false);
    ButterKnife.bind(this, view);
    initVariables();
    setUpRecyclerView();
    handleTagItems();
    return view;
  }

  private void initVariables() {
    BoxStore boxStore = MyApplication.getBoxStore();
    tagsItemBox = boxStore.boxFor(NerStandardTagsItem.class);
  }

  private void setUpRecyclerView() {
    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    mItemAdapter = new ItemAdapter<>();
    mFastAdapter = FastAdapter.with(mItemAdapter);
    recyclerView.setAdapter(mFastAdapter);
  }

  private void handleTagItems() {
    List<NerStandardTagsItem> tagsItems = tagsItemBox.getAll();
    mItemAdapter.clear();
    mItemAdapter.add(AppUtil.convertToTagsLegend(tagsItems));
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
}
