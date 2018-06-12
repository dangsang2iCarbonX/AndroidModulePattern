package com.icarbonx.smartdevice.account;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Interface created depends on {@link retrofit2.Retrofit}.
 * @author lavi
 */
public interface UserAccountHttpService {
    //获取验证码
    @FormUrlEncoded
    @POST("verifyCodeController/send.do")
    Observable<BaseResponse> getVerifyCode(@Field("phoneNumber") String phoneNumber, @Field("use") int use);

    //手机号和验证码登陆
    @FormUrlEncoded
    @POST("userAccountController/login.do")
    Observable<LoginResponse> login(@Field("phoneNumber") String phoneNumber, @Field("verifyCode") String verifyCode);

    //登陆后绑定新手机
    @FormUrlEncoded
    @POST("userAccountController/bindingPhoneNumber.do")
    Observable<BaseResponse> bingingPhoneNumber(@Field("phoneNumber")String phoneNumber,@Field("verifyCode")String verifyCode);

    @GET("userAccountController/forwardLogin.do")
    Observable<BaseResponse>forwardLogin();

    @GET("userAccountController/forwardWeeklyLogin.do")
    Observable<BaseResponse>forwardWeeklyLogin();

    //获取登陆的账户信息
    @GET("userAccountController/getAccountInfo.do")
    Observable<UserInfoResponse> getAccointInfo();

    //获取登陆用户的信息
    @GET("userAccountController/getUserInfo.do")
    Observable<UserInfoResponse> getUserInfo();

    //查询当前账号登陆信息
    @GET("userAccountController/query.do")
    Observable<QueryResponse>query();

    //更新当前用户资料
    @FormUrlEncoded
    @POST("userAccountController/update.do")
    Observable<UpdateResponse> update(@Field("accountPO")String accountPO);

    //更新用户名和邮件地址
    @FormUrlEncoded
    @POST("userAccountController/updateAccountInfo.do")
    Observable<BaseResponse> updateAccountInfo(@Field("accountName")String accountName,@Field("email")String email);

    //更新用户名和邮件地址
    @FormUrlEncoded
    @POST("userAccountController/updateUserInfo.do")
    Observable<BaseResponse> updateUserInfo(@Field("accountName")String accountName,@Field("email")String email);

    //更换用户头像
    @Multipart
    @POST("userAccountController/updateUserPhoto.do")
    Observable<BaseResponse> updateUserPhoto(@Part("file") String file);

    //更换用户头像
    @FormUrlEncoded
    @POST("userAccountController/uploadUserEncodedPhoto.do")
    Observable<BaseResponse> uploadUserEncodedPhoto(@Field("encodedPhoto") String encodedPhoto,@Field("format")String format);

    //解绑，绑定新手机时验证手机号
    @FormUrlEncoded
    @POST("userAccountController/verifyPhoneNumber.do")
//    Observable<BaseResponse> verifyPhoneNumber(@Field("phoneNumber")String accountName);
    Observable<BaseResponse> verifyPhoneNumber(@Field("phoneNumber")String accountName);
}
