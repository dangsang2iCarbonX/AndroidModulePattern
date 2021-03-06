package com.icarbonx.smartdevice.http;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;
import com.icarbonx.smartdevice.ble.manager.BleDevice;
import com.icarbonx.smartdevice.common.NeedPermissionManager;
import com.icarbonx.smartdevice.common.PermissionRequestCode;
import com.icarbonx.smartdevice.exceptin.NotActivityException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Manage uploading the bluetooth advertised data.
 * <p>After call the class method {@link BleHttpManager#getInstance()}, should call {@link BleHttpManager#init(Context)} to pass {@code context} in.</p>
 */
public class BleHttpManager extends NeedPermissionManager {
    private static BleHttpManager mBleHttpManager;

    //The upload data
    private String mDataBody;
    //Upload result callback
    private IBleHttpResult mIBleHttpResult;
    private long _id = 0;
    //OkhttpClient instance to control cancel.
    private OkHttpClient mOkHttpClient;
    //Requested call but not responsed
    private ArrayMap<Call, Long> mCallIDArrayMap;

    private final String URL = "http://192.168.101.131/test2/Devices";

    private final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    /**
     * Bluetooth LE data upload callbacks. Upload results are reported using these callbacks.
     */
    public interface IBleHttpResult {
        /**
         * Succeed to request
         *
         * @param callID       The ID for the request
         * @param responseBody The response body
         */
        void onSuccess(long callID, String responseBody);

        /**
         * Request failed.
         *
         * @param callID The ID for the request
         */
        void onFail(long callID);
    }

    /**
     * Get the instance of BleHttpManager
     *
     * @return {@link BleHttpManager} object
     */
    public static BleHttpManager getInstance() {
        if (mBleHttpManager != null) {
            return mBleHttpManager;
        }

        mBleHttpManager = new BleHttpManager();

        return mBleHttpManager;
    }

    protected BleHttpManager() {
        mCallIDArrayMap = new ArrayMap<>();
    }

    /**
     * Set PostBody to server
     *
     * @param jsonData {@code jsonData} is generated by{@link Gson#toJson(Object)}. The Object is {@link BleDevice}.
     * @return {@link BleHttpManager}
     */
    public BleHttpManager addDataBody(String jsonData) {
        this.mDataBody = jsonData;
        Gson gson;
        BleDevice bleDevice;
        return BleHttpManager.this;
    }

    /**
     * Set the upload callback.
     *
     * @param iBleHttpResult {@link IBleHttpResult}
     * @return {@link BleHttpManager}
     */
    public BleHttpManager addIBleHttpResult(IBleHttpResult iBleHttpResult) {
        this.mIBleHttpResult = iBleHttpResult;
        return BleHttpManager.this;
    }

    /**
     * Upload the setted data to server.
     */
    public void startRequest() {
        if (!isNetworkAvailable()) {
            mIBleHttpResult.onFail(-1);
            return;
        }
//        registerNetstate();

        RequestBody requestBody = RequestBody.create(JSON, this.mDataBody);

        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        Call call = mOkHttpClient
                .newCall(new Request.Builder()
                        .url(URL)
                        .post(requestBody)
                        .build());
        _id++;
        Log.e("","--:"+_id);
        mCallIDArrayMap.put(call, _id);
        call.enqueue(responseCallback);
    }

    /**
     * Stop all requests.
     */
    public void cancelAllRequest() {
        if (mOkHttpClient == null) return;

        mOkHttpClient.dispatcher().cancelAll();
    }

    private final Callback responseCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Long remove = mCallIDArrayMap.remove(call);
            //没有请求过就不回调
            if (remove == null) return;

            mIBleHttpResult.onFail(remove);
        }

        @Override
        public void onResponse(Call call, Response response) {
            Long remove = mCallIDArrayMap.remove(call);
            //没有请求过就不回调
            if (remove == null) return;

            try {//请求在执行，而资源已释放，会找不到键值对，以
                mIBleHttpResult.onSuccess(remove, response.body().string());
            }// 处理返回结果出现异常，返回空字符
            catch (IOException e) {
                mIBleHttpResult.onSuccess(remove, "");
            }
        }
    };

    /**
     * Release resources
     */
    public void release() {
        unregisterNetstate();

        mCallIDArrayMap.clear();

        mCallIDArrayMap = null;
        mBleHttpManager = null;
    }

    /**
     * Request internet permissions.
     * Check if network is closed, if not request to open
     *
     * @return {@code false} if network is closed,{@code true} otherwise.
     */
    @Override
    protected void checkPermissions() throws NotActivityException {
        if (mContext == null) return;

        //If sdk version is less than 23(android6.0), no need to ask for permissions.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        String[] permissions = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};
        ArrayList<String> unGtantedPermissions = new ArrayList<>();
        for (String p : permissions) {
            //Check if permission is granted.
            if (ContextCompat.checkSelfPermission(mContext, p) != PackageManager.PERMISSION_GRANTED) {
                unGtantedPermissions.add(p);
            }
        }
        //If there is ungranted permissions, ask for it.
        if (unGtantedPermissions.size() > 0) {
            if (mContext instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) mContext, (String[]) unGtantedPermissions.toArray(), PermissionRequestCode.NETWORK_PERMMISION_REQUEST);
            } else {
                throw new NotActivityException();
            }
        }
    }

    /**
     * Check network is available.
     *
     * @return {@code true} if the network is available, {@code false} otherwise
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //Network is available
        if (networkInfo != null && networkInfo.isAvailable()) {
            return true;
        }
        return false;
    }

    /**
     * Unregister net change receiver
     */
    private void unregisterNetstate() {
        //this.mContext.unregisterReceiver(netChangeReceiver);
    }

    /**
     * Register net change receiver
     */
    private void registerNetstate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.mContext.registerReceiver(netChangeReceiver, intentFilter);
    }

    /**
     * Net change broadcast receiver
     */
    private BroadcastReceiver netChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
}
