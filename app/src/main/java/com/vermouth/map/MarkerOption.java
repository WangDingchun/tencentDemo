package com.vermouth.map;

import android.graphics.Bitmap;
import com.bumptech.glide.BitmapRequestBuilder;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class MarkerOption {
  public BitmapRequestBuilder<Integer, Bitmap> load;
  public float anchorX;
  public float anchorY;

  public MarkerOption(
      BitmapRequestBuilder<Integer, Bitmap> load, float anchorX, float anchorY,
      LatLng location) {
    this.load = load;
    this.anchorX = anchorX;
    this.anchorY = anchorY;
    this.location = location;
  }

  public LatLng location;

}
