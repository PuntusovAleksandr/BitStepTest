package com.aleksandrp.bitsteptest.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AleksandrP on 25.09.2017.
 */

public class _UserModel {

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("avatar")
    @Expose
    public String avatar;
    @SerializedName("company")
    @Expose
    public String company;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("site")
    @Expose
    public String site;

}
