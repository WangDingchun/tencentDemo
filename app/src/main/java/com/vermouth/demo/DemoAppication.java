package com.vermouth.demo;

import android.app.Application;
import android.content.Context;

public class DemoAppication extends Application {
  public static Context context;
  @Override
  public void onCreate() {
    super.onCreate();
    context=this;
  }
}
