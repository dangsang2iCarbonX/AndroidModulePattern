package com.demo.icarbox.blereceiver;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.icarbonx.smartdevice.account.Apis;
import com.icarbonx.smartdevice.account.BaseResponse;
import com.icarbonx.smartdevice.account.LoginResponse;
import com.icarbonx.smartdevice.http.AbstractObserver;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class UserLoginActivity extends AppCompatActivity {

    EditText mPhoneNumber, mVerifyCode;
    Button mLogin;

    SmsObserver smsObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        mPhoneNumber = findViewById(R.id.email);
        mVerifyCode = findViewById(R.id.password);
        mLogin = findViewById(R.id.email_sign_in_button);

        mPhoneNumber.setKeyListener(new NumberKeys());
        mPhoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        findViewById(R.id.verifyCod).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


//        RxView.clicks(findViewById(R.id.verifyCod))
//                .throttleFirst(60, TimeUnit.SECONDS)
//                .subscribe(new AbstractObserver<Object>() {
//                    @Override
//                    public void onSuccess(Object o) {
                        if (!checkPhoneNumber()) return;

                        Apis.getInstance().getUserAccountService()
                                .getVerifyCode(mPhoneNumber.getText().toString(), 1)
                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new AbstractObserver<BaseResponse>() {
                                    @Override
                                    public void onSuccess(BaseResponse baseResponse) {
                                        if (baseResponse.getErrorCode() == 0) {
                                            Log.e("ss1",baseResponse.toJson());
                                        }//请求失败，尝试
                                        else {

                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        //获取验证码失败，尝试
                                    }
                                });
//                    }
//                });
                    }
                }
        );
        RxView.clicks(mLogin)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new AbstractObserver<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        //Login in check
                        if (!checkPhoneNumber()) return;
                        if (!checkVerifyCode()) return;
                        Apis.getInstance().getUserAccountService()
                                .login(mPhoneNumber.getText().toString().trim(),
                                        mVerifyCode.getText().toString().trim())
                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new AbstractObserver<LoginResponse>() {
                                    @Override
                                    public void onSuccess(LoginResponse loginResponse) {
                                        //to next page
                                        if (loginResponse.getErrorCode() == 0) {
                                            Log.e("ss2",loginResponse.toJson());
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

        smsObserver = new SmsObserver(this, smsHandler);
        getContentResolver().registerContentObserver(SMS_INBOX, true,
                smsObserver);

        checkSMSPermission();
    }

    private void checkSMSPermission() {
        if (ContextCompat.checkSelfPermission(UserLoginActivity.this, Manifest.permission.READ_SMS)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserLoginActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (1 == requestCode && permissions.length == 1) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                //授权读取短信
            }//授权失败
            else {

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public Handler smsHandler = new Handler() {
    };

    boolean checkPhoneNumber() {
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


    private Uri SMS_INBOX = Uri.parse("content://sms/inbox");

    public void getSmsFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"body", "address"};//"_id", "address", "person",, "date", "type
        String where = " date >  "
                + (System.currentTimeMillis() - 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
//        if (null == cur)
//            return;
//        if (cur.moveToFirst()) {
//            String number = cur.getString(cur.getColumnIndex("address"));//手机号
//            String body = cur.getString(cur.getColumnIndex("body"));
//
//        }

        Cursor cursor = getContentResolver().query(
                Uri.parse("content://sms"),
                new String[]{"_id", "address", "body", "date"},
                where, null, "date desc"); //
        if (cursor != null) {
//            String body = "";
            while (cursor.moveToNext()) {
                String number = cur.getString(cur.getColumnIndex("address"));//手机号
                String body = cur.getString(cur.getColumnIndex("body"));
                //-----------------写自己的逻辑

            }

        }
    }

    class SmsObserver extends ContentObserver {

        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //每当有新短信到来时，使用我们获取短消息的方法
            getSmsFromPhone();
        }
    }
}
