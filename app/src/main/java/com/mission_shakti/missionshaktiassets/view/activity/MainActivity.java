package com.mission_shakti.missionshaktiassets.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mission_shakti.missionshaktiassets.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        initClickListener();

        setContentView(mainBinding.getRoot());
    }

    private void initClickListener(){

        mainBinding.fillFormCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent contactInfo = new Intent(MainActivity.this, ContactInfoActivity.class);
                startActivity(contactInfo);
            }
        });
    }

}