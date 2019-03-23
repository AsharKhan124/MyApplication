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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.annotation.KeepName;
import com.google.firebase.ml.common.FirebaseMLException;


import com.google.firebase.samples.apps.mlkit.facedetection.FaceDetectionProcessor;
import com.google.firebase.samples.apps.mlkit.facedetection.FaceGraphic;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** Demo app showing the various features of ML Kit for Firebase. This class is used to
 * set up continuous frame processing on frames from a camera source. */
@KeepName
public final class LivePreviewActivity extends AppCompatActivity
        implements OnRequestPermissionsResultCallback,
        OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener {
  private static final String FACE_DETECTION = "Face Detection";
  private static final String TAG = "LivePreviewActivity";
  private static final int PERMISSION_REQUESTS = 1;
  private int CAMERA_PIC_REQUEST = 0;

  private CameraSource cameraSource = null;
  private CameraSourcePreview preview;
  private GraphicOverlay graphicOverlay;
  private String selectedModel = FACE_DETECTION;

  private ImageButton captureImage;
  private ImageView imageView;
  private ToggleButton facingSwitch;

  public static int DEVICE_WIDTH, DEVICE_HEIGHT;
  Thread maskThread;


  //***************************

  FaceDetectionProcessor faceDetectionProcessor;// = cameraSource.getFireBaseProcessor();
  //******************************



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");

    setContentView(R.layout.activity_live_preview);

      final Intent intent = new Intent(LivePreviewActivity.this, Dashboard.class);


      imageView = (ImageView)findViewById((R.id.facemask));
      try {

        ChooserActivity.maskThread.join();
       // HomeActivity.maskThread.join();
          this.imageView.setImageBitmap(MaskThread.finalBmp);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }



    preview = (CameraSourcePreview) findViewById(R.id.firePreview);
    graphicOverlay = (GraphicOverlay) findViewById(R.id.fireFaceOverlay);

    facingSwitch = (ToggleButton) findViewById(R.id.facingswitch);

    if (preview == null) {
      Log.d(TAG, "Preview is null");
    }

    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null");
    }


    facingSwitch.setOnCheckedChangeListener(this);



      if (allPermissionsGranted()) {

      createCameraSource(selectedModel);

      captureImage = (ImageButton)findViewById(R.id.imageButton);

      captureImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Log.d(TAG, "Picture Taken");
                takePicture();
                captureImage.setEnabled(false);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    intent.putExtra("fileName", cameraSource.getFilePath());
                    startActivity(intent);

                    }
                }, 3000);


            }
          });

    } else {
      getRuntimePermissions();
    }

  }

  //BottomNavugationView Setup
  private void setupBottomNavigationView(){

    BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
    //bottomNavigationViewEx.setItemBackground(2,R.color.colorAccent);
    //bottomNavigationViewEx.setIconSizeAt(2,20,20);
    bottomNavigationViewEx.enableShiftingMode(2,false);
    BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
    BottomNavigationViewHelper.enableNavigation(LivePreviewActivity.this,bottomNavigationViewEx);

    updateNavigationBarState(R.id.ic_camera);

  }

  private void updateNavigationBarState(int actionId){
    BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
    Menu menu = bottomNavigationViewEx.getMenu();

    for (int i = 0, size = menu.size(); i < size; i++) {
      MenuItem item = menu.getItem(i);
      item.setChecked(item.getItemId() == actionId);
    }
  }



  private void restart() {

    preview.stop();
    startCameraSource();
  }

  private void takePicture() {

      this.DEVICE_WIDTH = graphicOverlay.getWidth();
      this.DEVICE_HEIGHT = graphicOverlay.getHeight();

      Log.i("Liveactivity Taking pic","W = "+graphicOverlay.getWidth()+", H = "+graphicOverlay.getHeight());
     // Log.i("FIND 2","W 2 = "+faceDetectionProcessor.canvasWidth()+", H 2 = "+faceDetectionProcessor.canvasHeight());

    cameraSource.takePicture(DEVICE_WIDTH , DEVICE_HEIGHT);

  }


  @Override
  public synchronized void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    // An item was selected. You can retrieve the selected item using
    // parent.getItemAtPosition(pos)
    selectedModel = parent.getItemAtPosition(pos).toString();
    Log.d(TAG, "Selected model: " + selectedModel);
    preview.stop();
    if (allPermissionsGranted()) {
      createCameraSource(selectedModel);
      startCameraSource();
    } else {
      getRuntimePermissions();
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Do nothing.
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    Log.d(TAG, "Set facing");
    if (cameraSource != null) {
      if (isChecked) {
        cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
      } else {
        cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
      }
    }
    preview.stop();
    startCameraSource();
  }


/*
  private void showPicToImageView(){
      if (cameraSource == null) {
          cameraSource.takePicture(imageView);
      }
  }
*/

  private void createCameraSource(String model) {
    // If there's no existing cameraSource, create one.
    if (cameraSource == null) {
      cameraSource = new CameraSource(this, graphicOverlay);
      // cameraSource.zoom();
    }


    switch (model) {

      case FACE_DETECTION:
        Log.i(TAG, "Using Face Detector Processor");
        faceDetectionProcessor = new FaceDetectionProcessor();
        cameraSource.setMachineLearningFrameProcessor(faceDetectionProcessor);
        break;

      default:
        Log.e(TAG, "Unknown model: " + model);
    }

  }

  /**
   * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
   * (e.g., because onResume was called before the camera source was created), this will be called
   * again when the camera source is created.
   */
  private void startCameraSource() {
    if (cameraSource != null) {
      try {
        if (preview == null) {
          Log.d(TAG, "resume: Preview is null");
        }
        if (graphicOverlay == null) {
          Log.d(TAG, "resume: graphOverlay is null");
        }
        preview.start(cameraSource, graphicOverlay);
      } catch (IOException e) {
        Log.e(TAG, "Unable to start camera source.", e);
        cameraSource.release();
        cameraSource = null;
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "onResume");
    startCameraSource();
  }

  /** Stops the camera. */
  @Override
  protected void onPause() {
    super.onPause();
    preview.stop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (cameraSource != null) {
      cameraSource.release();
    }
  }

  private String[] getRequiredPermissions() {
    try {
      PackageInfo info =
              this.getPackageManager()
                      .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
      String[] ps = info.requestedPermissions;
      if (ps != null && ps.length > 0) {
        return ps;
      } else {
        return new String[0];
      }
    } catch (Exception e) {
      return new String[0];
    }
  }

  private boolean allPermissionsGranted() {
    for (String permission : getRequiredPermissions()) {
      if (!isPermissionGranted(this, permission)) {
        return false;
      }
    }
    return true;
  }

  private void getRuntimePermissions() {
    List<String> allNeededPermissions = new ArrayList<>();
    for (String permission : getRequiredPermissions()) {
      if (!isPermissionGranted(this, permission)) {
        allNeededPermissions.add(permission);
      }
    }

    if (!allNeededPermissions.isEmpty()) {
      ActivityCompat.requestPermissions(
              this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
    }
  }

  @Override
  public void onRequestPermissionsResult(
          int requestCode, String[] permissions, int[] grantResults) {
    Log.i(TAG, "Permission granted!");
    if (allPermissionsGranted()) {
      createCameraSource(selectedModel);
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  private static boolean isPermissionGranted(Context context, String permission) {
    if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED) {
      Log.i(TAG, "Permission granted: " + permission);
      return true;
    }
    Log.i(TAG, "Permission NOT granted: " + permission);
    return false;
  }



/*
Device width and height
 */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


}
