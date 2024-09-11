package com.mission_shakti.missionshaktiassets.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mission_shakti.missionshaktiassets.R;
import com.mission_shakti.missionshaktiassets.databinding.ActivityOptionBinding;

public class OptionActivity extends AppCompatActivity {

    private ActivityOptionBinding optionBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        optionBinding = ActivityOptionBinding.inflate(getLayoutInflater());

        initListener();

        setContentView(optionBinding.getRoot());
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    private void initListener(){

        optionBinding.sakhiNiwasCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactInfo = new Intent(OptionActivity.this, RegFormActivity.class);
                startActivity(contactInfo);
                finish();
            }
        });

    }
}