package ir.textmining.app.ui.fragment;

import android.app.Activity;
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

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SnackbarUtils;
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
import ir.textmining.app.R;
import ir.textmining.app.listener.SelectTagEventListener;
import ir.textmining.app.model.AuthInfo;
import ir.textmining.app.model.NerStandardTagsItem;
import ir.textmining.app.model.ResponseMessage;
import ir.textmining.app.model.WordsItem;
import ir.textmining.app.service.APIService;
import ir.textmining.app.util.AppUtil;
import ir.textmining.app.util.Constant;
import ir.textmining.app.util.MyApplication;
import ir.textmining.app.util.RetrofitUtil;
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
  private AuthInfo authInfo;
  private SelectTagEventListener listener;
  private Dialog loadingDialog;
  private Activity activity;


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_select_tag,
        container, false);
    ButterKnife.bind(this, view);
    initVariables();
    setUpRecyclerView();
    handleTagItems();
    return view;
  }

  private void initVariables() {
    activity = getActivity();
    BoxStore boxStore = MyApplication.getBoxStore();
    tagsItemBox = boxStore.boxFor(NerStandardTagsItem.class);
    Prefser prefser = new Prefser(activity);
    authInfo = prefser.get(Constant.AUTH_INFO, AuthInfo.class, null);
    loadingDialog = AppUtil.getLoadingDialog(activity);
  }

  private void setUpRecyclerView() {
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
  }

  private void submitTag(NerStandardTagsItem item) {
    if (NetworkUtils.isConnected()) {
      requestSubmitTag(item);
    } else {
      AppUtil.showSnackbar(title, getString(R.string.no_internet_label), activity, SnackbarUtils.LENGTH_LONG);
    }
  }

  private void requestSubmitTag(NerStandardTagsItem item) {
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
        AppUtil.showSnackbar(recyclerView, getString(R.string.retry_request_message), activity, SnackbarUtils.LENGTH_LONG);
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
  void close() {
    dismiss();
    if (getFragmentManager() != null) {
      getFragmentManager().popBackStack();
    }
  }
}
