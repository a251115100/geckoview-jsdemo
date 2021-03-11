package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.mozilla.geckoview.GeckoView;

public class MainActivity extends AppCompatActivity {

    private GeckoView geckoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geckoView = findViewById(R.id.gecko_v);
    }
}