package com.vermouth.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.vermouth.utils.DisplayUtils;

public class SizeTransform extends BitmapTransformation {

  public int width;
  public int height;
  private Paint paint;



  public SizeTransform(Context context, int width, int heigh) {
    super(context);
    this.width = width;
    this.height = heigh;
    paint = new Paint();
    paint.setDither(true);
    paint.setFilterBitmap(true);
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
  }

  @Override
  protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
    int toHeight = width * toTransform.getHeight() / toTransform.getWidth();
    if (toTransform.getWidth() == width && toTransform.getHeight() == toHeight) {
      return toTransform;
    }
    Bitmap neu = pool.get(width, toHeight, Bitmap.Config.ARGB_8888);
    if (neu == null) {
      return Bitmap.createScaledBitmap(toTransform, width, toHeight, true);
    } else {
      Canvas c = new Canvas(neu);
      c.drawBitmap(
          toTransform, new Rect(0, 0, toTransform.getWidth(), toTransform.getHeight()),
          new RectF(0F, 0F, width, toHeight), paint
      );
      return neu;
    }
  }

  @Override
  public String getId() {
    return "SizeTransform(" + width + "x" + height + ")";
  }
}
