package com.aleksandrp.bitsteptest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aleksandrp.bitsteptest.App;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AleksandrP on 25.09.2017.
 */

public class FileUtils {


    public static final String IMAGE_DIRECTORY_NAME = "Pictures//BitStep";
    /**
     * File mExtension.
     */
    public static String EXTENSION = ".jpg";

    /**
     * get new File
     *
     * @return
     */
    public static File getNewFile() {
        String fileNaame = "";
        // Create a file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile, image;
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + ".jpg");
        mediaFile = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME);
        if (!mediaFile.exists())
            mediaFile.mkdirs();
        fileNaame = "freeBe" + timeStamp + EXTENSION;
        image = new File(mediaFile, fileNaame);

        return image;
    }


    public static File onCaptureImageResult(Uri data, Context mContext) {
        Bitmap thumbnail = null;
        try {
            thumbnail = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), data);
        } catch (IOException mE) {
            mE.printStackTrace();
        }
        File destination = saveFileFromBitmap(thumbnail);

        return destination;
    }

    @NonNull
    public static File saveFileFromBitmap(Bitmap thumbnail) {
        ByteArrayOutputStream bytes = null;
        try {
            bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        } catch (Exception mE) {
            mE.printStackTrace();
            return new File("");
        }

        File destination = getNewFile();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destination;
    }


    /**
     * Refreshing the Gallery after saving new images
     */
    public static void reloadGallery(Context mContext, File mFile) {

        MediaScannerConnection.scanFile(mContext,
                new String[]{mFile.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }


    //save image
    public static void imageDownload(String urlStr) {
        new RetrieveFeedTask(urlStr).execute();
    }


    static class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        private String urlStr;
        private File file = null;

        public RetrieveFeedTask(String urlStr) {
            this.urlStr = urlStr;
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL(urlStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url == null) return null;

            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            file = saveFileFromBitmapinCach(bmp);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (file != null && file.exists())
                SettingsApp.getInstance().savePathIcon(file.getPath());
        }
    }

    @NonNull
    public static File saveFileFromBitmapinCach(Bitmap thumbnail) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File outputDir = App.getContext().getCacheDir(); // context being the Activity pointer
        File destination = null;
        try {
            destination = File.createTempFile("icon_", "_", outputDir);
        } catch (IOException e) {
            e.printStackTrace();
            destination = getNewFile();
        }
//        File destination = getNewFile();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destination;
    }

}
