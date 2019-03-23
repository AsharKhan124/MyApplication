package com.google.firebase.samples.apps.mlkit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Dashboard extends AppCompatActivity {


    TextView head1data;
    ImageView imageView;
    Button button, pimple, pores, pigment, wrinkles;




    private static final String PIMPLE_PATH = "pimple.tflite";
    private static final String PIGMENT_PATH = "pigment.tflite";
    private static final String PORES_PATH = "pores.tflite";
    private static final String WRINKLES_PATH = "wrinkles.tflite";

    private String TAG = "Dashboard";
    private ImageClassifier pimpleClassifier, poresClassifier, pigmentClassifier, wrinklesClassifier;
    ProgressDialog pd = null;

    ArrayList<Bitmap> chunkedImages;
    ArrayList<Bitmap> chunkedImagesPigment, chunkedImagesPores, chunkedImagesPimple, chunkedImagesWrinkles;

    int rows = 4,cols = 3;
    Bitmap ResultBitmapPimple, ResultBitmapPores, ResultBitmapPigment, ResultBitmapWrinkles;

    private int WIDTH, HEIGHT;

    static int OrigWidthChunk_0, OrigHeightChunk_0, OrigWidthChunk_1, OrigHeightChunk_1, OrigWidthChunk_2, OrigHeightChunk_2;

    Thread pimpleThread, poresThread, pigmentThread, wrinklesThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        head1data = (TextView)findViewById(R.id.head1data);
        button = (Button)findViewById(R.id.button);

        pimple = (Button)findViewById(R.id.pimple);
        pores = (Button)findViewById(R.id.pores);
        pigment = (Button)findViewById(R.id.pigment);
        wrinkles = (Button)findViewById(R.id.wrinkles);

        imageView = (ImageView) findViewById(R.id.imageView);

        this.WIDTH = LivePreviewActivity.DEVICE_WIDTH;
        this.HEIGHT = LivePreviewActivity.DEVICE_HEIGHT;


        Log.i("DashboardWidthHeight",""+LivePreviewActivity.DEVICE_WIDTH+", "+LivePreviewActivity.DEVICE_HEIGHT);

        String filename = getIntent().getStringExtra("fileName");



        Bitmap bitmap = null;
        try {
           // BitmapFactory.decodeResource(getResources(), R.drawable.test5
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(filename, options);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(bitmap == null){
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }


        final Bitmap finalBitmap = bitmap; //

        //moheb
        SplitImages(bitmap);




        initializeThread.chunkedImages = this.chunkedImages;
        pimpleThread = new Thread(new initializeThread(this, "pimple.tflite"));
        poresThread = new Thread(new initializeThread(this, "pores.tflite"));
        pigmentThread = new Thread(new initializeThread(this, "pigment.tflite"));
        wrinklesThread = new Thread(new initializeThread(this, "wrinkles.tflite"));

        pimpleThread.start();
        poresThread.start();
        pigmentThread.start();
        wrinklesThread.start();




        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                analyze(finalBitmap);
            }
        });


        pimple.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                imageView.setImageBitmap(ResultBitmapPimple);
            }
        });

        pores.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                imageView.setImageBitmap(ResultBitmapPores);
            }
        });


        pigment.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                imageView.setImageBitmap(ResultBitmapPigment);
            }
        });


        wrinkles.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                imageView.setImageBitmap(ResultBitmapWrinkles);
            }
        });


    }


    public void analyze(final Bitmap bitmap){


        final long[] startTime = new long[1];
        startTime[0] = System.currentTimeMillis()/1000;



        pd = new ProgressDialog(Dashboard.this);
        pd.setMessage("Analyzing ...");
        pd.setCancelable(false);
        pd.show();



        if(splittingComplete()){

            try {
                pimpleThread.join();
                this.pimpleClassifier = initializeThread.pimpleClassifier;
                this.chunkedImagesPimple = initializeThread.chunkedImagesPimple;
                ResultBitmapPimple = MergeImages(chunkedImagesPimple);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                poresThread.join();
                this.poresClassifier = initializeThread.poresClassifier;
                this.chunkedImagesPores = initializeThread.chunkedImagesPores;
                ResultBitmapPores = MergeImages(chunkedImagesPores);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                pigmentThread.join();
                this.pigmentClassifier = initializeThread.pigmentClassifier;
                this.chunkedImagesPigment = initializeThread.chunkedImagesPigment;
                ResultBitmapPigment = MergeImages(chunkedImagesPigment);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                wrinklesThread.join();
                this.wrinklesClassifier = initializeThread.wrinklesClassifier;
                this.chunkedImagesWrinkles = initializeThread.chunkedImagesWrinkles;
                ResultBitmapWrinkles = MergeImages(chunkedImagesWrinkles);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if(ResultBitmapPimple != null && ResultBitmapPigment != null && ResultBitmapPores != null && ResultBitmapWrinkles != null){
                imageView.setImageBitmap(ResultBitmapPimple);

                pimple.setVisibility(View.VISIBLE);
                pores.setVisibility(View.VISIBLE);
                pigment.setVisibility(View.VISIBLE);
                wrinkles.setVisibility(View.VISIBLE);
                wrinkles.setEnabled(false);
                button.setVisibility(View.INVISIBLE);


                saveImage(ResultBitmapPimple, "Pimples",1);
                saveImage(ResultBitmapPigment, "Pigment",2);
                saveImage(ResultBitmapPores, "Pores",3);
                saveImage(ResultBitmapWrinkles, "Wrinkles",4);

                pd.dismiss();


                long estimatedTime = ((System.currentTimeMillis())/1000) - startTime[0];


                Log.i("Dashboard", "Time to analyze "+estimatedTime);
            }
        }else{
            SplitImages(bitmap);
            analyze(bitmap);
        }

    }


    private void SplitImages(Bitmap bitmap){

        Bitmap src = bitmap;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);



        //forehead
        int fhL = 0, fhT = 0, fhR = 0, fhB = 0;

        //left chick
        int lcL = 0, lcT = 0, lcR = 0, lcB = 0;


        //right chick
        int rcL = 0, rcT = 0, rcR = 0, rcB = 0;


        int diff = WIDTH - getScreenWidth();

        chunkedImages = new ArrayList<>(1);

        int picWidth = Math.round((float) (73.2248/100) * HEIGHT);
        int calWidth = (picWidth-diff) - (fhB-diff);

        if(diff == 0){


            fhL = Math.round(  (float)((18.0555/100) * WIDTH));
            fhT = Math.round(  (float)(22.1893/100) * HEIGHT );
            fhR = Math.round(  (float)(81.9444/100) * WIDTH);
            fhB = Math.round(   (float)(36.9822/100) * HEIGHT);


            lcL = Math.round(  (float)((18.0555/100) * WIDTH));
            lcT = Math.round(  (float)(36.9822/100) * HEIGHT);
            lcR = Math.round(   (float)(49.7159/100) * WIDTH);
            lcB = Math.round(   (float)(73.2248/100) * HEIGHT);

            rcL = Math.round(  (float)((49.7159/100) * WIDTH));
            rcT = Math.round(  (float)(36.9822/100) * HEIGHT);
            rcR = Math.round(  (float)(81.9444/100) * WIDTH);
            rcB = Math.round(  (float)(73.2248/100) * HEIGHT);

            Log.i("Dashboard W n H orig",""+WIDTH+", "+HEIGHT);



            Log.i("Dashboard bitmap",""+scaledBitmap.getWidth()+", "+scaledBitmap.getHeight());
            Log.i("Dashboard coords",""+fhL+", "+fhT+", "+fhR+", "+fhB);
            chunkedImages.add(Bitmap.createBitmap(scaledBitmap, 0, 0, fhR-diff, fhB-diff));
            chunkedImages.add(Bitmap.createBitmap(scaledBitmap, 0, fhB-diff, lcR-diff, (picWidth-diff) - (fhB-diff)));
            chunkedImages.add(Bitmap.createBitmap(scaledBitmap, lcR-diff, fhB-diff, (fhR-diff) - (lcR - diff), (picWidth-diff) - (fhB-diff)));

        }
        else{

            fhL = Math.round(  (float)((25.0/100) * WIDTH));
            fhT = Math.round(  (float)(22.1893/100) * HEIGHT );
            fhR = Math.round(  (float)(75.0/100) * WIDTH);
            fhB = Math.round(   (float)(36.9822/100) * HEIGHT);


            lcL = Math.round(  (float)((25.0/100) * WIDTH));
            lcT = Math.round(  (float)(36.9822/100) * HEIGHT);
            lcR = Math.round(   (float)(49.7159/100) * WIDTH);
            lcB = Math.round(   (float)(73.2248/100) * HEIGHT);

            rcL = Math.round(  (float)((49.7159/100) * WIDTH));
            rcT = Math.round(  (float)(36.9822/100) * HEIGHT);
            rcR = Math.round(  (float)(75.0/100) * WIDTH);
            rcB = Math.round(  (float)(73.2248/100) * HEIGHT);

            chunkedImages.add(Bitmap.createBitmap(scaledBitmap, 0, 0, fhR-diff, fhB-diff));
            chunkedImages.add(Bitmap.createBitmap(scaledBitmap, 0, fhB-diff, lcR-diff, (picWidth-diff) - (fhB-diff)));
            chunkedImages.add(Bitmap.createBitmap(scaledBitmap, lcR-diff, fhB-diff, (fhR-diff) - (lcR - diff), (picWidth-diff) - (fhB-diff)));

        }




        Log.i("Dashboard W n H orig",""+WIDTH+", "+HEIGHT);



        Log.i("Dashboard bitmap",""+scaledBitmap.getWidth()+", "+scaledBitmap.getHeight());
        Log.i("Dashboard coords",""+fhL+", "+fhT+", "+fhR+", "+fhB);






        this.OrigWidthChunk_0 = chunkedImages.get(0).getWidth();  this.OrigHeightChunk_0 = chunkedImages.get(0).getHeight();
        this.OrigWidthChunk_1 = chunkedImages.get(1).getWidth();  this.OrigHeightChunk_1 = chunkedImages.get(1).getHeight();
        this.OrigWidthChunk_2 = chunkedImages.get(2).getWidth();  this.OrigHeightChunk_2 = chunkedImages.get(2).getHeight();


        Log.i("O w n h 1",""+OrigWidthChunk_0+", "+OrigHeightChunk_0);
        Log.i("O w n h 2",""+OrigWidthChunk_1+", "+OrigHeightChunk_1);
        Log.i("O w n h 3",""+OrigWidthChunk_2+", "+OrigHeightChunk_2);


//      saveImage(chunkedImages.get(0), "forehead",1);
//      saveImage(chunkedImages.get(1), "left",2);
//      saveImage(chunkedImages.get(2), "right",3);
    }




    private Bitmap MergeImages(ArrayList<Bitmap> chunks){

        int fhL, fhT, fhR, fhB;

        fhL = Math.round(  (float)((25.0/100) * WIDTH));
        fhT = Math.round(  (float)(23.4375/100) * HEIGHT );
        fhR = Math.round(  (float)(74.305555/100) * WIDTH);
        fhB = Math.round(   (float)(36.4583/100) * HEIGHT);


        //left chick
        int lcL, lcT, lcR, lcB;
        lcL = Math.round(  (float)((25.0/100) * WIDTH));
        lcT = Math.round(  (float)(36.4583/100) * HEIGHT);
        lcR = Math.round(   (float)(50.0/100) * WIDTH);
        lcB = Math.round(   (float)(73.9583/100) * HEIGHT);


        //right chick
        int rcL, rcT, rcR, rcB;
        rcL = Math.round(  (float)((50.0/100) * WIDTH));
        rcT = Math.round(  (float)(36.4583/100) * HEIGHT);
        rcR = Math.round(  (float)(74.305555/100) * WIDTH);
        rcB = Math.round(  (float)(73.9583/100) * HEIGHT);

        Bitmap bitmap ;
        int chunkWidth = chunks.get(0).getWidth();// chunks.get(1).getWidth() + chunks.get(2).getWidth();
        int chunkHeight = (chunks.get(0).getHeight() + chunks.get(1).getHeight()) ;

        Log.i("height",""+chunks.get(0).getHeight());
        bitmap = Bitmap.createBitmap(chunkWidth, chunkHeight, Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(chunks.get(0), 0, 0, null);
        canvas.drawBitmap(chunks.get(1), 0, chunks.get(0).getHeight(),null);
        canvas.drawBitmap(chunks.get(2), chunks.get(1).getWidth(), chunks.get(0).getHeight(),null);

       // saveImage(bitmap, "Final",4);

        return  bitmap;
    }





    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    private boolean splittingComplete(){

        if(chunkedImages.size() == 3 && chunkedImages.get(0) != null && chunkedImages.get(1) != null && chunkedImages.get(2) != null){
            return  true;
        }
        return false;
    }

    private void saveImage(Bitmap bitmap,String dirName, int index) {

        long startTime = System.nanoTime();

      //  pd.setMessage("Saving Report");

        Bitmap bm = null;
        File pictureFile = getOutputMediaFile(dirName, index);
        if (pictureFile == null) {
            return;
        }
        try {

            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            //fos.flush();
            // fos.write(data);
            fos.close();

            Toast.makeText(this, ""+ index + " Picture Saved", Toast.LENGTH_SHORT).show();


        } catch (FileNotFoundException e) {

        } catch (IOException e) {
        }

        long estimatedTime = System.nanoTime() - startTime;

    }


    private static File getOutputMediaFile(String dirName, int index) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                dirName);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(dirName, "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" +index+"_"+ timeStamp + ".png");

        return mediaFile;
    }




    @Override
    public void onDestroy() {

        pimpleClassifier.close();
        pigmentClassifier.close();
        poresClassifier.close();
        wrinklesClassifier.close();

        super.onDestroy();
    }

    static Bitmap addBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.RED);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

}
