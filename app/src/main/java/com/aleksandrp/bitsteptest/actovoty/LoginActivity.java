package com.aleksandrp.bitsteptest.actovoty;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.aleksandrp.bitsteptest.App;
import com.aleksandrp.bitsteptest.R;
import com.aleksandrp.bitsteptest.api.service.ServiceApi;
import com.aleksandrp.bitsteptest.presenter.LoginPresenter;
import com.aleksandrp.bitsteptest.presenter.interfaces.MvpActionView;
import com.aleksandrp.bitsteptest.rx.event.NetworkRequestEvent;
import com.aleksandrp.bitsteptest.utils.SettingsApp;
import com.aleksandrp.bitsteptest.utils.ShowToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

import static com.aleksandrp.bitsteptest.utils.InternetUtils.checkInternetConnection;
import static com.aleksandrp.bitsteptest.utils.STATIC_PARAMS.SERVICE_JOB_ID_TITLE;

public class LoginActivity extends AppCompatActivity implements MvpActionView {

    @Bind(R.id.progressBar_registration)
    RelativeLayout progressBar_registration;


    private LoginPresenter mPresenter;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        serviceIntent = new Intent(this, ServiceApi.class);
        mPresenter = new LoginPresenter();
        mPresenter.setMvpView(this);
        mPresenter.init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.registerSubscriber();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscriber();
    }


    @Override
    public void onDestroy() {
        mPresenter.unRegisterSubscriber();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
        stopService(serviceIntent);
        super.onDestroy();
    }

//    ==================================================

    @OnLongClick(R.id.iv_logo_big)
    public boolean iv_logo_bigClick() {
        StringBuilder mBuilder = new StringBuilder();
        mBuilder.append("Version name :");
        mBuilder.append(SettingsApp.getInstance().getVersionName());
        mBuilder.append("\nVersion code :");
        mBuilder.append(SettingsApp.getInstance().getVersionCode());
        showMessageError(mBuilder.toString());
        return true;
    }

    //    ==================================================
    private void login() {
        if (checkInternetConnection()) {
            showProgress(true);
            mPresenter.checkValidEmailPass(
                    et_e_mail.getText().toString(),
                    et_password.getText().toString());
        } else {
            showMessageError(App.getContext().getString(R.string.no_internet));
        }
    }


//    ==================================================

    // from presenter
    public void showMessageError(String mMessage) {
        clearPassword();
        showProgress(false);
        ShowToast.showMessageError(mMessage);
    }

    public void addTokenFireBase() {
        SettingsApp.getInstance().setLogin(true);
        mPresenter.addTokenFirebase();
    }

    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
            }
        }, 500);
    }

    public void clearPassword() {
        et_password.setText("");
        showProgress(false);
    }

    public void makeRequest(NetworkRequestEvent<Bundle> mEvent) {
        if (mEvent.getData() != null)
            serviceIntent.putExtras((Bundle) mEvent.getData());
        serviceIntent.putExtra(SERVICE_JOB_ID_TITLE, mEvent.getId());
        startService(serviceIntent);
    }

    private void showProgress(boolean mShowPhone) {
        if (mShowPhone) {
            progressBar_registration.setVisibility(View.VISIBLE);
        } else {
            progressBar_registration.setVisibility(View.GONE);
        }
    }


}
