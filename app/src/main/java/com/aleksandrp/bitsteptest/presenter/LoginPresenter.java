package com.aleksandrp.bitsteptest.presenter;

import android.os.Bundle;

import com.aleksandrp.bitsteptest.App;
import com.aleksandrp.bitsteptest.R;
import com.aleksandrp.bitsteptest.actovoty.LoginActivity;
import com.aleksandrp.bitsteptest.actovoty.RegisterActivity;
import com.aleksandrp.bitsteptest.api.constant.ApiConstants;
import com.aleksandrp.bitsteptest.api.model.NewUserModel;
import com.aleksandrp.bitsteptest.api.model.UserModel;
import com.aleksandrp.bitsteptest.presenter.interfaces.BasePresenter;
import com.aleksandrp.bitsteptest.presenter.interfaces.PresenterEventListener;
import com.aleksandrp.bitsteptest.rx.BusProvider;
import com.aleksandrp.bitsteptest.rx.event.NetworkFailEvent;
import com.aleksandrp.bitsteptest.rx.event.NetworkRequestEvent;
import com.aleksandrp.bitsteptest.rx.event.UpdateUiEvent;
import com.aleksandrp.bitsteptest.utils.SettingsApp;
import com.aleksandrp.bitsteptest.utils.Validation;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static com.aleksandrp.bitsteptest.api.constant.ApiConstants.RESPONSE_SIGN_IN;
import static com.aleksandrp.bitsteptest.api.constant.ApiConstants.RESPONSE_SIGN_UP;
import static com.aleksandrp.bitsteptest.utils.STATIC_PARAMS.KEY_NEW_USER;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class LoginPresenter extends BasePresenter implements PresenterEventListener {


    private Subscriber subscriber;

    @Override
    public void init() {

    }

    @Override
    public void registerSubscriber() {
        subscriber = new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object mO) {
                if (mO instanceof UpdateUiEvent) {
                    UpdateUiEvent event = (UpdateUiEvent) mO;
                    Object data = event.getData();

                    if (event.getId() == RESPONSE_SIGN_IN) {
                        saveTokenServer((UserModel) data);
                    } else if (event.getId() == RESPONSE_SIGN_UP) {
                        showData((UserModel) data);
                    }

                } else if (mO instanceof NetworkFailEvent) {
                    NetworkFailEvent event = (NetworkFailEvent) mO;
                    showMessageError(event.getMessage());
                }
            }
        };
        BusProvider.observe().observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    //================================================

    public void showMessageError(String mMessage) {
        try {
            ((LoginActivity) mvpView).showMessageError(mMessage);
        } catch (Exception mE) {
            mE.printStackTrace();
        }
        try {
            ((RegisterActivity) mvpView).showMessageError(mMessage);
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }


    //================================================
    @Override
    public void unRegisterSubscriber() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        subscriber = null;
    }

    //================================================

    public void login(String email, String password) {
        boolean validEmail = Validation.isValidEmail(email);
        boolean validPassword = Validation.isValidPassword(password);
        if (!validEmail) {
            showMessageError(App.getContext().getString(R.string.email_incorrect));
            ((LoginActivity) mvpView).clearPassword();
        } else if (!validPassword) {
            showMessageError(App.getContext().getString(R.string.password_rules));
            ((LoginActivity) mvpView).clearPassword();
        } else if (validEmail && validPassword) {
            SettingsApp.getInstance().setEmail(email);
            SettingsApp.getInstance().setPass(password);


            NetworkRequestEvent<Bundle> networkRequestEvent = new NetworkRequestEvent();
            networkRequestEvent.setId(ApiConstants.SIGN_IN);
            ((LoginActivity) mvpView).makeRequest(networkRequestEvent);
        }
    }


    public void registerNewUser(String mEmail, String mOrganisation, String mLocale, String mSite,
                                String mPassword, String mPhone, String mPath) {

        NewUserModel newUserModel = new NewUserModel(
                mEmail,
                mOrganisation,
                mLocale,
                mSite,
                mPassword,
                mPhone,
                mPath
        );
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_NEW_USER, newUserModel);

        NetworkRequestEvent<Bundle> networkRequestEvent = new NetworkRequestEvent();
        networkRequestEvent.setId(ApiConstants.SIGN_UP);
        networkRequestEvent.setData(bundle);
        ((RegisterActivity) mvpView).makeRequest(networkRequestEvent);
    }


    private void saveTokenServer(UserModel mData) {
        if (mData.status) {
            try {
                ((LoginActivity) mvpView).goToMainActivity();
            } catch (NullPointerException mE) {
                mE.printStackTrace();
            }
        } else {
            showMessageError(mData.error);
        }
    }

    private void showData(UserModel mData) {
        if (mData.error != null) {
            showMessageError(mData.error);
        }
    }
}
