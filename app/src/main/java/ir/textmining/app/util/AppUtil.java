package ir.textmining.app.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.SnackbarUtils;

import io.objectbox.Box;
import ir.textmining.app.R;
import ir.textmining.app.model.AuthInfo;
import ir.textmining.app.model.NerStandardTagsItem;
import ir.textmining.app.model.NerStandardTagsItem_;

public class AppUtil {

  public static boolean isTokenExpire(AuthInfo authInfo) {
    return false;
  }

  public static Dialog getLoadingDialog(Context context) {
    Dialog dialog = new Dialog(context);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    dialog.setContentView(R.layout.dialog_full_screen_loading);
    dialog.setCancelable(false);
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
    dialog.getWindow().setAttributes(lp);
    return dialog;
  }

  public static String colorHexString(String tagTitle, Box<NerStandardTagsItem> tagsItemBox) {
    NerStandardTagsItem tagItem = tagsItemBox.query().equal(NerStandardTagsItem_.title, tagTitle).build().findFirst();
    if (tagItem != null) {
      return tagItem.getColor();
    } else {
      return "#ffffff";
    }
  }

  public static void showFragment(Fragment fragment, FragmentManager fragmentManager) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
    transaction.add(android.R.id.content, fragment).addToBackStack(null).commit();
  }

  static boolean isAtLeastVersion(int version) {
    return Build.VERSION.SDK_INT >= version;
  }

  public static void showSnackbar(View view, String message, Context context, int duration) {
    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        SnackbarUtils.with(view)
            .setBottomMargin(20)
            .setMessage(message)
            .setMessageColor(Color.WHITE)
            .setBgColor(context.getResources().getColor(R.color.colorAccent))
            .setDuration(duration)
            .setAction(context.getString(R.string.ok_label), Color.YELLOW, new View.OnClickListener() {
              @Override
              public void onClick(View v) {
              }
            })
            .show();
      }
    }, 100);
  }
}
