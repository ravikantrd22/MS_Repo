package com.mission_shakti.missionshaktiassets.view.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.mission_shakti.missionshaktiassets.R;
import com.mission_shakti.missionshaktiassets.databinding.ActivityRegFormBinding;
import com.mission_shakti.missionshaktiassets.utility.ArraylistReturn;
import com.mission_shakti.missionshaktiassets.utility.CommomUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RegFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ActivityRegFormBinding formBinding;

    String areaId, fileName, documentTypeSelected;
    Uri imageUri, imageUri1;
    private long fileSize;
    private byte[] byteArray;
    private int statePos, districtPos, villagePos, blockPos, subDistPos, wardPos;
    private ArrayList<String> stateCode, districtCode, blockCode, subDistrictCd, villageCode, wardCode;
    private ArrayList<String> stateName, districtName, blockName, subDistrictName, villageName, wardName;
    private ArrayAdapter<String> stateAdapter, districtAdapter, villageAdapter, blockAdapter;
    private CommomUtility commonUtilClass = new CommomUtility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        formBinding = ActivityRegFormBinding.inflate(getLayoutInflater());
        formBinding.subDistrictLayout.setVisibility(View.GONE);
        formBinding.wardLayout.setVisibility(View.GONE);
        formBinding.fileDetailLayout.setVisibility(View.GONE);
        formBinding.rural.setChecked(true);
        areaId = "rural";
        formBinding.stateSpinner.setSelection(0);
        formBinding.distSpinner.setSelection(0);
        formBinding.blockSpinner.setSelection(0);
        formBinding.subDistrictSpinner.setSelection(0);
        formBinding.villSpinner.setSelection(0);
        formBinding.wardSpinner.setSelection(0);

        initListener();
        saveData();

        setContentView(formBinding.getRoot());
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    private void initListener(){

        formBinding.backIv.setColorFilter(ContextCompat.getColor(this, R.color.white));

        formBinding.backIv.setOnClickListener(view -> finish());

        formBinding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        formBinding.areaGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (formBinding.areaGrp.getCheckedRadioButtonId()) {
                    case R.id.rural:
                        areaId = "rural";
                        formBinding.urban.setChecked(false);
                        formBinding.blockLayout.setVisibility(View.VISIBLE);
                        formBinding.blockSpinner.setSelection(0);
                        formBinding.villageLayout.setVisibility(View.VISIBLE);
                        formBinding.villSpinner.setSelection(0);
                        formBinding.subDistrictLayout.setVisibility(View.GONE);
                        formBinding.subDistrictSpinner.setSelection(0);
                        formBinding.wardLayout.setVisibility(View.GONE);
                        formBinding.wardSpinner.setSelection(0);
                        break;
                    case R.id.urban:
                        areaId = "urban";
                        formBinding.rural.setChecked(false);
                        formBinding.subDistrictLayout.setVisibility(View.VISIBLE);
                        formBinding.subDistrictSpinner.setSelection(0);
                        formBinding.wardLayout.setVisibility(View.VISIBLE);
                        formBinding.wardSpinner.setSelection(0);
                        formBinding.blockLayout.setVisibility(View.GONE);
                        formBinding.blockSpinner.setSelection(0);
                        formBinding.villageLayout.setVisibility(View.GONE);
                        formBinding.villSpinner.setSelection(0);
                        break;
                }
            }
        });

        formBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formChecks();
            }
        });

        formBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegFormActivity.this, OptionActivity.class));
            }
        });

    }

    private void formChecks(){
        if (formBinding.nameEt.getText().toString().isEmpty()){
            customDialog("Please Enter Name");
        }else if (formBinding.mobileEt.getText().toString().isEmpty()){
            customDialog("Please Enter Mobile no.");
        }else if (!formBinding.mobileEt.getText().toString().isEmpty() && !formBinding.mobileEt.getText().toString().matches("^[6-9]{1}[0-9]{9}$")) {
            customDialog("Please Enter Correct Mobile no.");
        }else if (formBinding.emailEt.getText().toString().isEmpty()){
            customDialog("Please Enter Email Id");
        }else if (!formBinding.emailEt.getText().toString().isEmpty() && !formBinding.emailEt.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            customDialog("Please Enter Correct Email Id");
        }else if (formBinding.aadhaarNoEt.getText().toString().isEmpty() || formBinding.aadhaarNoEt1.getText().toString().isEmpty() || formBinding.aadhaarNoEt2.getText().toString().isEmpty()) {
            customDialog("Please Enter Aadhaar Number");
        }else {
            alertDialog("Asset Register Successfully");
        }
    }

    private void saveData(){

        commonUtilClass.getState(new ArraylistReturn() {
            @Override
            public void onCallback(ArrayList<String> name, ArrayList<String> code) {
                stateName = name;
                stateCode = code;
                stateAdapter = new ArrayAdapter<String>(RegFormActivity.this, android.R.layout.simple_spinner_item, stateName);
                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                formBinding.stateSpinner.setAdapter(stateAdapter);
                formBinding.stateSpinner.setSelection(0);
            }
        });

        formBinding.stateSpinner.setOnItemSelectedListener(this);
        formBinding.distSpinner.setOnItemSelectedListener(this);
        formBinding.blockSpinner.setOnItemSelectedListener(this);
        formBinding.villSpinner.setOnItemSelectedListener(this);
        formBinding.subDistrictSpinner.setOnItemSelectedListener(this);
        formBinding.wardSpinner.setOnItemSelectedListener(this);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Document");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
//                    alertDialog.show();
                    ImagePicker.with(RegFormActivity.this)
                            .crop()
                            .compress(2048)
                            .cameraOnly()
                            .start(101);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            System.out.println("Camera " + "inside camera");
            Uri extract = data.getData();
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), extract);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("CONTENT 5", e.getMessage());
            }
            String encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            documentTypeSelected = "image";
            try {
                getSavePdfPath(encodedString);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("CONTENT 6", e.getMessage());
            }

            try {
                Cursor cursor = RegFormActivity.this.getContentResolver().query(imageUri1, null, null, null, null);
                if (cursor.getCount() <= 0) {
                    cursor.close();
                    throw new IllegalArgumentException("Can't obtain file name, cursor is empty");
                }

                cursor.moveToFirst();

                if (fileSize < 1024) {
                    double size_roundedoff = Math.round(fileSize * 100.0) / 100.0;
                    cursor.close();
                    formBinding.fileDetailLayout.setVisibility(View.VISIBLE);
                    formBinding.fileNameTv.setText(fileName);
                    formBinding.fileSizeTv.setText(String.valueOf(size_roundedoff) + "KB");
                } else if (fileSize > 2048) {
                    androidx.appcompat.app.AlertDialog.Builder alertBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                            this);
                    formBinding.upload.setEnabled(true);
                    alertBuilder.setMessage("File size is more than 2 MB... Please try again !!!");

                    alertBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    formBinding.fileNameTv.setText("");
                                    formBinding.fileSizeTv.setText("");
                                    formBinding.fileDetailLayout.setVisibility(View.GONE);
                                    dialog.dismiss();
                                }
                            });
                    alertBuilder.show();
                } else {
                    float sizeMB = fileSize / 1024;
                    double size_roundedoff = Math.round(sizeMB * 100.0) / 100.0;
                    cursor.close();
                    formBinding.fileDetailLayout.setVisibility(View.VISIBLE);
                    formBinding.fileNameTv.setText(fileName);
                    formBinding.fileSizeTv.setText(String.valueOf(size_roundedoff) + "MB");
                }
                cursor.close();
            } catch (Exception e) {
                Log.d("CONTENT 7", e.getMessage());
            }
        }
    }

    public void getSavePdfPath(String fileNameBase64) throws IOException {
        System.out.println("Camera " + "inside camera");
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            file = new File(this.getExternalFilesDir(null) + "Garuda");
        } else {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "Garuda");
        }

        if (!file.exists()) {
            file.mkdirs();
        }

        Date date = new Date();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("ddmmyyyyHHMMSS");
        String date1 = simpleDateFormat1.format(date);
        if (documentTypeSelected.equals("pdf")) {
            fileName = "document" + date1 + ".pdf";
        } else if (documentTypeSelected.equals("image")) {
            fileName = "image" + date1 + ".jpg";
        }
        File dwldsPath = new File(this.getExternalFilesDir(null) + "Garuda");
        dwldsPath = new File(dwldsPath, fileName);
        byte[] pdfAsBytes = Base64.decode(fileNameBase64, Base64.DEFAULT);
        FileOutputStream os;
        os = new FileOutputStream(dwldsPath, false);
        os.write(pdfAsBytes);
        os.flush();
        os.close();
        fileSize = dwldsPath.length();
        fileSize = fileSize / 1024;
        imageUri1 = FileProvider.getUriForFile(this, "com.mission_shakti.missionshaktiassets" + ".provider", dwldsPath);
        System.out.println("msg");
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

    private void alertDialog(String message) {
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
            public void onClick(View v) {dialog.dismiss();}
        });

        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(RegFormActivity.this, MainActivity.class));
            }
        });

        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.state_spinner:
                statePos = position;
                commonUtilClass.getDistrict(Integer.parseInt(stateCode.get(statePos)), new ArraylistReturn() {
                    @Override
                    public void onCallback(ArrayList<String> name, ArrayList<String> code) {
                        districtName = name;
                        districtCode = code;
                        districtAdapter = new ArrayAdapter<String>(RegFormActivity.this, android.R.layout.simple_spinner_item, districtName);
                        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        formBinding.distSpinner.setAdapter(districtAdapter);
                        formBinding.distSpinner.setSelection(0);
                    }
                });
                break;
            case R.id.dist_spinner:
                districtPos = position;
                if (formBinding.rural.isChecked()) {
                    commonUtilClass.getBlock(Integer.parseInt(stateCode.get(statePos)), Integer.parseInt(districtCode.get(districtPos)), new ArraylistReturn() {
                        @Override
                        public void onCallback(ArrayList<String> name, ArrayList<String> code) {
                            blockName = name;
                            blockCode = code;
                            blockAdapter = new ArrayAdapter<String>(RegFormActivity.this, android.R.layout.simple_spinner_item, blockName);
                            blockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            formBinding.blockSpinner.setAdapter(blockAdapter);
                            formBinding.blockSpinner.setSelection(0);
                        }
                    });
                } else {
                    commonUtilClass.getSubDistrict(Integer.parseInt(stateCode.get(statePos)), Integer.parseInt(districtCode.get(districtPos)), new ArraylistReturn() {
                        @Override
                        public void onCallback(ArrayList<String> name, ArrayList<String> code) {
                            subDistrictName = name;
                            subDistrictCd = code;
                            blockAdapter = new ArrayAdapter<String>(RegFormActivity.this, android.R.layout.simple_spinner_item, subDistrictName);
                            blockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            formBinding.subDistrictSpinner.setAdapter(blockAdapter);
                            formBinding.subDistrictSpinner.setSelection(0);
                        }
                    });
                }
                break;
            case R.id.block_spinner:
                blockPos = position;
                commonUtilClass.getVillage(Integer.parseInt(stateCode.get(statePos)), Integer.parseInt(districtCode.get(districtPos)), Integer.parseInt(blockCode.get(blockPos)), new ArraylistReturn() {
                    @Override
                    public void onCallback(ArrayList<String> name, ArrayList<String> code) {
                        villageName = name;
                        villageCode = code;
                        villageAdapter = new ArrayAdapter<String>(RegFormActivity.this, android.R.layout.simple_spinner_item, villageName);
                        villageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        formBinding.villSpinner.setAdapter(villageAdapter);
                        formBinding.villSpinner.setSelection(0);
                    }
                });
                break;
            case R.id.sub_district_spinner:
                subDistPos = position;
                commonUtilClass.getWard(Integer.parseInt(stateCode.get(statePos)), Integer.parseInt(districtCode.get(districtPos)), Integer.parseInt(subDistrictCd.get(subDistPos)), new ArraylistReturn() {
                    @Override
                    public void onCallback(ArrayList<String> name, ArrayList<String> code) {
                        wardName = name;
                        wardCode = code;
                        villageAdapter = new ArrayAdapter<String>(RegFormActivity.this, android.R.layout.simple_spinner_item, wardName);
                        villageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        formBinding.wardSpinner.setAdapter(villageAdapter);
                        formBinding.wardSpinner.setSelection(0);
                    }
                });
                break;
            case R.id.vill_spinner:
                villagePos = position;
                break;
            case R.id.ward_spinner:
                wardPos = position;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}