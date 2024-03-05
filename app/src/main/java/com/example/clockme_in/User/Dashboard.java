package com.example.clockme_in.User;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.input.InputManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.clockme_in.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Dashboard extends AppCompatActivity {

 TextClock clockTC;
 TextView txtdate;
 Button confirm_btn;

 PinView pinview;
 LinearLayout btnclick;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.AuthenticationCallback authenticationCallback;
 private static final int REQUEST_CODE_PERMISSION = 1234;
//    SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtdate = findViewById(R.id.date);
        ImageView imageview = findViewById(R.id.u_mage);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        txtdate.setText(formattedDate);

        clockTC = findViewById(R.id.idTCClock);
        clockTC.setFormat12Hour("hh:mm:ss a");

        btnclick = findViewById(R.id.btn_click);

        btnclick.setOnClickListener(v -> {
            executor = Executors.newSingleThreadExecutor();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        showToast("Authentication error: " + errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Intent in = new Intent(Dashboard.this, cameraview.class);
                        startActivity(in);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        showToast("Authentication failed");
                    }
                };
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                biometricPrompt = new BiometricPrompt.Builder(this)
                        .setTitle("Fingerprint Authentication")
                        .setSubtitle("Login using your fingerprint")
                        .setDescription("Touch the fingerprint sensor")
                        .setNegativeButton("Cancel", executor, (dialog, which) -> {
                            // Handle cancellation
                        })
                        .build();
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_BIOMETRIC}, 1);
            } else {
                showBiometricPrompt();
            }
        });

    }
        private void showBiometricPrompt() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                biometricPrompt.authenticate(new CancellationSignal(), executor, authenticationCallback);
            } else {
                // Fallback to older API if device does not support BiometricPrompt
                showToast("Biometric authentication is not supported on this device.");
            }
        }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
