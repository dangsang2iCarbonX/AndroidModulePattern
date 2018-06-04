package com.demo.icarbox.blereceiver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.icarbonx.smartdevice.ble.manager.BleScanDevice;
import com.icarbonx.smartdevice.ble.manager.BleScanManager;
import com.icarbonx.smartdevice.common.ICarbonXEception;
import com.icarbonx.smartdevice.http.BleHttpManager;

/**
 * Demo activity for {@link com.icarbonx.smartdevice.http.BleHttpManager} usage.
 * @author lavi
 */
@Route(path = "/ble/demo/blescanmanager")
public class DemoBleHttpManagerActivity extends AppCompatActivity {
    private static final  String Tag =DemoBleHttpManagerActivity.class.getSimpleName();
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.str_demo_ble_http_manager_title);
        setContentView(R.layout.demo_content_text);

        text =findViewById(R.id.text);
        text.setMovementMethod(ScrollingMovementMethod.getInstance());

        try {
            BleHttpManager.getInstance().init(this);
        } catch (ICarbonXEception iCarbonXEception) {
            iCarbonXEception.printStackTrace();
        }
        Log.e(Tag,"onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(Tag,"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        BleHttpManager.getInstance().addDataBody("{\"deviceID\":\"ds2018\",\"advData\":\"whatever it is\"}");
        BleHttpManager.getInstance().addIBleHttpResult(new BleHttpManager.IBleHttpResult() {
            @Override
            public void onSuccess(long callID, String responseBody) {
                Log.e(""+callID,responseBody);
                text.post(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder builder = new StringBuilder();
                        builder.append("post request:"+"{\"deviceID\":\"ds2018\",\"advData\":\"whatever it is\"}\n");
                        builder.append(String.format("%d: success\n",callID));
                        builder.append(responseBody);
                        builder.append("\n\n");
                        builder.append(text.getText());
                        text.setText(builder.toString());
                    }
                });
            }

            @Override
            public void onFail(long callID) {
                text.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("" + callID, "failed");
                        StringBuilder builder = new StringBuilder();
                        builder.append(String.format("%d: ", callID));
                        builder.append("failed");
                        builder.append("\n\n");
                        builder.append(text.getText());
                        text.setText(builder.toString());
                    }
                });
            }
        }).startRequest();
        Log.e(Tag,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        BleHttpManager.getInstance().cancelAllRequest();
        Log.e(Tag,"onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BleHttpManager.getInstance().release();
        Log.e(Tag,"onDestroy");
    }
}
