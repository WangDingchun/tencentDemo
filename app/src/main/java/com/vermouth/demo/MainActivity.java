package com.vermouth.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnMapLoadedCallback;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.EmergeAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polygon;
import com.vermouth.glide.AddMarkerTitleTransform;
import com.vermouth.glide.SizeTransform;
import com.vermouth.map.MarkerOption;
import com.vermouth.utils.DisplayUtils;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements OnClickListener {

  private MapView mapView;
  private TencentMap implMap;

  private ImplMarker implMarker;
  private RequestManager glide;

  private int baseWidth;
  private int baseHeight;
  private int baseBottom;
  private float clickedScale = 1.9f;

  private SizeTransform baseTransform;
  private SizeTransform scaledTransform;

  private boolean isClick = true;


  private void init() {
    baseWidth = DisplayUtils.dip2px(this, 44);
    baseHeight = DisplayUtils.dip2px(this, 44);
    baseBottom = DisplayUtils.dip2px(this, 3.5f);
    clickedScale = 1.9f;

    baseTransform = new SizeTransform(this, baseWidth, baseHeight);
    scaledTransform = new SizeTransform(this, (int) (baseWidth * clickedScale),
        (int) (baseHeight * clickedScale));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    init();
    mapView = (MapView) findViewById(R.id.mapviewOverlay);
    findViewById(R.id.addMarker).setOnClickListener(this);
    findViewById(R.id.removeMarker).setOnClickListener(this);
    findViewById(R.id.clickMarker).setOnClickListener(this);
    glide = Glide.with(this);
    implMap = mapView.getMap();
    implMap.setOnMapLoadedCallback(callback);
  }

  private OnMapLoadedCallback callback = new OnMapLoadedCallback() {
    @Override
    public void onMapLoaded() {
      implMap.getUiSettings().setTiltGesturesEnabled(false);
      implMap.getUiSettings().setCompassEnabled(false);
      implMap.getUiSettings().setRotateGesturesEnabled(false);
      implMap.getUiSettings().setGestureScaleByMapCenter(true);
      implMap.setTrafficEnabled(false);
      implMap.setMapType(TencentMap.MAP_TYPE_NORMAL);
      implMap.setBuildingEnable(false);
      implMap.setMyLocationEnabled(false);
      System.out.println("vermouth " + implMap.getCameraPosition().target);
      updateMarker(false);
    }
  };


  private void addMarker(MarkerOption markerOption) {

    if (implMarker == null) {
      implMarker = new ImplMarker(markerOption);
    } else {
      implMarker.setIcon(markerOption);

    }
  }


  private void updateMarker(boolean clicked) {
    if (clicked) {
      int addedHeight = 0;
      MarkerOption markerOption = new MarkerOption(
          glide.load(R.drawable.home_marker_classic).asBitmap().
              transform(scaledTransform),
          0.5f,
          (addedHeight + ((baseHeight - baseBottom) * clickedScale)) / (scaledTransform.height
              + addedHeight),
          implMap.getCameraPosition().target);
      addMarker(markerOption);
    } else {
      MarkerOption markerOption = new MarkerOption(
          glide.load(R.drawable.home_marker_classic_unselect).asBitmap().transform(baseTransform),
          0.5f,
          (0 + ((baseHeight - baseBottom) * clickedScale)) / (baseTransform.height + 0),
          implMap.getCameraPosition().target);
      addMarker(markerOption);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    mapView.onRestart();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.addMarker:
        break;
      case R.id.removeMarker:
        break;
      case R.id.clickMarker:
        updateMarker(isClick);
        isClick = !isClick;
        break;
    }
  }

  private class ImplMarker {

    ImplMarker(@NonNull MarkerOption option) {
      setIcon(option);
    }

    private MarkerOption option;
    private boolean removed = false;
    private boolean pendingSetToTop = false;
    private Marker implMarker = null;
    private String tag = null;

    public void setVisible(boolean visibility) {
      if (implMarker != null) {
        implMarker.setVisible(visibility);
      }

    }


    public ImplMarker setTag(@NotNull String tag) {
      this.tag = tag;
      return this;

    }

    public String getTag() {
      return tag;
    }

    class MarkerTarget extends SimpleTarget<Bitmap> {

      @Override
      public void onLoadCleared(Drawable placeholder) {

      }

      @Override
      public void onLoadFailed(Exception e, Drawable errorDrawable) {
        super.onLoadFailed(e, errorDrawable);
        Timber.d("load marker fail");
      }

      @Override
      public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        if (target != this) {
          return;
        }
        if (removed) {
          return;
        }
        if (implMarker != null) {
          implMarker.setVisible(true);
          implMarker.setPosition(option.location);
          implMarker.setAnchor(option.anchorX, option.anchorY);
          implMarker.setIcon(BitmapDescriptorFactory.fromBitmap(resource));
          System.out.println(
              "demo update : " + resource.getWidth() + " " + resource.getHeight() + " anchor: "
                  + option.anchorX + " " + option.anchorY);
        } else {
          MarkerOptions bdOption = new MarkerOptions()
              .position(option.location)
              .icon(BitmapDescriptorFactory.fromBitmap(resource))
              .anchor(option.anchorX, option.anchorY);

          implMarker = implMap.addMarker(bdOption);
          System.out.println(
              "demo init : " + resource.getWidth() + " " + resource.getHeight() + " anchor: "
                  + option.anchorX + " " + option.anchorY);
          if (pendingSetToTop) {
            implMarker.setZIndex(3);
          }
        }
      }
    }


    MarkerTarget target;

    public void setIcon(@NonNull MarkerOption option) {
      if (target != null) {
        Glide.clear(target);
      }
      this.option = option;
      this.target = new MarkerTarget();
      option.load.into(target);
    }

    public void setToTop() {
      if (implMarker != null) {
        implMarker.setZIndex(3);
      } else {
        pendingSetToTop = true;
      }
    }

    public void removeSelf() {
      if (removed) {
        return;
      }
      removed = true;
      if (target != null) {
        Glide.clear(target);
      }
      if (implMarker != null) {
        implMarker.remove();
      }

    }

    public void remove() {
      removeSelf();
    }
  }
}
