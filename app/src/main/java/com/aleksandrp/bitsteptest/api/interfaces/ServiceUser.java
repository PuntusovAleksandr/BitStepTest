package com.aleksandrp.bitsteptest.api.interfaces;


import com.aleksandrp.bitsteptest.api.model.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public interface ServiceUser {

    @GET("auth")
    Observable<Response<UserModel>> sigIn(
            @Query("email") String email,
            @Query("password") String password);

    @Multipart
    @POST("reg")
    Observable<Response<UserModel>> sigUp(
            @Part("email") RequestBody email,
            @Part("company") RequestBody organisation,
            @Part("address") RequestBody locale,
            @Part("site") RequestBody site,
            @Part("password") RequestBody password,
            @Part("phone") RequestBody phone,
            @Part("token") RequestBody token,
            @Part MultipartBody.Part mPath);

    @Multipart
    @POST("reg")
    Observable<Response<UserModel>> sigUpNoPhoto(
            @Part("email") RequestBody email,
            @Part("company") RequestBody organisation,
            @Part("address") RequestBody locale,
            @Part("site") RequestBody site,
            @Part("password") RequestBody password,
            @Part("token") RequestBody token,
            @Part("phone") RequestBody phone);
}
