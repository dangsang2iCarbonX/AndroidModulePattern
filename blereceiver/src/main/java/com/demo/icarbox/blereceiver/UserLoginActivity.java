package com.demo.icarbox.blereceiver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.icarbonx.smartdevice.account.Apis;
import com.icarbonx.smartdevice.account.ApisOld;
import com.icarbonx.smartdevice.account.BaseResponse;
import com.icarbonx.smartdevice.account.LoginResponse;
import com.icarbonx.smartdevice.account.UserAccountHttpService;
import com.icarbonx.smartdevice.http.AbstractObserver;
import com.icarbonx.smartdevice.utils.LocationUtil;
import com.icarbonx.smartdevice.utils.PhoneInfoUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class UserLoginActivity extends AppCompatActivity {

    EditText mPhoneNumber, mVerifyCode;
    Button mLogin;
    TextView show;

    LocationUtil mLocationUtil;
    PhoneInfoUtil mPhoneInfoUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        mPhoneNumber = findViewById(R.id.email);
        mVerifyCode = findViewById(R.id.password);
        mLogin = findViewById(R.id.email_sign_in_button);
        show = findViewById(R.id.test);

        mPhoneNumber.setKeyListener(new NumberKeys());
        mPhoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);


        RxView.clicks(findViewById(R.id.verifyCod))
                .throttleFirst(30, TimeUnit.SECONDS)
                .subscribe(new AbstractObserver<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.e("phone info.", mPhoneInfoUtil.getBrand() + "\n"
                                + mPhoneInfoUtil.getDeviceId() + "\n"
                                + mPhoneInfoUtil.getDisplay() + "\n"
                                + mPhoneInfoUtil.getManufacturer() + "\n");

                        if (!checkPhoneNumber()) return;
                        ApisOld.getInstance().getUserAccountService()
                                .getVerifyCode(mPhoneNumber.getText().toString(), 1)
                                .subscribeOn(Schedulers.single())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new AbstractObserver<BaseResponse>() {
                                    @Override
                                    public void onSuccess(BaseResponse baseResponse) {
                                        if (baseResponse.getErrorCode() == 0) {
                                            Log.e("ss1", baseResponse.toJson());
                                        }//请求失败，尝试
                                        else {

                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        //获取验证码失败，尝试
                                    }
                                });

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("ss", "failure click");
                    }
                });
        RxView.clicks(mLogin)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new AbstractObserver<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        //Login in check
                        if (!checkPhoneNumber()) return;
                        if (!checkVerifyCode()) return;
                        ApisOld.getInstance().getUserAccountService()
                                .login(mPhoneNumber.getText().toString().trim(),
                                        mVerifyCode.getText().toString().trim())
                                .subscribeOn(Schedulers.single())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new AbstractObserver<LoginResponse>() {
                                    @Override
                                    public void onSuccess(LoginResponse loginResponse) {
                                        //to next page
                                        if (loginResponse.getErrorCode() == 0) {
                                            Log.e("ss2", loginResponse.toJson());
                                            startActivity(new Intent().setClass(UserLoginActivity.this,UserAskerActivity.class));
//                                            show.setText(loginResponse.toJson());
                                            finish();
                                        }//toast err msg
                                        else {
                                            Log.e("err", "[" + loginResponse.getErrorCode() + "]"
                                                    + loginResponse.getErrMsg());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        //登陆失败，尝试
                                    }
                                });
                    }
                });

        mPhoneNumber.setText("18923889519");
        mLocationUtil = new LocationUtil(this, new LocationUtil.LocationRequestListener() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(double lat, double lon) {

            }
        });
        mPhoneInfoUtil = new PhoneInfoUtil(this);
        mLocationUtil.checkPermission();
        mPhoneInfoUtil.checkPermission();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationUtil.permisssionGranted(requestCode);
        mPhoneInfoUtil.permisssionGranted(requestCode);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    boolean checkPhoneNumber() {
        Log.e("sms", "check number:" + mPhoneNumber.getText().toString());

        String number = mPhoneNumber.getText().toString();
        if (number.length() == 11 || number.length() == 14) {
            return true;
        }
        mPhoneNumber.setError("Number length is invalid");
        return false;
    }

    boolean checkVerifyCode() {
        String number = mVerifyCode.getText().toString();
        if (number.length() != 4) {
            mVerifyCode.setError("Verify code is invalid");
            return false;
        }
        return true;
    }


}
