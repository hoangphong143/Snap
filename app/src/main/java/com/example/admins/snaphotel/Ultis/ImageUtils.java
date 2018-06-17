package com.example.admins.snaphotel.Ultis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;




public class ImageUtils {
    private static File temFile;
    private static final String TAG = "ImageUtils";

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);


        Log.e("base64   ", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap base64ToImage(String base64Image) {
        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return Bitmap.createScaledBitmap(bitmap, 500, 500, true);
    }

    public static String endcodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap tmp = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
        tmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String imageBas64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        Log.d(TAG, "endcodeImageToBase64: "+imageBas64);
        return imageBas64;
    }

    public static Uri getUriFromImage(Context context) {
        temFile = null;
        try{
            temFile = File.createTempFile(
                    Calendar.getInstance().getTime().toString(),".jpg",
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            Log.d(TAG,"getUriFromImages: "+temFile.getPath());
            temFile.deleteOnExit();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //getUri
        Uri  uri= null;
        if(temFile!=null)
        {
            uri= FileProvider.getUriForFile(context,context.getPackageName()+".provider",temFile);
        }
        Log.d(TAG,"getUriFromImages: "+uri);
        return uri;
    }

    public static Bitmap getBitmap(Context context) {
        Bitmap bitmap = BitmapFactory.decodeFile(temFile.getPath());
        Log.d(TAG, "getBitmap: " + temFile.getPath());
        int screenWitdh = context.getResources().getDisplayMetrics().widthPixels;
        double ratio = (double) bitmap.getWidth() / bitmap.getHeight();
        Bitmap scaleBimap = bitmap.createScaledBitmap(bitmap, screenWitdh, (int) (screenWitdh / ratio), false);
        return scaleBimap;
    }

    public static String resizeBase64Image(String base64image) {
        byte[] encodeByte = Base64.decode(base64image.getBytes(), Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);


        if (image.getHeight() <= 400 && image.getWidth() <= 400) {
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, 250, 400, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);

        byte[] b = baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);

    }

    public static class MyAsync extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }


    }
    public static void openFragment(FragmentManager fragmentManager, int layoutID, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(layoutID, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}