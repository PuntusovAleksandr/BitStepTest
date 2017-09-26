package com.aleksandrp.bitsteptest.actovoty;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.aleksandrp.bitsteptest.R;
import com.aleksandrp.bitsteptest.api.model.NewUserModel;
import com.aleksandrp.bitsteptest.databaase.DBHelper;
import com.aleksandrp.bitsteptest.utils.SettingsApp;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.mihaelisaev.metw.MaskedEditTextWatcher;
import com.mihaelisaev.metw.MaskedEditTextWatcherDelegate;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.graphics.Color.WHITE;
import static com.aleksandrp.bitsteptest.utils.FileUtils.imageDownload;
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
    @Bind(R.id.rl_last_24)
    TextView rl_last_24;
    @Bind(R.id.rl_last_week)
    TextView rl_last_week;
    @Bind(R.id.rl_last_month)
    TextView rl_last_month;

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

    @Bind(R.id.line_char)
    LineChart line_char;

    @Bind(R.id.ll_char)
    RelativeLayout ll_char;

    private DBHelper dbHelper;
    private int countDays;
    private boolean flagZoom;

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


        File file = new File(SettingsApp.getInstance().getPathIcon());
        if (file.exists()) {
            showImageFromFile(file, iv_icon_user);
        }else {
            imageDownload(model.getPath());
            showImageFromFile(model.getPath(), iv_icon_user);
        }

        setDefaultBt();
        rl_last_24.setTextColor(WHITE);
        rl_last_24.setBackgroundResource(R.drawable.bg_active_bt);

        initChar();

        initMask();
    }

    private void initMask() {
        MaskedEditTextWatcher simpleListener = new MaskedEditTextWatcher(et_phone, new MaskedEditTextWatcherDelegate() {
            @Override
            public String maskForCountryCode(String text) {
                //Here you receive just entered text
                //and you should return the mask or null
                if (text.equals("1")) {
                    return "+1 ###-###-####";
                } else if (text.equals("7")) {
                    return "+7 (###) ###-##-##";
                } else if (text.equals("44")) {
                    return "+44 (##) ###-####";
                } else if (text.equals("64")) {
                    return "+64 ## # (###) ##-##";
                }
                return "+380 ## ### ## ##";
            }
        });
//Add the textWatcher to  text field
        et_phone.addTextChangedListener(simpleListener);
    }

    private void initChar() {

        line_char.setDrawGridBackground(false);
        line_char.setDescription(new Description());
        line_char.setDrawBorders(false);

        line_char.getAxisLeft().setEnabled(true);
        line_char.getAxisLeft().setDrawAxisLine(false);
        line_char.getAxisLeft().setDrawGridLines(false);
        line_char.getAxisLeft().setLabelCount(4, false);
        line_char.getAxisLeft().setDrawLabels(true);

        line_char.getAxisRight().setEnabled(false);
        line_char.getAxisRight().setDrawAxisLine(false);
        line_char.getAxisRight().setDrawGridLines(false);

        line_char.getXAxis().setDrawAxisLine(true);
        line_char.getXAxis().setDrawGridLines(true);
//      line_charrt.getXAxis().setValueFormatter(new MyValueFormatter());
        line_char.getXAxis().setDrawLabels(true);
        // skip label on top axis
        line_char.getXAxis().setLabelCount(0);
        // set position labels
        line_char.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        line_char.getXAxis().setAvoidFirstLastClipping(true);


        // enable touch gestures
        line_char.setTouchEnabled(true);
        // enable scaling and dragging
        line_char.setDragEnabled(true);
//        mChart.setScaleEnabled(true);
        line_char.setScaleXEnabled(true);
        line_char.setScaleYEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        line_char.setPinchZoom(true);

        line_char.setVisibleXRangeMaximum(15f);
        // set min size
        line_char.setAutoScaleMinMaxEnabled(false);

        Legend l = line_char.getLegend();
        l.setForm(Legend.LegendForm.SQUARE);
        l.setXEntrySpace(30f);      // size between legends
        l.setDirection(Legend.LegendDirection.RIGHT_TO_LEFT);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        ll_char.setVisibility(View.VISIBLE);
        countDays = 24;
        flagZoom = true;

        setDataToChart();
    }

    private void setDataToChart() {
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int i = 0; i < countDays; i++) {
            double val = (Math.random() * 10) + 3;
            values.add(new Entry(i, (float) val));
        }

        LineDataSet dataSet = new LineDataSet(values, "");
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleRadius(4f);
        dataSet.setColor(WHITE);
        dataSet.setCircleColor(WHITE);

        // disable value
        dataSet.setDrawValues(false);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_char_white);
        dataSet.setFillDrawable(drawable);
        dataSets.add(dataSet);

        //set bottom title
        String textVal = getString(R.string.month);
        if (countDays == 24) {
            textVal = textVal + getString(R.string.day);
        } else {
            textVal = textVal + getString(R.string.week);
        }

        // zoom for bid size entity
        if (countDays != 7 && flagZoom) {
            line_char.zoom(4f, 1f, 1f, 1f);
            flagZoom = false;
        } else if (countDays == 7) {
            line_char.zoom(0.25f, 1f, 1f, 1f);
            flagZoom = true;
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < countDays; i++) {
            xVals.add(((i) + 1) + textVal);
        }


        LineData data = new LineData(dataSets);
        data.setValueTextColor(WHITE);
        line_char.setData(data);
        line_char.getAxisLeft().setTextColor(WHITE);
        line_char.getXAxis().setTextColor(WHITE);

        line_char.invalidate();

    }

    @OnClick(R.id.rl_last_24)
    public void last_24Click() {
        if (countDays == 24) return;
        countDays = 24;
        setDataToChart();
        setDefaultBt();
        rl_last_24.setTextColor(WHITE);
        rl_last_24.setBackgroundResource(R.drawable.bg_active_bt);
    }

    @OnClick(R.id.rl_last_week)
    public void last_weekClick() {
        if (countDays == 7) return;
        countDays = 7;
        setDataToChart();
        setDefaultBt();
        rl_last_week.setTextColor(WHITE);
        rl_last_week.setBackgroundResource(R.drawable.bg_active_bt);
    }

    @OnClick(R.id.rl_last_month)
    public void last_monthlick() {
        if (countDays == 30) return;
        countDays = 30;
        setDataToChart();
        setDefaultBt();
        rl_last_month.setTextColor(WHITE);
        rl_last_month.setBackgroundResource(R.drawable.bg_active_bt);
    }

    private void setDefaultBt() {
        rl_last_24.setTextColor(Color.LTGRAY);
        rl_last_24.setBackgroundResource(R.drawable.bg_deactib_bt);
        rl_last_week.setTextColor(Color.LTGRAY);
        rl_last_week.setBackgroundResource(R.drawable.bg_deactib_bt);
        rl_last_month.setTextColor(Color.LTGRAY);
        rl_last_month.setBackgroundResource(R.drawable.bg_deactib_bt);
    }
}
