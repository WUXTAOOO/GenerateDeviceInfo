package com.generate.deviceinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.generate.deviceinfolib.DeviceInfoLib;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DeviceInfoLib.getInstance().init(this, "123123123123123");

        DeviceInfoLib.getInstance().test();
    }

}