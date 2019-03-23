package com.google.firebase.samples.apps.mlkit;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.samples.apps.mlkit.facedetection.FaceDetectionProcessor;

public class MultiTask extends AsyncTask <Void, Void, Void> {

    ProgressDialog progressDialog;
    Context context;


    public MultiTask(FaceDetectionProcessor faceDetectionProcessor, ImageButton captureImage, CameraSource cameraSource, Context context) {
        this.context = context;

    }

    @Override
    protected Void doInBackground(Void... i) {

        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Void result) {


    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
