package com.icarbonx.smartdevice.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Switch;

import com.icarbonx.smartdevice.common.PermissionListener;
import com.icarbonx.smartdevice.common.PermissionRequestCode;

import static android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
import static android.telephony.TelephonyManager.PHONE_TYPE_GSM;
import static android.telephony.TelephonyManager.PHONE_TYPE_NONE;
import static android.telephony.TelephonyManager.PHONE_TYPE_SIP;

/**
 * The util class to get phone base information
 *
 * @author lavi
 */
public class PhoneInfoUtil implements PermissionListener{
    private Context context;

    private Build build;
    private Display display;
    private Telephony telephony;

    public PhoneInfoUtil(Context context) {
        this.context = context;
        this.build = new Build();
        this.display = new Display(context);
        this.telephony = new Telephony(context);
    }

    public String getDeviceId() {
        return telephony.deviceId;
    }

    public String getSubscriberId() {
        return telephony.subscriberId;
    }

    public String getSimOperatorName() {
        return telephony.simOperatorName;
    }

    public String getSimSerialNumber() {
        return telephony.simSerialNumber;
    }

    public String getPhoneType() {
        return telephony.phoneType;
    }

    public String getSimCountryIso() {
        return telephony.simCountryIso;
    }

    public String getModel() {
        return build.getModel();
    }

    public String getBrand() {
        return build.getBrand();
    }

    public String getDisplay() {
        return build.getDisplay();
    }

    public String getHardware() {
        return build.getHardware();
    }

    public String getManufacturer() {
        return build.getManufacturer();
    }

    public String getSerial() {
        return build.getSerial();
    }

    public String getRadioVersion() {
        return build.getRadioVersion();
    }

    public int getSdkInt() {
        return build.getSdkInt();
    }

    public String getRelease() {
        return build.getRelease();
    }

    public float getDensity() {
        return display.density;
    }


    public int getDensityDpi() {
        return display.densityDpi;
    }

    public int getHeightPixels() {
        return display.heightPixels;
    }

    public int getWidthPixels() {
        return display.widthPixels;
    }

    @Override
    public void permisssionGranted(int requestCode) {
        if(PermissionRequestCode.PHONESTATE_PERMMISION_REQUEST == requestCode) {
            telephony.permissionGranted();
            build.permissionGranted();
        }
    }

    public void checkPermission(){
        telephony.checkPermission();
        build.checkPermission();
    }

    class Telephony {
        Telephony(Context context) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) return;
            simCountryIso = telephonyManager.getSimCountryIso();
            simOperatorName = telephonyManager.getSimOperatorName();
            switch (telephonyManager.getPhoneType()) {
                case PHONE_TYPE_GSM:
                    phoneType = "GSM";
                    break;
                case PHONE_TYPE_CDMA:
                    phoneType = "CDMA";
                    break;
                case PHONE_TYPE_SIP:
                    phoneType = "SIP";
                    break;
                default:
                    phoneType = "NONE";
                    break;
            }

            checkPermission();
        }

        String deviceId;
        String subscriberId;
        String simOperatorName;
        String simSerialNumber;
        String phoneType;
        String simCountryIso;

        @SuppressLint("MissingPermission")
        private void permissionGranted() {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) return;

            setSimSerialNumber(telephonyManager.getSimSerialNumber());
            setDeviceId(telephonyManager.getDeviceId());
            setSubscriberId(telephonyManager.getSubscriberId());
        }

        void checkPermission(){
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                permissionGranted();
                return;
            }
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                if (context instanceof Activity) {
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            PermissionRequestCode.PHONESTATE_PERMMISION_REQUEST);
                }
            } else {
                permissionGranted();
            }
        }

        private void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        private void setSubscriberId(String subscriberId) {
            this.subscriberId = subscriberId;
        }

        private void setSimSerialNumber(String simSerialNumber) {
            this.simSerialNumber = simSerialNumber;
        }
    }


    private class Build {
//        String model;
//        String brand;
//        String display;
//        String hardware;
//        String manufacturer;
        String serial;
//        String radioVersion;
//        int sdkInt;
//        String release;

        public String getModel() {
            return android.os.Build.MODEL;
        }

        public String getBrand() {
            return android.os.Build.BRAND;
        }

        public String getDisplay() {
            return android.os.Build.DISPLAY;
        }

        public String getHardware() {
            return android.os.Build.HARDWARE;
        }

        public String getManufacturer() {
            return android.os.Build.MANUFACTURER;
        }

        public String getSerial() {
            return serial;
        }

        public String getRadioVersion() {
            return android.os.Build.getRadioVersion();
        }

        public int getSdkInt() {
            return android.os.Build.VERSION.SDK_INT;
        }

        public String getRelease() {
            return android.os.Build.VERSION.RELEASE;
        }

        @SuppressLint("MissingPermission")
        void permissionGranted(){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                serial = android.os.Build.getSerial();
            }
        }

        public void checkPermission(){
//            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
//                permissionGranted();
//                return;
//            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    if (context instanceof Activity) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                PermissionRequestCode.PHONESTATE_PERMMISION_REQUEST);
                    }
                }else {
                    permissionGranted();
                }
            } else {
                serial= android.os.Build.SERIAL;
            }
        }
    }


    /**
     *  TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

     Log.e("ti","="+telephonyManager.getDeviceId());

     //        Log.e("ti","="+telephonyManager.getSubscriberId());
     //        Log.e("ti","="+telephonyManager.getDeviceSoftwareVersion());
     //        Log.e("ti","="+telephonyManager.getGroupIdLevel1());
     //        Log.e("ti",""+telephonyManager.getImei());//gsm
     //        Log.e("ti",""+telephonyManager.getMeid());//cdma
     Log.e("ti","="+telephonyManager.getSimOperatorName());
     Log.e("ti","="+telephonyManager.getSimSerialNumber());

     * @see #PHONE_TYPE_NONE
     * @see #PHONE_TYPE_GSM
     * @see #PHONE_TYPE_CDMA
     * @see #PHONE_TYPE_SIP

    Log.e("ti","="+telephonyManager.getPhoneType());
    Log.e("ti","="+telephonyManager.getSimCountryIso());

    Log.e("phone info1",Build.MODEL);
    //        Log.e("phone info2",Build.BOARD);//
    Log.e("phone info3",Build.BRAND);
    //        Log.e("phone info4",Build.DEVICE);//
    Log.e("phone info5",Build.DISPLAY);
    //        Log.e("phone info6",Build.FINGERPRINT);//
    Log.e("phone info7",Build.HARDWARE);
    //        Log.e("phone info8",Build.HOST);//
    //        Log.e("phone info9",Build.ID);//
    Log.e("phone info0",Build.MANUFACTURER);
    //        Log.e("phone info1",Build.PRODUCT);//
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    Log.e("phone info2",Build.getSerial());
    }else{
    Log.e("phone info2",Build.SERIAL);

    }
    Log.e("phone info2","="+Build.getRadioVersion());
    Log.e("phone info1","="+Build.VERSION.SDK_INT);
    //        Log.e("phone info1","="+Build.VERSION.BASE_OS);
    //        Log.e("phone info1","="+Build.VERSION.CODENAME);//
    //        Log.e("phone info1","="+Build.VERSION.INCREMENTAL);//
    Log.e("phone info1","="+Build.VERSION.RELEASE);
    //        Log.e("phone info1","="+Build.VERSION.SECURITY_PATCH);
    //        Log.e("phone info1","="+Build.VERSION.PREVIEW_SDK_INT);

    WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics outMetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(outMetrics);
    Log.e("json",new Gson().toJson(outMetrics));

    PackageManager pm = getPackageManager();
    PackageInfo pi = null;
    try {
    pi = pm.getPackageInfo(getPackageName(), 0);
    Log.e("package","="+pi.versionName+"="+pi.versionCode+pi.applicationInfo.name);
    } catch (PackageManager.NameNotFoundException e) {
    e.printStackTrace();
    }
     */

    /**
     * Phone device display information class.
     */
    class Display {
        float density;
        int densityDpi;
        int heightPixels;
        int widthPixels;

        public Display(Context context) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);

            this.density = outMetrics.density;
            this.densityDpi = outMetrics.densityDpi;
            this.heightPixels = outMetrics.heightPixels;
            this.widthPixels = outMetrics.widthPixels;
        }
    }
}
