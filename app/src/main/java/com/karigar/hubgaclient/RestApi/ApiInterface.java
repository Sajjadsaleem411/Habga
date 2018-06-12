package com.karigar.hubgaclient.RestApi;

import android.database.Observable;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("api/client/register")
    Call<String> register(@Field("name")String name, @Field("email")String email,
                          @Field("password")String password,@Field("mobile")String mobile);

    @FormUrlEncoded
    @POST("api/client/verifyCode")
    Call<String> verifyCode(@Field("email")String email,@Field("code")Integer code);

    @FormUrlEncoded
    @POST("api/client/resendCode")
    Call<String> resendCode(@Field("email")String email);

    @FormUrlEncoded
    @POST("api/client/signIn")
    Call<String> signIn(@Field("email")String email,@Field("password")String password);

    @FormUrlEncoded
    @POST("api/client/forgot")
    Call<String> forgot(@Field("email")String email);

    @FormUrlEncoded
    @POST("api/client/resetCode")
    Call<String> resetCode(@Field("email")String email,@Field("rest_code")String rest_code);

    @FormUrlEncoded
    @POST("api/client/settingResetEmail")
    Call<String> settingResetEmail(@Field("email")String email, @Field("new_e_mail")String new_e_mail,
                                         @Field("password")String password);

    @FormUrlEncoded
    @POST("api/client/settingResetPassword")
    Call<String> settingResetPassword(@Field("email")String email,@Field("password")String password,
                                            @Field("new_password")String new_password);

    @FormUrlEncoded
    @POST("api/clientServices/fetchService")
    Call<String> fetchService(@Field("service_id")Integer service_id);


    @FormUrlEncoded
    @POST("api/clientServices/menuService")
    Call<String> menuService(@Field("category_id")Integer category_id);

    @FormUrlEncoded
    @POST("api/clientServices/booking")
    Call<String> booking(@Field("service_id")Integer service_id, @Field("count")Integer count, @Field("price")Integer price);

    @FormUrlEncoded
    @POST("api/clientServices/fetchBooking")
    Call<String> fetchBooking(@Field("booking_id")Integer booking_id);

    @FormUrlEncoded
    @POST("api/ads/viewAds")
    Call<String> viewAds(@Field("catID")Integer catID);
}
