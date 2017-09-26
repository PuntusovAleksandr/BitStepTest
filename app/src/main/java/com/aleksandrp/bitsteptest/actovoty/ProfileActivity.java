package com.aleksandrp.bitsteptest.actovoty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.aleksandrp.bitsteptest.R;
import com.aleksandrp.bitsteptest.api.model.NewUserModel;
import com.aleksandrp.bitsteptest.databaase.DBHelper;
import com.aleksandrp.bitsteptest.utils.SettingsApp;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aleksandrp.bitsteptest.utils.ShowImages.showImageFromFile;

public class ProfileActivity extends AppCompatActivity {

    @Bind(R.id.iv_menu)
    ImageView iv_menu;
    @Bind(R.id.iv_icon_user)
    ImageView iv_icon_user;

    @Bind(R.id.tv_title_toolbar)
    TextView tv_title_toolbar;
    @Bind(R.id.tv_sign_up)
    TextView tv_sign_up;

    @Bind(R.id.et_email)
    EditText et_email;
    @Bind(R.id.et_organisation)
    EditText et_organisation;
    @Bind(R.id.et_locale)
    EditText et_locale;
    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.et_site)
    EditText et_site;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.et_password_confirm)
    EditText et_password_confirm;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setActionBar(toolbar);
        getActionBar().setDisplayShowTitleEnabled(false);

        setUi();
    }

    @Override
    public void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Операции для выбранного пункта меню
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(ProfileActivity.this, AboutActivity.class));
                return true;
            case R.id.logout:
                SettingsApp.getInstance().setLogin(false);
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //    ================================================
    @OnClick(R.id.iv_menu)
    public void iv_menuClick() {
        this.openOptionsMenu();
    }

    private void setUi() {

        tv_sign_up.setVisibility(View.GONE);
        iv_menu.setVisibility(View.VISIBLE);
        tv_title_toolbar.setText(R.string.profile);
        et_email.setEnabled(false);

        dbHelper = new DBHelper(this);
        dbHelper.initSqlDb(dbHelper);
        NewUserModel model = dbHelper.getUser();

        if (model == null) return;
        et_email.setText(model.getEmail());
        et_organisation.setText(model.getOrganisation());
        et_locale.setText(model.getLocale());
        et_phone.setText(model.getPhone());
        et_site.setText(model.getSite());

        showImageFromFile(model.getPath(), iv_icon_user);
    }
}
