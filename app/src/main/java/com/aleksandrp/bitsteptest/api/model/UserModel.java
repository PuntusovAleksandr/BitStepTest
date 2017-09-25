package com.aleksandrp.bitsteptest.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class UserModel {


    @SerializedName("data")
    @Expose
    public _UserModel data;

    @SerializedName("message")
    @Expose
    public String error;

    @SerializedName("status")
    @Expose
    public  boolean status;
}
