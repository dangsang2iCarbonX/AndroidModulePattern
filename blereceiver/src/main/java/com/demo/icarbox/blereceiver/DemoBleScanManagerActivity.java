package com.demo.icarbox.blereceiver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Scroller;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.guiying.module.common.base.BaseActionBarActivity;
import com.guiying.module.common.utils.Utils;
import com.icarbonx.smartdevice.ble.manager.BleScanDevice;
import com.icarbonx.smartdevice.ble.manager.BleScanManager;

/**
 * Demo activity for {@link BleScanManager} usage.
 * @author lavi
 */
public class DemoBleScanManagerActivity extends AppCompatActivity {
    TextView text;


    String byteArray2String(byte[] data){
        if (data==null)return "";

        final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<data.length; i++){
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if(i < data.length-1)
                stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.str_demo_ble_scan_manager_title);
        setContentView(R.layout.demo_content_text);

        text =findViewById(R.id.text);
        text.setMovementMethod(ScrollingMovementMethod.getInstance());

        BleScanManager.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BleScanManager.getInstance(this).filterByRssi(-65);
//        BleScanManager.getInstance(this).filterByName("bong4");
//        BleScanManager.getInstance(this).filterByMac(":::::");
        BleScanManager.getInstance(this).setIBleScanResult(new BleScanManager.IBleScanResult() {
            @Override
            public void onResult(BleScanDevice bleScanDevice) {
                StringBuilder builder = new StringBuilder();
                builder.append("Mac: ");
                builder.append(bleScanDevice.getMac());
                builder.append("\n");
                builder.append("name: ");
                builder.append(bleScanDevice.getName());
                builder.append("\n");
                builder.append("Scanned raw data:");
                builder.append("\n");
                builder.append(byteArray2String(bleScanDevice.getScanRawData()));
                builder.append("\n\n");
                builder.append(text.getText());
                text.setText(builder.toString());

                text.setScrollY(0);
            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BleScanManager.getInstance(this).startScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BleScanManager.getInstance(this).stopScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleScanManager.getInstance(this).release();
    }


}
