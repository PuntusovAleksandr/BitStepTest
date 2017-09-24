package com.aleksandrp.bitsteptest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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


}
