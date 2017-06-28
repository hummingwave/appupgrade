package com.hummingwave.waveupgrade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hummingwave.upgrade.Upgrade;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Upgrade.init(this);

    }

}
