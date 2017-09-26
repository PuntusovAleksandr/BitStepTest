package com.aleksandrp.bitsteptest.api.helper;

import android.net.Uri;

import com.aleksandrp.bitsteptest.api.RestAdapter;
import com.aleksandrp.bitsteptest.api.constant.ApiConstants;
import com.aleksandrp.bitsteptest.api.interfaces.ServiceUser;
import com.aleksandrp.bitsteptest.api.model.NewUserModel;
import com.aleksandrp.bitsteptest.api.model.UserModel;
import com.aleksandrp.bitsteptest.rx.BusProvider;
import com.aleksandrp.bitsteptest.rx.event.NetworkResponseEvent;
import com.aleksandrp.bitsteptest.utils.SettingsApp;

import java.io.File;
import java.io.IOException;

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

    //================================================================================================

    public void sigIn() {
        String email = SettingsApp.getInstance().getEmail();
        String pass = SettingsApp.getInstance().getPass();

        restAdapter.init(false, "sigIn");
        ServiceUser serviceUser =
                restAdapter.getRetrofit().create(ServiceUser.class);
        Observable<Response<UserModel>> allSources =
                serviceUser.sigIn(email, pass);
        allSources
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<UserModel>>() {
                    private NetworkResponseEvent event;
                    private NetworkResponseEvent<String> eventError;
                    private UserModel body;
                    private String errorText = "";

                    @Override
                    public void onCompleted() {

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
                    public void onNext(Response<UserModel> mResponse) {
                        if (mResponse.isSuccessful()) {
                            event = new NetworkResponseEvent<>();
                            event.setId(ApiConstants.SIGN_IN);
                            body = mResponse.body();
                        } else {
                            if (mResponse.errorBody() != null) {
                                try {
                                    errorText = mResponse.errorBody().string();
                                } catch (IOException mE) {
                                    mE.printStackTrace();
                                }
                            }
                        }
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
                });

    }

    public void sigUp(NewUserModel mNewUserModel) {
        boolean isPhoto = true;
        String tokenFCM = SettingsApp.getInstance().getTokenFCM();

        File file = new File(mNewUserModel.getPath());
        if (!file.exists()) {
            isPhoto = false;
        }

        restAdapter.init(false, "sigUp");
        ServiceUser serviceUser =
                restAdapter.getRetrofit().create(ServiceUser.class);
        Observable<Response<UserModel>> allSources =
                serviceUser.sigUpNoPhoto(
                        createPartFromString(mNewUserModel.getEmail()),
                        createPartFromString(mNewUserModel.getOrganisation()),
                        createPartFromString(mNewUserModel.getLocale()),
                        createPartFromString(mNewUserModel.getSite()),
                        createPartFromString(mNewUserModel.getPassword()),
                        createPartFromString(tokenFCM),
                        createPartFromString(mNewUserModel.getPhone())
                );
        if (isPhoto) {
            MultipartBody.Part part = prepareFile("icon", file);
            allSources = serviceUser.sigUp(
                    createPartFromString(mNewUserModel.getEmail()),
                    createPartFromString(mNewUserModel.getOrganisation()),
                    createPartFromString(mNewUserModel.getLocale()),
                    createPartFromString(mNewUserModel.getSite()),
                    createPartFromString(mNewUserModel.getPassword()),
                    createPartFromString(mNewUserModel.getPhone()),
                    createPartFromString(tokenFCM),
                    part
            );
        }
        allSources
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<UserModel>>() {
                    private NetworkResponseEvent event;
                    private NetworkResponseEvent<String> eventError;
                    private UserModel body;
                    private String errorText = "";

                    @Override
                    public void onCompleted() {

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
                    public void onNext(Response<UserModel> mResponse) {
                        if (mResponse.isSuccessful()) {
                            event = new NetworkResponseEvent<>();
                            event.setId(ApiConstants.SIGN_UP);
                            body = mResponse.body();
                        } else {
                            if (mResponse.errorBody() != null) {
                                try {
                                    errorText = mResponse.errorBody().string();
                                } catch (IOException mE) {
                                    mE.printStackTrace();
                                }
                            }
                        }
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
                });
    }
}
