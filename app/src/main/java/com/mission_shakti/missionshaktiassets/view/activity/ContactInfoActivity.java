package com.mission_shakti.missionshaktiassets.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mission_shakti.missionshaktiassets.R;
import com.mission_shakti.missionshaktiassets.databinding.ActivityContactInfoBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ContactInfoActivity extends AppCompatActivity {

    private ActivityContactInfoBinding contactInfoBinding;
    String loginId, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactInfoBinding = ActivityContactInfoBinding.inflate(getLayoutInflater());

        loginId = "No";

        contactInfoBinding.otpValidation.setVisibility(View.GONE);

        initListener();

        setContentView(contactInfoBinding.getRoot());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        if (getCurrentFocus() != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    private void initListener(){

        contactInfoBinding.backIv.setColorFilter(ContextCompat.getColor(this, R.color.white));

        contactInfoBinding.backIv.setOnClickListener(view -> finish());

        contactInfoBinding.reqOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userId = contactInfoBinding.mobileNoEt.getText().toString();

                if (!loginId.equals("Yes")) {
                    if (contactInfoBinding.nameEt.getText().toString().isEmpty()) {
                        customDialog("Please Enter Name");
                    } else if (contactInfoBinding.mobileNoEt.getText().toString().isEmpty()) {
                        customDialog("Please Enter Mobile number");
                    }else if (!contactInfoBinding.mobileNoEt.getText().toString().isEmpty() && !contactInfoBinding.mobileNoEt.getText().toString().matches("^[6-9]{1}[0-9]{9}$")) {
                        customDialog("Please Enter Correct Mobile number");
                    } else {
                        sendSMS(userId,"OTP for login is 201303");
                        contactInfoBinding.contactLayout.setVisibility(View.GONE);
                        contactInfoBinding.otpValidation.setVisibility(View.VISIBLE);
                        contactInfoBinding.headerTv.setText("Validation");
                    }
                }else{
                    if (contactInfoBinding.mobileNoEt.getText().toString().isEmpty()) {
                        customDialog("Please Enter Mobile number");
                    }else if (!contactInfoBinding.mobileNoEt.getText().toString().isEmpty() && !contactInfoBinding.mobileNoEt.getText().toString().matches("^[6-9]{1}[0-9]{9}$")) {
                        customDialog("Please Enter Correct Mobile number");
                    }else {
                        contactInfoBinding.otpValidation.setVisibility(View.VISIBLE);
                        contactInfoBinding.contactLayout.setVisibility(View.GONE);
                        contactInfoBinding.headerTv.setText("Validation");
                    }
                }
            }
        });

        contactInfoBinding.validationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactInfoBinding.otpEt.getText().toString().isEmpty()){
                    customDialog("Please Enter OTP");
                }else if (!contactInfoBinding.otpEt.getText().toString().isEmpty() && (contactInfoBinding.otpEt.getText().toString().length()<6)){
                    customDialog("Please Enter Correct OTP");
                }else {
                    Intent contactInfo = new Intent(ContactInfoActivity.this, OptionActivity.class);
                    startActivity(contactInfo);
                    finish();
                }

            }
        });

        contactInfoBinding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginId = "Yes";
                contactInfoBinding.headerTv.setText("Login");
                contactInfoBinding.click.setVisibility(View.GONE);
                contactInfoBinding.nameLayout.setVisibility(View.GONE);
                contactInfoBinding.designationLayout.setVisibility(View.GONE);
            }
        });

        StrictMode.ThreadPolicy st = new  StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(st);
    }

    public static void sendSMS(String reciever, String message) {
        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;
        try
        {
            //prepare connection
            myURL = new URL(buildRequestString(reciever, message));
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new
                    InputStreamReader(myURLConnection.getInputStream()));

            //reading response
            String response;
            while ((response = reader.readLine()) != null)
                //print response
                Log.d("RESPONSE", ""+response);
            //finally close connection
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildRequestString(String reciever, String message){
        //encoding message
        String encoded_message= URLEncoder.encode(message);

        //Send SMS API
        String mainUrl="http://smsgw.sms.gov.in/failsafe/MLink?";

        //Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("username="+"pmmvy.otp");
        sbPostData.append("&pin="+"$gyuygu5");
        sbPostData.append("&message="+encoded_message);
        sbPostData.append("&mnumber="+reciever);
        sbPostData.append("&signature="+"PMMVYI");
        sbPostData.append("&dlt_entity_id="+"1001793466340463992");
        sbPostData.append("&dlt_template_id="+"1007098563950591800");
        return sbPostData.toString();
    }

    private void customDialog(String message) {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_alert_dialog, null);
        TextView text = (TextView) view.findViewById(R.id.text_dialog);
        ImageButton dialogButton = (ImageButton) view.findViewById(R.id.btn_dialog_iv);
        Button okayBtn = (Button) view.findViewById(R.id.btn_dialog);
        alert.setView(view);
        alert.setCancelable(false);
        text.setText(message);

        final AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}