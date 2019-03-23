package com.google.firebase.samples.apps.mlkit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class initializeThread extends AppCompatActivity implements Runnable
{
    static ImageClassifier pimpleClassifier, poresClassifier, pigmentClassifier, wrinklesClassifier;
    static  ArrayList<Bitmap> chunkedImages;
    static ArrayList<Bitmap> chunkedImagesPigment, chunkedImagesPores, chunkedImagesPimple, chunkedImagesWrinkles;
    String TAG = "InitializeThread";
    String PIMPLE_PATH = "pimple.tflite";
    String PIGMENT_PATH = "pigment.tflite";
    String PORES_PATH = "pores.tflite";
    String WRINKLES_PATH = "wrinkles.tflite";
    Activity activity;
    String path;
//    Log.e(TAG, "Runningg");

    public initializeThread(Activity activity, String path)
    {
        this.activity = activity;
        this.path = path;
    }

    @Override
    public void run() {
//        Log.e(TAG, "Running");
        if(path == PIMPLE_PATH)
        {
            try {
                pimpleClassifier = new ImageClassifier(this.activity, "pimple.tflite");
            } catch (IOException e) {
                Log.e(TAG, "Failed to initialize an image classifier for pimple.");
            }

            if (pimpleClassifier == null ) {
//                Dashboard.showToast("Uninitialized Classifier or invalid context.");
                return;
            }


            chunkedImagesPimple = new ArrayList<>(chunkedImages.size());

            Bitmap test1 = null, resizedImage;

            for (int i = 0; i<chunkedImages.size(); i++){
                test1 =  chunkedImages.get(i);

                Log.i("SIZEEEE",""+chunkedImages.size());




                test1 = Bitmap.createScaledBitmap(test1, ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y, true);


                // test = textureView.getBitmap(ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y);;
                //  Bitmap bitmap =
                //        textureView.getBitmap(ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y);

                int j;
                String textToShow = "";
                for (j = 0; j<10; j++){
                    textToShow = pimpleClassifier.classifyFrame(test1);
                    //   head1data.setText(textToShow);
                }

                if(textToShow.equals("Found")){

                    test1 = Dashboard.addBorder(test1,8 );


                    //restore original size
                    if(i == 0){
                        test1 = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_0, Dashboard.OrigHeightChunk_0, true);
                    }else if(i == 1){
                        test1 = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_1, Dashboard.OrigHeightChunk_1, true);
                    }else if(i == 2){
                        test1 = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_2, Dashboard.OrigHeightChunk_2, true);
                    }

                    chunkedImagesPimple.add(test1);

                    // imageView.setImageBitmap(test1);
                }
                else{

                    resizedImage = Bitmap.createScaledBitmap(test1, test1.getWidth() + 8 * 2, test1.getHeight() + 8 * 2, true);

                    if(i == 0){
                        resizedImage = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_0, Dashboard.OrigHeightChunk_0, true);
                    }else if(i == 1){
                        resizedImage = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_1, Dashboard.OrigHeightChunk_1, true);
                    }else if(i == 2){
                        resizedImage = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_2, Dashboard.OrigHeightChunk_2, true);
                    }



                    chunkedImagesPimple.add(resizedImage);
                }


            }
        }
        else if(path == PORES_PATH)
        {
            try {
                poresClassifier = new ImageClassifier(this.activity, "pigment.tflite");
            } catch (IOException e) {
                Log.e(TAG, "Failed to initialize an image classifier for pores.");
            }

//            pd.setMessage("Analyzing Pores ...");

            if (poresClassifier == null ) {
//                showToast("Uninitialized Classifier or invalid context.");
                return;
            }


            chunkedImagesPores= new ArrayList<Bitmap>(chunkedImages.size());

            Bitmap test1 = null, resizedImage;

            for (int i = 0; i<chunkedImages.size(); i++){
                test1 =  chunkedImages.get(i);

                test1 = Bitmap.createScaledBitmap(test1, ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y, true);
                // test = textureView.getBitmap(ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y);;
                //  Bitmap bitmap =
                //        textureView.getBitmap(ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y);

                int j;
                String textToShow = "";
                for (j = 0; j<10; j++){
                    textToShow = poresClassifier.classifyFrame(test1);
                }

                if(textToShow.equals("Found")){

                    test1 = Dashboard.addBorder(test1,8 );

                    if(i == 0){
                        test1 = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_0, Dashboard.OrigHeightChunk_0, true);
                    }else if(i == 1){
                        test1 = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_1, Dashboard.OrigHeightChunk_1, true);
                    }else if(i == 2){
                        test1 = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_2, Dashboard.OrigHeightChunk_2, true);
                    }


                    chunkedImagesPores.add(test1);
                    // imageView.setImageBitmap(test1);
                }
                else{

                    resizedImage = Bitmap.createScaledBitmap(test1, test1.getWidth() + 8 * 2, test1.getHeight() + 8 * 2, true);


                    if(i == 0){
                        resizedImage = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_0, Dashboard.OrigHeightChunk_0, true);
                    }else if(i == 1){
                        resizedImage = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_1, Dashboard.OrigHeightChunk_1, true);
                    }else if(i == 2){
                        resizedImage = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_2, Dashboard.OrigHeightChunk_2, true);
                    }

                    chunkedImagesPores.add(resizedImage);
                }


            }
        }
        else if(path == PIGMENT_PATH)
        {
            try {
                pigmentClassifier = new ImageClassifier(this.activity, PIGMENT_PATH);
            } catch (IOException e) {
                Log.e(TAG, "Failed to initialize an image classifier for pigments.");
            }

//            pd.setMessage("Analyzing Pigment...");

            if (pigmentClassifier == null ) {
//                showToast("Uninitialized Classifier or invalid context.");
                return;
            }


            chunkedImagesPigment = new ArrayList<Bitmap>(chunkedImages.size());

            Bitmap test1 = null, resizedImage;

            for (int i = 0; i<chunkedImages.size(); i++){
                test1 =  chunkedImages.get(i);

                test1 = Bitmap.createScaledBitmap(test1, ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y, true);
                // test = textureView.getBitmap(ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y);;
                //  Bitmap bitmap =
                //        textureView.getBitmap(ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y);

                int j;
                String textToShow = "";
                for (j = 0; j<10; j++){
                    textToShow = pigmentClassifier.classifyFrame(test1);
                }

                if(textToShow.equals("Found")){

                    test1 = Dashboard.addBorder(test1,8 );

                    if(i == 0){
                        test1 = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_0, Dashboard.OrigHeightChunk_0, true);
                    }else if(i == 1){
                        test1 = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_1, Dashboard.OrigHeightChunk_1, true);
                    }else if(i == 2){
                        test1 = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_2, Dashboard.OrigHeightChunk_2, true);
                    }


                    chunkedImagesPigment.add(test1);
                    // imageView.setImageBitmap(test1);
                }
                else{

                    resizedImage = Bitmap.createScaledBitmap(test1, test1.getWidth() + 8 * 2, test1.getHeight() + 8 * 2, true);


                    if(i == 0){
                        resizedImage = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_0, Dashboard.OrigHeightChunk_0, true);
                    }else if(i == 1){
                        resizedImage = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_1, Dashboard.OrigHeightChunk_1, true);
                    }else if(i == 2){
                        resizedImage = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_2, Dashboard.OrigHeightChunk_2, true);
                    }

                    chunkedImagesPigment.add(resizedImage);
                }


            }
        }
        else if(path == WRINKLES_PATH)
        {
            try {
                wrinklesClassifier = new ImageClassifier(this.activity, WRINKLES_PATH);
            } catch (IOException e) {
                Log.e(TAG, "Failed to initialize an image classifier for wrinkles.");
            }

//            pd.setMessage("Analyzing Wrinkles...");
            if (wrinklesClassifier == null ) {
//                showToast("Uninitialized Classifier or invalid context.");
                return;
            }


            chunkedImagesWrinkles= new ArrayList<Bitmap>(chunkedImages.size());

            Bitmap test1 = null, resizedImage;

            for (int i = 0; i<chunkedImages.size(); i++){
                test1 =  chunkedImages.get(i);

                test1 = Bitmap.createScaledBitmap(test1, ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y, true);
                // test = textureView.getBitmap(ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y);;
                //  Bitmap bitmap =
                //        textureView.getBitmap(ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y);

                int j;
                String textToShow = "";
                for (j = 0; j<10; j++){
                    textToShow = wrinklesClassifier.classifyFrame(test1);
                }

                if(textToShow.equals("Found")){

                    test1 = Dashboard.addBorder(test1,8 );


                    if(i == 0){
                        test1 = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_0, Dashboard.OrigHeightChunk_0, true);
                    }else if(i == 1){
                        test1 = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_1, Dashboard.OrigHeightChunk_1, true);
                    }else if(i == 2){
                        test1 = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_2, Dashboard.OrigHeightChunk_2, true);
                    }

                    chunkedImagesWrinkles.add(test1);
                    // imageView.setImageBitmap(test1);
                }
                else{

                    resizedImage = Bitmap.createScaledBitmap(test1, test1.getWidth() + 8 * 2, test1.getHeight() + 8 * 2, true);


                    if(i == 0){
                        resizedImage = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_0, Dashboard.OrigHeightChunk_0, true);
                    }else if(i == 1){
                        resizedImage = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_1, Dashboard.OrigHeightChunk_1, true);
                    }else if(i == 2){
                        resizedImage = Bitmap.createScaledBitmap(test1, Dashboard.OrigWidthChunk_2, Dashboard.OrigHeightChunk_2, true);
                    }


                    chunkedImagesWrinkles.add(resizedImage);
                }


            }
        }
    }

//    void showToast(final String text) {
//        final Activity activity = this;
//        if (activity != null) {
//            activity.runOnUiThread(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//
//                            head1data.setText(text);
//                            //  textView.setText(text);
//                        }
//                    });
//        }
//    }
}
