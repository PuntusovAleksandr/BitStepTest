package com.aleksandrp.bitsteptest.utils;

import android.widget.ImageView;

import com.aleksandrp.bitsteptest.App;
import com.aleksandrp.bitsteptest.R;
import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by AleksandrP on 25.09.2017.
 */

public class ShowImages {


    public static void showImageFromFile(File photo, ImageView toImage) {
        Glide.with(App.getContext())
                .load(photo)
                .centerCrop()
//                .placeholder(R.drawable.ic_pic_white)
                .into(toImage);
    }

    public static void showImageFromFile(String photo, ImageView toImage) {
        Glide.with(App.getContext())
                .load(photo)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(toImage);
    }

}