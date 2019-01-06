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
import com.github.bkhezry.persianner.listener.SelectTagEventListener;
import com.github.bkhezry.persianner.model.AuthInfo;
import com.github.bkhezry.persianner.model.NerStandardTagsItem;
import com.github.bkhezry.persianner.model.ResponseMessage;
import com.github.bkhezry.persianner.model.WordsItem;
import com.github.bkhezry.persianner.service.APIService;
import com.github.bkhezry.persianner.util.AppUtil;
import com.github.bkhezry.persianner.util.Constant;
import com.github.bkhezry.persianner.util.MyApplication;
import com.github.bkhezry.persianner.util.RetrofitUtil;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectTagFragment extends DialogFragment {
  @BindView(R.id.title)
  AppCompatTextView title;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  private WordsItem wordsItem;
  private String sentenceId;
  private Box<NerStandardTagsItem> tagsItemBox;
  private FastAdapter<NerStandardTagsItem> mFastAdapter;
  private ItemAdapter<NerStandardTagsItem> mItemAdapter;
  private Prefser prefser;
  private AuthInfo authInfo;
  private SelectTagEventListener listener;
  private Dialog loadingDialog;


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_select_tag,
        container, false);
    ButterKnife.bind(this, view);
    BoxStore boxStore = MyApplication.getBoxStore();
    tagsItemBox = boxStore.boxFor(NerStandardTagsItem.class);
    prefser = new Prefser(getActivity());
    authInfo = prefser.get(Constant.AUTH_INFO, AuthInfo.class, null);
    loadingDialog = AppUtil.getLoadingDialog(getActivity());
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
    loadingDialog.show();
    APIService apiService = RetrofitUtil.getRetrofit(authInfo.getToken()).create(APIService.class);
    Call<ResponseMessage> call = apiService.tagWord(sentenceId, wordsItem.getWordId(), item.getTitle());
    call.enqueue(new Callback<ResponseMessage>() {
      @Override
      public void onResponse(@NonNull Call<ResponseMessage> call, @NonNull Response<ResponseMessage> response) {
        loadingDialog.dismiss();
        if (response.isSuccessful()) {
          if (listener != null) {
            listener.tagSuccess(item.getTitle());
            close();
          }
        }
      }

      @Override
      public void onFailure(@NonNull Call<ResponseMessage> call, @NonNull Throwable t) {
        loadingDialog.dismiss();
        t.printStackTrace();
      }
    });
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
    this.wordsItem = wordsItem;
    this.sentenceId = sentenceId;
  }

  public void setListener(SelectTagEventListener selectTagEventListener) {
    this.listener = selectTagEventListener;
  }

  @OnClick(R.id.close_button)
  public void close() {
    dismiss();
    if (getFragmentManager() != null) {
      getFragmentManager().popBackStack();
    }
  }
}
