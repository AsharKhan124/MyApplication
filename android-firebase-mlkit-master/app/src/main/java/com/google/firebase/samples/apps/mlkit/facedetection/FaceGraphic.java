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

package com.google.firebase.samples.apps.mlkit.facedetection;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.google.android.gms.vision.CameraSource;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.samples.apps.mlkit.GraphicOverlay;
import com.google.firebase.samples.apps.mlkit.GraphicOverlay.Graphic;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
public class FaceGraphic extends Graphic {
  private static final float FACE_POSITION_RADIUS = 10.0f;
  private static final float ID_TEXT_SIZE = 40.0f;
  private static final float ID_Y_OFFSET = 50.0f;
  private static final float ID_X_OFFSET = -50.0f;
  private static final float BOX_STROKE_WIDTH = 5.0f;
  private float left, top, right, bottom;
  private int w, h;

  private static final int[] COLOR_CHOICES = {
          Color.RED// Color.BLUE , Color.CYAN, Color.GREEN, Color.MAGENTA, Color.RED, Color.WHITE, Color.YELLOW
  };
  private static int currentColorIndex = 0;

  private int facing;

  private final Paint facePositionPaint;
  private final Paint idPaint;
  private final Paint boxPaint;

  private volatile FirebaseVisionFace firebaseVisionFace;

  public FaceGraphic(GraphicOverlay overlay) {
    super(overlay);

    currentColorIndex = (currentColorIndex + 1) % COLOR_CHOICES.length;
    final int selectedColor = COLOR_CHOICES[currentColorIndex];

    facePositionPaint = new Paint();
    facePositionPaint.setColor(selectedColor);

    idPaint = new Paint();
    idPaint.setColor(selectedColor);
    idPaint.setTextSize(ID_TEXT_SIZE);

    boxPaint = new Paint();
    boxPaint.setColor(selectedColor);
    boxPaint.setStyle(Paint.Style.STROKE);
    boxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
  }

  /**
   * Updates the face instance from the detection of the most recent frame. Invalidates the relevant
   * portions of the overlay to trigger a redraw.
   */
  public void updateFace(FirebaseVisionFace face, int facing) {
    firebaseVisionFace = face;
    this.facing = facing;
    postInvalidate();
  }

  /** Draws the face annotations for position on the supplied canvas. */
  @Override
  public void draw(Canvas canvas) {
    FirebaseVisionFace face = firebaseVisionFace;
    if (face == null) {
      return;
    }


    // Draws a circle at the position of the detected face, with the face's track id below.
    float x = translateX(face.getBoundingBox().centerX());
    float y = translateY(face.getBoundingBox().centerY());



    float xOffset = scaleX(face.getBoundingBox().width() / 2.0f);
    float yOffset = scaleY(face.getBoundingBox().height() / 2.0f);
    float left = x - xOffset;
    float top = y - yOffset;
    float right = x + xOffset;
    float bottom = y + yOffset;


   // canvas.drawCircle(x, y, 5, boxPaint);
    Log.i("Circle",""+x+", "+y);
/*

    canvas.drawText("Corner",5,5,idPaint);
      canvas.drawText("Last",500,500,idPaint);
*/
//    canvas.drawText("Top Left ("+top+","+left+")",left,top,idPaint);
//    canvas.drawText("Top Right("+top+","+right+")",right,top,idPaint);
//    canvas.drawText("Bottom Left("+bottom+","+left+")",left,bottom,idPaint);
//    canvas.drawText("Bottom Right("+bottom+","+right+")",right,bottom,idPaint);

    //canvas.drawText("NEW WDTH OF CANVAAASSS",((right - left)+left),top,idPaint);


      float calL, calT, calR, calB;


      if(canvas.getWidth() - getScreenWidth() == 0){
        calL = (float)(18.0555/100) * canvas.getWidth();
        calT = (float)(22.1893/100) * canvas.getHeight();

        calR = (float)(81.9444/100) * canvas.getWidth();
        calB = (float)(73.2248/100) * canvas.getHeight();

        Log.i("small mbl","true");
      }else{


        calL = (float)(25.0/100) * canvas.getWidth();
        calT = (float)(22.1893/100) * canvas.getHeight();

        calR = (float)(75.0/100) * canvas.getWidth();
        calB = (float)(73.2248/100) * canvas.getHeight();

        Log.i("big mbl","true");
      }



//
//
//    double perL, perT, perR, perB;
//    int diff = 0;//canvas.getWidth() - getScreenWidth();
//
//    Log.i("difference",""+diff);
//
//    perL = (double) (550.0/getScreenWidth() * 100.0);
//    perT = (double)(600.0/getScreenHeight() * 100.0);
//    perR =(double)( (1550.0 - (diff)) / getScreenWidth() ) * 100.0;
//    perB =(double)( (1980.0 - (diff)) / getScreenHeight() ) * 100.0;
//
//    Log.i("Calculations",""+perL+", "+perT+", "+perR+", "+perB);
//
//
//    calL = (float)(perL/100) * getScreenWidth();
//    calT = (float)(perT/100) * getScreenHeight();
//
//    calR = (float)(perR/100) * getScreenWidth();
//    calB = (float)(perB/100) * getScreenHeight();


    Log.i("Graphic M Wn H","W = "+getScreenWidth()+", H = "+getScreenHeight());
      Log.i("Graphic C Wn H","W = "+canvas.getWidth() +", H = "+canvas.getHeight());

      Log.i("Test1",""+calL+", "+calT+", "+calR+", "+calB);

    //canvas.drawRect(calL, calT , calR, calB,  boxPaint);
    // canvas.drawRect( 130, 284, 590, 938,  boxPaint);

    //  canvas.drawCircle(336, 5, 10, boxPaint);
      //forehead
      float fhL, fhT, fhR, fhB;

      fhL = (float)((25.0/100) * canvas.getWidth());
      fhT = (float)(22.1893/100) * canvas.getHeight();

      fhR = (float)(75.0/100) * canvas.getWidth();
      fhB = (float)(36.9822/100) * canvas.getHeight();

     // canvas.drawRect(fhL, fhT , fhR, fhB,  boxPaint);
    //  canvas.drawRect(550, 600 , 1550, 1000,  boxPaint);



      //left chick
      float lcL, lcT, lcR, lhB;
      lcL = (float)((25.0/100) * canvas.getWidth());
      lcT = (float)(36.9822/100) * canvas.getHeight();

      lcR = (float)(49.7159/100) * canvas.getWidth();
      lhB = (float)(73.2248/100) * canvas.getHeight();

    //  canvas.drawRect(lcL, lcT , lcR, lhB,  boxPaint);

     // canvas.drawRect(550, 1000 , 1050, 1980,  boxPaint);



      //right chick
      float rcL, rcT, rcR, rcB;

      rcL = (float)((49.7159/100) * canvas.getWidth());
      rcT = (float)(36.9822/100) * canvas.getHeight();

      rcR = (float)(75.0/100) * canvas.getWidth();
      rcB = (float)(73.2248/100) * canvas.getHeight();

    //  canvas.drawRect(rcL, rcT , rcR, rcB,  boxPaint);
     // canvas.drawRect(1050, 1000 , 1550, 1980,  boxPaint);



     // canvas.drawRect(360, 450,1070,1420,boxPaint);

     // canvas.drawLine(360,700,1070,700,boxPaint);

      //canvas.drawLine(720,700,720,1420,boxPaint);


      Log.i("Left",""+calL+", width = "+canvas.getWidth());
      //canvas.drawRect(calL, calT,calR,calB,boxPaint);

     // canvas.drawRect(fhL, fhT,fhR,fhB,boxPaint);

   // canvas.drawRect(left, top, right, bottom, boxPaint);
    this.left = left; this.right = right; this.top = top; this.bottom = bottom;


   Log.d("width and height Face", ""+canvas.getWidth()+" ,"+canvas.getHeight());

    //Log.i("Bounding box Face","L = "+left+", T ="+top+", R ="+right+", B ="+bottom);




    /*
    if(canvas.getWidth() == (right - left)){
        Log.i("Width","Equal");
    }else{
        Log.i("Width","Not Equal"+canvas.getWidth());
    }

      if(canvas.getHeight() == (bottom - top)){
          Log.i("Height","Equal");
      }else{
          Log.i("Height","Not Equal"+canvas.getHeight());
      }

*/
    this.w = canvas.getWidth(); this.h = canvas.getHeight();
  }






    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }








  public int canvasWidth(){
      return this.w;
  }

  public int canvasHeight(){
      return this.h;
  }

  public float getLeft() { return this.left; }

  public float getTop() {
    return this.top;
  }

  public float getRight() {
    return this.right;
  }

  public float getBottom() {
    return this.bottom;
  }
}
