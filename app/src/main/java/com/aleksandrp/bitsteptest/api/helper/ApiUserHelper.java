package com.aleksandrp.bitsteptest.api.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.aleksandrp.bitsteptest.App;
import com.aleksandrp.bitsteptest.R;
import com.aleksandrp.bitsteptest.api.RestAdapter;
import com.aleksandrp.bitsteptest.api.constant.ApiConstants;
import com.aleksandrp.bitsteptest.rx.BusProvider;
import com.aleksandrp.bitsteptest.rx.event.NetworkResponseEvent;
import com.aleksandrp.bitsteptest.utils.SettingsApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class ApiUserHelper {

    protected RestAdapter restAdapter;

    public ApiUserHelper() {
        restAdapter = new RestAdapter();
    }

    private RequestBody createPartFromString(String mS) {
        return RequestBody.create(MultipartBody.FORM, mS);
    }

    private MultipartBody.Part prepareFile(String mS, File mFile) {
        Uri url = Uri.fromFile(mFile);
        RequestBody body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        mFile
                );
        return MultipartBody.Part.createFormData(mS, mFile.getName(), body);
    }

    private File getFileFromRes() {
        Bitmap bm =
                BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.icon_task_white);
        File imageFile = new File(getCacheDir(), "freeBe" + EXTENSION);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            try {
                bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fos.close();
        } catch (IOException e) {
            Log.e("app", e.getMessage());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return imageFile;
    }

    //================================================================================================

    public void sigIn() {
        String email = SettingsApp.getInstance().getEmail();
        String pass = SettingsApp.getInstance().getPass();

        restAdapter.init(false, "sigIn");
        ServiceUser serviceUser =
                restAdapter.getRetrofit().create(ServiceUser.class);
        Observable<Response<RegisterUserModel>> allSources =
                serviceUser.sigIn(API_CLIENT_ID, API_CLIENT_SECRET, API_GRANT_TYPE, email, pass);
        allSources.subscribeOn(Schedulers.newThread()).
                subscribe(new Subscriber<Response<RegisterUserModel>>() {
                    private NetworkResponseEvent event;
                    private NetworkResponseEvent<String> eventError;
                    private RegisterUserModel body;
                    private String errorText = "";

                    @Override
                    public void onCompleted() {
                        if (event != null) {
                            event.setSucess(true);
                            event.setData(body);
                        } else {
                            event = new NetworkResponseEvent<>();
                            event.setId(ApiConstants.ERROR);
                            event.setData(errorText);
                            event.setSucess(false);
                        }
                        BusProvider.send(event);
                    }

                    @Override
                    public void onError(Throwable e) {
                        eventError = new NetworkResponseEvent<>();
                        eventError.setId(ApiConstants.ERROR);
                        eventError.setData("Error load Sources ::: " + e.getMessage());
                        eventError.setSucess(false);
                        BusProvider.send(eventError);
                    }

                    @Override
                    public void onNext(Response<RegisterUserModel> mResponse) {
                        if (mResponse.isSuccessful()) {
                            event = new NetworkResponseEvent<>();
                            event.setId(ApiConstants.SIGN_IN);
                            body = mResponse.body();
                        } else {
                            if (mResponse.errorBody() != null) {
                                try {
                                    errorText = mResponse.errorBody().string();
                                    int length = errorText.length();
                                    errorText = errorText.substring(20, length - 3);
                                } catch (IOException mE) {
                                    mE.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    public void signUp(String tokenFb) {
        restAdapter.init(false, "sigInFb");
        ServiceUser serviceUser =
                restAdapter.getRetrofit().create(ServiceUser.class);
        Observable<Response<RegisterUserModel>> allSources =
                serviceUser.sigInFb(API_CLIENT_ID, API_CLIENT_SECRET, API_GRANT_TYPE, tokenFb);
        allSources.subscribeOn(Schedulers.newThread()).
                subscribe(new Subscriber<Response<RegisterUserModel>>() {
                    private NetworkResponseEvent event;
                    private NetworkResponseEvent<String> eventError;
                    private RegisterUserModel body;
                    private String errorText = "";

                    @Override
                    public void onCompleted() {
                        if (event != null) {
                            event.setSucess(true);
                            event.setData(body);
                        } else {
                            event = new NetworkResponseEvent<>();
                            event.setId(ApiConstants.ERROR);
                            event.setSucess(false);
                            event.setData(errorText);
                        }
                        BusProvider.send(event);
                    }

                    @Override
                    public void onError(Throwable e) {
                        eventError = new NetworkResponseEvent<>();
                        eventError.setId(ApiConstants.ERROR);
                        eventError.setData("Error load Sources ::: " + e.getMessage());
                        eventError.setSucess(false);
                        BusProvider.send(eventError);
                    }

                    @Override
                    public void onNext(Response<RegisterUserModel> mResponse) {
                        if (mResponse.isSuccessful()) {
                            event = new NetworkResponseEvent<>();
                            event.setId(ApiConstants.SIGN_IN_FB);
                            body = mResponse.body();
                        } else {
                            errorText = handlerErrors(mResponse);
                        }
                    }
                });
    }

    private String handlerErrors(Response mResponse) {
        if (mResponse.code() == 401) {
            return App.getContext().getString(R.string.invalid_mail_or_password);
        } else {
            return App.getContext().getString(R.string.unknowb_error) + mResponse.code();
        }
    }
}
