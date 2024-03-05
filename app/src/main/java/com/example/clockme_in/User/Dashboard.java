package com.example.clockme_in.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.clockme_in.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Dashboard extends AppCompatActivity {

 TextClock clockTC;
 TextView txtdate;
 Button confirm_btn;

 LinearLayout btnclick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtdate=findViewById(R.id.date);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        txtdate.setText(formattedDate);

        clockTC = findViewById(R.id.idTCClock);
        clockTC.setFormat12Hour("hh:mm:ss a");

        btnclick= findViewById(R.id.btn_click);

        btnclick.setOnClickListener(v->{
            BottomSheetDialog dialog= new BottomSheetDialog(Dashboard.this);
            View view1 = LayoutInflater.from(Dashboard.this).inflate(R.layout.bottom_sheet_layout,null);
            dialog.setContentView(view1);
            dialog.show();
            confirm_btn= view1.findViewById(R.id.confirm_button);
            confirm_btn.setOnClickListener(v1 -> {
                Intent in = new Intent(Dashboard.this,cameraview.class);
                startActivity(in);
            });
        });

        BiometricManager biomanager = BiometricManager.from(this);
    }
}