package ir.textmining.app.model;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import ir.textmining.app.R;

@Entity
public class NerStandardTagsItem extends AbstractItem<NerStandardTagsItem, NerStandardTagsItem.MyViewHolder> {

  @Id
  private Long id;

  @SerializedName("PersianName")
  private String persianName;

  @SerializedName("Description")
  private String description;

  @SerializedName("Shortcut")
  private String shortcut;

  @SerializedName("Color")
  private String color;

  @SerializedName("Title")
  private String title;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPersianName() {
    return persianName;
  }

  public void setPersianName(String persianName) {
    this.persianName = persianName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getShortcut() {
    return shortcut;
  }

  public void setShortcut(String shortcut) {
    this.shortcut = shortcut;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @NonNull
  @Override
  public MyViewHolder getViewHolder(@NonNull View view) {
    return new MyViewHolder(view);
  }

  @Override
  public int getType() {
    return R.id.fastadapter_item_adapter;
  }

  @Override
  public int getLayoutRes() {
    return R.layout.item_tag_info;
  }


  protected static class MyViewHolder extends FastAdapter.ViewHolder<NerStandardTagsItem> {
    View view;
    @BindView(R.id.title_text_view)
    AppCompatTextView titleTextView;
    @BindView(R.id.card_view)
    MaterialCardView cardView;

    MyViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      this.view = view;
    }

    @Override
    public void bindView(@NonNull NerStandardTagsItem item, @NonNull List<Object> payloads) {
      titleTextView.setText(String.format("%s \n %s", item.getTitle(), item.getPersianName()));
      cardView.setCardBackgroundColor(Color.parseColor(item.getColor()));
      if (item.getTitle().equals("O")) {
        titleTextView.setTextColor(Color.BLACK);
      }
    }

    @Override
    public void unbindView(@NonNull NerStandardTagsItem item) {
      titleTextView.setText(null);
    }

  }
}