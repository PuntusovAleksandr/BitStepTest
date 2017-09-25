package com.aleksandrp.bitsteptest.actovoty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aleksandrp.bitsteptest.App;
import com.aleksandrp.bitsteptest.R;
import com.aleksandrp.bitsteptest.api.model._UserModel;
import com.aleksandrp.bitsteptest.api.service.ServiceApi;
import com.aleksandrp.bitsteptest.databaase.DBHelper;
import com.aleksandrp.bitsteptest.presenter.LoginPresenter;
import com.aleksandrp.bitsteptest.presenter.interfaces.MvpActionView;
import com.aleksandrp.bitsteptest.rx.event.NetworkRequestEvent;
import com.aleksandrp.bitsteptest.utils.SettingsApp;
import com.aleksandrp.bitsteptest.utils.ShowToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aleksandrp.bitsteptest.utils.InternetUtils.checkInternetConnection;
import static com.aleksandrp.bitsteptest.utils.STATIC_PARAMS.SERVICE_JOB_ID_TITLE;

public class LoginActivity extends AppCompatActivity implements MvpActionView {

    @Bind(R.id.progressBar_registration)
    RelativeLayout progressBar_registration;

    @Bind(R.id.tv_login)
    TextView tv_login;
    @Bind(R.id.tv_sign_up)
    TextView tv_sign_up;
    @Bind(R.id.tv_forgot_pass)
    TextView tv_forgot_pass;

    @Bind(R.id.et_email)
    EditText et_email;
    @Bind(R.id.et_password)
    EditText et_password;

    @Bind(R.id.iv_fb)
    ImageView iv_fb;

    private LoginPresenter mPresenter;
    private Intent serviceIntent;

    private Uri sourceImageUri;
    private String mPath;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SettingsApp.getInstance().isLogin()){
            startUntent();
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        tv_sign_up.setText(Html.fromHtml(getString(R.string.text_sign_up)));

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
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }


//    ==================================================

    @OnClick(R.id.tv_login)
    public void tv_loginClick() {
        login();
    }

    @OnClick(R.id.tv_forgot_pass)
    public void tv_forgot_passClick() {
        showMessageError("Напоминание пароля");
    }

    @OnClick(R.id.iv_fb)
    public void iv_fbClick() {
        showMessageError("Регистрация через ФБ");
    }

    @OnClick(R.id.tv_sign_up)
    public void tv_sign_upClick() {
        signUp();
    }

    //    ==================================================
    private void login() {
        if (checkInternetConnection()) {
            mPresenter.login(
                    et_email.getText().toString(),
                    et_password.getText().toString());
        } else {
            showMessageError(App.getContext().getString(R.string.no_internet));
        }
    }


    private void signUp() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
//    ==================================================

    // from presenter
    public void showMessageError(String mMessage) {
        clearPassword();
        showProgress(false);
        ShowToast.showMessageError(mMessage);
    }

    public void goToMainActivity(_UserModel mData) {
        dbHelper = new DBHelper(this);
        dbHelper.initSqlDb(dbHelper);
        dbHelper.putUser(mData);

        startUntent();
    }

    private void startUntent() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        SettingsApp.getInstance().setLogin(true);
        finish();
    }

    public void clearPassword() {
        et_password.setText("");
        showProgress(false);
    }

    public void makeRequest(NetworkRequestEvent<Bundle> mEvent) {
        showProgress(true);
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
