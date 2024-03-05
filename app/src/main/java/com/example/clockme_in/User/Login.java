package com.example.clockme_in.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.clockme_in.Controller.Callclass;
import com.example.clockme_in.Controller.callfunction;
import com.example.clockme_in.Models.ApiResponse;
import com.example.clockme_in.Models.Users;
import com.example.clockme_in.R;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    Button btn_click;
    private EditText txtemail;
    private EditText txtpass;
    final String url="https://clockin.smartskep.com/";
    private Callclass retro= new Callclass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_click= findViewById(R.id.login);
        txtemail= findViewById(R.id.email);
        txtpass = findViewById(R.id.password);
        btn_click.setOnClickListener(v -> {
            logoinprocess();
        });

    }
    private void  login(){
        Intent in = new Intent(Login.this, Dashboard.class);
        startActivity(in);
        finish();
    }

    public void logoinprocess(){

        String email=txtemail.getText().toString();
        String pass= txtpass.getText().toString();


        retrofitcall(email, pass);
    }

    public  void  retrofitcall(String email, String password ){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        callfunction retrofitAPI  = retrofit.create(callfunction.class);
        Call<ApiResponse> call = retrofitAPI.loginUser("login",email,password);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful())
                {
                    String mess = Objects.requireNonNull(response.body()).getMessage();
                    if(mess.equals("Login successful")) {
                        assert response.body() != null;
                        populate(response.body());
                    }
                    else {
                        alert(mess);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    private void populate(ApiResponse body){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Users user = body.getUser();
        editor.putString("Id", user.getUser_id());
        editor.putString("fullname", user.getFullname());
        editor.putString("email", user.getEmail());
        editor.putString("phone", user.getPhone());
        editor.putBoolean("is_logged_in", true);
        editor.apply();
        Intent in = new Intent(Login.this, Dashboard.class);
        startActivity(in);
        finish();
    }
     private void alert(String mess){
         AlertDialog.Builder builder= new AlertDialog.Builder(Login.this);
         builder.setIcon(R.drawable.baseline_error_24).setTitle("  Error");
         builder.setTitle("Error");
         builder.setMessage(mess);
         builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int whichButton) {
                 dialog.dismiss();
             }
         });
         AlertDialog dialog = builder.create();
         dialog.show();
     }
}