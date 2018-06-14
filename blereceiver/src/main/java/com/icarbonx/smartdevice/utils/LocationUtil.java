package com.icarbonx.smartdevice.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import com.icarbonx.smartdevice.common.PermissionListener;
import com.icarbonx.smartdevice.common.PermissionRequestCode;
import com.icarbonx.smartdevice.http.AbstractObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Proceed with gettting location class
 */
public class LocationUtil implements PermissionListener {
    private Context context;
    private LocationManager locationManager;
    private String locationProvider;

    //GPS
    private double latitude = 0;
    private double longitude = 0;
//    private double accuracy = 0;

    //Cell
    private int sid = 0;
    private int cid = 0;
    private int lac = 0;
    private String mcc = null;
    private String mnc = null;
    private String radioType="gsm";

    private boolean isGpsSuccess = false;
    private boolean isCellSuccess = false;

    private LocationRequestListener locationRequestListener;

    public interface LocationRequestListener {
        void onFail();

        void onSuccess(double lat, double lon);
    }

    public LocationUtil(Context context,LocationRequestListener requestListener) {
        this.context = context;
        this.locationRequestListener = requestListener;
    }

    @SuppressLint("MissingPermission")
    public void requestLocation() {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
//            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT);
            Log.e("T","no provider");
            locationRequestListener.onFail();
            isGpsSuccess = false;
            return;
        }

//        @SuppressLint("MissingPermission")
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
//            showLocation(location);
            locationRequestListener.onSuccess(location.getLatitude(),location.getLongitude());
        }else{
            Log.e("T","unkown last location");
            locationRequestListener.onFail();
        }
        //监视地理位置变化
//        @SuppressLint("MissingPermission")
//        locationManager.requestLocationUpdates(locationProvider, 300, 1, locationListener);
//        locationManager.requestSingleUpdate(locationProvider, locationListener, null);
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */
    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
            Log.e("", "provider status changed" + status);
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            showLocation(location);
            locationManager.removeUpdates(locationListener);
        }
    };

    private void showLocation(Location location) {
        Log.d("定位成功------->", "location------>"
                + "经度为：" + location.getLatitude()
                + "\n纬度为：" + location.getAltitude());

    }

    @Override
    public void permisssionGranted(int requestCode) {
        if (requestCode != PermissionRequestCode.LOCATION_PERMMISION_REQUEST) return;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        //可以读取Gps定位的定位信息
        {
            requestLocation();
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        //可以读取SIM基站定位信息
        {
//            requestCell();
        }
    }

    @Override
    public void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            requestLocation();
//            requestCell();
            return;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE};
        ArrayList<String> permissionWanted = new ArrayList<>();
        for (String p : permissions) {
            if (ActivityCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED) {
                permissionWanted.add(p);
            }
        }

        if (permissionWanted.size() > 0) {//Acvitity调用请求权限
            if (context instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) context,
                        (String[]) permissionWanted.toArray(),
                        PermissionRequestCode.LOCATION_PERMMISION_REQUEST);
            } else {
                throw new IllegalArgumentException("context should be from Activity");
            }
        } else {
            permisssionGranted(PermissionRequestCode.LOCATION_PERMMISION_REQUEST);
        }
    }

    void requestCell() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            Log.e("t","电话服务不存在");
            isCellSuccess = false;
            return;
        }

        //TelephonyManager#getNetworkOperator方法获取目前注册网络MCC+MNC信息，一般是5-6位的字符串，前3位为MCC，后面的是MNC。
//        Log.e("===", telephonyManager.getNetworkOperator());
//        Log.e("===", String.valueOf(telephonyManager.getNetworkType()));
        String operator = telephonyManager.getNetworkOperator();
        this.mcc = operator.substring(0, 3);
        this.mnc = operator.substring(3);

        if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            radioType = "cdma";

            @SuppressLint("MissingPermission")
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) telephonyManager.getCellLocation();
            @SuppressLint("MissingPermission")
            List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
            @SuppressLint("MissingPermission")
            List<NeighboringCellInfo> neighboringCellInfo = telephonyManager.getNeighboringCellInfo();
            if(neighboringCellInfo.size()>0){
                NeighboringCellInfo cellInfo = neighboringCellInfo.get(0);
                Log.e("T", "" + cellInfo.getCid()+"/"+cellInfo.getLac());
            }
            if(allCellInfo.size()>0) {
                CellInfoCdma cellInfoCdma = (CellInfoCdma) allCellInfo.get(0);
                Log.e("T", "" + cellInfoCdma.getCellSignalStrength() + cellInfoCdma);

            }
            if (cdmaCellLocation == null) {
                Log.e("t","基站定位失败");
                isCellSuccess = false;
                return;
            }

            this.cid = cdmaCellLocation.getBaseStationId();
            //获取cdma基站识别标号 BID
            this.lac = cdmaCellLocation.getNetworkId();
            // 获取cdma网络编号NID
            this.sid = cdmaCellLocation.getSystemId();
            // 用谷歌API的话cdma网络的mnc要用这个getSystemId()取得→SID
        } else if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
            radioType = "gsm";
            @SuppressLint("MissingPermission")
            GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            if (gsmCellLocation == null) {
                isCellSuccess = false;
                return;
            }

            this.cid = gsmCellLocation.getCid(); //获取gsm基站识别标号
            this.lac = gsmCellLocation.getLac(); //获取gsm网络编号
        }

        //获得mcc,mnc,cid,lac[sid]
        isCellSuccess = true;
    }

//    void cell2gps(int mcc,int mnc,int lac,int cid){
//        /** 构造POST的JSON数据 */
//        JSONObject holder = new JSONObject();
//
//        holder.put("version", "1.1.0");
//        holder.put("host", "maps.google.com");
//        holder.put("address_language", "zh_CN");
//        holder.put("request_address", true);
//        holder.put("radio_type", "gsm");
//        holder.put("carrier", "HTC");
//
//        JSONObject tower = new JSONObject();
//        tower.put("mobile_country_code", mcc);
//        tower.put("mobile_network_code", mnc);
//        tower.put("cell_id", cid);
//        tower.put("location_area_code", lac);
//
//        JSONArray towerarray = new JSONArray();
//        towerarray.put(tower);
//        holder.put("cell_towers", towerarray);
//
//        new Retrofit.Builder()
//                .baseUrl("http://www.google.com/loc/json")
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build()
//                .create(cell2gsp2google.class)
//                .getLocation("1.1.0","maps.google.com",
//                        "zh_CN",true,radioType,);
//
//
//        StringEntity query = new StringEntity(holder.toString());
//        post.setEntity(query);
//
//        /** 发出POST数据并获取返回数据 */
//        HttpResponse response = client.execute(post);
//        HttpEntity entity = response.getEntity();
//        BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
//        StringBuffer strBuff = new StringBuffer();
//        String result = null;
//        while ((result = buffReader.readLine()) != null) {
//            strBuff.append(result);
//        }
//
//        /** 解析返回的JSON数据获得经纬度 */
//        JSONObject json = new JSONObject(strBuff.toString());
//        JSONObject subjosn = new JSONObject(json.getString("location"));
//
//        itude.latitude = subjosn.getString("latitude");
//        itude.longitude = subjosn.getString("longitude");
//
//    }

    interface cell2gsp2google{
        @FormUrlEncoded
        @POST
        AbstractObserver getLocation(@Field("version")String version,
                                     @Field("host")String host,
                                     @Field("address_language")String language,
                                     @Field("request_address")boolean requestAddress,
                                     @Field("radio_type")String radioType,
                                     @Field("carrier")String carrier,
                                     @Field("cell_towers")String towers);

    }

// /
//    /** 基站信息结构体 */
//    public class SCell{
//        public int MCC;
//        public int MNC;
//        public int LAC;
//        public int CID;
//    }
//
//    /** 经纬度信息结构体 */
//    public class SItude{
//        public String latitude;
//        public String longitude;
//    }

}
