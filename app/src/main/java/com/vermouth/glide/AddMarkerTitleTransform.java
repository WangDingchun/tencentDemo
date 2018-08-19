package com.vermouth.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.vermouth.demo.R;
import com.vermouth.utils.DisplayUtils;

public class AddMarkerTitleTransform extends BitmapTransformation {

  private String title;
  private Context context;
  private int marginAdjustment = 0;

  public AddMarkerTitleTransform(Context context, String title) {
    super(context);
    this.context = context;
    this.title = title;
  }

  @Override
  protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
    View view = LayoutInflater.from(context).inflate(R.layout.home_marker_titled, null, false);
    if (view != null) {
      TextView title = (TextView) view.findViewById(R.id.title);
      ImageView image = (ImageView) view.findViewById(R.id.image);
      title.setText(this.title);
      LinearLayout.LayoutParams layoutParams = (LayoutParams) image.getLayoutParams();
      layoutParams.topMargin = marginAdjustment;
      image.setLayoutParams(layoutParams);
      image.setImageBitmap(toTransform);
      view.measure(
          View.MeasureSpec
              .makeMeasureSpec(DisplayUtils.getDisplayWidth(context), View.MeasureSpec.AT_MOST),
          View.MeasureSpec
              .makeMeasureSpec(DisplayUtils.getDisplayHeight(context), View.MeasureSpec.AT_MOST)
      );
      view.layout(0, 0, DisplayUtils.getDisplayWidth(context),
          DisplayUtils.getDisplayHeight(context));
      Bitmap toBitmap = pool.get(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
      if (toBitmap == null) {
        toBitmap = Bitmap.createBitmap(
            view.getWidth(), view.getHeight(),
            Bitmap.Config.ARGB_8888
        );
      }
      Canvas canvas = new Canvas(toBitmap);
      view.draw(canvas);
      return toBitmap;
    } else {
      return toTransform;
    }
  }

  @Override
  public String getId() {
    return "MarkerTransform(" + title + ")";
  }
}
