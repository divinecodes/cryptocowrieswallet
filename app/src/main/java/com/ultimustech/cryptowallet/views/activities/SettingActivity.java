package com.ultimustech.cryptowallet.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ultimustech.cryptowallet.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTitle(getString(R.string.nav_settings));
    }
}
