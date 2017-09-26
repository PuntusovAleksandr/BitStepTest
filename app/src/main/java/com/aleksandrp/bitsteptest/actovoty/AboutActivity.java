package com.aleksandrp.bitsteptest.actovoty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aleksandrp.bitsteptest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.tv_title_toolbar)
    TextView tv_title_toolbar;

    @Bind(R.id.iv_back)
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        iv_back.setVisibility(View.VISIBLE);
        tv_title_toolbar.setText(getString(R.string.about));
    }

    //    ==================================================

    @OnClick(R.id.iv_back)
    public void iv_backClick() {
        onBackPressed();
    }
}
