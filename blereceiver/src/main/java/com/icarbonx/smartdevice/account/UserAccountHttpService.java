package com.icarbonx.smartdevice.account;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Interface created depends on {@link retrofit2.Retrofit}.
 * @author lavi
 */
public interface UserAccountHttpService {

//    @GET("verifyCodeController/send.do")
//    Call<BaseResp> getVerifyCode(@Query("phoneNumber")String phoneNumber, @Query("use")int use);

    @FormUrlEncoded
    @POST("verifyCodeController/send.do")
    Call<BaseResp> getVerifyCode(@Field("phoneNumber") String phoneNumber, @Field("use") int use);

    @FormUrlEncoded
    @POST("userAccountController/login.do")
    Call<LoginResp> login(@Field("phoneNumber") String phoneNumber, @Field("verifyCode") String verifyCode);
}
