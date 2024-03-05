package com.example.clockme_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.clockme_in.User.Login;

public class MainActivity extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn= findViewById(R.id.click_start);
        btn.setOnClickListener(v -> {
            Intent intent= new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        });
    }
}