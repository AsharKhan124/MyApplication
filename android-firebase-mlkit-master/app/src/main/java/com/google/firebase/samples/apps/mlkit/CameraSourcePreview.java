// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.firebase.samples.apps.mlkit;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Camera;
import android.support.annotation.RequiresPermission;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.google.android.gms.common.images.Size;

import java.io.IOException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/** Preview the camera image in the screen. */
public class CameraSourcePreview extends ViewGroup {
  private static final String TAG = "MIDemoApp:Preview";

  private Context context;
  private SurfaceView surfaceView;
  private boolean startRequested;
  private boolean surfaceAvailable;
  private CameraSource cameraSource;

  private GraphicOverlay overlay;

  public CameraSourcePreview(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    startRequested = false;
    surfaceAvailable = false;

    surfaceView = new SurfaceView(context);
    surfaceView.getHolder().addCallback(new SurfaceCallback());
    addView(surfaceView);
  }

  public void start(CameraSource cameraSource) throws IOException {
    if (cameraSource == null) {
      stop();
    }

    this.cameraSource = cameraSource;

    if (this.cameraSource != null) {
      startRequested = true;
      startIfReady();
    }
  }

  public void start(CameraSource cameraSource, GraphicOverlay overlay) throws IOException {
    this.overlay = overlay;
    start(cameraSource);
  }

  public void stop() {
    if (cameraSource != null) {
      cameraSource.stop();
    }
  }

  public void release() {
    if (cameraSource != null) {
      cameraSource.release();
      cameraSource = null;
    }
  }

  private void startIfReady() throws IOException {
    if (startRequested && surfaceAvailable) {
      cameraSource.start(surfaceView.getHolder());
      if (overlay != null) {
        Size size = cameraSource.getPreviewSize();
        int min = Math.min(size.getWidth(), size.getHeight());
        int max = Math.max(size.getWidth(), size.getHeight());
        if (isPortraitMode()) {
          // Swap width and height sizes when in portrait, since it will be rotated by
          // 90 degrees
          overlay.setCameraInfo(min, max, cameraSource.getCameraFacing());
        } else {
          overlay.setCameraInfo(max, min, cameraSource.getCameraFacing());
        }
        overlay.clear();
      }
      startRequested = false;
    }
  }
  private class SurfaceCallback implements SurfaceHolder.Callback {
    @Override
    public void surfaceCreated(SurfaceHolder surface) {
      surfaceAvailable = true;
      try {

        startIfReady();
      } catch (IOException e) {
        Log.e(TAG, "Could not start camera source.", e);
      }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surface) {
      surfaceAvailable = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

    final int layoutWidth = right - left;
    final int layoutHeight = bottom - top;


    float ratio = getScreenWidth()/getScreenHeight();



      Log.i("Dashboard L W n H","W = "+layoutWidth+" , H = "+layoutHeight);

      Log.i("Dashboard M W n H","W = "+getScreenWidth()+" , H = "+getScreenHeight());

//      float perW = 320;// (float)((22.2222/100) *layoutWidth);
//    float perH = 240;//(float)((10.1351/100) *layoutHeight);
//
    int  width = 320;
    int height = 240;


    int childWidth;
    int childHeight;



    if (cameraSource != null) {
      Size size = cameraSource.getPreviewSize();
      if (size != null) {
        width = size.getWidth();
        height = size.getHeight();

        Log.i("Dashboard P W n H","W = "+width+" , H = "+height);
      }
    }

      Log.i("Dashboard P2 W n H","W = "+width+" , H = "+height);


    // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
    if (isPortraitMode()) {
      int tmp = width;
      width = height;
      height = tmp;
    }



    /*
    Assign preview height equal to mobile height
    And adjust width to overcome streching of preview
     */
    Log.i("Dashboard L2 H n L-H","W = "+height+" , H = "+layoutHeight);
    childWidth = (int) (   (   (float) layoutHeight / (float) height  ) * width);//getScreenWidth();
    childHeight = layoutHeight;   //(int) (((float) layoutWidth / (float) width) * height);;



/*
adjusting width may be greater than mobile Width so move preview towards left
 */
    int diff =layoutWidth - childWidth;
 //   diff = diff/2;
    Log.i("DIFF",""+diff);

    for (int i = 0; i < getChildCount(); ++i) {
      getChildAt(i).layout(diff, 0, childWidth, childHeight-diff);
      Log.d(TAG, "Assigned view: " + i+", Childs = "+getChildCount());
    }
      //getChildAt(0).layout(-338, 0, childWidth, childHeight);

    try {
      startIfReady();
    } catch (IOException e) {
      Log.e(TAG, "Could not start camera source.", e);
    }
  }

  private boolean isPortraitMode() {
    int orientation = context.getResources().getConfiguration().orientation;
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
      return false;
    }
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      return true;
    }

    Log.d(TAG, "isPortraitMode returning false by default");
    return false;
  }



    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }



  private Size getOptimalSize(List<Size> sizes, int w, int h) {

    final double ASPECT_TOLERANCE = 0.2;
    double targetRatio = (double) w / h;
    if (sizes == null)
      return null;
    Size optimalSize = null;
    double minDiff = Double.MAX_VALUE;
    int targetHeight = h;
    // Try to find an size match aspect ratio and size
    for (Size size : sizes)
    {
//          Log.d("CameraActivity", "Checking size " + size.width + "w " + size.height + "h");
      double ratio = (double) size.getWidth() / size.getHeight();
      if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
        continue;
      if (Math.abs(size.getHeight() - targetHeight) < minDiff)
      {
        optimalSize = size;
        minDiff = Math.abs(size.getHeight() - targetHeight);
      }
    }
    // Cannot find the one match the aspect ratio, ignore the requirement

    if (optimalSize == null)
    {
      minDiff = Double.MAX_VALUE;
      for (Size size : sizes) {
        if (Math.abs(size.getHeight() - targetHeight) < minDiff)
        {
          optimalSize = size;
          minDiff = Math.abs(size.getHeight() - targetHeight);
        }
      }
    }

//    SharedPreferences previewSizePref;
//    if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
//      previewSizePref = getSharedPreferences("PREVIEW_PREF",MODE_PRIVATE);
//    } else {
//      previewSizePref = getSharedPreferences("FRONT_PREVIEW_PREF",MODE_PRIVATE);
//    }
//
//    SharedPreferences.Editor prefEditor = previewSizePref.edit();
//    prefEditor.putInt("width", optimalSize.width);
//    prefEditor.putInt("height", optimalSize.height);
//
//    prefEditor.commit();

//      Log.d("CameraActivity", "Using size: " + optimalSize.width + "w " + optimalSize.height + "h");
    return optimalSize;
  }


}
