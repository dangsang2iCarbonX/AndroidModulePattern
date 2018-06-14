package com.icarbonx.smartdevice.account;

import android.util.ArrayMap;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Apis manager class
 */
public class Apis<T> {
    //Api interface service
    private T mService;
    //Api interface class
    private Class<T> mServiceClass;
    //Base Url for retrofit
    private String mBaseUrl;

    public Apis() {
    }

    /**
     * Setup the service class and base url.
     *
     * @param serviceClass service class
     * @param baseUrl      base url
     */
    public Apis setService(Class<T> serviceClass, String baseUrl) {
        this.mServiceClass = serviceClass;
        this.mBaseUrl = baseUrl;
        if (serviceClass == null) {
            throw new NullPointerException("service class should not be null");
        }
        if (baseUrl == null || baseUrl.length() < 1) {
            throw new IllegalArgumentException("Invalid Url");
        }
        return this;
    }

    /**
     * Get the api service
     *
     * @return {@link T}
     */
    public T getService() {
        if (mService == null) synchronized (this) {
            if (mService == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(mBaseUrl)
                        .client(mHttpClient)
                        // 添加Retrofit到RxJava的转换器
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        // 添加Gson转换器
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                mService = retrofit.create((Class<T>) mServiceClass);
            }
        }
        return mService;
    }


    //访问超时
    protected long TIMEOUT = 30;
    // Retrofit是基于OkHttpClient的，可以创建一个OkHttpClient进行一些配置
    protected OkHttpClient mHttpClient = new OkHttpClient.Builder()
            //打印接口信息，方便接口调试
            .addInterceptor(new LoggingInterceptor())
            .addNetworkInterceptor(new HttpingInterceptor())
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    /**
     * Override the default {@link OkHttpClient} configure.
     *
     * @param httpClient {@link OkHttpClient}
     */
    protected void setHttpClient(OkHttpClient httpClient) {
        httpClient = httpClient;
    }

    /**
     * Set the timeout of connection and read timeout.
     *
     * @param TIMEOUT time in mills.
     */
    protected static void setTIMEOUT(long TIMEOUT) {
        TIMEOUT = TIMEOUT;
    }
}
