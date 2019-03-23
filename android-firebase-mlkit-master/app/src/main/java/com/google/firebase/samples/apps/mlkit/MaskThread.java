package com.google.firebase.samples.apps.mlkit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MaskThread extends AppCompatActivity implements Runnable
{

  static Bitmap finalBmp;
  Activity activity;

  public MaskThread(Activity activity)
  {
    this.activity = activity;
  }
  @Override
  public void run() {


      final long[] startTime = new long[1];
      startTime[0] = System.currentTimeMillis()/1000;


    Bitmap bmp = BitmapFactory.decodeResource(this.activity.getResources(), R.drawable.trysec);
    finalBmp = Bitmap.createScaledBitmap(bmp,LivePreviewActivity.getScreenWidth(), LivePreviewActivity.getScreenHeight(),true);

    long estimatedTime = ((System.currentTimeMillis())/1000) - startTime[0];

      Log.i("Mask thread start","Time : "+estimatedTime);
  }

}
