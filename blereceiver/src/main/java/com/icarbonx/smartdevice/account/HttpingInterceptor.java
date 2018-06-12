package com.icarbonx.smartdevice.account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Http interceptor do nothing
 * @author lavi
 */
public class HttpingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
