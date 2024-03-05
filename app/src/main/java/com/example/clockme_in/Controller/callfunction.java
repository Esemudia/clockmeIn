package com.example.clockme_in.Controller;

import com.example.clockme_in.Models.ApiResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface callfunction {

    /* LOGIN API CODE*/
    @FormUrlEncoded
    @POST("api/apiservice.php")
    Call<ApiResponse> loginUser(
            @Field("login") String login,
            @Field("email") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/apiservice.php")
    Call<ApiResponse>UpdateTimeStamp(
            @Field("Clock_Ibn") String Reg,
            @Field("") String Name,
            @Field("") String time,
            @Field("") String date
    );

    @FormUrlEncoded
    @POST("api/apiservice.php")
    Call<ApiResponse>checkIp(
            @Field("checkIp") String Ip,
            @Field("checkIp") String IpAddress
    );
    @FormUrlEncoded
    @POST("api/apiservice.php")
    Call<ResponseBody>getImage(
            @Field("getImage") String getImage,
            @Field("email") String email
    );
}
