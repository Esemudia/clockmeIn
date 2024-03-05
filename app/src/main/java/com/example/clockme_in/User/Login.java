package com.example.clockme_in.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.clockme_in.R;

public class Login extends AppCompatActivity {
    Button btn_click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_click= findViewById(R.id.login);
        btn_click.setOnClickListener(v -> {
            login();
        });

    }
    private void  login(){
        Intent in = new Intent(Login.this, Dashboard.class);
        startActivity(in);
        finish();
    }
}