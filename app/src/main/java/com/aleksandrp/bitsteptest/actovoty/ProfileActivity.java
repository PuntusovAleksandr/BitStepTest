package com.aleksandrp.bitsteptest.actovoty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aleksandrp.bitsteptest.R;
import com.aleksandrp.bitsteptest.api.model.NewUserModel;
import com.aleksandrp.bitsteptest.databaase.DBHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setUi();
    }

    @Override
    public void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
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
