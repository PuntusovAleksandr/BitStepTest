package com.aleksandrp.bitsteptest.api.interfaces;


import com.aleksandrp.bitsteptest.api.model.UserModel;

import retrofit2.Response;
import retrofit2.http.GET;
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
}
