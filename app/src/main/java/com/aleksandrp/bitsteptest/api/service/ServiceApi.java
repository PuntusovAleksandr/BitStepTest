package com.aleksandrp.bitsteptest.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.aleksandrp.bitsteptest.App;
import com.aleksandrp.bitsteptest.R;
import com.aleksandrp.bitsteptest.api.constant.ApiConstants;
import com.aleksandrp.bitsteptest.api.helper.ApiUserHelper;
import com.aleksandrp.bitsteptest.api.model.NewUserModel;
import com.aleksandrp.bitsteptest.rx.BusProvider;
import com.aleksandrp.bitsteptest.rx.event.NetworkFailEvent;
import com.aleksandrp.bitsteptest.rx.event.NetworkResponseEvent;
import com.aleksandrp.bitsteptest.rx.event.UpdateUiEvent;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static com.aleksandrp.bitsteptest.utils.STATIC_PARAMS.KEY_NEW_USER;
import static com.aleksandrp.bitsteptest.utils.STATIC_PARAMS.SERVICE_JOB_ID_TITLE;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class ServiceApi extends Service {

    private Subscriber subscriber;
    private static final String LOGGER_TAG = "ServiceApi";
    private int startId;

    private ApiUserHelper mApiUserHelper;

    public ServiceApi() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ID_SERVICE", "onCreate ID_SERVICE " + startId);
        subscriber = new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                if (o instanceof NetworkResponseEvent) {
                    NetworkResponseEvent event = (NetworkResponseEvent) o;
                    if (!event.isSucess()) {
                        requestFailed(event);
                    } else {
                        requestCallBack(event);
                    }
                }
            }
        };
        BusProvider.observe()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        Log.d(LOGGER_TAG, "subscribe");
        mApiUserHelper = new ApiUserHelper();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriber.unsubscribe();
        subscriber = null;
        mApiUserHelper = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("ID_SERVICE", "onBind ID_SERVICE " + startId);
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLowMemory() {
        Log.d("ID_SERVICE", "onLowMemory  stopSelf ID_SERVICE " + startId);
        stopSelf(startId);
        super.onLowMemory();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;
        int jobId = intent.getIntExtra(SERVICE_JOB_ID_TITLE, -1);
        Bundle bundle = intent.getExtras();
        NewUserModel newUserModel = bundle.getParcelable(KEY_NEW_USER);

        switch (jobId) {
            // for user
            case ApiConstants.SIGN_IN:
                mApiUserHelper.sigIn();
                break;
            case ApiConstants.SIGN_UP:
                mApiUserHelper.sigUp(newUserModel);
                break;

        }
        return START_NOT_STICKY;
    }


    private void requestCallBack(NetworkResponseEvent event) {
        Log.d("ID_SERVICE", "requestCallBack ID_SERVICE " + startId);
        UpdateUiEvent updateUiEvent = new UpdateUiEvent();
        updateUiEvent.setSuccess(event.isSucess());
        updateUiEvent.setData(event.getData());
        Log.d("INIT", "answer " + event.getId());
        switch (event.getId()) {
            // for user
            case ApiConstants.SIGN_IN:
                updateUiEvent.setId(ApiConstants.RESPONSE_SIGN_IN);
                break;
            case ApiConstants.SIGN_UP:
                updateUiEvent.setId(ApiConstants.RESPONSE_SIGN_UP);
                break;

        }
        if (updateUiEvent != null) {
            BusProvider.send(updateUiEvent);
        }
        Log.d("ID_SERVICE", "requestCallBack  stopSelf ID_SERVICE " + startId);
        stopSelf(startId);
        Log.d("ID_SERVICE_", "onStartCommand stopSelf " + startId);
    }

    private void requestFailed(NetworkResponseEvent event) {

        NetworkFailEvent networkFailEvent = new NetworkFailEvent();
        if (event.getId() == ApiConstants.ERROR) {
            String data = (String) event.getData();
            if (data.isEmpty()) data = App.getContext().getString(R.string.no_internet);
            networkFailEvent.setMessage(data);
        } else {
            networkFailEvent.setMessage("Error request");
        }
        BusProvider.send(networkFailEvent);
        Log.d("ID_SERVICE", "requestFailed ID_SERVICE " + startId);
        stopSelf(startId);
    }
}
