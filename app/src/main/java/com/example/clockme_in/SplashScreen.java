package com.example.clockme_in;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.clockme_in.Controller.callfunction;
import com.example.clockme_in.Models.ApiResponse;
import com.example.clockme_in.User.Dashboard;
import com.example.clockme_in.User.Login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreen extends AppCompatActivity {

    String ipAddress;
    final String url="https://clockin.smartskep.com/";
    private static final String IP_API_URL = "https://api.ipify.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView textView = findViewById(R.id.textView);

        new GetPublicIPAddressTask().execute();


        int colorFrom = getResources().getColor(R.color.black);
        int color_next = getResources().getColor(R.color.gold);
        int colorTo = getResources().getColor(R.color.end_color);
        ValueAnimator colorAnimation = ObjectAnimator.ofObject(textView, "textColor", new ArgbEvaluator(), colorFrom,color_next ,colorTo);
        colorAnimation.setDuration(3000); // Duration of the animation
        colorAnimation.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.start();

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
//                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
                    new GetPublicIPAddressTask().execute();

                }
            }
        };
        timer.start();
    }

    @SuppressLint("StaticFieldLeak")
    private class GetPublicIPAddressTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(IP_API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                Log.e("GetPublicIPAddress", "Error retrieving IP address", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String ipAddress) {
            super.onPostExecute(ipAddress);
            if (ipAddress != null) {
                IpAddress(ipAddress);
            }
            else {
                AlertDialog.Builder builder= new AlertDialog.Builder(SplashScreen.this);
                builder.setIcon(R.drawable.baseline_error_24).setTitle("  Error");
                builder.setTitle("Error");
                builder.setMessage("Failed to retrieve public IP address");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private  void IpAddress(String Address){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        callfunction retrofitAPI  = retrofit.create(callfunction.class);
       retrofitAPI.checkIp("checkIp",Address).enqueue(new Callback<ApiResponse>() {
           @Override
           public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
               if (response.isSuccessful() && response.body() != null) {
                   if(response.body().getMessage().equals("success"))
                   {
                       if(isLoggedIn) {
                           Intent intent = new Intent(SplashScreen.this, Dashboard.class);
                           startActivity(intent);
                           finish();
                       }
                       else{
                           Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                           startActivity(intent);
                           finish();
                       }
                   }
                   else {

                       AlertDialog.Builder builder= new AlertDialog.Builder(SplashScreen.this);
                       builder.setIcon(R.drawable.baseline_error_24).setTitle("  Error");
                       builder.setTitle("Error");
                       builder.setMessage( response.body().getMessage());
                       builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               dialog.dismiss();
                           }
                       });
                       AlertDialog dialog = builder.create();
                       dialog.show();
                   }
               }
           }

           @Override
           public void onFailure(Call<ApiResponse> call, Throwable t) {

           }
       });
    }
}