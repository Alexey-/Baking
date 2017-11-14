package com.example.baking.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.baking.R;

public class BaseActivity extends AppCompatActivity {

    private boolean mTablet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTablet = getResources().getBoolean(R.bool.is_tablet);
    }

    protected boolean isTablet() {
        return mTablet;
    }

}
