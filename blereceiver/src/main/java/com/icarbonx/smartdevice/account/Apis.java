package com.icarbonx.smartdevice.account;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.icarbonx.smartdevice.UrlConstants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * All api services
 *
 * @author lavi
 */
public class Apis {
    private UserAccountHttpService userAccountHttpService;
    private static final Apis ourInstance = new Apis();

    public static Apis getInstance() {
        return ourInstance;
    }

    private Apis() {
    }

    /**
     * Get user account api service
     *
     * @return {@link UserAccountHttpService}
     */
    public UserAccountHttpService getUserAccountService() {
        if (userAccountHttpService == null) {
            synchronized (this) {
                if (userAccountHttpService == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(UrlConstants.BASE_URL)
                            .client(httpClient)
                            // 添加Retrofit到RxJava的转换器
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            // 添加Gson转换器
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    userAccountHttpService = retrofit.create(UserAccountHttpService.class);
                }
            }
        }
        return userAccountHttpService;
    }


    //访问超时
    private static final long TIMEOUT = 30;

    // Retrofit是基于OkHttpClient的，可以创建一个OkHttpClient进行一些配置
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            //打印接口信息，方便接口调试
            .addInterceptor(new LoggingInterceptor())
            .addNetworkInterceptor(new HttpingInterceptor())
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();
}
